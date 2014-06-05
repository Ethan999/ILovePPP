package com.annmars.iloveppp.page;

import java.util.ArrayList;
import java.util.List;

import com.annmars.iloveppp.html.ButtonLink;
import com.annmars.iloveppp.html.ImageLink;
import com.annmars.iloveppp.html.Table;
import com.annmars.iloveppp.html.TextLink;

public class PageData {

	private List<Table> mTableList;
	private List<ButtonLink> mButtonLinkList;
	private List<TextLink> mTextLinkList;
	private List<ImageLink> mImageLinkList;

	public PageData() {
		mTableList = new ArrayList<Table>();
		mButtonLinkList = new ArrayList<ButtonLink>();
		mTextLinkList = new ArrayList<TextLink>();
		mImageLinkList = new ArrayList<ImageLink>();
	}

	public void clear() {
		mTableList.clear();
		mButtonLinkList.clear();
		mTextLinkList.clear();
		mImageLinkList.clear();
	}
	
	public List<ImageLink> getImageLinkList()
	{
		return mImageLinkList;
	}

	public List<Table> getTableList() {
		return mTableList;
	}

	public List<ButtonLink> getButtonLinkList() {
		return mButtonLinkList;
	}

	public List<TextLink> getTextLinkList() {
		return mTextLinkList;
	}
}
