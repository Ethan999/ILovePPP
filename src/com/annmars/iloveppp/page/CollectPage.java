package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.JsoupCollectParser;


public class CollectPage extends WebPage{
	
	public CollectPage(String URL) {
		super(URL, new JsoupCollectParser());
	}
}
