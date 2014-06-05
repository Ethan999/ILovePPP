package com.annmars.iloveppp.browser;

import com.annmars.iloveppp.html.Link;
import com.annmars.iloveppp.page.Page;

public interface Browser {

	public void goTo(String URL);

	public void goTo(Link link);

	public void goTo(Page page);

	public boolean canGoBack();

	public void goBack();

	public Page getCurrentPage();
}
