package com.annmars.iloveppp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

@SuppressLint({ "HandlerLeak", "NewApi" })
public class PPPImageActivity extends Activity implements OnPageChangeListener {

	private ExtendedViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private DisplayImageOptions mOptions;

	private ArrayList<String> mImageList;
	private LinearLayout groupDown;
	private LinearLayout groupUp;
	private Button save;
	private Button delete;
	private TextView number;
	private int mType;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_activity);

		mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.no_pics)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey(Conf.ACTIVITY_ARG_IMAGE_LIST)
				&& bundle.containsKey(Conf.ACTIVITY_ARG_IMAGE_TYPE)) {
			mImageList = (ArrayList<String>) bundle
					.getSerializable(Conf.ACTIVITY_ARG_IMAGE_LIST);
			mType = bundle.getInt(Conf.ACTIVITY_ARG_IMAGE_TYPE);
			if (mImageList.isEmpty()) {
				mImageList.add("drawable://" + R.drawable.no_pics);
			}
		}
		int mCurrentImage = 0;
		if (bundle != null
				&& bundle.containsKey(Conf.ACTIVITY_ARG_CURRENT_IMAGE)) {
			mCurrentImage = bundle.getInt(Conf.ACTIVITY_ARG_CURRENT_IMAGE);
		}

		mViewPager = (ExtendedViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(mCurrentImage);

		groupDown = (LinearLayout) findViewById(R.id.viewGroupDown);
		groupUp = (LinearLayout) findViewById(R.id.viewGroupUp);
		save = (Button) findViewById(R.id.save);
		delete = (Button) findViewById(R.id.delete);
		number = (TextView) findViewById(R.id.number);
		number.setText((1 + mCurrentImage) + "/" + mImageList.size());
		if (mType == Conf.ACTIVITY_ARG_IMAGE_TYPE_WEB) {
			delete.setVisibility(View.GONE);
		} else {
			save.setVisibility(View.GONE);
		}
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String image = mImageList.get(mViewPager.getCurrentItem());
				switchShowHideMenu();
				MyCollectedImage.getInstance().saveImage(image);
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String image = mImageList.get(mViewPager.getCurrentItem());
				switchShowHideMenu();
				mImageList.remove(image);
				mPagerAdapter.notifyDataSetChanged();
				if (image.startsWith("file://")) {
					image = image.substring(7, image.length());
				}
				MyCollectedImage.getInstance().deleteImage(image);
			}
		});
		hideMenu();
	}

	private void hideMenu() {
		groupDown.setVisibility(View.INVISIBLE);
		groupUp.setVisibility(View.INVISIBLE);
	}

	private void switchShowHideMenu() {
		if (groupDown.getVisibility() == View.INVISIBLE) {
			groupDown.setVisibility(View.VISIBLE);
			groupUp.setVisibility(View.VISIBLE);
		} else {
			hideMenu();
		}
	}

	private void back(int cmd) {
		Intent data = new Intent();
		data.putExtra(PPPBrowserActivity.PARA, cmd);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back(PPPBrowserActivity.EXIT_NORMAL);
		}
		return super.onKeyDown(keyCode, event);
	}

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
			View newView = LayoutInflater.from(PPPImageActivity.this).inflate(
					R.layout.touch_image_view, view, false);

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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		number.setText((1 + mViewPager.getCurrentItem()) + "/"
				+ mImageList.size());
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}
}
