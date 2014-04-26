package com.kensai.av.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.products.ProductCsvReader;

public class UpdateQuotesActionITTest {

	private Path currentFolder = Paths.get("target", "current");
	private Path oldFolder = Paths.get("target", "old");

	private UpdateQuotesAction action;

	@Before
	public void before() throws IOException {
		Files.createDirectories(currentFolder);
		Files.createDirectories(oldFolder);

		action = new UpdateQuotesAction(currentFolder, oldFolder);
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
		// GIVEN: 5 products
		Path productCsvPath = Paths.get("src", "test", "resources", "products.csv");
		ProductCsvReader productReader = new ProductCsvReader();
		List<Product> products = productReader.extract(productCsvPath);
		assertThat(products).hasSize(5);

		// AND: Quotes for all
		List<ProductQuotes> allQuotes = new ArrayList<ProductQuotes>();
		products.forEach(product -> allQuotes.add(new ProductQuotes(product)));

		// WHEN: Retrieve Quotes for each product and persist it
		action.execute(allQuotes);

		// THEN: 5 files have been wrote
		String[] listFilesName = currentFolder.toFile().list();
		assertThat(listFilesName).isNotNull().hasSize(5);
		for (Product product : products) {
			assertThat(listFilesName).contains(product.getIsin() + ".csv");
		}

		// WHEN: DataZip files
		String[] zipFilesName = oldFolder.toFile().list();
		assertThat(zipFilesName).isNotNull().hasSize(0);
	}
}
