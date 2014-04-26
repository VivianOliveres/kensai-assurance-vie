package com.kensai.av;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;
import com.kensai.av.products.ProductCsvReader;

public class RetrieveAllQuotesTest {

	private Path currentFolder = Paths.get("target", "current");
	private Path oldFolder = Paths.get("target", "old");

	@Before
	public void before() throws IOException {
		Files.createDirectories(currentFolder);
		Files.createDirectories(oldFolder);
	}

	@After
	public void after() throws IOException {
		// Clean files
		SimpleFileVisitor<Path> deleteFileVisitor = new SimpleFileVisitor<Path>() {
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

		};
		Files.walkFileTree(oldFolder, deleteFileVisitor);
		Files.walkFileTree(currentFolder, deleteFileVisitor);
	}

	@Test
	public void should_retrieve_and_persist_all_quotes_and_save_previous_quotes() throws IOException {
		// WHEN: read all products
		Path productCsvPath = Paths.get("src", "test", "resources", "products.csv");
		ProductCsvReader productReader = new ProductCsvReader();
		List<Product> products = productReader.extract(productCsvPath);

		// THEN: found 5 products
		assertThat(products).hasSize(5);

		// WHEN: Retrieve Quotes for each product and persist it
		QuoteRetriever retriever = new QuoteRetriever();
		ProductQuotesWritter writter = new ProductQuotesWritter(currentFolder);
		for (Product product : products) {
			Quote quote = retriever.retrieve(product);
			ProductQuotes quotes = new ProductQuotes(product, quote);
			writter.write(quotes);
		}

		// THEN: 5 files have been wrote
		String[] listFilesName = currentFolder.toFile().list();
		assertThat(listFilesName).isNotNull().hasSize(5);
		for (Product product : products) {
			assertThat(listFilesName).contains(product.getIsin() + ".csv");
		}

		// WHEN: DataZip files
		DatasZipper zipper = new DatasZipper(currentFolder, oldFolder);
		Path saveZip = zipper.save();
		assertThat(Files.exists(saveZip)).isTrue();
		assertThat(saveZip.toFile().getName().equals(LocalDate.now() + "zip"));
	}
}
