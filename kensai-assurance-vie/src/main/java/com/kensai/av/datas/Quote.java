package com.kensai.av.datas;

import java.time.LocalDate;

import com.google.common.base.Objects;

public class Quote {

	private final Product product;
	private final LocalDate date;
	private final double price;
	private final String priceCurrency;
	private final int notationMorningStar;
	private final int notationLipperCapitalPreservation;
	private final int notationLipperRegularPerformances;
	private final int notationLipperAbsolutePerformances;
	private final double sharpeRatio1;
	private final double sharpeRatio3;
	private final double sharpeRatio5;
	private final double sharpeRatio10;

	public Quote(Product product, LocalDate date, double price, String priceCurrency, int notationMorningStar, int notationLipperCapitalPreservation,
		int notationLipperRegularPerformances, int notationLipperAbsolutePerformances, double sharpeRatio1, double sharpeRatio3, double sharpeRatio5,
		double sharpeRatio10) {
		this.product = product;
		this.date = date;
		this.price = price;
		this.priceCurrency = priceCurrency;
		this.notationMorningStar = notationMorningStar;
		this.notationLipperCapitalPreservation = notationLipperCapitalPreservation;
		this.notationLipperRegularPerformances = notationLipperRegularPerformances;
		this.notationLipperAbsolutePerformances = notationLipperAbsolutePerformances;
		this.sharpeRatio1 = sharpeRatio1;
		this.sharpeRatio3 = sharpeRatio3;
		this.sharpeRatio5 = sharpeRatio5;
		this.sharpeRatio10 = sharpeRatio10;
	}

	public Product getProduct() {
		return product;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getPrice() {
		return price;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

	public int getNotationMorningStar() {
		return notationMorningStar;
	}

	public int getNotationLipperCapitalPreservation() {
		return notationLipperCapitalPreservation;
	}

	public int getNotationLipperRegularPerformances() {
		return notationLipperRegularPerformances;
	}

	public int getNotationLipperAbsolutePerformances() {
		return notationLipperAbsolutePerformances;
	}

	public double getSharpeRatio1() {
		return sharpeRatio1;
	}

	public double getSharpeRatio3() {
		return sharpeRatio3;
	}

	public double getSharpeRatio5() {
		return sharpeRatio5;
	}

	public double getSharpeRatio10() {
		return sharpeRatio10;
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(product, date);
	}

	@Override
	public boolean equals(Object object){
		if (object instanceof Quote) {
			Quote that = (Quote) object;
			return Objects.equal(this.product, that.product) && Objects.equal(this.date, that.date);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("product", product)
			.add("date", date)
			.add("price", price)
			.add("priceCurrency", priceCurrency)
			.add("notationMorningStar", notationMorningStar)
			.add("notationLipperCapitalPreservation", notationLipperCapitalPreservation)
			.add("notationLipperRegularPerformances", notationLipperRegularPerformances)
			.add("notationLipperAbsolutePerformances", notationLipperAbsolutePerformances)
			.add("sharpeRatio1", sharpeRatio1)
			.add("sharpeRatio3", sharpeRatio3)
			.add("sharpeRatio5", sharpeRatio5)
			.add("sharpeRatio10", sharpeRatio10)
			.toString();
	}

}
