package com.kensai.av;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.gui.histo.HistoViewController;
import com.kensai.av.gui.products.ProductsViewController;
import com.kensai.av.persist.DatasZipper;
import com.kensai.av.persist.ProductQuotesReader;
import com.kensai.av.products.ProductCsvReader;
import com.kensai.av.service.DataService;

public class MainKensaiAV extends Application {

	private static Logger log = LogManager.getLogger(MainKensaiAV.class);

	@Override
	public void start(Stage stage) throws Exception {
		log.info("Start application");

		// Read all products
		URL urlProducts = MainKensaiAV.class.getClassLoader().getResource("products.csv");
		Path productsPath = Paths.get(urlProducts.toURI());
		List<Product> products = new ProductCsvReader().extract(productsPath);

		// Read all Quotes
		ProductQuotesReader reader = new ProductQuotesReader();
		List<ProductQuotes> allQuotes = new ArrayList<>();
		for (Product product : products) {
			ProductQuotes quotes = reader.extract(product, DatasZipper.CURRENT_FOLDER);
			allQuotes.add(quotes);
		}

		// Create Services and views
		DataService service = new DataService(allQuotes);
		ProductsViewController productsController = new ProductsViewController(service);

		HistoViewController histoController = new HistoViewController();
		productsController.getSelectionEventStream().subscribe(event -> histoController.updateView(event));

		// Init stage
		Scene scene = createScene(productsController, histoController);
		stage.setScene(scene);
		stage.setTitle("Kensai Assurance Vie");
		stage.show();
		log.info("Application started");
	}

	private Scene createScene(ProductsViewController productsController, HistoViewController histoController) {
		BorderPane root = new BorderPane();
		root.setLeft(productsController.getView());
		root.setCenter(histoController.getView());
		return new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
