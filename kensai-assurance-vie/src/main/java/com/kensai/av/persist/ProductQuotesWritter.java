package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;

public class ProductQuotesWritter {
	private static Logger log = LogManager.getLogger(DatasZipper.class);

	private final QuoteCsvConverter converter = new QuoteCsvConverter();
	private final Path toFolder;

	public ProductQuotesWritter() {
		this(DatasZipper.CURRENT_FOLDER);
	}

	public ProductQuotesWritter(Path toFolder) {
		this.toFolder = toFolder;
	}

	public Path write(ProductQuotes quotes) throws IOException {
		log.info("Write quotes for " + quotes.getProduct());
		String fileName = quotes.getProduct().getIsin() + ".csv";
		Path csvPath = toFolder.resolve(fileName);

		// Create file
		log.info("Create file: " + csvPath);
		Files.deleteIfExists(csvPath);
		Files.createFile(csvPath);

		// Write comment
		com.google.common.io.Files.append(converter.getCsvComment() + "\n", csvPath.toFile(), Charsets.UTF_8);

		// Write all quotes
		for (Quote quote : quotes) {
			com.google.common.io.Files.append(converter.toCsvString(quote) + "\n", csvPath.toFile(), Charsets.UTF_8);
		}

		return csvPath;
	}
}
