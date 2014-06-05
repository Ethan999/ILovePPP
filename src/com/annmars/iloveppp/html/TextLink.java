package com.annmars.iloveppp.html;

public class TextLink extends Link {

	protected String mTEXT;

	public TextLink(String HTML, String URL, String TEXT) {
		super(HTML, URL);
		mTEXT = TEXT;
	}

	public String getTEXT() {
		return mTEXT;
	}

}
