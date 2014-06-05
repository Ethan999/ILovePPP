package com.annmars.iloveppp.html;

public class ButtonLink extends Link {

	protected String mTEXT;

	public ButtonLink(String HTML, String URL, String TEXT) {
		super(HTML, URL);
		mTEXT = TEXT;
	}

	public String getTEXT() {
		return mTEXT;
	}

}
