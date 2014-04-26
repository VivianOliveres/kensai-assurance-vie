package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;

public class ProductQuotesReader {

	private QuoteCsvConverter converter = new QuoteCsvConverter();

	public ProductQuotes extract(Product product, Path folder) throws IOException {
		Path productPath = folder.resolve(product.getIsin() + ".csv");
		return extract(product, Files.readAllLines(productPath));
	}

	public ProductQuotes extract(Product product, List<String> lines) {
		List<Quote> quotes = new ArrayList<>();
		for (String line : lines) {
			if (line.trim().startsWith("#")) {
				continue;
			}

			Quote quote = converter.fromCsvString(product, line);
			quotes.add(quote);
		}

		return new ProductQuotes(product, quotes);
	}
}
