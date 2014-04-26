package com.kensai.av.gui.products;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;

import org.reactfx.EventStream;
import org.reactfx.EventStreams;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.service.DataService;

public class ProductsViewController {

	private DataService service;

	private BorderPane root = new BorderPane();
	private ListView<Product> productsList = new ListView<>();

	private EventStream<ProductQuotes> eventStream;

	public ProductsViewController(DataService service) {
		this.service = service;
		initProductsListView(service.getProducts());
		initRootNode();
	}

	private void initProductsListView(List<Product> products) {
		productsList.setCellFactory(list -> new ProductListCell());
		productsList.getItems().setAll(products);
		productsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		ReadOnlyObjectProperty<Product> selectedItemProperty = productsList.getSelectionModel().selectedItemProperty();
		eventStream = EventStreams.changesOf(selectedItemProperty).map(change -> service.getProductQuotes(change.getNewValue()));
	}

	private void initRootNode() {
		root.setCenter(productsList);
	}

	public BorderPane getView() {
		return root;
	}

	public EventStream<ProductQuotes> getSelectionEventStream() {
		return eventStream;
	}

}
