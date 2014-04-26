package com.kensai.av;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.gui.histo.HistoViewController;
import com.kensai.av.gui.lipper.LipperViewController;
import com.kensai.av.gui.products.ProductsViewController;
import com.kensai.av.gui.sharpe.SharpeRatioViewController;
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
		ProductsViewController productsController = new ProductsViewController(service);

		HistoViewController histoController = new HistoViewController();
		productsController.getSelectionEventStream().subscribe(event -> histoController.updateView(event));

		SharpeRatioViewController sharpeRatioController = new SharpeRatioViewController();
		productsController.getSelectionEventStream().subscribe(event -> sharpeRatioController.updateView(event));

		LipperViewController lipperViewController = new LipperViewController();
		productsController.getSelectionEventStream().subscribe(event -> lipperViewController.updateView(event));

		// Init stage
		Scene scene = createScene(productsController, histoController, sharpeRatioController, lipperViewController);
		stage.setScene(scene);
		stage.setTitle("Kensai Assurance Vie");
		stage.show();
		log.info("Application started");
	}

	private DataService createDataService() throws URISyntaxException, IOException {
		// Read all products
		URL urlProducts = MainKensaiAV.class.getClassLoader().getResource("products.csv");
		Path productsPath = Paths.get(urlProducts.toURI());
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

	private Scene createScene(ProductsViewController productsCtrl, 
									  HistoViewController histoCtrl,
									  SharpeRatioViewController sharpeRatioCtrl,
									  LipperViewController lipperCtrl) {

		BorderPane root = new BorderPane();
		root.setLeft(productsCtrl.getView());
		root.setCenter(histoCtrl.getView());

		VBox righNode = new VBox();
		righNode.getChildren().add(sharpeRatioCtrl.getView());
		righNode.getChildren().add(lipperCtrl.getView());
		root.setRight(righNode);

		return new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
