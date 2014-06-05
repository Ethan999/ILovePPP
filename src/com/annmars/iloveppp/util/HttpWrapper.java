package com.annmars.iloveppp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpWrapper {

	public static String getHttpContext(String url) {
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
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getHttpclientImage(String imageUrl) {
		// httpGet���Ӷ���
		HttpGet httpRequest = new HttpGet(imageUrl);
		// ȡ��HttpClient ����
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// ����httpClient ��ȡ��HttpRestponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�������Ϣ ȡ��HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// ���һ��������
				InputStream is = httpEntity.getContent();

				// ͼƬת��Ϊbitmap��ʽ
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				is.close();

				return bitmap;
			}

		} catch (Exception e) {
		}
		return null;
	}

}
