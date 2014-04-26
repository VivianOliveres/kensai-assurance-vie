package com.kensai.av.gui.price;

import static javafx.collections.FXCollections.observableArrayList;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.gui.utils.LocalDateTimeAxis;

public class PriceViewController {
	private static Logger log = LogManager.getLogger(PriceViewController.class);

	private Series<LocalDateTime, Number> seriePrice = new Series<>("Price", FXCollections.observableArrayList());

	private LineChart<LocalDateTime, Number> priceChart = new LineChart<>(new LocalDateTimeAxis(), new NumberAxis());

	private BorderPane root = new BorderPane();

	public PriceViewController() {
		initChart();
		initRootNode();
	}

	private void initChart() {
		ObservableList<Series<LocalDateTime, Number>> datas = observableArrayList(seriePrice);
		priceChart.setData(datas);
	}

	private void initRootNode() {
		root.setCenter(priceChart);
	}

	public BorderPane getView() {
		return root;
	}

	public void updateView(ProductQuotes quotes) {
		priceChart.getData().forEach(serie -> serie.getData().clear());

		String currency = quotes.isEmpty() ? "" : quotes.getQuotes().get(0).getPriceCurrency();
		priceChart.setTitle("Price for " + quotes.getProduct().getIsin() + " [" + currency + "]");

		log.info("Update view with " + quotes.size() + " quotes on " + quotes.getProduct());
		for (Quote quote : quotes) {
			LocalDate date = quote.getDate();
			LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
			log.info("Add data: Date[" + time + "] Price[" + quote.getPrice() + "]]");
			seriePrice.getData().add(new Data<>(time, quote.getNotationMorningStar()));
		}
	}
}
