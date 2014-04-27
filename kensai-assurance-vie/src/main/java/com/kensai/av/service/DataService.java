package com.kensai.av.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;

public class DataService {

	private Map<Product, ProductQuotes> allQuotes = new HashMap<>();

	public DataService(ProductQuotes... productQuotes) {
		this(Lists.newArrayList(productQuotes));
	}

	public DataService(List<ProductQuotes> list) {
		for (ProductQuotes productQuotes : list) {
			allQuotes.put(productQuotes.getProduct(), productQuotes);
		}
	}

	public ProductQuotes getProductQuotes(Product product) {
		return allQuotes.get(product);
	}

	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
		products.addAll(allQuotes.keySet());
		return products;
	}

	public List<ProductQuotes> getAllProductQuotes() {
		List<ProductQuotes> products = new ArrayList<>();
		products.addAll(allQuotes.values());
		return products;
	}

}
