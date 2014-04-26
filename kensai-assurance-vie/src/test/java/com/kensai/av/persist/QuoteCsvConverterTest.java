package com.kensai.av.persist;

import org.junit.Test;

import com.kensai.av.assertions.KensaiAssertions;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.Quote;
import com.kensai.av.datas.QuoteBuilder;

public class QuoteCsvConverterTest {

	private static final String CSV_SEP = ";";
	private QuoteCsvConverter converter = new QuoteCsvConverter(CSV_SEP);

	@Test
	public void should_have_same_size() {
		// GIVEN: Product and quote
		Product amundi = new Product("LU0568605769", "Amundi Fds Eq US Relative Value AU-C", true, false,
			"http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");
		Quote quote = QuoteBuilder.create().withProduct(amundi).build();

		// WHEN: create csv comment line AND csv line
		String csvComment = converter.getCsvComment();
		String csvLine = converter.toCsvString(quote);

		// THEN: their size are the same
		int sizeCommentLine = csvComment.split(CSV_SEP).length;
		int sizeLine = csvLine.split(CSV_SEP).length;
		KensaiAssertions.assertThat(sizeCommentLine).isEqualTo(sizeLine);
	}
}
