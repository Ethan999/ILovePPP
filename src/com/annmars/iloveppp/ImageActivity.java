package com.annmars.iloveppp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("HandlerLeak")
public class ImageActivity extends BrowserActivityBase implements OnPageChangeListener{

	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private DisplayImageOptions mOptions;
	private PPPWebPage mWebPage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.image_activity);
		
		mOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true).cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		
		
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.containsKey("PAGE")) {
			mWebPage = (PPPWebPage) bundle.getSerializable("PAGE");
			Log.i(TAG, "Bundle recevie URL=" + mWebPage.getURL());
			
		} else {
			Log.i(TAG, "URL is empty");
		}
		
		if(mWebPage != null)
		{
			if (mWebPage.getImageList().isEmpty()) {
				mWebPage.getImageList().add(
						new PPPImage("drawable://" + R.drawable.ic_empty));
			}
			// mViewPager.setCurrentItem(0);
		}
		
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mWebPage == null ? 0 : mWebPage.getImageList().size();
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
			View imageLayout = getLayoutInflater().inflate(
					R.layout.item_pager_image, view, false);
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			String URL = mWebPage.getImageList().get(position).getURL();

			ImageLoader.getInstance().displayImage(URL, imageView, mOptions,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(ImageActivity.this, message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			imageLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//switchShowHideMenu();//??
				}
			});
			return imageLayout;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
