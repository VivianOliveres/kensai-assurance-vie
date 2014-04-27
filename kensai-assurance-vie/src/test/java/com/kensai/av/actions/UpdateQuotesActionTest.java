package com.kensai.av.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.kensai.av.service.DataService;

@RunWith(MockitoJUnitRunner.class)
public class UpdateQuotesActionTest {

	private UpdateQuotesAction action;

	@Mock private DatasZipper zipper;
	@Mock private ProductQuotesWritter writter;
	@Mock private QuoteRetriever retriever;
	@Mock private DataService service;

	@Mock Product p1 = mock(Product.class);
	@Mock Product p2 = mock(Product.class);

	@Mock private ProductQuotes quotes1;
	@Mock private ProductQuotes quotes2;
	

	@Before
	public void before() {
		when(quotes1.getProduct()).thenReturn(p1);
		when(quotes2.getProduct()).thenReturn(p2);
		
		when(service.getAllProductQuotes()).thenReturn(Lists.newArrayList(quotes1, quotes2));

		action = new UpdateQuotesAction(service, retriever, writter, zipper);
	}

	@Test
	public void should_execute_action() throws Exception {
		// WHEN: call
		List<ProductQuotes> result = action.call();

		// THEN: result should be ok
		assertThat(result).containsOnly(quotes1, quotes2);

		// AND: zipper has saved datas
		verify(zipper).save();

		// AND: new quotes has been retrieve and saved for product 1
		verify(retriever).retrieve(p1);
		verify(quotes1).add(any(Quote.class));
		verify(writter).write(quotes1);

		// AND: new quotes has been retrieve and saved for product 2
		verify(retriever).retrieve(p2);
		verify(quotes2).add(any(Quote.class));
		verify(writter).write(quotes2);
	}

}
