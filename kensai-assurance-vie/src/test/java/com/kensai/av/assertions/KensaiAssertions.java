package com.kensai.av.assertions;

import org.assertj.core.api.Assertions;

import com.kensai.av.products.Product;

/**
 * Entry point for assertion of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
public class KensaiAssertions extends Assertions {

	/**
	 * Creates a new instance of <code>{@link ProductAssert}</code>.
	 *
	 * @param actual the actual value.
	 * @return the created assertion object.
	 */
	public static ProductAssert assertThat(Product actual) {
		return new ProductAssert(actual);
	}

	/**
	 * Creates a new </code>{@link Assertions}</code>.
	 */
	protected KensaiAssertions() {
		// empty
	}
}
