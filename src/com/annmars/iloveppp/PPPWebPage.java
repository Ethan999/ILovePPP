package com.annmars.iloveppp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PPPWebPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 25627159787472534L;
	private String mURL;
	private String mContent;
	private List<PPPImage> mImageList;
	private List<PPPLink> mControlLinkList;
	private List<PPPLink> mPageLinkList;
	private Set<String> mSet;

	public PPPWebPage(String URL) {
		mURL = URL;
		mImageList = new ArrayList<PPPImage>();
		mSet = new HashSet<String>();
		mControlLinkList = new ArrayList<PPPLink>();
		mPageLinkList = new ArrayList<PPPLink>();
	}
	
	private Boolean isImagePage = null;
	private Boolean isCategoryPage = null;
	private void getPageType()
	{
		//主题模式 
			//bbstdoc,board,PPPerson.html
			//bbstdoc,board,PPPerson,page,214.html
		
		//主题模式图片页面：
			//bbstcon,board,PPPerson,reid,1398392126.html

		//精华区
			//bbs0an?path=%2Fgroups%2FGROUP_7%2FPPPerson
			//bbs0an,path,%2Fgroups%2FGROUP_7%2FPPPerson%2FD783CD619.html
			//bbs0an,path,%2Fgroups%2FGROUP_7%2FPPPerson%2FD6960334B.html
	     
		//图片页面
			//bbsanc,path,%2Fgroups%2FGROUP_7%2FPPPerson%2FD783CD619%2FD9A47902C%2FM.1131978557.A.html

		//新上载区
			//bbsfdoc2?board=PPPerson
			//bbsfdoc2?board=PPPerson&start=104780

		if(mURL.contains("bbstdoc,board,PPPerson.html")
				|| mURL.contains("bbstdoc,board,PPPerson,page,"))//主题模式
		{
			isImagePage = false;
			isCategoryPage = true;
		}
		else if(mURL.contains("bbstcon,board,PPPerson,reid,"))//主题模式图片页面
		{
			isImagePage = true;
			isCategoryPage = false;
		}
		else if(mURL.contains("bbs0an?path"))//精华区
		{
			isImagePage = false;
			isCategoryPage = true;
		}
		else if(mURL.contains("bbsanc,path,"))//图片页面
		{
			isImagePage = true;
			isCategoryPage = false;
		}
		else if(mURL.contains("bbsfdoc2?board=PPPerson"))//新上载区
		{
			isImagePage = true;
			isCategoryPage = true;
		}
		else 
		{
			if(mImageList.isEmpty())
			{
				isImagePage = false;
			}
			else
			{
				isImagePage = true;
			}
			if(mControlLinkList.isEmpty() && mPageLinkList.isEmpty())
			{
				isCategoryPage = false;
			}
			else
			{
				isCategoryPage = true;
			}
		}
	}
	public boolean isImagePage()
	{
		if(isImagePage == null)
		{
			getPageType();
		}
		return isImagePage;
	}
	
	public boolean isCategoryPage()
	{
		if(isCategoryPage == null)
		{
			getPageType();
		}
		return isCategoryPage;
	}
	
	public String getURL()
	{
		return mURL;
	}

	public List<PPPImage> getImageList() {
		return mImageList;
	}

	public List<PPPLink> getControlLinkList() {
		return mControlLinkList;
	}
	
	public List<PPPLink> getPageLinkList() {
		return mPageLinkList;
	}

	private void parseImages() {
		String IMGURL_REG = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";  
	    
	    Matcher matcher = Pattern.compile(IMGURL_REG, Pattern.CASE_INSENSITIVE).matcher(mContent);
		while (matcher.find()) {
			String URL = matcher.group(3);
			if (mSet.contains(URL) == false) {
				mImageList.add(new PPPImage(URL));
				mSet.add(URL);
			}
		}

		
		// file/PPPerson/1399255121157750.jpg
		Pattern titlePicture = Pattern
				.compile("file\\/PPPerson\\/[0-9]\\d*.jpg", Pattern.CASE_INSENSITIVE);
		Matcher matcherPicture = titlePicture.matcher(mContent);
		while (matcherPicture.find()) {
			String URL = matcherPicture.group();
			if (mSet.contains(URL) == false) {
				mImageList.add(new PPPImage(URL));
				mSet.add(URL);
			}
		}
	}

	private void parseControlLink(String parttern, String begin, String end, String text) {
		Pattern title = Pattern.compile(parttern);
		Matcher matcher = title.matcher(mContent);
		if (matcher.find()) {
			String URL = matcher.group();
			int from = URL.indexOf(begin);
			int to = URL.indexOf(end);
			URL = URL.substring(from, to + end.length());
			if(mSet.contains(URL) == false)
			{
				mControlLinkList.add(new PPPLink(text, URL));
			}
		}
	}
	
	private void parsePageLinks(String parttern, String begin, String end, String begin2, String end2)
	{
		Pattern title = Pattern.compile(parttern);
		Matcher matcher = title.matcher(mContent);
		while (matcher.find()) {
			String tmp = matcher.group();
			int from = tmp.indexOf(begin);
			int to = tmp.indexOf(end);
			String URL = tmp.substring(from, to + end.length());
			
			int from2 = mContent.indexOf(begin2, matcher.start());
			int to2 = mContent.indexOf(end2, matcher.end());
			String text = mContent.substring(from2 + begin2.length(), to2);
			if(mSet.contains(URL) == false)
			{
				mPageLinkList.add(new PPPLink(text, URL));
			}
		}
	}

	private void parsePageLinks2()
	{
		//<a href=bbs0an,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2FD5288EDFA.html>明星</a>
		{
			Pattern title = Pattern.compile("<a href=bbs0an,path,");
			Matcher matcher = title.matcher(mContent);
			while (matcher.find()) {
				String tmp = matcher.group();
				int from = tmp.indexOf("bbs0an,path,");
				int to = mContent.indexOf(".html>", matcher.end());
				String URL = mContent.substring(matcher.start() + from, to + ",html".length());
				int from2 = mContent.indexOf(".html>", matcher.end());
				int to2 = mContent.indexOf("</a", matcher.end());
				String text = mContent.substring(from2 + ".html>".length(), to2);
				if(mSet.contains(URL) == false)
				{
					mPageLinkList.add(new PPPLink(text, URL));
				}
			}
		}
		
		//<a href=bbsanc,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2F3A6%2EA.html>台湾极品学生“豆花妹” </a>
		{
			Pattern title = Pattern.compile("<a href=bbsanc,path,");
			Matcher matcher = title.matcher(mContent);
			while (matcher.find()) {
				String tmp = matcher.group();
				int from = tmp.indexOf("bbsanc,path,");
				int to = mContent.indexOf(".html>", matcher.end());
				String URL = mContent.substring(matcher.start() + from, to + ",html".length());
				int from2 = mContent.indexOf(".html>", matcher.end());
				int to2 = mContent.indexOf("</a", matcher.end());
				String text = mContent.substring(from2 + ".html>".length(), to2);
				if(mSet.contains(URL) == false)
				{
					mPageLinkList.add(new PPPLink(text, URL));
				}
			}
		}
	}
	
	//call in thread
	public void fetchParserHTML() {
		mContent = HttpWrapper.getHttpContext(Conf.WEB_ADDRESS_BASE + mURL);
		if (mContent == null) {
			return;
		}

		parseImages();
		
		
		// <a href=bbstdoc,board,PPPerson,page,212.html>上一页</a>
		parseControlLink("<a href=bbstdoc,board,PPPerson,page,[0-9]\\d*.html>上一页</a>", "bbstdoc", "html", "上一页");
		// <a href=bbstdoc,board,PPPerson,page,214.html>下一页</a>
		parseControlLink("<a href=bbstdoc,board,PPPerson,page,[0-9]\\d*.html>下一页</a>", "bbstdoc", "html", "下一页");
		
		
		//<a href=bbstcon,board,PPPerson,reid,1398312415.html>○ 如此母女 </a>
		parsePageLinks("<a href=bbstcon,board,PPPerson,reid,[0-9]\\d*.html>○ ", "bbstcon", "html", ".html>○ ", "</a>");
				
		//<a href=bbs0an,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2FD5288EDFA.html>明星</a>
		//<a href=bbsanc,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2FD783CD619%2FD6CED321F%2FD8F3A61D8%2FM%2E1257000508%2EA.html>台湾极品学生“豆花妹”      
		parsePageLinks2();

	}
}
