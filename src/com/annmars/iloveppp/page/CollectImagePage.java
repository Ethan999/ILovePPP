package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.JsoupCollectImageParser;


public class CollectImagePage extends ImagePage{
	
	public CollectImagePage(String URL) {
		super(URL, new JsoupCollectImageParser());
	}
}
