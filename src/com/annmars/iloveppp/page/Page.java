package com.annmars.iloveppp.page;

import com.annmars.iloveppp.util.HtmlNullException;

public abstract class Page {

	private boolean mIsFetched;
	protected String mURL;

	protected PageData mPageData;

	public Page(String URL) {
		mURL = URL;
		mIsFetched = false;
		mPageData = new PageData();
	}

	public String getURL() {
		return mURL;
	}

	public PageData getPageData() {
		return mPageData;
	}

	public void refresh() throws HtmlNullException {
		mIsFetched = false;
		mPageData = new PageData();
		fetch();
	}

	public void fetch() throws HtmlNullException {
		if (!mIsFetched) {
			doFetch();
			mIsFetched = true;
		}
	}

	public boolean isFetched() {
		return mIsFetched;
	}

	abstract protected void doFetch() throws HtmlNullException;
}
