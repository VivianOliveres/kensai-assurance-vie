package com.kensai.av.gui.lipper;

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

import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;
import com.kensai.av.gui.utils.LocalDateTimeAxis;

public class LipperViewController {
	private Series<LocalDateTime, Number> serieAbsPerf = new Series<>("Absolute Perf.", FXCollections.observableArrayList());
	private Series<LocalDateTime, Number> serieCapitalPreservation = new Series<>("Capital Preservation", FXCollections.observableArrayList());
	private Series<LocalDateTime, Number> serieRegularPerf = new Series<>("Regular Perf.", FXCollections.observableArrayList());

	private LineChart<LocalDateTime, Number> lipperChart = new LineChart<>(new LocalDateTimeAxis(), new NumberAxis());

	private BorderPane root = new BorderPane();

	public LipperViewController() {
		initChart();
		initRootNode();
	}

	private void initChart() {
		ObservableList<Series<LocalDateTime, Number>> datas = observableArrayList(serieAbsPerf, serieCapitalPreservation, serieRegularPerf);
		lipperChart.setData(datas);
	}

	private void initRootNode() {
		root.setCenter(lipperChart);
	}

	public BorderPane getView() {
		return root;
	}

	public void updateView(ProductQuotes quotes) {
		lipperChart.getData().forEach(serie -> serie.getData().clear());

		lipperChart.setTitle("Lipper notations for " + quotes.getProduct().getIsin());

		for (Quote quote : quotes) {
			LocalDate date = quote.getDate();
			LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
			serieAbsPerf.getData().add(new Data<>(time, quote.getNotationLipperAbsolutePerformances()));
			serieCapitalPreservation.getData().add(new Data<>(time, quote.getNotationLipperCapitalPreservation()));
			serieRegularPerf.getData().add(new Data<>(time, quote.getNotationLipperRegularPerformances()));
		}
	}
}
