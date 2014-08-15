package scalar.process;

import java.util.HashSet;

import scalar.virtuoso.VirtuosoServiceIMP;




public class Main {
	public static void main(String args[]) throws Exception
	{
		ScalarPageManager m=new ScalarPageManager();
		//m.deleteAllPages();//获取最新页面数据，删除所有页面,已测试，仅仅隐藏页面，可登录到scalar平台，dashboard中的pages，进行完全删除
		//m.deleteAllArtistImage();//获取最新图片数据，删除所有图片，已测试，可登录到scalar平台，dashboard中的media，进行完全删除
		//m.addArtistListPage();//添加姓名索引页面,已测试
		//////m.addLinkedArtistListPage();//添加链接的姓名索引页面
		//m.addAllArtistImage();//添加所有图片,已测试
		//m.addAllArtistPage();// 添加作者页面,部分图片暂时未显示,需要进一步测试
		//m.addAllCollectionImage();//测试了100条数据，需要重新测试图片问题，因为现在包括作家和作品图片
		//m.addAllCollectionPage();
		//m.addTest();
		//m.addArtistNameTag("http://collection.americanart.si.edu/id/person-institution/1079");
		//m.addAllArtistNameTag();
		
		
		//String personURI="http://collection.americanart.si.edu/id/person-institution/2283";//homer
		//m.addArtistRelatedPage(personURI);
		//String personURI="http://collection.americanart.si.edu/id/person-institution/4253";//john singer sargent
		//m.addArtistRelatedPage(personURI);
		//Person person=new VirtuosoServiceIMP().getPersonByURI(personURI);
		//println(person.getAssociatePlaceSet());
		//System.out.println(person.getAssociatePlaceURISet());
		//System.out.println(person.getAssociateEventURISet());
		
		
		
		//m.addAllSubjectGeneralTag();
		//m.addAllPlaceTag(("http://collection.americanart.si.edu/id/person-institution/4253"));
		//String personURI="http://collection.americanart.si.edu/id/person-institution/4253";//john singer sargent
	    //m.addArtistRelatedPage(personURI);
		//m.addPlaceTagToArtistPage("http://collection.americanart.si.edu/id/person-institution/4253");
		
		//for(Work temp:new VirtuosoServiceIMP().getCollectionList(personURI))
			//System.out.println(temp.getKeeper()+" "+temp.getTitle());
		addTest();
		//String personURI="http://collection.americanart.si.edu/id/person-institution/4253";
		//new ScalarPageManager().addArtistRelatedPage(personURI);
		/*HashMap<String,String> map=new VirtuosoServiceIMP().getAllLinkedArtistName("J");
		Set<String> set=map.keySet();
		for(String temp:set)
			System.out.println(temp+map.get(temp));
			*/
	}
	
	public static void addTest()
	{
		ScalarPageManager m=new ScalarPageManager();
		m.addLinkedArtistListPage();//添加姓名索引页面,已测试
		m.addAllSubjectGeneralTag();
		
		
		HashSet<String> personURISet=new VirtuosoServiceIMP().getAllLinkedPersonURI();
		
		//m.addAllPlaceTag(personURISet);
		int i=1;
		for(String personURI:personURISet)
		{
			System.out.println("*******adding "+i);
			if(i>=27 && i<=100)
			{
				m.addArtistRelatedPage(personURI);
			}
			i++;
		}//5,18,19,24 no finished
			
		System.out.println(personURISet.size());
		
			
	}
	
	/*
	 * 
	 <iframe src="http://cdn.knightlab.com/libs/timeline/latest/embed/index.html?source=0ApZ6dUsHVeLWdE5GUGdYaThLS3ljUGRPb1NzOE9rWlE&amp;font=Bevan-PotanoSans&amp;maptype=toner&amp;lang=en&amp;height=650" width="100%" height="650" frameborder="0"></iframe>
	 */
	
	
	

}
