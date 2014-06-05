package com.annmars.iloveppp.parser;

import com.annmars.iloveppp.page.PageData;
import com.annmars.iloveppp.util.HtmlNullException;

public abstract class Parser {

	private boolean mIsParsed;
	protected String mHTML;
	protected PageData mPageData;

	public Parser() {
		init();
	}

	private void init() {
		mPageData = new PageData();
		reset();
	}

	private void reset() {
		mIsParsed = false;
		mHTML = null;
		clear();
	}

	private void clear() {
		mPageData.clear();
	}

	public void setHTML(String HTML) {
		reset();
		mHTML = HTML;
	}

	public String getHTML() {
		return mHTML;
	}

	public void parser() throws HtmlNullException {
		if (!mIsParsed) {
			if (mHTML == null) {
				throw new HtmlNullException();
			}
			clear();
			doParser();
			mIsParsed = true;
		}
	}

	public boolean isParsed() {
		return mIsParsed;
	}

	public PageData getPageData() {
		return mPageData;
	}

	abstract protected void doParser();
}
