package com.kensai.av.gui.histo;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import org.reactfx.inhibeans.property.SimpleStringProperty;

import com.kensai.av.datas.ProductQuotes;
import com.kensai.av.datas.Quote;

public class HistoViewController {

	private BorderPane root = new BorderPane();
	private TableView<Quote> quoteTable = new TableView<>();
	private ObservableList<Quote> rows;

	public HistoViewController() {
		initTable();
		initRootNode();

		rows = quoteTable.getItems();
	}

	private void initTable() {
		// Initialize ColumnResizePolicy
		quoteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Initialize all columns
		TableColumn<Quote, String> columnIsin = new TableColumn<>("Isin");
		columnIsin.setCellValueFactory((cell) -> new SimpleStringProperty(cell.getValue().getProduct().getIsin()));
		quoteTable.getColumns().add(columnIsin);

		TableColumn<Quote, String> columnName = new TableColumn<>("Name");
		columnName.setCellValueFactory((cell) -> new SimpleStringProperty(cell.getValue().getProduct().getName()));
		quoteTable.getColumns().add(columnName);

		TableColumn<Quote, LocalDate> columnDate = new TableColumn<>("Date");
		columnDate.setCellValueFactory((cell) -> new SimpleObjectProperty<LocalDate>(cell.getValue().getDate()));
		quoteTable.getColumns().add(columnDate);

		TableColumn<Quote, Number> columnPrice = new TableColumn<>("Price");
		columnPrice.setCellValueFactory((cell) -> new SimpleDoubleProperty(cell.getValue().getPrice()));
		quoteTable.getColumns().add(columnPrice);

		TableColumn<Quote, String> columnPriceCurrency = new TableColumn<>("Currency");
		columnPriceCurrency.setCellValueFactory((cell) -> new SimpleStringProperty(cell.getValue().getPriceCurrency()));
		quoteTable.getColumns().add(columnPriceCurrency);

		TableColumn<Quote, Number> columnNotationMorningStar = new TableColumn<>("MorningStar");
		columnNotationMorningStar.setCellValueFactory((cell) -> new SimpleIntegerProperty(cell.getValue().getNotationMorningStar()));
		quoteTable.getColumns().add(columnNotationMorningStar);

		TableColumn<Quote, Number> columnNotationLipperAbsPerf = new TableColumn<>("Lipper [AbsPerf]");
		columnNotationLipperAbsPerf.setCellValueFactory((cell) -> new SimpleIntegerProperty(cell.getValue().getNotationLipperAbsolutePerformances()));
		quoteTable.getColumns().add(columnNotationLipperAbsPerf);

		TableColumn<Quote, Number> columnNotationLipperCapPreserv = new TableColumn<>("Lipper [CapPreserv]");
		columnNotationLipperCapPreserv.setCellValueFactory((cell) -> new SimpleIntegerProperty(cell.getValue().getNotationLipperCapitalPreservation()));
		quoteTable.getColumns().add(columnNotationLipperCapPreserv);

		TableColumn<Quote, Number> columnNotationLipperRegularPerf = new TableColumn<>("Lipper [RegularPerf]");
		columnNotationLipperRegularPerf.setCellValueFactory((cell) -> new SimpleIntegerProperty(cell.getValue().getNotationLipperRegularPerformances()));
		quoteTable.getColumns().add(columnNotationLipperRegularPerf);

		TableColumn<Quote, Number> columnSharpeRatio1 = new TableColumn<>("SharpeRatio1");
		columnSharpeRatio1.setCellValueFactory((cell) -> new SimpleDoubleProperty(cell.getValue().getSharpeRatio1()));
		quoteTable.getColumns().add(columnSharpeRatio1);

		TableColumn<Quote, Number> columnSharpeRatio3 = new TableColumn<>("SharpeRatio3");
		columnSharpeRatio3.setCellValueFactory((cell) -> new SimpleDoubleProperty(cell.getValue().getSharpeRatio3()));
		quoteTable.getColumns().add(columnSharpeRatio3);

		TableColumn<Quote, Number> columnSharpeRatio5 = new TableColumn<>("SharpeRatio5");
		columnSharpeRatio5.setCellValueFactory((cell) -> new SimpleDoubleProperty(cell.getValue().getSharpeRatio5()));
		quoteTable.getColumns().add(columnSharpeRatio5);

		TableColumn<Quote, Number> columnSharpeRatio10 = new TableColumn<>("SharpeRatio10");
		columnSharpeRatio10.setCellValueFactory((cell) -> new SimpleDoubleProperty(cell.getValue().getSharpeRatio10()));
		quoteTable.getColumns().add(columnSharpeRatio10);
	}

	private void initRootNode() {
		root.setCenter(quoteTable);
	}

	public BorderPane getView() {
		return root;
	}

	public void updateView(ProductQuotes quotes) {
		rows.clear();
		rows.setAll(quotes.getQuotes());
	}

}
