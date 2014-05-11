package com.annmars.iloveppp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class BrowserActivity extends BrowserActivityBase {

	private Stack<PPPWebPage> mPageStack;
	private PPPWebPage mCurrentWebPage;
	private PPPWebPage mNextWebPage;

	private ListView mListView;
	private MenuAdapter mMenuAdapter;
	private View mBack;
	private View mSubject;
	private View mCollect;
	//private View mUpload;
	private LinearLayout mPageFooterLinearLayout;

	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;
	private Thread mThread;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				handleCallBack_doShowPage();
				break;

			case MSG_FAILURE:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);

		mPageStack = new Stack<PPPWebPage>();

		mBack = (View) findViewById(R.id.back);
		mSubject = (View) findViewById(R.id.subject);
		mCollect = (View) findViewById(R.id.collect);
		//mUpload = (View) findViewById(R.id.upload);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();
			}
		});
		mSubject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(Conf.PPP_SUBJECT_PAGE);
			}
		});
		mCollect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goTo(Conf.PPP_COLLECT_SECTION);
			}
		});
		//mUpload.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
		//		goTo(Conf.PPP_NEW_UPLOAD);
		//	}
		//});

		mPageFooterLinearLayout = (LinearLayout) findViewById(R.id.page_footer_linearlayout);
				
		mListView = (ListView) findViewById(R.id.listView);
		mMenuAdapter = new MenuAdapter();
		mListView.setAdapter(mMenuAdapter);
		mListView.setCacheColorHint(0);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView url = (TextView) view.findViewById(R.id.item_url);
				TextView text = (TextView) view.findViewById(R.id.item_text);
				if (url != null && text != null) {
					String URL = url.getText().toString();
					String TEXT = text.getText().toString();
					if (TEXT.equals("BACK")) {
						goBack();
					} else {
						goTo(URL);
					}
				}
			}
		});

		String URL = Conf.HOME;
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey("URL")) {
			URL = bundle.getString("URL");
		}
		goTo(URL);
	}

	private boolean goBack() {
		if (mPageStack.isEmpty()) {
			return false;
		} else {
			mCurrentWebPage = mPageStack.pop();
			updateCurrentWebPage();
			return true;
		}
	}

	private void goTo(String URL) {
		mNextWebPage = new PPPWebPage(URL);
		mThread = new Thread(new Runnable() {
			@Override
			public void run() {
				mNextWebPage.fetchParserHTML();
				mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			}
		});
		mThread.start();
	}

	private void handleCallBack_doShowPage() {
		if (mNextWebPage.isCategoryPage()) {
			if (mCurrentWebPage != null && mCurrentWebPage.isCategoryPage()) {
				mPageStack.push(mCurrentWebPage);
			}
			mCurrentWebPage = mNextWebPage;
			updateCurrentWebPage();
		} else {
			Intent intent = new Intent(BrowserActivity.this,
					ImageActivity.class);
			intent.putExtra("PAGE", mNextWebPage);
			startActivity(intent);
		}
	}

	private void updateCurrentWebPage() {
		mMenuAdapter.notifyDataSetChanged();
		
		mPageFooterLinearLayout.removeAllViews();
		if(mCurrentWebPage != null)
		{
			for(final PPPLink link : mCurrentWebPage.getControlLinkList())
			{
				Button button = (Button) LayoutInflater.from(BrowserActivity.this).inflate(
						R.layout.page_footer_item, mPageFooterLinearLayout, false);
				mPageFooterLinearLayout.addView(button);
				button.setText(link.getText());
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						goTo(link.getURL());
						
					}
				});
			}
			if(mCurrentWebPage.getControlLinkList().isEmpty())
			{
				mPageFooterLinearLayout.setVisibility(View.GONE);
			}
			else
			{
				mPageFooterLinearLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	class MenuAdapter extends BaseAdapter {

		private List<PPPLink> linkList;

		public MenuAdapter() {
			updateData();
		}

		private void updateData() {
			linkList = new ArrayList<PPPLink>();
			if (mCurrentWebPage != null) {
				linkList.addAll(mCurrentWebPage.getPageLinkList());
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
			PPPLink link = linkList.get(position);
			A a = null;
			if (view == null) {
				view = LayoutInflater.from(BrowserActivity.this).inflate(
						R.layout.page_item, parent, false);
			}

			a = new A();
			a.text = (TextView) view.findViewById(R.id.item_text);
			a.url = (TextView) view.findViewById(R.id.item_url);

			a.text.setText(link.getText());
			a.url.setText(link.getURL());

			return view;
		}

		class A {
			TextView text;
			TextView url;
		}
	}
}
