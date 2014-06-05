package com.annmars.iloveppp.page;

import com.annmars.iloveppp.util.Conf;
import com.annmars.iloveppp.util.HtmlNullException;

public class PPPPageFactory implements PageFactory{

	/*
	 * 
	1. ppp �������� https://bbs.sjtu.edu.cn/bbsfdoc2?board=PPPerson
		1. <a href=bbsfdoc2?board=PPPerson&start=104777>��һҳ</a>
		2. <a href=bbsfdoc2?board=PPPerson&start=104797>��һҳ</a>
		3. <a href=bbsfdoc2?board=PPPerson>ĩҳ</a>
		4. <a href=http://bbs.sjtu.edu.cn/file/PPPerson/1398866684128972.jpg target=_blank>1398866684128972.jpg </a>

	2. ����ģʽ  https://bbs.sjtu.edu.cn/bbstdoc,board,PPPerson.html
		1.  <a href=bbstdoc,board,PPPerson,page,213.html>��һҳ</a>
		2. <a href=bbstdoc,board,PPPerson,page,215.html>��һҳ</a>
		3. <a href=bbstcon,board,PPPerson,reid,1396946316.html>�� ��� </a>
			1. file/PPPerson/1399255124156930.jpg


	3.       ������ https://bbs.sjtu.edu.cn/bbs0an?path=%2Fgroups%2FGROUP_7%2FPPPerson
		1. <a href=bbs0an,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2FD5288EDFA.html>����</a>
			1. <a href=bbs0an,path,%2Fgroups%2FGROUP%5F7%2FPPPerson%2FD5288EDFA%2FD494E96BD.html>Ů����</a>
				1. http://bbs.sjtu.edu.cn:8000/PPPerson/1101213924223210.jpg
				https://bbs.sjtu.edu.cn/bbsanc,path,%2Fgroups%2FGROUP_7%2FPPPerson%2FD783CD619%2FD861834F6%2FM.1260032420.A.html
	 */
	
	@Override
	public Page createPage(String URL) {
		Page page = PageCache.getInstance().getPage(URL);
		if(page != null)
		{
			if(page instanceof MyCollectedImagePage)
			{
				try {
					page.refresh();
				} catch (HtmlNullException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return page;
		}
		
		if(URL.equals(Conf.LOCAL_URL_MY_COLLECTED_IMAGE))
		{
			page = new MyCollectedImagePage(URL);
		}
		else if(URL.startsWith(Conf.WEB_ADDRESS_BASE + "bbstdoc,board,PPPerson"))
		{
			page = new SubjectPage(URL);
		}
		else if(URL.startsWith(Conf.WEB_ADDRESS_BASE + "bbs0an"))
		{
			page = new CollectPage(URL);
		}
		else if(URL.startsWith(Conf.WEB_ADDRESS_BASE + "bbstcon,board,PPPerson,reid"))
		{
			page = new SubjectImagePage(URL);
		}
		else if(URL.startsWith(Conf.WEB_ADDRESS_BASE + "bbsanc,path,"))
		{
			page = new CollectImagePage(URL);
		}
		else
		{
			page = new ImagePage(URL);
		}
		PageCache.getInstance().addPage(page);
		return page;
	}
}
