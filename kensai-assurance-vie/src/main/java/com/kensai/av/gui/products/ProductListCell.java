package com.kensai.av.gui.products;

import javafx.scene.control.ListCell;

import com.kensai.av.products.Product;

public class ProductListCell extends ListCell<Product> {

	@Override
	public void updateItem(Product item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);

		} else {
			setText(item.getName());
		}
	}
}
