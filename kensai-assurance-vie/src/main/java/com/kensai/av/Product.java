package com.kensai.av;

import com.google.common.base.Objects;

public class Product {

	private final String name;
	private final boolean isPEA;
	private final boolean isAV;
	private final String url;
	
	public Product(String name, boolean isPEA, boolean isAV, String url) {
		this.name = name;
		this.isPEA = isPEA;
		this.isAV = isAV;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public boolean isPEA() {
		return isPEA;
	}

	public boolean isAV() {
		return isAV;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(name, isPEA, isAV, url);
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof Product) {
			Product that = (Product) object;
			return Objects.equal(this.name, that.name)
				&& Objects.equal(this.isPEA, that.isPEA)
				&& Objects.equal(this.isAV, that.isAV)
				&& Objects.equal(this.url, that.url);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("name", name)
			.add("isPEA", isPEA)
			.add("isAV", isAV)
			.add("url", url)
			.toString();
	}
	
}
