package com.kensai.av.actions;

import java.io.IOException;
import java.util.concurrent.ForkJoinTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;

public class UpdateQuoteAction extends ForkJoinTask<ProductQuotes> {
	private static final long serialVersionUID = 44377348113530727L;

	private static Logger log = LogManager.getLogger(UpdateQuoteAction.class);

	private final ProductQuotesWritter writter;
	private final QuoteRetriever retriever;

	private ProductQuotes quotes;

	public UpdateQuoteAction(ProductQuotes quotes, ProductQuotesWritter writter, QuoteRetriever retriever) {
		this.quotes = quotes;
		this.writter = writter;
		this.retriever = retriever;
	}

	@Override
	public ProductQuotes getRawResult() {
		return quotes;
	}

	@Override
	protected void setRawResult(ProductQuotes value) {
		log.warn("setRawResult from [" + quotes + "] to [" + value + "]");
		quotes = value;
	}

	@Override
	protected boolean exec() {
		// Retrieve quote from bourso
		log.info("exec for " + quotes);
		try {
			Quote quote = retriever.retrieve(quotes.getProduct());
			quotes.add(quote);
		} catch (IOException | NumberFormatException e) {
			log.error("Can not retrieve quote from " + quotes.getProduct(), e);
			return false;
		}

		// Write them into file
		try {
			writter.write(quotes);
		} catch (IOException e) {
			log.error("Can not persist quotes from " + quotes.getProduct(), e);
			return false;
		}

		log.info("exec for " + quotes + " return true");
		return true;
	}

}
