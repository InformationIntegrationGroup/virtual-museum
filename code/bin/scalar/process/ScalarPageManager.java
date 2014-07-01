package scalar.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import scalar.entity.PageProperty;
import scalar.entity.Person;
import scalar.entity.Work;
import scalar.virtuoso.VirtuosoServiceIMP;

/**
 * 该类主要对Scalar平台上一本书的内容进行管理
 * 包括添加页面，图片，删除页面，图片等
 * @author wan jing
 *
 */
public class ScalarPageManager implements ScalarPageManagerInterface{
	VirtuosoServiceIMP virtuosoService=new VirtuosoServiceIMP();
	PageContentGenerator generator=new PageContentGenerator();
	PagePropertyObtainer propertyObtainer=new PagePropertyObtainer();
	//ArrayList<Person> errorList=new ArrayList<Person>();//用来测试在添加作家页面时，有多少个作家没有名称，该类作家页面不被创建
   
	/**
	 * 该方法用来向scalar平台中添加作家姓名索引页面，一共26个，从A－Z
	 */
	public  void addArtistListPage() {
		HashMap<String, String> result;
		String content;
		HashMap<String, String> data;
		int sum=0;

		for (int i = 0; i <= 25; i++) {
			String firstCharacter = new Character((char) (65 + i)).toString();
			result = virtuosoService.getAllArtistName(firstCharacter); // 得到所有artist的名字和uri
			sum=sum+result.size();
			content = generator.generateArtistListPageContent(result, firstCharacter); // 生成html页面的内容
            System.out.println("content:"+content);
			// 设置带传递的参数
			data = new HashMap<String, String>();
			data.put("id", PageProperty.ID);
			data.put("action", "ADD");
			data.put("dcterms:title", "List of Artists | " + firstCharacter);
			data.put("sioc:content", content);
			data.put("rdf:type","http://scalar.usc.edu/2012/01/scalar-ns#Composite");
			data.put("api_key", PageProperty.API_KEY);
			data.put("scalar:child_urn", "urn:scalar:book:2842");
			data.put("scalar:child_type","http://scalar.usc.edu/2012/01/scalar-ns#Book");
			data.put("scalar:child_rel", "page");
			data.put("scalar:metadata:is_live", "1");
			String url = PageProperty.BASE_URL+"/api/add";
			try {
				// 向scalar平台提交数据
				getResponse(post(url, generateContent(data)));
				System.out.println("提交页面数据"+i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("一共有作家"+sum);
	}

	
	
// ****添加所有作家的照片*****开始*****/
    /**
	 * 该方法用来向scalar平台提交所有作家的照片，每个作家只提交一张照片
	 */
	public  void addAllImage() {
		TreeSet<String> artistImageURIList = virtuosoService.getAllArtistImageURI();// 获取所有作家的照片的uri
		for (String temp : artistImageURIList) 
		{
		    addImage(temp);
		}
		System.out.println("共添加作家照片"+artistImageURIList.size());
	}

	/**
	 * 该方法用来向scalar平台上提交一个照片
	 * 对于smithsonian American art museum，由于每个图片都有对应的url，该方法是有效的
	 * @param imageURI 照片的url，照片对应的网络上的url
	 */
	public  void addImage(String imageURI) {
	    HashMap<String, String> data;
        // 设置带传递的参数
		data = new HashMap<String, String>();
		data.put("id", PageProperty.ID);
		data.put("action", "ADD");
		data.put("dcterms:title",imageURI.substring(imageURI.lastIndexOf("/") + 1,imageURI.length()));
		data.put("sioc:content", "");
		data.put("scalar:metadata:url", imageURI);
		data.put("rdf:type", "http://scalar.usc.edu/2012/01/scalar-ns#Media");
		data.put("api_key", PageProperty.API_KEY);
		data.put("scalar:child_urn", "urn:scalar:book:2842");
		data.put("scalar:child_type","http://scalar.usc.edu/2012/01/scalar-ns#Book");
		data.put("scalar:child_rel", "referenced");
		data.put("scalar:metadata:is_live", "1");
		String url = PageProperty.BASE_URL+"/api/add";
		try {
			// 向scalar平台提交数据
			System.out.println("添加图片"+imageURI);
			getResponse(post(url, generateContent(data)));
          } catch (IOException e) {
			e.printStackTrace();
		}

	}
// ****添加所有作家的照片*****结束*****/

	// ****添加所有作品的照片*****开始*****/
    /**
	 * 该方法用来向scalar平台提交所有作品的照片，每个作品只提交一张照片
	 */
	public  void addAllCollectionImage() {
		TreeSet<String> collectionImageURIList = virtuosoService.getAllCollectionImageURI();// 获取所有作品的照片的uri
		int i=0;
		for (String temp : collectionImageURIList) 
		{
			if (i>=100)
				break;
		    addImage(temp);
		    i++;
		}
		System.out.println("共添加作品照片"+collectionImageURIList.size());
	}

	
// ****添加所有作品的照片*****结束*****/
	
// ****添加所有作家*****开始*****/
	/**
	 * 该方法用来向scalar平台添加所有作家的页面
	 * 由于需要在页面上添加照片的链接，所以在该方法使用之前，所有的作家照片应该已经提交到了scalar平台上
	 * 该方法从平台上实时获取照片的信息
	 */
	public  void addAllArtistPage() {
		TreeSet<String> artistURIList = virtuosoService.getAllArtistURI();// 获取所有作家的uri
		propertyObtainer.getImageResourceVersion();//从scalar平台获取Image的resource和version，以便在页面添加链接
        
		/*//测试图片的version取值是否正确，已完成测试
		 String result="";
		for(String temp:PageProperty.ArtistImageResourceVersionMap.keySet())
        	result=result+temp+" "+PageProperty.ArtistImageResourceVersionMap.get(temp)+"/r/n";
        writeToFile("/users/jwan/desktop/result.txt",result);
		System.out.println(PageProperty.ArtistImageResourceVersionMap.size());
		*/
		int sum=0;
		int i=0;
		for (String temp : artistURIList) {
			System.out.print(i);
			if(addArtistPage(temp))
				sum++;
			if(i>=20)//测试用
				break;
			i++;
		}
		System.out.println("作家uri共"+artistURIList.size());
		System.out.println("共添加作家页面"+sum);
		

		/*
		for(Person temp:errorList)
		{
			System.out.println("出错数据:"+temp.getURI()+"  "+temp.getDisplayName());
		}
		System.out.println("出错数据共"+errorList.size());
		*/
	}

	/**
	 * 该方法用来向scalar平台上添加一个作家页面
	 * 对于smithsonian American art museum，artisturi的例子为
	 * http://collection.americanart.si.edu/id/person-institution/4253 
	 * @param artistURI 作家的uri，来自triple store
	 * @return boolean 如果添加成功，则返回true
	 */
	public  boolean addArtistPage(String artistURI) {
		boolean addResult=false;
        // 得到artist的各种信息
		Person person = virtuosoService.getPersonByURI(artistURI); 
		
		//由于嵌入图片时需要知道图片url，resource和version
		//图片的version，从scalar平台获取，保存在ArtistImageResourceVersionMap中
		String imageUrnScalarVersion= (String) PageProperty.ImageResourceVersionMap.get(person.getMainRepresentationURI());
		//图片的url 保存在person中
		String imageURI = person.getMainRepresentationURI();
		//图片的resource，resource是图片名字，替换所有符号“.”得到
		String resource = "";
		if (imageURI != null && imageURI.length() > 0)
			resource = imageURI.substring(imageURI.lastIndexOf("/") + 1,imageURI.length()).replace(".", "").toLowerCase();
		
		// 作家页面的内容
		String content = generator.generateArtistPageContent(person, imageUrnScalarVersion,resource); 
		/*
		if(person.getDisplayName()!=null&&person.getDisplayName().length()>0)
			if(imageUrnScalarVersion!=null&&imageUrnScalarVersion.length()>0&&imageUrnScalarVersion.indexOf("191634")>0)
			     System.out.println(person.getURI()+"  "+person.getMainRepresentationURI()+" "+imageUrnScalarVersion+"  "+resource+"\n");
        */
		
		// 设置带传递的参数
		HashMap<String, String> data;//要提交的参数
		data = new HashMap<String, String>();
		data.put("id", PageProperty.ID);
		data.put("action", "ADD");
		data.put("dcterms:title", person.getDisplayName());
		data.put("sioc:content", content);
		data.put("rdf:type","http://scalar.usc.edu/2012/01/scalar-ns#Composite");
		data.put("api_key", PageProperty.API_KEY);
		data.put("scalar:child_urn", "urn:scalar:book:2842");
		data.put("scalar:child_type","http://scalar.usc.edu/2012/01/scalar-ns#Book");
		data.put("scalar:child_rel", "page");
		data.put("scalar:metadata:is_live", "1");
		String url = PageProperty.BASE_URL+"/api/add";
		
	
		// 向scalar平台提交数据
		try {
			if(person.getDisplayName()!=null&&person.getDisplayName().length()>0)
			{
			    System.out.println("添加作家页面:"+person.getDisplayName());
		        getResponse(post(url, generateContent(data)));
		        addResult=true;
			}
			else
			{
				//errorList.add(person);
			}
           } catch (IOException e) {
			e.printStackTrace();
			}
		return addResult;
		
    }
// ************添加所有作家*****结束*************/

	/**
	 * 该方法用来向scalar平台上添加一个作品页面 对于smithsonian American art museum，artisturi的例子为
	 *  http://collection.americanart.si.edu/id/object/1970.39 
     * @param collectionURI 作品的uri，来自triple store
     * @return boolean 如果添加成功，则返回true
	 */
	public boolean addCollectionPage(String collectionURI)
	{
		boolean addResult=false;
        // 得到collection的各种信息
		Work collection = virtuosoService.getCollectionByURI(collectionURI); 
		
		//由于嵌入图片时需要知道图片url，resource和version
		//图片的version，从scalar平台获取，保存在ImageResourceVersionMap中
		String imageUrnScalarVersion= (String) PageProperty.ImageResourceVersionMap.get(collection.getRepresentationURI());
		//图片的url 保存在collection中
		String imageURI = collection.getRepresentationURI();
		//图片的resource，resource是图片名字，替换所有符号“.”得到
		String resource = "";
		if (imageURI != null && imageURI.length() > 0)
			resource = imageURI.substring(imageURI.lastIndexOf("/") + 1,imageURI.length()).replace(".", "").toLowerCase();
		
		// 作品页面的内容
		String content = generator.generateCollectionPageContent(collection, imageUrnScalarVersion,resource); 
		/*
		if(person.getDisplayName()!=null&&person.getDisplayName().length()>0)
			if(imageUrnScalarVersion!=null&&imageUrnScalarVersion.length()>0&&imageUrnScalarVersion.indexOf("191634")>0)
			     System.out.println(person.getURI()+"  "+person.getMainRepresentationURI()+" "+imageUrnScalarVersion+"  "+resource+"\n");
        */
		
		// 设置带传递的参数
		HashMap<String, String> data;//要提交的参数
		data = new HashMap<String, String>();
		data.put("id", PageProperty.ID);
		data.put("action", "ADD");
		data.put("dcterms:title", collection.getTitle());
		data.put("sioc:content", content);
		data.put("rdf:type","http://scalar.usc.edu/2012/01/scalar-ns#Composite");
		data.put("api_key", PageProperty.API_KEY);
		data.put("scalar:child_urn", "urn:scalar:book:2842");
		data.put("scalar:child_type","http://scalar.usc.edu/2012/01/scalar-ns#Book");
		data.put("scalar:child_rel", "page");
		data.put("scalar:metadata:is_live", "1");
		String url = PageProperty.BASE_URL+"/api/add";
		
	
		// 向scalar平台提交数据
		try {
			if(collection.getTitle()!=null&&collection.getTitle().length()>0)
			{
			    System.out.println("添加作品页面:"+collection.getTitle());
		        getResponse(post(url, generateContent(data)));
		        addResult=true;
			}
			else
			{
				//errorList.add(person);
			}
           } catch (IOException e) {
			e.printStackTrace();
			}
		return addResult;
	}
	
	/**
	 * 该方法用来向scalar平台添加所有作品的页面
	 * 由于需要在页面上添加照片的链接，所以在该方法使用之前，所有的作品照片应该已经提交到了scalar平台上 该方法从平台上实时获取照片的信息
	 */
	public void addAllCollectionPage()
	{
		TreeSet<String> collectionURIList = virtuosoService.getAllCollectionURI();// 获取所有作品的uri
		propertyObtainer.getImageResourceVersion();//从scalar平台获取Image的resource和version，以便在页面添加链接
        
		/*//测试图片的version取值是否正确，已完成测试
		 String result="";
		for(String temp:PageProperty.ArtistImageResourceVersionMap.keySet())
        	result=result+temp+" "+PageProperty.ArtistImageResourceVersionMap.get(temp)+"/r/n";
        writeToFile("/users/jwan/desktop/result.txt",result);
		System.out.println(PageProperty.ArtistImageResourceVersionMap.size());
		*/
		int sum=0;
		int i=0;
		for (String temp : collectionURIList) {
			System.out.print(i);
			if(addCollectionPage(temp))
				sum++;
			i++;
			if (i>=5)
				break;
		}
		System.out.println("作品uri共"+collectionURIList.size());
		System.out.println("共添加作品页面"+sum);
		

		/*
		for(Person temp:errorList)
		{
			System.out.println("出错数据:"+temp.getURI()+"  "+temp.getDisplayName());
		}
		System.out.println("出错数据共"+errorList.size());
		*/
		
	}
	
	
	/**
	 * 该方法用来删除Scalar上所有的已有页面
	 */
	public  void deleteAllPages() {
		propertyObtainer.getAllPage();// 获取所有页面的结构数据
		for (String temp : PageProperty.UrnScalarVersionList)
			deleteItem(temp);
		System.out.println("所有页面已经删除,共删除"+PageProperty.UrnScalarVersionList.size());
     }
	
	/**
	 * 该方法用来删除Scalar上所有的作家的照片
	 */
	public void deleteAllArtistImage()
	{
		propertyObtainer.getImageResourceVersion();//获取所有作家图片的结构数据
		Set<String> keySet=PageProperty.ImageResourceVersionMap.keySet();
		for(String temp:keySet)
		{
			deleteItem(PageProperty.ImageResourceVersionMap.get(temp));
		}
		System.out.println("所有作家图片已经删除,共删除"+PageProperty.ImageResourceVersionMap.size());
	}

	/**
	 * 该方法用来删除scalar上一个指定的页面或图片
	 * @param urnScalarVersion
	 */
	public  void deleteItem(String urnScalarVersion) {
		String url = PageProperty.BASE_URL+"/api/delete";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("id", PageProperty.ID);
		data.put("action", "DELETE");
		data.put("api_key", PageProperty.API_KEY);
		data.put("scalar:urn", urnScalarVersion);
		try {
			// 向scalar平台提交数据
			post(url, generateContent(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// update a page
	public  void update() throws IOException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("id", PageProperty.ID);
		data.put("action", "UPDATE");
		data.put("scalar:urn", "urn:scalar:version:187426");
		data.put("dcterms:title", "Test page, updated 3");
		data.put("sioc:content", "This is the updated content of the page.");
		data.put("rdf:type",
				"http://scalar.usc.edu/2012/01/scalar-ns#Composite");
		data.put("api_key", PageProperty.API_KEY);

		String url = PageProperty.BASE_URL+"/api/update";
		post(url, generateContent(data));
	}

	/**
	 * 该方法用来向scalar平台提交数据
	 * 需要与getResponse配合使用，getResponse（post（））
	 * @param urlString 提交数据的url
	 * @param content 提交的数据内容
	 * @return URLConnection url链接，用于获取平台返回信息
	 * @throws IOException
	 */
	public  URLConnection post(String urlString, String content) throws IOException {
		URL url;
		URLConnection urlConn;
		DataOutputStream printout;
		
		// URL of CGI-Bin script.
		url = new URL(urlString);
		// URL connection channel.
		urlConn = url.openConnection();
		// Let the run-time system (RTS) know that we want input.
		urlConn.setDoInput(true);
		// Let the RTS know that we want to do output.
		urlConn.setDoOutput(true);
		// No caching, we want the real thing.
		urlConn.setUseCaches(false);
		// Specify the content type.
		urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		// Send POST output.
		printout = new DataOutputStream(urlConn.getOutputStream());
		printout.writeBytes(content);
		printout.flush();
		printout.close();
		return urlConn;
	}
	
	/**
	 * 该方法用于在向scalar平台post数据之后，获取平台返回的信息
	 * @param urlConn post时建立的url connection
	 * @return scalar平台返回的数据
	 * @throws IOException
	 */
	public  String getResponse(URLConnection urlConn) throws IOException
	{
		String result = "";
		BufferedReader input= new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		String str;
		while (null != ((str = input.readLine()))) {
			result = result + str;
		}
		input.close();
		return result;
	}
	
	/**
	 * 该方法根据要提交的参数对，生成post的内容
	 * @param data 要提交的属性名和值
	 * @return 生成的post的内容
	 */
	public  String generateContent(HashMap<String,String> data) 
	{
		String content = null;
		try {
			Set<String> keyset = data.keySet();
			int i = 0;
			for (Object key : keyset) 
			{
				String value = (String) data.get(key);
				if (i == 0)
					content = (String) key + "="+ URLEncoder.encode(value, "utf-8");
				else
					content = content + "&" + (String) key + "="+ URLEncoder.encode(value, "utf-8");
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	private void writeToFile(String fileName,String content)
	{
		File file = new File(fileName);  
        try{
		file.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(file));  
        out.write(content); // \r\n即为换行  
        out.flush(); // 把缓存区内容压入文件  
        out.close(); }
        catch(IOException e)
        {
        	e.printStackTrace();
        }
	}
}
