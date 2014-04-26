package com.kensai.av.gui.sharpe;

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

public class SharpeRatioViewController {
	private Series<LocalDateTime, Number> serie1 = new Series<>("1 An", FXCollections.observableArrayList());
	private Series<LocalDateTime, Number> serie3 = new Series<>("3 Ans", FXCollections.observableArrayList());
	private Series<LocalDateTime, Number> serie5 = new Series<>("5 Ans", FXCollections.observableArrayList());
	private Series<LocalDateTime, Number> serie10 = new Series<>("10 Ans", FXCollections.observableArrayList());

	private LineChart<LocalDateTime, Number> sharpeRatioChart = new LineChart<>(new LocalDateTimeAxis(), new NumberAxis());

	private BorderPane root = new BorderPane();

	public SharpeRatioViewController() {
		initChart();
		initRootNode();
	}

	private void initChart() {
		ObservableList<Series<LocalDateTime, Number>> datas = observableArrayList(serie1, serie3, serie5, serie10);
		sharpeRatioChart.setData(datas);
	}

	private void initRootNode() {
		root.setCenter(sharpeRatioChart);
	}

	public BorderPane getView() {
		return root;
	}

	public void updateView(ProductQuotes quotes) {
		sharpeRatioChart.getData().forEach(serie -> serie.getData().clear());

		sharpeRatioChart.setTitle("Sharpe Ratios for " + quotes.getProduct().getIsin());

		for (Quote quote : quotes) {
			LocalDate date = quote.getDate();
			LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
			serie1.getData().add(new Data<>(time, quote.getSharpeRatio1()));
			serie3.getData().add(new Data<>(time, quote.getSharpeRatio3()));
			serie5.getData().add(new Data<>(time, quote.getSharpeRatio5()));
			serie10.getData().add(new Data<>(time, quote.getSharpeRatio10()));
		}
	}
}
