package com.annmars.iloveppp.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.annmars.iloveppp.util.Conf;

public class PageCache {

	private String TAG = "PageCache";

	static private PageCache mPageCache = new PageCache();

	static public PageCache getInstance() {
		return mPageCache;
	}

	private Map<String, Page> mMap;
	private List<Page> mList;

	private PageCache() {
		mMap = new HashMap<String, Page>();
		mList = new ArrayList<Page>();
	}

	public Page getPage(String URL) {
		Page page = mMap.get(URL);
		if (page != null) {
			Log.i(TAG, "====cache hit " + page.getURL());
		}
		return page;
	}

	public void addPage(Page page) {
		if (mMap.containsKey(page.getURL())) {
			return;
		}
		Log.i(TAG, "add page " + page.getURL());
		mMap.put(page.getURL(), page);
		mList.add(mList.size(), page);
		if (mList.size() > Conf.MAX_CACHE_PARSED_PAGE_NUMBER) {
			Page page2 = mList.get(0);
			Log.i(TAG, "==remove page " + page2.getURL());
			mList.remove(0);
			mMap.remove(page2.getURL());
		}
	}
}
