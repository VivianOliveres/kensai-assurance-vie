package com.kensai.av.products;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductCsvReader {
	private static Logger log = LogManager.getLogger(ProductCsvReader.class);

	public List<Product> extract(Path path, String csvSeparator) throws IOException {
		return extract(Files.readAllLines(path), csvSeparator);
	}

	public List<Product> extract(Path path) throws IOException {
		return extract(Files.readAllLines(path), ";");
	}

	public List<Product> extract(List<String> allLines) {
		return extract(allLines, ";");
	}

	public List<Product> extract(List<String> allLines, String csvSeparator) {
		List<Product> products = new ArrayList<>();
		for (int i = 0; i < allLines.size(); i++) {
			String line = allLines.get(i).trim();
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}

			try {
				Product product = extractProduct(line, csvSeparator);
				products.add(product);
			} catch (Exception e) {
				log.error("Can not extract product from line [" + i + "]: [" + line + "]", e);
			}
		}

		return products;
	}

	private Product extractProduct(String line, String csvSeparator) {
		String[] split = line.split(csvSeparator);
		String name = split[0];
		boolean isAV = Boolean.parseBoolean(split[1]);
		boolean isPEA = Boolean.parseBoolean(split[2]);
		String url = split[3];
		Product product = new Product(name, isPEA, isAV, url);
		return product;
	}
}
