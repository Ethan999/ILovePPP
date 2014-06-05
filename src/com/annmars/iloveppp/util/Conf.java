package com.annmars.iloveppp.util;

import com.annmars.iloveppp.R;
import com.annmars.iloveppp.page.PPPPageFactory;
import com.annmars.iloveppp.page.PageFactory;

public class Conf {

	public static final String WEB_ADDRESS_BASE_WITHOUT_SLASH = "https://bbs.sjtu.edu.cn";

	public static final String WEB_ADDRESS_BASE = WEB_ADDRESS_BASE_WITHOUT_SLASH
			+ "/";

	public static final String PPP_SUBJECT_PAGE = "bbstdoc,board,PPPerson.html";

	public static final String PPP_COLLECT_PAGE = "bbs0an,path,%2Fgroups%2FGROUP_7%2FPPPerson.html";

	public static final String PPP_NEW_UPLOAD_PAGE = "bbsfdoc2?board=PPPerson";

	public static final String HOME = PPP_SUBJECT_PAGE;

	public static final String SAVED_IMAGE_EXT = ".png";

	//parameter for ImageActivity
	public static final String ACTIVITY_ARG_IMAGE_LIST = "IMAGE_LIST";
	public static final String ACTIVITY_ARG_IMAGE_TYPE = "PAGE_TYPE";
	public static final String ACTIVITY_ARG_CURRENT_IMAGE = "CURRENT_IMAGE";
	public static final int ACTIVITY_ARG_IMAGE_TYPE_WEB = 0;
	public static final int ACTIVITY_ARG_IMAGE_TYPE_SAVED = 1;
	
	
	//parameter for ThemeArticalActivity
	public static final String ACTIVITY_ARG_ARTICLE_URL = "URL";

	
	public static final String LOCAL_URL_PREFIX = "local://";
	public static final String LOCAL_URL_MY_COLLECTED_IMAGE = LOCAL_URL_PREFIX + "mycollectedimage";

	
	public static final PageFactory PAGE_FACTORY = new PPPPageFactory();
	
	// configure
	public static final int MAX_CACHE_PARSED_PAGE_NUMBER = 10;

	public static final boolean ENABLE_PRELOAD = true;

	
	// for test
	public static final String PPP_SUBJECT_IMAGE_PAGE_TEST[] = {
		WEB_ADDRESS_BASE + "bbstcon,board,PPPerson,reid,1398312415.html",
		WEB_ADDRESS_BASE + "bbstcon,board,PPPerson,reid,1398690664.html",
		WEB_ADDRESS_BASE + "bbstcon,board,PPPerson,reid,1399098868.html"
	};

	public static final String PPP_IMAGE_LIST[] = {
			"https://bbs.sjtu.edu.cn/file/PPPerson/127480169252724.jpg",
			"https://bbs.sjtu.edu.cn/file/PPPerson/1274773192139720.jpg",
			"https://bbs.sjtu.edu.cn/file/PPPerson/1274773192140990.jpg",
			"drawable://" + R.drawable.no_pics,
			"drawable://" + R.drawable.loading,
			"drawable://" + R.drawable.ic_1_navigation_back,
			"drawable://" + R.drawable.ic_error,
			"drawable://" + R.drawable.ic_launcher
	};
}
