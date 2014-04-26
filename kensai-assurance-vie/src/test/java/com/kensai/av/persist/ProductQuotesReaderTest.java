package com.kensai.av.persist;

import static com.kensai.av.assertions.KensaiAssertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.kensai.av.assertions.KensaiAssertions;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;

public class ProductQuotesReaderTest {
	private static final Product AMUNDI = new Product("LU0568605769", 
																	  "Amundi Fds Eq US Relative Value AU-C", 
																	  true, 
																	  false,
																	  "http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");

	private ProductQuotesReader reader;

	@Before
	public void before() {
		reader = new ProductQuotesReader();
	}
	
	@Test
	public void should_read_quotes_for_amundi() throws IOException {
		// GIVEN: Quotes file for AMUNDI
		Path path = Paths.get("src", "test", "resources");
		KensaiAssertions.assertThat(Files.exists(path)).isTrue();

		// WHEN: read quotes
		ProductQuotes quotes = reader.extract(AMUNDI, path);

		// THEN: 1 quote
		KensaiAssertions.assertThat(quotes).isNotNull().hasSize(1);

		Quote quote = quotes.getQuotes().get(0);
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
