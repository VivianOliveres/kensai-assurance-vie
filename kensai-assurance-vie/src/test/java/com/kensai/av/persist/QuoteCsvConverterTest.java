package com.kensai.av.persist;

import static com.kensai.av.assertions.KensaiAssertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import com.kensai.av.assertions.KensaiAssertions;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.Quote;
import com.kensai.av.datas.QuoteBuilder;

public class QuoteCsvConverterTest {
	private static final Product AMUNDI = new Product("LU0568605769", 
																	  "Amundi Fds Eq US Relative Value AU-C", 
																	  true, 
																	  false,
																	  "http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");

	private static final String CSV_SEP = ";";
	private QuoteCsvConverter converter = new QuoteCsvConverter(CSV_SEP);

	@Test
	public void should_have_same_size() {
		// GIVEN: Product and quote
		Quote quote = QuoteBuilder.create().withProduct(AMUNDI).build();

		// WHEN: create csv comment line AND csv line
		String csvComment = converter.getCsvComment();
		String csvLine = converter.toCsvString(quote);

		// THEN: their size are the same
		int sizeCommentLine = csvComment.split(CSV_SEP).length;
		int sizeLine = csvLine.split(CSV_SEP).length;
		KensaiAssertions.assertThat(sizeCommentLine).isEqualTo(sizeLine);
	}
	
	@Test
	public void should_convert_from_csv_line() {
		// GIVEN: csv line
		String line = "LU0568605769;2014-04-26;148.17;USD;2;3;3;3;1.05;1.0;1.28;0.19";

		// WHEN: convert
		Quote quote = converter.fromCsvString(AMUNDI, line);

		// THEN: 
		assertThat(quote).isNotNull()
							  .hasProduct(AMUNDI)
							  .hasDate(LocalDate.of(2014, 04, 26))
							  .hasPrice(148.17)
							  .hasPriceCurrency("USD")
							  .hasNotationMorningStar(2)
							  .hasNotationLipperCapitalPreservation(3)
							  .hasNotationLipperRegularPerformances(3)
							  .hasNotationLipperAbsolutePerformances(3)
							  .hasSharpeRatio1(1.05)
							  .hasSharpeRatio3(1.0)
							  .hasSharpeRatio5(1.28)
							  .hasSharpeRatio10(0.19);
	}
}
