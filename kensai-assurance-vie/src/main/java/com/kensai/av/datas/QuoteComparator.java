package com.kensai.av.datas;

import java.util.Comparator;

public class QuoteComparator implements Comparator<Quote> {

	@Override
	public int compare(Quote o1, Quote o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}
