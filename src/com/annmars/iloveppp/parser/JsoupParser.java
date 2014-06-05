package com.annmars.iloveppp.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class JsoupParser extends Parser {

	protected Document mDocument;
	
	public JsoupParser() {
		super();
	}
	
	@Override
	protected void doParser() {
		mDocument = Jsoup.parse(mHTML);
	}
}
