package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.JsoupSubjectImageParser;
import com.annmars.iloveppp.parser.Parser;


public class ImagePage extends WebPage{
	
	public ImagePage(String URL) {
		super(URL, new JsoupSubjectImageParser());
	}
	
	public ImagePage(String URL, Parser parser) {
		super(URL, parser);
	}
}
