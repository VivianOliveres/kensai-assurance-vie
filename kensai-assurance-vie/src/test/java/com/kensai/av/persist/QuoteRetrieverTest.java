package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.Test;

import com.kensai.av.assertions.KensaiAssertions;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.Quote;

public class QuoteRetrieverTest {

	private QuoteRetriever retriever = new QuoteRetriever();

	@Test
	public void should_retrieve_amundi() throws NumberFormatException, IOException {
		// GIVEN: amundi
		Product amundi = new Product("LU0568605769", null, false, false, "http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");
		Path htmlPath = Paths.get("src", "test", "resources", "amundi.html");

		// WHEN: retrieve
		Quote values = retriever.retrieve(htmlPath, amundi);

		// THEN: values are ok
		KensaiAssertions.assertThat(values).isNotNull()
													  .hasProduct(amundi)
													  .hasDate(LocalDate.now())
													  .hasPrice(148.170)
													  .hasPriceCurrency("USD")
													  .hasNotationMorningStar(2)
													  .hasNotationLipperAbsolutePerformances(3)
													  .hasNotationLipperCapitalPreservation(3)
													  .hasNotationLipperRegularPerformances(3)
													  .hasSharpeRatio1(1.05)
													  .hasSharpeRatio3(1)
													  .hasSharpeRatio5(1.28)
													  .hasSharpeRatio10(0.19);
	}

}
