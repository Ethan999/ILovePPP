package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.JsoupSubjectParser;


public class SubjectPage extends WebPage{
	
	public SubjectPage(String URL) {
		super(URL, new JsoupSubjectParser());
	}
}
