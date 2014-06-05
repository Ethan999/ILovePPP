package com.annmars.iloveppp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.annmars.iloveppp.html.ImageLink;
import com.annmars.iloveppp.html.Table;
import com.annmars.iloveppp.page.CollectImagePage;
import com.annmars.iloveppp.page.Page;
import com.annmars.iloveppp.ui.ExtendedViewPager;
import com.annmars.iloveppp.ui.TouchImageView;
import com.annmars.iloveppp.util.Conf;
import com.annmars.iloveppp.util.MyCollectedImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PPPImageTextActivity extends Activity {
	private static final String TAG = "ThemeArticleActivity";
	private static final int TEXT_MODE = 1;
	private static final int IMAGE_MODE = 2;
	private int mMode = IMAGE_MODE;

	private ListView mContentListView;
	private TextView mTitleTextView;
	private TextView mSwitch;
	private ExtendedViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private DisplayImageOptions mOptions;

	private Button save;
	private Button delete;
	private View header;
	private View footer;

	private ThemeArticleAdapter mThemeArticleAdapter;
	private Page mPage;

	private List<String> mImageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_text_activity);

		mContentListView = (ListView) findViewById(R.id.articlelist);
		mTitleTextView = (TextView) findViewById(R.id.activitytitle);
		mSwitch = (TextView) findViewById(R.id.switch1);
		header = findViewById(R.id.head);
		footer = findViewById(R.id.foot);
		save = (Button) findViewById(R.id.save);
		delete = (Button) findViewById(R.id.delete);
		mViewPager = (ExtendedViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new MyPagerAdapter();

		mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.no_pics)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		String URL = null;
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey(Conf.ACTIVITY_ARG_ARTICLE_URL)) {
			URL = bundle.getString(Conf.ACTIVITY_ARG_ARTICLE_URL);
			Log.i(TAG, "start " + URL);
		}

		if (URL == null) {
			Log.i(TAG, "URL is null");
			return;
		}

		mPage = Conf.PAGE_FACTORY.createPage(URL);
		if (!mPage.isFetched()) {
			Log.i(TAG, "page is not fetched");
			return;
		}

		mImageList = new ArrayList<String>();
		for (ImageLink image : mPage.getPageData().getImageLinkList()) {
			mImageList.add(image.getURL());
		}
		if (mImageList.isEmpty()) {
			mImageList.add("drawable://" + R.drawable.no_pics);
		}

		mSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMode == TEXT_MODE) {
					mMode = IMAGE_MODE;
				} else {
					mMode = TEXT_MODE;
				}
				update();
			}
		});

		mThemeArticleAdapter = new ThemeArticleAdapter();
		mContentListView.setAdapter(mThemeArticleAdapter);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchShowHideMenu();
				String image = mImageList.get(mViewPager.getCurrentItem());
				MyCollectedImage.getInstance().saveImage(image);
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchShowHideMenu();
				String image = mImageList.get(mViewPager.getCurrentItem());
				mImageList.remove(mViewPager.getCurrentItem());
				if (image.startsWith("file://")) {
					image = image.substring(7, image.length());
				}
				MyCollectedImage.getInstance().deleteImage(image);
				update();
			}
		});
		if (mPage.getURL().equals(Conf.LOCAL_URL_MY_COLLECTED_IMAGE)) {
			save.setVisibility(View.GONE);

		} else {
			delete.setVisibility(View.GONE);

		}
		if ((mPage instanceof CollectImagePage)
				|| mPage.getURL().equals(Conf.LOCAL_URL_MY_COLLECTED_IMAGE)) {
			mSwitch.setVisibility(View.GONE);
			mMode = IMAGE_MODE;
		}

		update();
	}

	private void update() {
		String title = "";
		if (mMode == TEXT_MODE) {
			if (!mPage.getPageData().getTableList().isEmpty()) {
				title = mPage.getPageData().getTableList().get(0).getTitle();
			}
			mSwitch.setText("查看图片");
			mContentListView.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.GONE);
			showMenu();
			mThemeArticleAdapter.notifyDataSetChanged();
		} else {
			title = (1 + mViewPager.getCurrentItem()) + "/" + mImageList.size();
			mSwitch.setText("查看文章");
			mContentListView.setVisibility(View.GONE);
			mViewPager.setVisibility(View.VISIBLE);
			hideMenu();
			mPagerAdapter.notifyDataSetChanged();
		}
		mTitleTextView.setText(title);
	}

	private void hideMenu() {
		if (mMode == TEXT_MODE) {
			header.setVisibility(View.VISIBLE);
			footer.setVisibility(View.GONE);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			header.setVisibility(View.GONE);
			footer.setVisibility(View.GONE);
		}
	}

	private void showMenu() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (mMode == TEXT_MODE) {
			header.setVisibility(View.VISIBLE);
			footer.setVisibility(View.GONE);
		} else {
			header.setVisibility(View.VISIBLE);
			footer.setVisibility(View.VISIBLE);
		}
	}

	private void switchShowHideMenu() {
		if (footer.getVisibility() == View.GONE) {
			showMenu();
		} else {
			hideMenu();
		}
	}

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			String title = (1 + mViewPager.getCurrentItem()) + "/"
					+ mImageList.size();
			mTitleTextView.setText(title);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
	};

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			if (mPage == null) {
				return null;
			}
			View newView = LayoutInflater.from(PPPImageTextActivity.this)
					.inflate(R.layout.touch_image_view, view, false);

			final ImageView loading = (ImageView) newView
					.findViewById(R.id.loading);
			final TouchImageView loaded = (TouchImageView) newView
					.findViewById(R.id.loaded);

			view.addView(newView, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			loaded.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switchShowHideMenu();
				}
			});
			loading.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switchShowHideMenu();
				}
			});

			String URL = mImageList.get(position);
			ImageLoader.getInstance().displayImage(URL, loaded, mOptions,
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								// message = "输入输出错误";
								break;
							case DECODING_ERROR:
								// message = "无法解析图片";
								break;
							case NETWORK_DENIED:
								// message = "下载被拒绝";
								break;
							case OUT_OF_MEMORY:
								// message = "内存不够";
								break;
							case UNKNOWN:
								// message = "未知错误";
								break;
							}
							loading.setImageResource(R.drawable.ic_error);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							loading.setVisibility(View.GONE);
							loaded.setVisibility(View.VISIBLE);
						}
					});

			return newView;
		}
	}

	private class ThemeArticleAdapter extends BaseAdapter {
		public ThemeArticleAdapter() {
		}

		@Override
		public int getCount() {
			if (mPage == null) {
				return 0;
			} else {
				return mPage.getPageData().getTableList().size();
			}
		}

		@Override
		public Object getItem(int location) {
			return mPage.getPageData().getTableList().get(location);
		}

		@Override
		public long getItemId(int location) {
			return location;
		}

		@Override
		public View getView(int location, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			if (mPage == null) {
				return null;
			}
			Table tbl = mPage.getPageData().getTableList().get(location);
			convertView = LayoutInflater.from(PPPImageTextActivity.this)
					.inflate(R.layout.image_text_activity_item, null);
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.author);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);

			viewHolder.author.setText(tbl.getAuthor());
			viewHolder.time.setText(tbl.getTime());
			String text = "";
			for (String obj : tbl.getContentList()) {
				String txt = obj;
				text += txt;
			}
			viewHolder.content.setText(text);
			return convertView;
		}

		public class ViewHolder {
			TextView author;
			TextView time;
			TextView content;
		}
	}
}
