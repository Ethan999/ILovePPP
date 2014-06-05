package com.annmars.iloveppp.page;

import com.annmars.iloveppp.html.ImageLink;
import com.annmars.iloveppp.util.MyCollectedImage;


public class MyCollectedImagePage extends ImagePage{

	public MyCollectedImagePage(String URL) {
		super(URL);
	}

	@Override
	public void doFetch() {
		for(String path : MyCollectedImage.getInstance().getSavedImageList())
		{
			mPageData.getImageLinkList().add(new ImageLink("", "file://" + path));
		}
	}
}
