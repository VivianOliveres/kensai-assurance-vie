package com.kensai.av.gui.morningstar;

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

public class MorningStarViewController {
	private static Logger log = LogManager.getLogger(MorningStarViewController.class);

	private Series<LocalDateTime, Number> serieMorningStar = new Series<>("Morning Star", FXCollections.observableArrayList());

	private LineChart<LocalDateTime, Number> morningStarChart = new LineChart<>(new LocalDateTimeAxis(), new NumberAxis());

	private BorderPane root = new BorderPane();

	public MorningStarViewController() {
		initChart();
		initRootNode();
	}

	private void initChart() {
		ObservableList<Series<LocalDateTime, Number>> datas = observableArrayList(serieMorningStar);
		morningStarChart.setData(datas);
	}

	private void initRootNode() {
		root.setCenter(morningStarChart);
	}

	public BorderPane getView() {
		return root;
	}

	public void updateView(ProductQuotes quotes) {
		morningStarChart.getData().forEach(serie -> serie.getData().clear());

		morningStarChart.setTitle("Morning Star notations for " + quotes.getProduct().getIsin());

		log.info("Update view with " + quotes.size() + " quotes on " + quotes.getProduct());
		for (Quote quote : quotes) {
			LocalDate date = quote.getDate();
			LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
			log.info("Add data: Date[" + time + "] MorningStar[" + quote.getNotationMorningStar() + "]]");
			serieMorningStar.getData().add(new Data<>(time, quote.getNotationMorningStar()));
		}
	}
}
