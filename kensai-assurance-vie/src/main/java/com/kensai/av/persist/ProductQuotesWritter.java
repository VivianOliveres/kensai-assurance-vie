package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.base.Charsets;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;

public class ProductQuotesWritter {

	private final QuoteCsvConverter converter = new QuoteCsvConverter();
	private final Path toFolder;

	public ProductQuotesWritter() {
		this(DatasZipper.CURRENT_FOLDER);
	}

	public ProductQuotesWritter(Path toFolder) {
		this.toFolder = toFolder;
	}

	public Path write(ProductQuotes quotes) throws IOException {
		String fileName = quotes.getProduct().getIsin()+".csv";
		Path csvPath = toFolder.resolve(fileName);

		// Create file
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
