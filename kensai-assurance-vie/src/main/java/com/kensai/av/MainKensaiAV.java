package com.kensai.av;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.gui.GuiInitializer;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesReader;
import com.kensai.av.products.ProductCsvReader;
import com.kensai.av.service.DataService;

public class MainKensaiAV extends Application {

	private static Logger log = LogManager.getLogger(MainKensaiAV.class);

	@Override
	public void start(Stage stage) throws Exception {
		log.info("Start application");

		DataService service = createDataService();

		// Create views
		GuiInitializer initializer = new GuiInitializer(service);

		// Init stage
		Scene scene = createScene(initializer);
		scene.getStylesheets().add("style-dark.css");
		stage.setScene(scene);
		stage.setTitle("Kensai Assurance Vie");
		stage.show();
		log.info("Application started");
	}

	private DataService createDataService() throws URISyntaxException, IOException {
		// Read all products
		// URL urlProducts = MainKensaiAV.class.getClassLoader().getResource("products.csv");
		// Path productsPath = Paths.get(urlProducts.toURI());
		Path productsPath = Paths.get("datas", "current", "products.csv");
		List<Product> products = new ProductCsvReader().extract(productsPath);
		log.info("Initialized with " + products.size() + " products");

		// Read all Quotes
		ProductQuotesReader reader = new ProductQuotesReader();
		List<ProductQuotes> allQuotes = new ArrayList<>();
		for (Product product : products) {
			ProductQuotes quotes = reader.extract(product, DatasZipper.CURRENT_FOLDER);
			allQuotes.add(quotes);
		}

		// Create Services
		DataService service = new DataService(allQuotes);
		return service;
	}

	private Scene createScene(GuiInitializer initializer) {
		BorderPane root = new BorderPane();

		root.setTop(initializer.getMenuCtrl().getView());

		root.setLeft(initializer.getProductsCtrl().getView());
		root.setCenter(initializer.getHistoCtrl().getView());

		HBox bottom = new HBox();
		bottom.getChildren().add(initializer.getSharpeRatioCtrl().getView());
		bottom.getChildren().add(initializer.getMorningStarCtrl().getView());
		bottom.getChildren().add(initializer.getLipperViewCtrl().getView());
		bottom.getChildren().add(initializer.getPriceCtrl().getView());
		root.setBottom(bottom);

		VBox righ = new VBox();
		root.setRight(righ);

		return new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
