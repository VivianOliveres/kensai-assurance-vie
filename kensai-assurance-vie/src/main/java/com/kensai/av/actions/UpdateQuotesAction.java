package com.kensai.av.actions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;

public class UpdateQuotesAction {
	private static Logger log = LogManager.getLogger(DatasZipper.class);

	private final ProductQuotesWritter writter;
	private final DatasZipper zipper;
	private final QuoteRetriever retriever;

	public UpdateQuotesAction() {
		this(DatasZipper.CURRENT_FOLDER, DatasZipper.OLD_FOLDER);
	}

	public UpdateQuotesAction(Path fromFolder, Path toFolder) {
		this(new QuoteRetriever(), new ProductQuotesWritter(fromFolder), new DatasZipper(fromFolder, toFolder));
	}

	public UpdateQuotesAction(QuoteRetriever retriever, ProductQuotesWritter writter, DatasZipper zipper) {
		this.retriever = retriever;
		this.writter = writter;
		this.zipper = zipper;
	}

	public void execute(List<ProductQuotes> allQuotes) {
		// Save old values
		try {
			zipper.save();
		} catch (IOException e) {
			log.error("Can not save old datas in zip file", e);
			return;
		}

		// retrieve quotes from Bourso and persist them
		for (ProductQuotes quotes : allQuotes) {
			try {
				Quote quote = retriever.retrieve(quotes.getProduct());
				quotes.add(quote);
			} catch (IOException | NumberFormatException e) {
				log.error("Can not retrieve quote from " + quotes.getProduct(), e);
				continue;
			}

			try {
				writter.write(quotes);
			} catch (IOException e) {
				log.error("Can not persist quotes from " + quotes.getProduct(), e);
				continue;
			}
		}
	}
}
