package com.annmars.iloveppp.parser;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;

import com.annmars.iloveppp.html.ImageLink;

public class JsoupImageParser extends JsoupParser{

	@Override
	protected void doParser() {
		super.doParser();
		for (Element table : mDocument.select("table")) {
			parserTable(table);
		}
	}
	
	private void parserTable(Element table) {
		Set<String> set = new HashSet<String>();
		for (Element e : table.select("img")) {
			String URL = e.attr("src");
			if (URL != null && URL != "" && !set.contains(URL)) {
				mPageData.getImageLinkList().add(new ImageLink(e.toString(), URL));
				set.add(URL);
			}
		}
	}
}
