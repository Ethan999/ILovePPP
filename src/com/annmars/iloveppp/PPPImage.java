package com.annmars.iloveppp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PPPImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2499190600987014028L;
	private String mURL = null;

	public PPPImage(String URL) {
		if(!URL.startsWith("http") && !URL.startsWith("drawable"))
		{
			URL = Conf.WEB_ADDRESS_BASE + URL;
		}
		mURL = URL;
	}

	public String getURL() {
		return mURL;
	}

	public Bitmap getBitMap() {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(mURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
