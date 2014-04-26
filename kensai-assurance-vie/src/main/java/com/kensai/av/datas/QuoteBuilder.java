package com.kensai.av.datas;

import java.time.LocalDate;

public class QuoteBuilder {

	private Product product;
	private LocalDate date = LocalDate.now();
	private double price;
	private String priceCurrency;
	private int notationMorningStar;
	private int notationLipperCapitalPreservation;
	private int notationLipperRegularPerformances;
	private int notationLipperAbsolutePerformances;
	private double sharpeRatio1;
	private double sharpeRatio3;
	private double sharpeRatio5;
	private double sharpeRatio10;

	public static QuoteBuilder create() {
		return new QuoteBuilder();
	}

	public QuoteBuilder withProduct(Product product) {
		this.product = product;
		return this;
	}

	public QuoteBuilder withDate(LocalDate date) {
		this.date = date;
		return this;
	}

	public QuoteBuilder withPrice(double price) {
		this.price = price;
		return this;
	}

	public QuoteBuilder withPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
		return this;
	}

	public QuoteBuilder withNotationMorningStar(int notationMorningStar) {
		this.notationMorningStar = notationMorningStar;
		return this;
	}

	public QuoteBuilder withNotationLipperCapitalPreservation(int notationLipperCapitalPreservation) {
		this.notationLipperCapitalPreservation = notationLipperCapitalPreservation;
		return this;
	}

	public QuoteBuilder withNotationLipperRegularPerformances(int notationLipperRegularPerformances) {
		this.notationLipperRegularPerformances = notationLipperRegularPerformances;
		return this;
	}

	public QuoteBuilder withNotationLipperAbsolutePerformances(int notationLipperAbsolutePerformances) {
		this.notationLipperAbsolutePerformances = notationLipperAbsolutePerformances;
		return this;
	}

	public QuoteBuilder withSharpeRatio1(double sharpeRatio1) {
		this.sharpeRatio1 = sharpeRatio1;
		return this;
	}

	public QuoteBuilder withSharpeRatio3(double sharpeRatio3) {
		this.sharpeRatio3 = sharpeRatio3;
		return this;
	}

	public QuoteBuilder withSharpeRatio5(double sharpeRatio5) {
		this.sharpeRatio5 = sharpeRatio5;
		return this;
	}

	public QuoteBuilder withSharpeRatio10(double sharpeRatio10) {
		this.sharpeRatio10 = sharpeRatio10;
		return this;
	}

	public Quote build() {
		return new Quote(product, 
										 date, 
										 price, 
										 priceCurrency, 
										 notationMorningStar, 
										 notationLipperCapitalPreservation, 
										 notationLipperRegularPerformances,
										 notationLipperAbsolutePerformances, 
										 sharpeRatio1, 
										 sharpeRatio3, 
										 sharpeRatio5, 
										 sharpeRatio10);
	}
}
