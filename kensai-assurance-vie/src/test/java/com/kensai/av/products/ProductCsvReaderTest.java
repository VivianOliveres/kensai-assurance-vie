package com.kensai.av.products;


import static com.kensai.av.assertions.KensaiAssertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kensai.av.datas.Product;

public class ProductCsvReaderTest {

	private ProductCsvReader reader;

	@Before
	public void before() {
		reader = new ProductCsvReader();
	}

	@Test
	public void should_read_all_lines() throws IOException {
		// GIVEN: PATH
		Path path = Paths.get("src", "test", "resources", "products.csv");

		// WHEN: extract products
		List<Product> products = reader.extract(path);

		// THEN: 5 products
		assertThat(products).isNotNull().hasSize(5);
		assertThat(products.get(0)).isNotNull()
											.hasIsin("LU0119345287")
											.hasName("Pioneer Fds Euroland Equity A EUR ND")
											.isNotAV()
											.isPEA()
											.hasUrl("http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=MP-356460");
		assertThat(products.get(1)).isNotNull()
											.hasIsin("FR0007046578")
											.hasName("Europe Value Acc")
											.isAV()
											.isPEA()
											.hasUrl("http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=MP-804164");
		assertThat(products.get(2)).isNotNull()
											.hasIsin("FR0000445173")
											.hasName("Europe Value Inc")
											.isNotAV()
											.isPEA()
											.hasUrl("http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=MP-804165");
		assertThat(products.get(3)).isNotNull()
											.hasIsin("FR0010032326")
											.hasName("Allianz Euro High Yield RC")
											.isAV()
											.isNotPEA()
											.hasUrl("http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=MP-805965");
		assertThat(products.get(4)).isNotNull()
											.hasIsin("LU0568605769")
											.hasName("Amundi Fds Eq US Relative Value AU-C")
											.isAV()
											.isNotPEA()
											.hasUrl("http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");
	}
}
