package com.annmars.iloveppp.page;

import com.annmars.iloveppp.parser.Parser;
import com.annmars.iloveppp.util.HtmlNullException;
import com.annmars.iloveppp.util.HttpWrapper;

public abstract class WebPage extends Page{

	
	protected Parser mParser;
	protected String mHTML;
	
	public WebPage(String URL, Parser parser) {
		super(URL);
		mParser = parser;
	}
	
	public String getHTML()
	{
		return mHTML;
	}

	@Override
	protected void doFetch() throws HtmlNullException {
		fetchHTML();
		mParser.setHTML(mHTML);
		mParser.parser();
		mPageData = mParser.getPageData();
	}
	
	private void fetchHTML() throws HtmlNullException {
		if (mHTML == null) {
			mHTML = HttpWrapper.getHttpContext(mURL);
		}
		if(mHTML == null)
		{
			throw new HtmlNullException();
		}
	}
}
