package com.kensai.av.persist;

import java.time.LocalDate;
import java.util.StringJoiner;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.Quote;
import com.kensai.av.datas.QuoteBuilder;

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

	public Quote fromCsvString(Product product, String csvLine) {
		String[] split = csvLine.split(csvSeparator);
		return QuoteBuilder.create()
								 .withProduct(product)
								 .withDate(LocalDate.parse(split[1]))
								 .withPrice(Double.parseDouble(split[2]))
								 .withPriceCurrency(split[3])
								 .withNotationMorningStar(Integer.parseInt(split[4]))
								 .withNotationLipperCapitalPreservation(Integer.parseInt(split[5]))
								 .withNotationLipperRegularPerformances(Integer.parseInt(split[6]))
								 .withNotationLipperAbsolutePerformances(Integer.parseInt(split[7]))
								 .withSharpeRatio1(Double.parseDouble(split[8]))
								 .withSharpeRatio3(Double.parseDouble(split[9]))
								 .withSharpeRatio5(Double.parseDouble(split[10]))
								 .withSharpeRatio10(Double.parseDouble(split[11]))
								 .build();
	}

}
