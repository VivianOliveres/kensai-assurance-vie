package com.kensai.av.actions;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesWritter;
import com.kensai.av.persist.QuoteRetriever;

@RunWith(MockitoJUnitRunner.class)
public class UpdateQuotesActionTest {

	private UpdateQuotesAction action;

	@Mock private DatasZipper zipper;
	@Mock private ProductQuotesWritter writter;
	@Mock private QuoteRetriever retriever;

	@Before
	public void before() {
		action = new UpdateQuotesAction(retriever, writter, zipper);
	}

	@Test
	public void should_execute_action() throws IOException {
		// GIVEN: quotes for 2 products
		Product p1 = mock(Product.class);
		ProductQuotes quotes1 = mockProductQuotes(p1);
		Product p2 = mock(Product.class);
		ProductQuotes quotes2 = mockProductQuotes(p2);
		List<ProductQuotes> allQuotes = Lists.newArrayList(quotes1, quotes2);

		// WHEN:
		action.execute(allQuotes);

		// THEN: zipper has saved datas
		verify(zipper).save();

		// AND: for each product, its new quotes has been retrieve and saved
		for (ProductQuotes quotes : allQuotes) {
			verify(retriever).retrieve(quotes.getProduct());
			verify(quotes).add(any(Quote.class));
			verify(writter).write(quotes);
		}
	}

	private ProductQuotes mockProductQuotes(Product product) {
		ProductQuotes quotes = mock(ProductQuotes.class);
		when(quotes.getProduct()).thenReturn(product);
		return quotes;
	}

}
