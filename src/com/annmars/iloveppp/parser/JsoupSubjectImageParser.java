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
		// (��̫��), ����: PPPerson�� ��: ���ĸŮ����վ: ��ˮ˼Դ (2014��04��24��12:06:55 ������)
		// (һ���ӵĽ���Ħ��), ����: PPPerson�� ��: Re: ���ĸŮ����վ: ��ˮ˼Դ (2014��04��24��12:49:08
		// ������)��Ǯ��á� �� NeutrinoI �Ĵ������ᵽ: ��
		String title = subString(metaInfo, "�� ��:", "����վ: ��ˮ˼Դ (");
		String time = subString(metaInfo, "����վ: ��ˮ˼Դ (", ")");
		String msg = subString(metaInfo, time+")", "�� ");
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
