package com.kensai.av.gui.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

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
		MenuItem exitMenuItem=new MenuItem("Exit");
		EventStreams.changesOf(exitMenuItem.onActionProperty()).subscribe(event -> {
			log.info("User quit application");
			System.exit(0);
		});

		Menu fileMenu=new Menu("File");
		fileMenu.getItems().add(exitMenuItem);

		MenuItem updateMenuItem=new MenuItem("Update");
		EventStreams.changesOf(updateMenuItem.onActionProperty())
						.subscribe(event -> new UpdateQuotesAction().execute(service.getAllProductQuotes()));

		Menu actionsMenu=new Menu("Actions");
		actionsMenu.getItems().add(updateMenuItem);

		bar.getMenus().add(fileMenu);
		bar.getMenus().add(actionsMenu);
	}

	public MenuBar getView() {
		return bar;
	}

}
