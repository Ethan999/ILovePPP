package com.annmars.iloveppp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpWrapper {
	
	static String getHttpContext(String url)
	{
		HttpResponse mHttpResponse = null;
		HttpEntity mHttpEntity = null;
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream = null;
		try {
			mHttpResponse = httpClient.execute(httpGet);
			mHttpEntity = mHttpResponse.getEntity();
			inputStream = mHttpEntity.getContent();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, "GB2312"));
			StringBuilder respHtml = new StringBuilder();
			String line = "";

			while (null != (line = bufferedReader.readLine())) {
				respHtml.append(line);
			}
		
			return respHtml.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(inputStream != null)
				{
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
