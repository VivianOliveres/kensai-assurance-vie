package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kensai.av.datas.Product;
import com.kensai.av.datas.ProductValues;
import com.kensai.av.datas.ProductValuesBuilder;

public class ProductValuesRetriever {
	private static Logger log = LogManager.getLogger(ProductValuesRetriever.class);

	public ProductValues retrieveSafe(Product product) {
		try {
			return retrieve(product);

		} catch (IOException | NumberFormatException e) {
			log.error("Can not retrieve " + product, e);
			return null;
		}
	}

	public ProductValues retrieve(Path htmlPath, Product product) throws IOException, NumberFormatException {
		Document doc = Jsoup.parse(htmlPath.toFile(), "UTF-8", product.getUrl());
		return retrieve(doc, product);
	}

	public ProductValues retrieve(Product product) throws IOException, NumberFormatException {
		Document doc = Jsoup.connect(product.getUrl()).get();
		return retrieve(doc, product);
	}

	private ProductValues retrieve(Document doc, Product product) throws IOException, NumberFormatException {
		ProductValuesBuilder builder = ProductValuesBuilder.create().withDate(LocalDate.now()).withProduct(product);

		// NAME
		// Element name = doc.select("a[href=/bourse/opcvm/opcvm.phtml?symbole=MP-355456]").first();
		// System.out.println(name.text());

		// ISIN
		// Element isin = doc.select("div[class=fv-isin ellipsis]").first();
		// System.out.println(isin.text());

		// COTATION
		extractPrices(builder, doc);

		// DATE
		// Optional<Element> dateOpt = doc.select("tr[class=L20]").stream().filter(tr ->
		// tr.select("td").first().text().contains("Date")).findFirst();
		// String date = dateOpt.get().select("td").get(2).text();
		// System.out.println(date);

		// NOTATION MORNINGSTAR
		extractMorningStar(builder, doc);

		// LIPPERS
		extractLippers(builder, doc);

		// SHARPE RATIO
		extractSharpeRatios(builder, doc);

		return builder.build();
	}

	private void extractPrices(ProductValuesBuilder builder, Document doc) {
		Element cotation = doc.select("span[class=cotation]").first();
		String[] split = cotation.text().split(" ");
		builder.withPrice(Double.parseDouble(split[0]));
		builder.withPriceCurrency(split[1]);
	}

	private void extractMorningStar(ProductValuesBuilder builder, Document doc) {
		Optional<Element> notationOpt = doc.select("TR[class=L10]")
													  .stream()
													  .filter(tr -> tr.select("td")
														  					.first()
														  					.text()
														  					.contains("Notation Morningstar"))
													  .findFirst();
		// style=color: #444
		// class=icon icon-star
		int stars = notationOpt.get().select("td").last().select("span[style=color: #444;]").size();
		builder.withNotationMorningStar(stars);
	}

	private void extractLippers(ProductValuesBuilder builder, Document doc) {
		// LIPPER: Préservation du capital
		String lipperSaveCapital = doc.select("span[title=Préservation du capital]").first().text();
		builder.withNotationLipperCapitalPreservation(Integer.parseInt(lipperSaveCapital));

		// LIPPER: Performances régulières
		String lipperPerfReg = doc.select("span[title=Performances régulières]").first().text();
		builder.withNotationLipperRegularPerformances(Integer.parseInt(lipperPerfReg));

		// LIPPER: Performance absolue
		String lipperPerfAbs = doc.select("span[title=Performance absolue]").first().text();
		builder.withNotationLipperAbsolutePerformances(Integer.parseInt(lipperPerfAbs));
	}

	private void extractSharpeRatios(ProductValuesBuilder builder, Document doc) {
		Elements tables = doc.select("table");
		Optional<Element> sharpeRatio = tables.stream()
														  .filter(table -> table.select("tr")
															  							.first()
															  							.select("td")
															  							.first()
															  							.text()
															  							.contains("1 an"))
														  .findFirst();

		Element element = sharpeRatio.get();
		String text1An = element.select("tr").first().select("td").get(2).text();
		builder.withSharpeRatio1(Double.parseDouble(text1An));
		String text3An = element.select("tr").get(1).select("td").get(2).text();
		builder.withSharpeRatio3(Double.parseDouble(text3An));
		String text5An = element.select("tr").first().select("td").get(6).text();
		builder.withSharpeRatio5(Double.parseDouble(text5An));
		String text10An = element.select("tr").get(1).select("td").get(6).text();
		builder.withSharpeRatio10(Double.parseDouble(text10An));
	}
}
