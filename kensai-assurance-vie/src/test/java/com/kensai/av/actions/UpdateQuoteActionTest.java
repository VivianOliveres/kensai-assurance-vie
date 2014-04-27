package com.kensai.av.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;

@RunWith(MockitoJUnitRunner.class)
public class UpdateQuoteActionTest {

	private UpdateQuoteAction action;

	@Mock private DatasZipper zipper;
	@Mock private ProductQuotesWritter writter;
	@Mock private QuoteRetriever retriever;

	@Mock private Product product = mock(Product.class);
	@Mock private ProductQuotes quotes = mock(ProductQuotes.class);

	@Before
	public void before() {
		when(quotes.getProduct()).thenReturn(product);

		action = new UpdateQuoteAction(quotes, writter, retriever);
	}

	@Test
	public void should_execute_action() throws IOException {
		// WHEN: exec
		boolean isExecuted = action.exec();

		// THEN: works well
		assertThat(isExecuted).isTrue();

		// AND: has worked on correct Quotes
		assertThat(action.getRawResult()).isEqualTo(quotes);

		// AND: new quotes has been retrieve and saved
		verify(retriever).retrieve(quotes.getProduct());
		verify(quotes).add(any(Quote.class));
		verify(writter).write(quotes);
	}

}
