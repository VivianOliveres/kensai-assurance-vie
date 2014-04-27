package com.kensai.av.actions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;
import com.kensai.av.service.DataService;

public class UpdateQuotesAction extends Task<List<ProductQuotes>> {
	private static Logger log = LogManager.getLogger(UpdateQuotesAction.class);

	private final ProductQuotesWritter writter;
	private final DatasZipper zipper;
	private final QuoteRetriever retriever;

	private ForkJoinPool pool = new ForkJoinPool(3);

	private final DataService service;

	public UpdateQuotesAction(DataService service) {
		this(service, DatasZipper.CURRENT_FOLDER, DatasZipper.OLD_FOLDER);
	}

	public UpdateQuotesAction(DataService service, Path fromFolder, Path toFolder) {
		this(service, new QuoteRetriever(), new ProductQuotesWritter(fromFolder), new DatasZipper(fromFolder, toFolder));
	}

	public UpdateQuotesAction(DataService service, QuoteRetriever retriever, ProductQuotesWritter writter, DatasZipper zipper) {
		this.retriever = retriever;
		this.writter = writter;
		this.zipper = zipper;
		this.service = service;
	}

	@Override
	protected List<ProductQuotes> call() throws Exception {
		// Save old values
		try {
			zipper.save();
		} catch (IOException e) {
			log.error("Can not save old datas in zip file", e);
			return Lists.newArrayList();
		}

		List<ForkJoinTask<ProductQuotes>> tasks = new ArrayList<>();
		for (ProductQuotes quotes : service.getAllProductQuotes()) {
			UpdateQuoteAction task = new UpdateQuoteAction(quotes, writter, retriever);
			ForkJoinTask<ProductQuotes> forkJoinTask = pool.submit(task);
			tasks.add(forkJoinTask);
		}

		boolean hasFinished = pool.awaitQuiescence(1, TimeUnit.MINUTES);
		log.info("Task has finished: " + hasFinished);

		List<ProductQuotes> results = new ArrayList<>();
		for (ForkJoinTask<ProductQuotes> task : tasks) {
			results.add(task.get());
		}

		log.info("Task ends with result: " + results);
		return results;

	}

}
