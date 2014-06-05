package com.annmars.iloveppp;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annmars.iloveppp.browser.Browser;
import com.annmars.iloveppp.browser.PPPBrowser;
import com.annmars.iloveppp.html.ButtonLink;
import com.annmars.iloveppp.html.ImageLink;
import com.annmars.iloveppp.html.Link;
import com.annmars.iloveppp.html.TextLink;
import com.annmars.iloveppp.page.ImagePage;
import com.annmars.iloveppp.page.Page;
import com.annmars.iloveppp.util.Conf;

public class PPPBrowserActivity extends BrowserActivity {

	private Browser mBrowser;

	private WebView mWebView;
	private ListView mListView;
	private MenuAdapter mMenuAdapter;
	private View mBack;
	private View mExit;
	private View mSubject;
	private View mCollect;
	private View mSaved;
	private LinearLayout mPageFooterLinearLayout;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppp_browser_activity_page);

		mBrowser = new PPPBrowser(this, Conf.PAGE_FACTORY);

		mBack = (View) findViewById(R.id.back);
		mExit = (View) findViewById(R.id.exit);
		mSubject = (View) findViewById(R.id.subject);
		mCollect = (View) findViewById(R.id.collect);
		mSaved = (View) findViewById(R.id.mysaved);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBrowser.goBack();
			}
		});
		mExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mSubject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBrowser.goTo(Conf.PPP_SUBJECT_PAGE);
			}
		});
		mCollect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBrowser.goTo(Conf.PPP_COLLECT_PAGE);
			}
		});
		mSaved.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBrowser.goTo(Conf.LOCAL_URL_MY_COLLECTED_IMAGE);
			}
		});

		mPageFooterLinearLayout = (LinearLayout) findViewById(R.id.page_footer_linearlayout);

		mWebView = (WebView) findViewById(R.id.webView);
		mListView = (ListView) findViewById(R.id.listView);
		mMenuAdapter = new MenuAdapter();
		mListView.setAdapter(mMenuAdapter);
		mListView.setCacheColorHint(0);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Object obj = view.getTag();
				if (obj != null && obj instanceof Link) {
					mBrowser.goTo((Link) obj);
				}
			}
		});

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey("URL")) {
			mBrowser.goTo(bundle.getString("URL"));
		} else {
			mSubject.performClick();
		}
	}

	@Override
	public void updateLoadingStatus(STATUS status) {

		switch (status) {
		case LOADING:
			updateHeader();
			if (!fresh) {
				return;
			}
			updateLoading();
			break;
		case SUCCESS:
			if (!fresh) {
				fresh = true;
				return;
			}
			updateContent();
			break;
		case FAILURE:
			fresh = true;
			updateFailure();
			break;
		}
	}

	private void updateHeader() {
		if (!mBrowser.canGoBack()) {
			mBack.setVisibility(View.GONE);
		} else {
			mBack.setVisibility(View.VISIBLE);
		}

	}

	private void updateLoading() {
		mProgressDialog = ProgressDialog.show(this, null, "Loading");
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mBrowser.goBack();
			}
		});
	}

	private void updateFailure() {
		mProgressDialog.dismiss();
		Toast.makeText(this.getApplicationContext(), "“≥√Ê‘ÿ»Î ß∞‹",
				Toast.LENGTH_SHORT).show();
	}

	public static int REQUEST_CODE = 1;
	public static int EXIT_NORMAL = 2;
	public static int EXIT_BEFORE_PAGE = 3;
	public static int EXIT_NEXT_PAGE = 4;
	public static String PARA = "PARA";

	
	@SuppressWarnings("unused")
	private void startImageActivity() {
		Page mPage = mBrowser.getCurrentPage();
		int type = Conf.ACTIVITY_ARG_IMAGE_TYPE_WEB;
		if (mPage == null) {
			return;
		}
		if (mPage.getURL().equals(Conf.LOCAL_URL_MY_COLLECTED_IMAGE)) {
			type = Conf.ACTIVITY_ARG_IMAGE_TYPE_SAVED;
		}
		ArrayList<String> images = new ArrayList<String>();
		for (ImageLink link : mPage.getPageData().getImageLinkList()) {
			images.add(link.getURL());
		}
		Intent intent = new Intent(this, PPPImageActivity.class);
		intent.putExtra(Conf.ACTIVITY_ARG_IMAGE_LIST, images);
		intent.putExtra(Conf.ACTIVITY_ARG_IMAGE_TYPE, type);
		intent.putExtra(Conf.ACTIVITY_ARG_CURRENT_IMAGE, 0);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	private void startThemaArticleActivity() {
		Page mPage = mBrowser.getCurrentPage();
		Intent intent = new Intent(this, PPPImageTextActivity.class);
		intent.putExtra(Conf.ACTIVITY_ARG_ARTICLE_URL, mPage.getURL());
		startActivityForResult(intent, REQUEST_CODE);
	}

	private void viewImagePage() {
		// Page mPage = mBrowser.getCurrentPage();
		// if (mPage.getURL().equals(Conf.LOCAL_URL_MY_COLLECTED_IMAGE)) {
		// startImageActivity();
		// return;
		// }
		//startImageActivity();// TO DO
		startThemaArticleActivity();
	}

	boolean fresh = true;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if(resultCode != RESULT_OK)
		// {
		// return;
		// }

		// Bundle b = data.getExtras();
		// int cmd = b.getInt(PARA);

		fresh = false;
		mBrowser.goBack();
	}

	private void updateContent() {
		Page mPage = mBrowser.getCurrentPage();
		if (mPage == null) {
			return;
		}
		if (mPage instanceof ImagePage) {
			viewImagePage();
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			return;
		}

		mWebView.setVisibility(View.INVISIBLE);
		mMenuAdapter.notifyDataSetChanged();
		mListView.setSelection(0);
		mPageFooterLinearLayout.removeAllViews();
		if (mPage != null) {
			for (final ButtonLink link : mPage.getPageData()
					.getButtonLinkList()) {
				Button button = (Button) LayoutInflater.from(
						PPPBrowserActivity.this).inflate(
						R.layout.ppp_browser_activity_page_footer_item, mPageFooterLinearLayout,
						false);
				mPageFooterLinearLayout.addView(button);
				button.setText(link.getTEXT());
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mBrowser.goTo(link);

					}
				});
			}
			if (mPage.getPageData().getButtonLinkList().isEmpty()) {
				mPageFooterLinearLayout.setVisibility(View.GONE);
			} else {
				mPageFooterLinearLayout.setVisibility(View.VISIBLE);
			}
		}
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	class MenuAdapter extends BaseAdapter {

		private List<TextLink> linkList;

		public MenuAdapter() {
			updateData();
		}

		private void updateData() {
			Page mPage = mBrowser.getCurrentPage();
			linkList = new ArrayList<TextLink>();
			if (mPage != null) {
				linkList.addAll(mPage.getPageData().getTextLinkList());
			}
		}

		@Override
		public int getCount() {
			return linkList.size();
		}

		@Override
		public Object getItem(int position) {
			return linkList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			updateData();
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			TextLink link = linkList.get(position);
			A a = null;
			if (view == null) {
				view = LayoutInflater.from(PPPBrowserActivity.this).inflate(
						R.layout.ppp_browser_activity_page_item, parent, false);
			}

			a = new A();
			a.text = (TextView) view.findViewById(R.id.item_text);

			a.text.setText(link.getTEXT());
			view.setTag(link);

			return view;
		}

		class A {
			TextView text;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				return false;
			} else if (mBrowser.canGoBack()) {
				mBrowser.goBack();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
