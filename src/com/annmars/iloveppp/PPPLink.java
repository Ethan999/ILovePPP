package com.annmars.iloveppp;

import java.io.Serializable;

public class PPPLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4772900869591292264L;
	private String mText;
	private String mURL;
	
	public PPPLink(String text, String URL)
	{
		mText = text;
		mURL = URL;
	}
	
	public String getText()
	{
		return mText;
	}
	
	public String getURL()
	{
		return mURL;
	}
}
