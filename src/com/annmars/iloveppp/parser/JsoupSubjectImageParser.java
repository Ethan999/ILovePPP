package com.annmars.iloveppp.parser;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.annmars.iloveppp.html.Table;

public class JsoupSubjectImageParser extends JsoupImageParser {

	@Override
	protected void doParser() {
		super.doParser();
		for (Element table : mDocument.select("table")) {
			parserTable(table);
		}
	}

	private void parserTable(Element table) {
		Table tbl = new Table(table.toString());

		Elements as = table.select("a");
		String replyURL = as.get(0).attr("href");
		String author = as.get(2).ownText();

		Elements pres = table.select("pre");
		List<Node> nodes = pres.get(0).childNodes();
		String metaInfo = "";
		if(nodes.get(6) instanceof TextNode)
		{
			metaInfo = ((TextNode)nodes.get(6)).text();
		}
		//else if(nodes.get(4) instanceof TextNode)
		//{
		//	metaInfo = ((TextNode)nodes.get(4)).text();
		//}
		// (以太海), 信区: PPPerson标 题: 如此母女发信站: 饮水思源 (2014年04月24日12:06:55 星期四)
		// (一分钟的紧扣摩擦), 信区: PPPerson标 题: Re: 如此母女发信站: 饮水思源 (2014年04月24日12:49:08
		// 星期四)有钱真好【 在 NeutrinoI 的大作中提到: 】
		String title = subString(metaInfo, "标 题:", "发信站: 饮水思源 (");
		String time = subString(metaInfo, "发信站: 饮水思源 (", ")");
		String msg = subString(metaInfo, time+")", "【 ");
		if(msg != "") {
			tbl.addElement(msg);
		}
		
		for (int i = 7; i != nodes.size(); ++i) {
			Node node = nodes.get(i);
			if (node instanceof TextNode) {
				tbl.addElement(((TextNode) node).text());
			}
		}

		tbl.setReplyURL(replyURL);
		tbl.setAuthor(author);
		tbl.setTitle(title);
		tbl.setTime(time);

		mPageData.getTableList().add(tbl);
	}

	private String subString(String text, String before, String after) {
		String sub = "";
		int i = text.indexOf(before);
		int j = text.indexOf(after, i);
		if(j == -1) {
			j = text.length();
		}
		if (i != -1) {
			sub = text.substring(i + before.length(), j);
		}
		return sub;
	}
}
