package com.kensai.av.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kensai.av.assertions.KensaiAssertions;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.persist.ProductQuotesReader;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;

public class UpdateQuoteActionITTest {
	private static final Product AMUNDI = new Product("LU0568605769", 
																	  "Amundi Fds Eq US Relative Value AU-C", 
																	  true, 
																	  false,
																	  "http://www.boursorama.com/bourse/opcvm/opcvm.phtml?symbole=0P0000TJC5");

	private Path currentFolder = Paths.get("target", "current");
	private Path oldFolder = Paths.get("target", "old");

	private ProductQuotesWritter writter;
	private QuoteRetriever retriever;

	private UpdateQuoteAction action;

	private ProductQuotes quotes;

	@Before
	public void before() throws IOException {
		Files.createDirectories(currentFolder);
		Files.createDirectories(oldFolder);
		
		writter = new ProductQuotesWritter(currentFolder);
		retriever = new QuoteRetriever();

		ProductQuotesReader reader = new ProductQuotesReader();
		Path path = Paths.get("src", "test", "resources");
		quotes = reader.extract(AMUNDI, path);
		KensaiAssertions.assertThat(quotes).hasSize(1);

		action = new UpdateQuoteAction(quotes, writter, retriever);
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
	public void should_retrieve_and_persist_quotes() throws IOException {
		// WHEN: Retrieve Quotes persist them
		boolean isExecuted = action.exec();

		// THEN: quotes has been added
		assertThat(isExecuted).isTrue();
		assertThat(quotes).hasSize(2);

		// THEN: 5 files have been wrote
		String[] listFilesName = currentFolder.toFile().list();
		assertThat(listFilesName).isNotNull().hasSize(1);
		assertThat(listFilesName).contains(AMUNDI.getIsin() + ".csv");
	}
}
