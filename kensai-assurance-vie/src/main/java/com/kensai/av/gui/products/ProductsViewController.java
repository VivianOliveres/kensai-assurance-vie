package com.kensai.av.gui.products;

import java.util.List;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;

import com.kensai.av.datas.Product;

public class ProductsViewController {

	private BorderPane root = new BorderPane();
	private ListView<Product> productsList = new ListView<>();

	public ProductsViewController(List<Product> products) {
		initProductsListView(products);
		initRootNode();
	}

	private void initProductsListView(List<Product> products) {
		productsList.setCellFactory(list -> new ProductListCell());
		productsList.getItems().setAll(products);
		productsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private void initRootNode() {
		root.setCenter(productsList);
	}

	public BorderPane getView() {
		return root;
	}

}
