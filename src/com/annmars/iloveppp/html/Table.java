package com.annmars.iloveppp.html;

import java.util.ArrayList;
import java.util.List;

public class Table extends HtmlElement {

	private String mReplyURL;
	private String mAuthor;
	private String mTitle;
	private String mTime;
	private List<String> mContentList;

	public Table(String HTML) {
		super(HTML);
		mContentList = new ArrayList<String>();
	}

	public String getReplyURL() {
		return mReplyURL;
	}
	
	public void setReplyURL(String replyURL) {
		mReplyURL = replyURL;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public List<String> getContentList() {
		return mContentList;
	}
	
	public void addElement(String obj)
	{
		mContentList.add(obj);
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}
}
