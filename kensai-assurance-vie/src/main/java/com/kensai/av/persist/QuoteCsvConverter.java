package com.kensai.av.persist;

import java.util.StringJoiner;

import com.kensai.av.datas.Quote;

public class QuoteCsvConverter {

	private String csvSeparator;

	public QuoteCsvConverter() {
		this(";");
	}

	public QuoteCsvConverter(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}

	public String getCsvComment() {
		StringJoiner joiner = new StringJoiner(csvSeparator, "#", "");
		joiner.add("isin");
		joiner.add("date");
		joiner.add("price");
		joiner.add("priceCurrency");
		joiner.add("NotationMorningStar");
		joiner.add("NotationLipperCapitalPreservation");
		joiner.add("NotationLipperRegularPerformances");
		joiner.add("NotationLipperAbsolutePerformances");
		joiner.add("SharpeRatio1");
		joiner.add("SharpeRatio3");
		joiner.add("SharpeRatio5");
		joiner.add("SharpeRatio10");
		return joiner.toString();
	}

	public String toCsvString(Quote quote) {
		StringJoiner joiner = new StringJoiner(csvSeparator);
		joiner.add(quote.getProduct().getIsin());
		joiner.add(quote.getDate().toString());
		joiner.add(Double.toString(quote.getPrice()));
		joiner.add(quote.getPriceCurrency());
		joiner.add(Integer.toString(quote.getNotationMorningStar()));
		joiner.add(Integer.toString(quote.getNotationLipperCapitalPreservation()));
		joiner.add(Integer.toString(quote.getNotationLipperRegularPerformances()));
		joiner.add(Integer.toString(quote.getNotationLipperAbsolutePerformances()));
		joiner.add(Double.toString(quote.getSharpeRatio1()));
		joiner.add(Double.toString(quote.getSharpeRatio3()));
		joiner.add(Double.toString(quote.getSharpeRatio5()));
		joiner.add(Double.toString(quote.getSharpeRatio10()));
		return joiner.toString();
	}

}
