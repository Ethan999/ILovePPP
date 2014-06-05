package com.annmars.iloveppp.html;

import com.annmars.iloveppp.util.Conf;

public class Link extends HtmlElement {

	protected String mURL;

	public Link(String HTML, String URL) {
		super(HTML);
		mURL = filerURL(URL);
	}

	public String getURL() {
		return mURL;
	}

	private String filerURL(String URL) {
		if (!URL.startsWith("http://") && !URL.startsWith("https://")
				&& !URL.startsWith("drawable://") && !URL.startsWith("file://")
				&& !URL.startsWith(Conf.LOCAL_URL_PREFIX)) {
			if (URL.startsWith("/")) {
				URL = Conf.WEB_ADDRESS_BASE_WITHOUT_SLASH + URL;
			} else {
				URL = Conf.WEB_ADDRESS_BASE + URL;
			}
		}
		return URL;
	}
}
