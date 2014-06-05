package com.annmars.iloveppp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.annmars.iloveppp.html.TextLink;

public class JsoupCollectParser extends JsoupParser {

	@Override
	protected void doParser() {
		super.doParser();
		
		for (Element table : mDocument.select("table")) {
			parserCollectPageLinks(table);
		}
	}

	private void parserCollectPageLinks(Element table) {
		// 编号 类别 标题 作者 日期
		Elements trs = table.select("tr");
		for (int i = 1; i != trs.size(); ++i) {
			Element tr = trs.get(i);
			Elements tds = tr.select("td");
			if (tds.size() < 5) {
				continue;
			}
			Element td_title = tds.get(2);

			Elements as = td_title.select("a");
			if (as.isEmpty()) {
				continue;
			}
			Element a = td_title.select("a").get(0);
			String URL = a.attr("href");
			if (URL == null || URL == "") {
				continue;
			}
			String text = a.ownText();
			mPageData.getTextLinkList().add(new TextLink(a.toString(), URL, text));
		}
	}
}
