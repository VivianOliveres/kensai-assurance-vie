package com.kensai.av.datas;

import java.time.LocalDate;

public class ProductValuesBuilder {

	private Product product;
	private LocalDate date;
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

	public static ProductValuesBuilder create() {
		return new ProductValuesBuilder();
	}

	public ProductValuesBuilder withProduct(Product product) {
		this.product = product;
		return this;
	}

	public ProductValuesBuilder withDate(LocalDate date) {
		this.date = date;
		return this;
	}

	public ProductValuesBuilder withPrice(double price) {
		this.price = price;
		return this;
	}

	public ProductValuesBuilder withPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
		return this;
	}

	public ProductValuesBuilder withNotationMorningStar(int notationMorningStar) {
		this.notationMorningStar = notationMorningStar;
		return this;
	}

	public ProductValuesBuilder withNotationLipperCapitalPreservation(int notationLipperCapitalPreservation) {
		this.notationLipperCapitalPreservation = notationLipperCapitalPreservation;
		return this;
	}

	public ProductValuesBuilder withNotationLipperRegularPerformances(int notationLipperRegularPerformances) {
		this.notationLipperRegularPerformances = notationLipperRegularPerformances;
		return this;
	}

	public ProductValuesBuilder withNotationLipperAbsolutePerformances(int notationLipperAbsolutePerformances) {
		this.notationLipperAbsolutePerformances = notationLipperAbsolutePerformances;
		return this;
	}

	public ProductValuesBuilder withSharpeRatio1(double sharpeRatio1) {
		this.sharpeRatio1 = sharpeRatio1;
		return this;
	}

	public ProductValuesBuilder withSharpeRatio3(double sharpeRatio3) {
		this.sharpeRatio3 = sharpeRatio3;
		return this;
	}

	public ProductValuesBuilder withSharpeRatio5(double sharpeRatio5) {
		this.sharpeRatio5 = sharpeRatio5;
		return this;
	}

	public ProductValuesBuilder withSharpeRatio10(double sharpeRatio10) {
		this.sharpeRatio10 = sharpeRatio10;
		return this;
	}

	public ProductValues build() {
		return new ProductValues(product, 
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
