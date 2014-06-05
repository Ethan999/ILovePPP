package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.JsoupSubjectImageParser;


public class SubjectImagePage extends ImagePage{
	
	public SubjectImagePage(String URL) {
		super(URL, new JsoupSubjectImageParser());
	}
}
