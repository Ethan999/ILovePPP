package com.annmars.iloveppp.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.annmars.iloveppp.html.ButtonLink;
import com.annmars.iloveppp.html.TextLink;

public class JsoupSubjectParser extends JsoupParser {

	public JsoupSubjectParser() {
		super();
	}

	@Override
	protected void doParser() {
		super.doParser();
		for (Element table : mDocument.select("table")) {
			parseSubjectPageLinks(table);
		}
		parseControlLinks(mDocument);
	}
	
	private void parseControlLinks(Element element) {
		for (Element e : element.select("a")) {
			if (e.ownText().equals("上一页") || e.ownText().equals("下一页")) {
				String URL = e.attr("href");
				String text = e.ownText();
				mPageData.getButtonLinkList().add(new ButtonLink(e.toString(), URL, text));
			}
		}
	}

	private void parseSubjectPageLinks(Element table) {
		// 序号 状态 作者 日期 标题
		Elements trs = table.select("tr");
		for (int i = 1; i != trs.size(); ++i) {
			Element tr = trs.get(i);
			Elements tds = tr.select("td");
			if (tds.size() < 5) {
				continue;
			}
			Element td_title = tds.get(4);

			Elements as = td_title.select("a");
			if (as.isEmpty()) {
				continue;
			}
			Element a = td_title.select("a").get(0);
			String REPLAY = td_title.ownText();
			String URL = a.attr("href");
			String text = a.ownText();
			if (REPLAY.length() <= 4) {
				continue;
			}
			int replay = 0;
			try {
				replay = Integer.parseInt(REPLAY.substring(1,
						REPLAY.length() - 3));
			} catch (Exception e) {
				continue;
			}
			String r = (replay < 10 ? "  " : "") + replay;
			text = "[" + r + "回复] " + text;
			mPageData.getTextLinkList().add(new TextLink(a.toString(), URL, text));
		}
	}
}
