package com.kensai.av;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.gui.products.ProductsViewController;
import com.kensai.av.products.Product;
import com.kensai.av.products.ProductCsvReader;

public class MainKensaiAV extends Application {

	private static Logger log = LogManager.getLogger(MainKensaiAV.class);

	@Override
	public void start(Stage stage) throws Exception {
		log.info("Start application");
		
		URL urlProducts = MainKensaiAV.class.getClassLoader().getResource("products.csv");
		Path productsPath = Paths.get(urlProducts.toURI());
		List<Product> products = new ProductCsvReader().extract(productsPath);
		ProductsViewController productsController = new ProductsViewController(products);

		// Init stage
		Scene scene = createScene(productsController);
		stage.setScene(scene);
		stage.setTitle("Kensai Assurance Vie");
		stage.show();
		log.info("Application started");
	}

	private Scene createScene(ProductsViewController productsController) {
		BorderPane root = new BorderPane();
		root.setLeft(productsController.getView());
		return new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
