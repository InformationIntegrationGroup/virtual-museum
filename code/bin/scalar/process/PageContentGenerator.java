package scalar.process;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scalar.entity.PageProperty;
import scalar.entity.Person;
import scalar.entity.Work;

/**
 * 该类主要用于生成各种要提交到scalar平台的页面内容
 * @author wan jing
 * 
 */
public class PageContentGenerator {

	/**
	 * 生成artist列表页面内容。
	 * 根据artist的名字和uri生成html页面的内容,startString为firstname为startString的artist
	 * 该页面包括所有符合条件的artist
	 * 
	 * @param result 包含artist的uri和firstName##displayname,格式为（uri，firstname＃＃displayname）
	 * @param startString 指定firstName的首字母
	 * @return String 返回artist列表页面内容，包含首字母为startString的artist
	 */
	public String generateArtistListPageContent(
			HashMap<String, String> result, String startString) {

		//最上方内容
		String header = "<div id=\" contentSearchAlpha\"  style=\" height: 12px; padding: 12px 0px 0px; width: 580px; background-color: rgb(255, 255, 255);\" >"
				+ "<ul style=\" list-style: none; margin: 0px; padding: 0px;\" >";

		//A｜B｜C｜...|Z ,生成相应的链接
		for (int k = 0; k <= 25; k++) {
			header = header
					+ "<li style=\" float: left; padding: 0px 6px; border-right-width: 1px; border-right-style: solid; border-right-color: rgb(0, 0, 0); font-size: 1em; line-height: 11.999999046325684px; font-family: palatino, georgia, serif; margin-bottom: 1.4em;\" >"
					+ "<a href=\" http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--"
					+ (new Character((char) (65 + k))).toString().toLowerCase()
					+ "\" style=\" color: rgb(0, 0, 0); border: 0px;\" >"
					+ (new Character((char) (65 + k))).toString() + "</a></li>";
		}
		header = header + "</ul></div>";
		String middle = " <div class=\" callout\"  style=\" padding: 15px 20px 30px; clear: both; border-color: rgb(0, 69, 89); background-color: rgb(238, 238, 238);\" ><div id=\" contentSearchListArtist\" ><ul style=\" margin: 0px; padding: 0px; list-style: none;\" >";

		Set<String> keysets = result.keySet();

		// 排序firstname
		TreeSet<String> sort = new TreeSet<String>();
		for (Object temp : keysets) {
			String uri = (String) temp;
			String nameString = (String) result.get(temp);
			String name = nameString.substring(0, nameString.indexOf("##"));
			String firstname = nameString.substring(
					nameString.indexOf("##") + 2, nameString.length());
			sort.add(firstname + "##" + name + "$$" + uri);
		}

		
		//添加每一个作家的姓名和链接
		for (String temp : sort) {
            String uri = temp.substring(temp.indexOf("$$") + 2, temp.length());
			String name = temp.substring(temp.lastIndexOf("##") + 2,
					temp.indexOf("$$"));
			System.out.println(uri);
			System.out.println(name);
			middle = middle
					+ "<li style=\" border-bottom-width: 1px; border-bottom-style: dotted; border-bottom-color: rgb(0, 0, 0); padding: 8px 0px 8px 40px; position: relative; margin-bottom: 1.4em; font-size: 1em; line-height: 14px; font-family: palatino, georgia, serif; margin-top: 0px !important; margin-right: 0px !important; margin-left: 0px !important;\" ><a href=\" ";
			middle = middle 
					+ PageProperty.BASE_URL+"/"+convertNameToUrl(name);
			middle = middle
					+ "\"  style=\" color: rgb(0, 0, 0); border: 0px; font-size: 12px !important; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif !important;\" ><span class=\" artistName\"  style=\" white-space: nowrap; font-weight: bold;\" >";
			middle = middle + name;
			middle = middle + "</span>&nbsp;</a></li>";
		}

		middle = middle + "</ul></div></div>";
    	return header + middle;
	}

	
	
	/**
	 * 生成一个作家的页面内容
	 * @param person 包含作家的所有信息
	 * @param imageUrnScalarVersion 作家照片在scalar平台上对应的version
	 * @param resource 作家照片在scalar平台上对应的resource
	 * @return String 一个作家的页面的内容
	 */
	public  String generateArtistPageContent(Person person,String imageUrnScalarVersion, String resource) {
		//生成图片链接
		String picture = "";
		if (person.getMainRepresentationURI() != null&& person.getMainRepresentationURI().length() > 0) 
		{
			picture = "<a class=\"inline\" href=\""
					+ person.getMainRepresentationURI() + "\" resource=\""
					+ resource + "\" rel=\"" + imageUrnScalarVersion
					+ "\"></a>";
		}

		//出生地点和日期
		String birth="";
		if((person.getBirthDate()!=null&&person.getBirthDate().length()>0)||
				(person.getBirthPlace()!=null&&person.getBirthPlace().length()>0))
		{
		     birth= "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px !important; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif;\"><strong>Born:</strong>"
				+ person.getBirthPlace()
				+ "&nbsp;"
				+ person.getBirthDate()
				+ "</p>";
		}
		
		//死亡地点和日期
		String death="";
		if((person.getDeathDate()!=null&&person.getDeathDate().length()>0)||
				(person.getDeathPlace()!=null&&person.getDeathPlace().length()>0))
		{
			death = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\"><strong>Died:</strong>"
		        + person.getDeathPlace()
				+ "&nbsp;"
				+ person.getDeathDate()
				+ "</p>";
		}

		// 应该有多个地点，需要修改
		//活动地点
		String activePlace="";
		if(person.getAssociatePlace()!=null&&person.getAssociatePlace().length()>0)
		{
			activePlace = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\">"
		        + "<strong>Active in:</strong></p>"
				+ "<ul style=\"list-style: square inside; margin: -12px 0px 0px; padding: 0px 0px 12px; font-size: 12px; line-height: 16px; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif; background-color: rgb(255, 255, 255);\">"
				+ " <li  style=\"margin: 0px ; padding: 0px ;\">"
				+ person.getAssociatePlace() + "</li>" + "</ul>";
		}
		
		//作家简介
		String bio = "";
		if(person.getArtistBio()!=null&&person.getArtistBio().length()>0)
		{
		    bio = "<h3>Biography</h3>"
				+ "<p style=\"padding: 0px; background-color: rgb(238, 238, 238); font-size: 14px ; line-height: 28px ; font-family: Georgia, 'Times New Roman', Times, serif ; margin: 0px 0px 14px ;\">"
				+ person.getArtistBio() + "</p>";
		}

		//smithsonian american art museum链接
		String linkToSmith = "";
		if (person.getId().length() > 0) {
			linkToSmith = "<h3>Links</h3>"
					+ "<p style=\"padding: 0px; background-color: rgb(238, 238, 238); font-size: 14px ; line-height: 28px ; font-family: Georgia, 'Times New Roman', Times, serif ; margin: 0px 0px 14px ;\">"
					+ "<a href=\"http://americanart.si.edu/collections/search/artist/?id="
					+ person.getId()
					+ "\">Smithsonian American Art Museum</a></p>";
		}

		//new york times链接
		String linkToNY = "";
		if (person.getNytimesURI() != null
				&& person.getNytimesURI().length() > 0) {
			linkToNY = "<p style=\"padding: 0px; background-color: rgb(238, 238, 238); font-size: 14px ; line-height: 28px ; font-family: Georgia, 'Times New Roman', Times, serif ; margin: 0px 0px 14px ;\"><a href=\""
					+ person.getNytimesURI() + "\">New York Times</a></p>";
		}
        
		//wikipedia链接
		String linkToWiki = "";
		if (person.getWikipediaURI() != null
				&& person.getWikipediaURI().length() > 0) {
			linkToWiki = "<p style=\"padding: 0px; background-color: rgb(238, 238, 238); font-size: 14px ; line-height: 28px ; font-family: Georgia, 'Times New Roman', Times, serif ; margin: 0px 0px 14px ;\"><a href=\""
					+ person.getWikipediaURI() + "\">Wikipedia</a></p>";
		}
		

		String result = picture + birth + death + activePlace + bio
				+ linkToSmith + linkToNY + linkToWiki;

		return result;
	}
	
	
	/**
	 * 生成一个作品的页面内容
	 * @param collection 包含作品的所有信息
	 * @param imageUrnScalarVersion 作品照片在scalar平台上对应的version
	 * @param resource 作品照片在scalar平台上对应的resource
	 * @return String 一个作品的页面的内容
	 */
	public  String generateCollectionPageContent(Work collection,String imageUrnScalarVersion, String resource) {
		//生成图片链接
		String picture = "";
		if (collection.getRepresentationURI()!= null&& collection.getRepresentationURI().length() > 0) 
		{
			picture = "<a class=\"inline\" href=\""
					+ collection.getRepresentationURI() + "\" resource=\""
					+ resource + "\" rel=\"" + imageUrnScalarVersion
					+ "\"></a>";
		}
      
		//年代
		String produceDate="";
		if((collection.getProduceDate()!=null&&collection.getProduceDate().length()>0))
		{
		     produceDate= "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px !important; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif;\">"
				+ collection.getProduceDate()
				+ "</p>";
		}
		
		
		//作者
		String artist="";
		if((collection.getPersonList()!=null&&collection.getPersonList().size()>0))
		{
			 artist = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\">";
				     
			 for(Person temp:collection.getPersonList())
			 {
				 artist=artist+"<a href=\""
						 +temp.getScalarURI()+"\">"
						 +temp.getDisplayName()+"&nbsp;"+"</a>";
			 }
			 artist=artist+ "&nbsp;"+ "</p>";
		}
		
		//大小
		String dimension="";
		if((collection.getWidth()!=null&&collection.getWidth().length()>0)||
				(collection.getHeight()!=null&&collection.getHeight().length()>0))
		{
			dimension = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\">"
		        + collection.getWidth()
				+ "cm *&nbsp;"
				+ collection.getHeight()
				+"cm"
				+ "</p>";
		}
		  
		//材质
		String medium="";
		if((collection.getMedium()!=null&&collection.getMedium().length()>0))
		{
			 medium = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\">"
				        + collection.getMedium()
						+ "&nbsp;"
						+ "</p>";
		}
		
		//博物馆
		String museum="";
		if((collection.getKeeper()!=null&&collection.getKeeper().length()>0))
		{
			 museum = "<p style=\"padding: 0px; background-color: rgb(238, 238, 238); font-size: 14px ; line-height: 28px ; font-family: Georgia, 'Times New Roman', Times, serif ; margin: 0px 0px 14px ;\">";
			 if(collection.getKeeper().indexOf("smithsonian")>=0)
				 museum=museum+"<a href=\"http://americanart.si.edu/collections/search/artwork/?id="
				+ collection.getId()
				+ "\">";
			 museum=museum+	collection.getKeeper()
				+"</a></p>";
		}
		
		/*//描述
		String description="";
		if((collection.getMdium()!=null&&collection.getDescription().length()>0))
		{
			 description = "<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\"><strong></strong>"
						 + collection.getDescription()
						 + "&nbsp;"
						 + "</p>";
		}*/
		
		
		//关键词
		String keywords="";
		if(collection.getKeywordList()!=null&&collection.getKeywordList().size()>0)
		{
			keywords="<p style=\"margin: 0px; padding: 0px 0px 12px; background-color: rgb(255, 255, 255); font-size: 12px ; line-height: 16px ; font-family: 'Lucida Sans Unicode', 'Lucida Grande', Arial, Helvetica, sans-serif ;\">"
					 + collection.getKeywordList().toString()
					 + "&nbsp;"
					 + "</p>";
		}
		
		//类别
		

		
        String result = picture+produceDate+artist+dimension+medium+museum+keywords;

		return result;
	}
	
	//将作家名字转换成scalar平台上的url链接格式
	public String convertNameToUrl(String name)
	{
	   String url;
	   //url=name.replace(". ","-").replace(" ","-").replace(".","-").toLowerCase();
	   url=name.replace(" ","-").toLowerCase();
	   url=url.replace("--","-").toLowerCase();
	   
	   //url中删除除了空格-,/,字母，数字之外的其他符号
	   String regex = "[^A-Za-z0-9/-]";
       Pattern pattern = Pattern.compile(regex);
       Matcher mp = pattern.matcher(url);
       url=mp.replaceAll("").trim(); 
	   
	   
	   return url;
	 }

}
