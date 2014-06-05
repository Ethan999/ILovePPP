package com.annmars.iloveppp.browser;

import java.util.Stack;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.annmars.iloveppp.BrowserActivity;
import com.annmars.iloveppp.html.Link;
import com.annmars.iloveppp.page.Page;
import com.annmars.iloveppp.page.PageFactory;

public class PPPBrowser implements Browser {

	private Stack<Page> mPageStack;
	private Page mCurrentPage;
	private BrowserActivity mActivity;
	private PageFactory mFactory;

	private static final int PARSER_PAGE_SUCCESS = 0;
	private static final int PARSER_PAGE_FAILURE = 1;

	public PPPBrowser(BrowserActivity activity, PageFactory factory) {
		mActivity = activity;
		mFactory = factory;
		mPageStack = new Stack<Page>();
	}

	@Override
	public Page getCurrentPage() {
		return mCurrentPage;
	}

	@Override
	public void goTo(String URL) {
		goTo(new Link("", URL));
	}

	@Override
	public void goTo(Link link) {
		goTo(mFactory.createPage(link.getURL()));
	}

	@Override
	public boolean canGoBack() {
		return !mPageStack.isEmpty();
	}

	@Override
	public void goBack() {
		if (!mPageStack.isEmpty()) {
			mCurrentPage = null;
			goTo(mPageStack.pop());
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PARSER_PAGE_SUCCESS:
				updatePage(BrowserActivity.STATUS.SUCCESS);
				break;

			case PARSER_PAGE_FAILURE:
				updatePage(BrowserActivity.STATUS.FAILURE);
				break;
			}
		}
	};

	private void updatePage(BrowserActivity.STATUS status) {
		if (mCurrentPage == null) {
			return;
		}
		mActivity.updateLoadingStatus(status);
	}

	@Override
	public void goTo(final Page page) {
		if (mCurrentPage != null && mCurrentPage.getURL().equals(page.getURL())) {
			return;
		}
		if (mCurrentPage != null) {
			mPageStack.push(mCurrentPage);
		}
		mCurrentPage = page;
		updatePage(BrowserActivity.STATUS.LOADING);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int msg = PARSER_PAGE_SUCCESS;
				try{
				mCurrentPage.fetch();
				}catch(Exception e)
				{
					msg = PARSER_PAGE_FAILURE;
				}
				mHandler.obtainMessage(msg).sendToTarget();
			}
		}).start();
	}

}
