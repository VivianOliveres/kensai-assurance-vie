package com.kensai.av.gui;

import com.kensai.av.gui.histo.HistoViewController;
import com.kensai.av.gui.lipper.LipperViewController;
import com.kensai.av.gui.menus.MenuViewController;
import com.kensai.av.gui.morningstar.MorningStarViewController;
import com.kensai.av.gui.price.PriceViewController;
import com.kensai.av.gui.products.ProductsViewController;
import com.kensai.av.gui.sharpe.SharpeRatioViewController;
import com.kensai.av.service.DataService;

public class GuiInitializer {

	private DataService service;

	private ProductsViewController productsCtrl;
	private HistoViewController histoCtrl;
	private SharpeRatioViewController sharpeRatioCtrl;
	private LipperViewController lipperViewCtrl;
	private MorningStarViewController morningStarCtrl;
	private PriceViewController priceCtrl;
	private MenuViewController menuCtrl;

	public GuiInitializer(DataService service) {
		this.service = service;
		init();
	}

	private void init() {
		productsCtrl = new ProductsViewController(service);

		histoCtrl = new HistoViewController();
		productsCtrl.getSelectionEventStream().subscribe(event -> histoCtrl.updateView(event));

		sharpeRatioCtrl = new SharpeRatioViewController();
		productsCtrl.getSelectionEventStream().subscribe(event -> sharpeRatioCtrl.updateView(event));

		lipperViewCtrl = new LipperViewController();
		productsCtrl.getSelectionEventStream().subscribe(event -> lipperViewCtrl.updateView(event));

		morningStarCtrl = new MorningStarViewController();
		productsCtrl.getSelectionEventStream().subscribe(event -> morningStarCtrl.updateView(event));

		priceCtrl = new PriceViewController();
		productsCtrl.getSelectionEventStream().subscribe(event -> priceCtrl.updateView(event));

		menuCtrl = new MenuViewController(service);
	}

	public DataService getService() {
		return service;
	}

	public ProductsViewController getProductsCtrl() {
		return productsCtrl;
	}

	public HistoViewController getHistoCtrl() {
		return histoCtrl;
	}

	public SharpeRatioViewController getSharpeRatioCtrl() {
		return sharpeRatioCtrl;
	}

	public LipperViewController getLipperViewCtrl() {
		return lipperViewCtrl;
	}

	public MorningStarViewController getMorningStarCtrl() {
		return morningStarCtrl;
	}

	public PriceViewController getPriceCtrl() {
		return priceCtrl;
	}

	public MenuViewController getMenuCtrl() {
		return menuCtrl;
	}

}
