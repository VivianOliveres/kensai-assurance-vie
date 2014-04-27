package com.kensai.av.gui.menus;

import javafx.animation.FadeTransition;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactfx.EventStreams;

import com.kensai.av.actions.UpdateQuotesAction;
import com.kensai.av.service.DataService;

public class MenuViewController {
	private static Logger log = LogManager.getLogger(MenuViewController.class);

	private DataService service;
	private MenuBar bar = new MenuBar();

	public MenuViewController(DataService service) {
		this.service = service;
		initRootNode();
	}

	private void initRootNode() {
		MenuItem exitMenuItem = new MenuItem("Exit");
		EventStreams.changesOf(exitMenuItem.onActionProperty()).subscribe(event -> {
			log.info("User quit application");
			System.exit(0);
		});

		Menu fileMenu = new Menu("File");
		fileMenu.getItems().add(exitMenuItem);

		MenuItem updateMenuItem = new MenuItem("Update");
		updateMenuItem.setOnAction(event -> doUpdate(event));

		Menu actionsMenu = new Menu("Actions");
		actionsMenu.getItems().add(updateMenuItem);

		bar.getMenus().add(fileMenu);
		bar.getMenus().add(actionsMenu);
	}

	private Void doUpdate(ActionEvent event) {
		// Fade GUI
		log.info("doUpdate - fade transition");
		FadeTransition fade = new FadeTransition(Duration.seconds(1.5), bar.getParent());
		fade.setFromValue(1);
		fade.setToValue(0.7);
		fade.playFromStart();

		// Start update task
		UpdateQuotesAction action = new UpdateQuotesAction(service);
		new Thread(action).start();

		// Show a dialog with a progress bar
		ProgressBar progressBar = new ProgressBar();
		progressBar.progressProperty().bind(action.progressProperty());

		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.setScene(new Scene(progressBar));
		dialog.show();

		action.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				log.info("Update finish -> un-fade");
				fade.setRate(-1);
				fade.play();

				dialog.close();
			}

		});

		return null;
	}

	public MenuBar getView() {
		return bar;
	}

}
