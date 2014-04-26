package com.kensai.av.datas;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class ProductQuotes implements Iterable<Quote> {

	private final Product product;
	private final Set<Quote> quotes = new HashSet<>();

	public ProductQuotes(Product product, Quote... quotes) {
		this(product, Lists.newArrayList(quotes));
	}

	public ProductQuotes(Product product, Collection<Quote> quotes) {
		this.product = product;
		this.quotes.addAll(quotes);
	}

	@Override
	public Iterator<Quote> iterator() {
		return quotes.iterator();
	}

	public Product getProduct() {
		return product;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), product);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ProductQuotes) {
			if (!super.equals(object))
				return false;
			ProductQuotes that = (ProductQuotes) object;
			return Objects.equal(this.product, that.product);
		}
		return false;
	}

	public void add(Quote quote) {
		quotes.add(quote);
	}

	public List<Quote> getQuotes() {
		return Lists.newArrayList(quotes);
	}

	public int size() {
		return quotes.size();
	}

	public boolean isEmpty() {
		return quotes.isEmpty();
	}
}
