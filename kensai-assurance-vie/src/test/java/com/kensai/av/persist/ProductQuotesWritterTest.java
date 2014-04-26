package com.kensai.av.persist;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.datas.QuoteBuilder;

public class ProductQuotesWritterTest {

	private static final Product AMUNDI = new Product("LU0568605769", 
																	  "Amundi Fds Eq US Relative Value AU-C", 
																	  true, 
																	  false,
																	  "http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");

	private final static Quote QUOTE = QuoteBuilder.create()
																  .withProduct(AMUNDI)
																  .withNotationLipperAbsolutePerformances(1)
																  .withNotationLipperCapitalPreservation(2)
																  .withNotationLipperRegularPerformances(3)
																  .withPrice(123.456)
																  .withPriceCurrency("EUR")
																  .withSharpeRatio1(1.01)
																  .withSharpeRatio3(1.03)
																  .withSharpeRatio5(1.05)
																  .withSharpeRatio10(1.10)
																  .build();

	private Path folder;
	private ProductQuotesWritter writter;

	@Before
	public void before() throws IOException {
		// Create folder wich will contains file with quotes
		folder = Paths.get("target", "file-tests");
		Files.createDirectories(folder);
		assertThat(Files.exists(folder));
		assertThat(Files.isDirectory(folder));

		// Init writter
		writter = new ProductQuotesWritter(folder);
	}

	@After
	public void after() throws IOException {
		// Clean files
		Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});
	}

	@Test
	public void should_write_quote() throws IOException {
		// GIVEN: Quotes
		ProductQuotes quotes = new ProductQuotes(AMUNDI, QUOTE);

		// WHEN: write Quotes
		Path path = writter.write(quotes);

		// THEN: File is created
		assertThat(path).isNotNull();
		assertThat(Files.exists(path)).isTrue();

		// AND contains two lines
		List<String> allLines = Files.readAllLines(path);
		QuoteCsvConverter converter = new QuoteCsvConverter();
		assertThat(allLines).hasSize(2).containsExactly(converter.getCsvComment(), converter.toCsvString(QUOTE));
	}
}
