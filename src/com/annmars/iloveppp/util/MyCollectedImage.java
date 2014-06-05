package com.annmars.iloveppp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class MyCollectedImage {

	private static MyCollectedImage instance;

	public static MyCollectedImage getInstance() {
		if (instance == null) {
			instance = new MyCollectedImage();
		}
		return instance;
	}

	private String TAG = "MyCollectedImage";

	private List<String> mImagePathList;

	private MyCollectedImage() {
		mImagePathList = new ArrayList<String>();
		loadSavedImage();
	}

	public List<String> getSavedImageList() {
		return mImagePathList;
	}

	private void loadSavedImage() {
		String path = getImagePath();
		if (path == null) {
			return;
		}
		File f = new File(path);
		for (File pic : f.listFiles()) {
			if (pic.getName().endsWith(Conf.SAVED_IMAGE_EXT)) {
				loadSavedImage(pic.getAbsolutePath());
			}
		}
	}

	private void loadSavedImage(String path) {
		mImagePathList.add(path);
	}
	
	private String toHex(String arg) {
		return String.format("%040x", new BigInteger(1, arg.getBytes()));
	}

	public void saveImage(final String URL) {
		final String path = getImagePath();
		if (path == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap btimap = HttpWrapper.getHttpclientImage(URL);
				File f = new File(path + "/" + toHex(URL)
						+ Conf.SAVED_IMAGE_EXT);
				saveImage(btimap, f, URL);
				loadSavedImage(f.getAbsolutePath());
			}
		}).start();
	}

	public void deleteImage(String path) {
		File f = new File(path);
		if (f.exists()) {
			if (f.delete()) {
				Log.i(TAG, "success delete " + path);
			} else {
				Log.i(TAG, "faliure delete " + path);
			}
		}

		for (String savedImage : mImagePathList) {
			if (savedImage.equals(path)) {
				mImagePathList.remove(savedImage);
				return;
			}
		}
	}

	private boolean saveImage(Bitmap bitmap, File f, String URL) {
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getImagePath() {
		boolean EXIT_SDCARD = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);

		if (EXIT_SDCARD) {
			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/PPP";
			new File(path).mkdirs();
			return path;
		} else {
			return null;
		}
	}
}
