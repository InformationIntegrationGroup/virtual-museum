package scalar.virtuoso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scalar.entity.PageProperty;
import scalar.entity.Person;
import scalar.entity.Work;
import scalar.process.PageContentGenerator;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;


/**
 * @author wan jing
 *
 */
public class VirtuosoServiceIMP  {
	
    private VirtGraph set;
    
    public static void main(String []args)
    {
    	//new VirtuosoServiceIMP().getAllLinkedPersonURI();
    	//for (String temp:new VirtuosoServiceIMP().getAllPlaceURI("http://collection.americanart.si.edu/id/person-institution/4253"))
    	//for (String temp:new VirtuosoServiceIMP().getAllGeneralSubject())
    	
    	//System.out.println(new VirtuosoServiceIMP().getImageURIByCollectionURI("http://collection.americanart.si.edu/id/object/1935.13.407"));
    	//System.out.println(new VirtuosoServiceIMP().getGeneralSubjectByCollectionURI("http://collection.americanart.si.edu/id/object/1935.13.407"));
    	//System.out.println(new VirtuosoServiceIMP().getImageURIByPersonURI("http://collection.americanart.si.edu/id/person-institution/4253"));
    	//System.out.println(new VirtuosoServiceIMP().ifLinkToSmith("http://collection.cartermuseum.org/id/person-institution/john_singer_sargent"));//true
    	//System.out.println(new VirtuosoServiceIMP().ifLinkToSmith("http://collection.cartermuseum.org/id/person-institution/otto_krebs"));//false
    	
    	//for(String temp:new VirtuosoServiceIMP().getAllArtistURI())
    		//System.out.println(temp);
    	//for(String temp:new VirtuosoServiceIMP().getAllCollectionURI())
    		//System.out.println(temp);
    	
    	//for(String temp:new VirtuosoServiceIMP().getAllArtistImageURI())
    	  //System.out.println(temp);
    	//for(String temp:new VirtuosoServiceIMP().getAllCollectionImageURI())
    	 //System.out.println(temp);
    	//System.out.println(new VirtuosoServiceIMP().getMuseumNameByURI("http://collection.americanart.si.edu/id/the-smithsonian-american-art-museum"));
    	//System.out.println(new VirtuosoServiceIMP().getMuseumNameByURI("http://collection.cartermuseum.org/id/the-amon-carter-museum-of-american-art"));
    	//System.out.println(new VirtuosoServiceIMP().getMuseumNameByURI("http://collection.crystalbridges.org/id/the-crystal-bridges-museum-of-american-art"));
    	
    	//for(Work temp:new VirtuosoServiceIMP().getCollectionList("http://collection.americanart.si.edu/id/person-institution/4253"))
    		//System.out.println(temp.getTitle());
    	
	    // System.out.println(new VirtuosoServiceIMP().getAllArtistName("A"));
    	
    }
    
    /**
     * 查询并返回所有同时在其他的博物馆中也存在的smithsonian american art museum的作者
     * @return 包含PersonURI的集合
     */
    public HashSet<String> getAllLinkedPersonURI()
    {
    	HashSet<String> resultSet=new HashSet<String>();
    	
		
		String query = "select ?smithsonianPersonURI from <"+PageProperty.PersonLink_Graph+">  where { "
				+ "?personURI  <http://www.w3.org/2002/07/owl#sameAs>  ?smithsonianPersonURI. "
				+"}";
		
		resultSet=getHashSetValue(query,"smithsonianPersonURI");
    	return resultSet;
    }
    
    
    /**
     * 获取sparql返回的一个hash集合，取出的内容由name指定
     * 例如：select ?smithsonianPersonURI  where { 
				?personURI  <http://www.w3.org/2002/07/owl#sameAs>  ?smithsonianPersonURI.
				｝
				此处需要获取的值为smithsonianPersonURI
     * @param query 要执行的sparql语句
     * @param name 需要获取的值
     * @return
     */
    public HashSet<String> getHashSetValue(String query, String name)
    {
    	HashSet<String> resultSet=new HashSet<String>();
    	return (HashSet<String>)getSetValue(query,name,resultSet);
    }
    
    
    public Set<String> getSetValue(String query,String name,Set<String> resultSet)
    {
    	
        //System.out.println(query);
        
        set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
        
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{

			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get(name);
			resultSet.add(p.toString().trim());
			//System.out.println(p.toString().trim());
		}
	    return resultSet;
    	
    }
    
    public TreeSet<String> getTreeSetValue(String query, String name)
    {
    	TreeSet<String> resultSet=new TreeSet<String>();
    	return (TreeSet<String>)getSetValue(query,name,resultSet);
    }
    
    
    /**
     * 获取sparql返回的一个值，取出的内容由name指定
     * 例如：select ?smithsonianPersonURI  where { 
				?personURI  <http://www.w3.org/2002/07/owl#sameAs>  ?smithsonianPersonURI.
				｝
				此处需要获取的值为smithsonianPersonURI
     * @param query 要执行的sparql语句
     * @param name 需要获取的值
     * @return
     */
    public  String getValue(String query,String name)
	{
		String value="";
		HashSet<String> set=getHashSetValue(query,name);
		if(set!=null&&set.size()>=1)
			for(String temp:set)
			{
				if(name.indexOf("name")<0)
					value=temp;
				else if(name.indexOf("name")>=0&&temp.indexOf("Unidentified")<0)//对于作者姓名，有时候会有多个返回值，除去这个值
			        value=temp;
			}
		return value;
	}
    
    
    /**
     * 该方法用来获取所有的地点名称，出生地，死亡地，活跃地点
     * @param personURI
     * @return
     */
    public HashSet<String> getAllPlaceURI()
    {
    	HashSet<String> resultSet=new HashSet<String>();
    		
		String query = "select ?placeuri"+getGraph()+" where { "
				+ "?placeuri  <http://www.cidoc-crm.org/cidoc-crm/P2_has_type> <http://collection.americanart.si.edu/id/thesauri/placetype/City>."
			    +"}";
		
		resultSet=getHashSetValue(query,"placeuri");
	    return resultSet;
    }
    
    
    /**
     * 获取所有的graph，组合在一起用来查询
     * 
     */
    public String getGraph()
    {
    	String result="";
    	for(int i=0;i<PageProperty.Museum_Graph_Array.length;i++)
    		result=result+" from  <"+PageProperty.Museum_Graph_Array[i]+"> ";
    	return result;
    }
    
    
    /**
     * 该方法用来获取一个作者相关所有的地点名称，出生地，死亡地，活跃地点
     * @param personURI
     * @return
     */
    public HashSet<String> getAllPlaceURI(String personURI)
    {
    	HashSet<String> resultSet=new HashSet<String>();
    	
		String [] query=new String[3];
		//birthplace
		query[0]="select ?placeuri  "+getGraph()+" where { "
				+"?placeuri  <http://www.cidoc-crm.org/cidoc-crm/P2_has_type> <http://collection.americanart.si.edu/id/thesauri/placetype/City>."
    	        +"?birth <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
				+"?person <http://www.cidoc-crm.org/cidoc-crm/P98i_was_born> ?birth "
    	        +"Filter regex(?person,\" *"+personURI+"\")."
    	        +"}";
		//deathplace
		query[1]="select ?placeuri  "+getGraph()+" where { "
				+"?placeuri  <http://www.cidoc-crm.org/cidoc-crm/P2_has_type> <http://collection.americanart.si.edu/id/thesauri/placetype/City>."
    	        +"?death <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
				+"?person <http://www.cidoc-crm.org/cidoc-crm/P100i_died_in> ?death "
    	        +"Filter regex(?person,\" *"+personURI+"\")."
    	        +"}";
		//activeplace
		query[2]="select ?placeuri  "+getGraph()+" where { "
				+"?placeuri  <http://www.cidoc-crm.org/cidoc-crm/P2_has_type> <http://collection.americanart.si.edu/id/thesauri/placetype/City>."
    	        +"?event <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
				+"?person <http://www.cidoc-crm.org/cidoc-crm/P69_is_associated_with> ?event "
    	        +"Filter regex(?person,\" *"+personURI+"\")."
    	        +"}";
    	   
		resultSet=unionSet(unionSet(getHashSetValue(query[0],"placeuri"),getHashSetValue(query[1],"placeuri")),getHashSetValue(query[2],"placeuri"));
		
	    return resultSet;
    	
    }
    
    
    /**
     * 该方法用来获取所有的general subject的名字 
     * @return 包含所有general subject名字的集合
     */
    public HashSet<String> getAllGeneralSubject()
    {
    	HashSet<String> resultSet=new HashSet<String>();
    	set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		
		String query = "select ?label "+getGraph()+"  where { "
				+ "?term  <http://www.w3.org/2008/05/skos#inScheme> <http://collection.americanart.si.edu/id/thesauri/term/Subject_General>."
				+"?term <http://www.w3.org/2008/05/skos#prefLabel> ?label."
				+"}";
		
		resultSet=getHashSetValue(query,"label");
	    return resultSet;
    }
	
    
    /**
     * 该方法根据collecitonURI获取ImageURI
     * @return
     */
    public String getImageURIByCollectionURI(String collectionURI)
    {
    	String imageURI="";
    	String query= "select ?imageURI "+getGraph()+" where { " + "<" + collectionURI
        		  + ">" + " <http://www.cidoc-crm.org/cidoc-crm/P138i_has_representation> ?imageURI."
        		  + " }";
    	//System.out.println(query);
        imageURI=getValue(query,"imageURI");
    	return imageURI;
    }
    
    
    /**
     * 该方法根据collecitonURI获取General subject
     * @return
     */
    public String getGeneralSubjectByCollectionURI(String collectionURI)
    {
    	
    	String query = "select ?subject "+getGraph()+" where {  "
    			+"?collection <http://www.cidoc-crm.org/cidoc-crm/P128_carries> ?concept "
    			+"Filter regex(?collection,\" *"+collectionURI+"\")."
    			+"?concept <http://www.cidoc-crm.org/cidoc-crm/P129_is_about> ?term."
    			+"?term  <http://www.w3.org/2008/05/skos#inScheme> <http://collection.americanart.si.edu/id/thesauri/term/Subject_General>."
    			+"?term <http://www.w3.org/2008/05/skos#prefLabel> ?subject."
        		+ " }";
    	String subject=getValue(query,"subject");
    	return subject;
    }
    
    
    /**
     * 该方法根据personURI获取ImageURI
     * @return
     */
    public String getImageURIByPersonURI(String personURI)
    {
    	
    	String query = "select ?imageURI "+getGraph()+" where { " + "<" + personURI
        		+ ">" + " <http://collection.americanart.si.edu/id/ontologies/PE_has_main_representation> ?imageURI."
        		+ " }";
        String imageURI=getValue(query,"imageURI");
    	return imageURI;
    }

	/**
	 * 该函数用来得到所有的artist的uri,名字和firstname。
	 * 使用HashMap用来去掉uri重复的artist.
	 * @return HashMap<String,String> 其中一条数据的格式为，uri为键，值为name##firstname
	 * 此处firsname主要用于在页面显示的时候排序，因为name中有的包括Mr等内容
	 */
    
    //*****未测试＊＊＊＊
	public  HashMap<String,String> getAllArtistName()
	{
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		HashMap<String,String> result=new HashMap<String,String>();
		
		String query = "select ?person ?name  ?firstname "+getGraph()+"  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+"?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
				+"?appellation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E82_Actor_Appellation>."
				+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name. "
				+ "?appellation <http://collection.americanart.si.edu/id/ontologies/PE_firstname> ?firstname."
				+"}";
		
		//System.out.println(query);
		
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{
			//System.out.println("has result");
			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get("person");
			RDFNode s = r.get("name");
			RDFNode f=r.get("firstname");
			//System.out.println(" ******************* " + "  " + p + "  " + s);
			result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
		}
		
        return result;
	}
	

	/**
	 * 该函数用来得到firstname以特定字母开头的所有的artist的uri,名字和firstname。
	 * 使用HashMap用来去掉uri重复的artist.
	 * @return HashMap<String,String> 其中一条数据的格式为，uri为键，值为name##firstname
	 * 此处firsname主要用于在页面显示的时候排序，因为name中有的包括Mr等内容
	 */
	//还未测试
	public  HashMap<String,String> getAllArtistName(String startString)
	{
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		HashMap<String,String> result=new HashMap<String,String>();
		
		String query = "select ?person ?name  ?firstname ?appellation "+getGraph()+"  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
				+" ?appellation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E82_Actor_Appellation>."
				+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name. "
				+ "?appellation <http://collection.americanart.si.edu/id/ontologies/PE_firstname> ?firstname "
				+" filter regex(?firstname,\"^"+startString+"\")."
				+"}";
		
		//System.out.println(query);
		
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{
			//System.out.println("has result");
			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get("person");
			RDFNode s = r.get("name");
			RDFNode f=r.get("firstname");
			RDFNode a=r.get("appellation");
			//System.out.println(" ******************* " + "  " + p + "  " + s);
			
			//将smithsonian博物馆中displayname放入，其他的名字不放入，因为一个作家有多个名字
			if ( p.toString().trim().indexOf(PageProperty.Smith_Graph)>=0&&a.toString().trim().indexOf("displayname")>=0)
			{
			    result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
			}
			if(p.toString().trim().indexOf(PageProperty.Smith_Graph)<0)
			{
				//判断该作者是否已经在smithsonian中存在，如果存在则不添加
				if (!ifLinkToSmith(p.toString().trim()))
			        result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
			}
		}
		return result;
	}
	
	/**
	 * 该函数用来得到firstname以特定字母开头的包含在其他博物馆中的smith博物馆的artist的uri,名字和firstname。
	 * 使用HashMap用来去掉uri重复的artist.
	 * @return HashMap<String,String> 其中一条数据的格式为，uri为键，值为name##firstname
	 * 此处firsname主要用于在页面显示的时候排序，因为name中有的包括Mr等内容
	 */
	public  HashMap<String,String> getAllLinkedArtistName(String startString)
	{
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		HashMap<String,String> result=new HashMap<String,String>();
		HashSet<String> linkedPersonSet=getAllLinkedPersonURI();
		
		
		String query = "select ?person ?name  ?firstname ?appellation "+getGraph()+"  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
				+" ?appellation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E82_Actor_Appellation>."
				+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name. "
				+ "?appellation <http://collection.americanart.si.edu/id/ontologies/PE_firstname> ?firstname "
				+" filter regex(?firstname,\"^"+startString+"\")."
				+"}";
		
		//System.out.println(query);
		
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{
			//System.out.println("has result");
			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get("person");
			RDFNode s = r.get("name");
			RDFNode f=r.get("firstname");
			RDFNode a=r.get("appellation");
			//System.out.println(" ******************* " + "  " + p + "  " + s);
			
			if(linkedPersonSet.contains(p.toString().trim()))
			{
			    //将smithsonian博物馆中displayname放入，其他的名字不放入，因为一个作家有多个名字
			    if ( p.toString().trim().indexOf(PageProperty.Smith_Graph)>=0&&a.toString().trim().indexOf("displayname")>=0)
			    {
			        result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
			    }
			    if(p.toString().trim().indexOf(PageProperty.Smith_Graph)<0)
			    {
				    //判断该作者是否已经在smithsonian中存在，如果存在则不添加
				    if (!ifLinkToSmith(p.toString().trim()))
			            result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
			    }
			}
		}
		return result;
	}
	
	/**
	 * 判断某个人是否已经包含在smithsonian博物馆中
	 * @param personURI
	 * @return
	 */
	public boolean ifLinkToSmith(String personURI)
	{
		boolean result=false;
		String query="select ?smithsonianPersonURI from <"+PageProperty.PersonLink_Graph+">  where { "
				+ "?personURI  <http://www.w3.org/2002/07/owl#sameAs>  ?smithsonianPersonURI"
				+ " Filter regex(?personURI,\" *"+personURI+"*\"). "
				+"}";
		String uri=getValue(query,"smithsonianPersonURI");
		if(uri!=null&&uri.length()>0)
			result=true;
		return result;
	}
	
	
	
	
	/**
	 * 该函数用来获取所有作家的uri
	 * 由于在triple store中有些作家仅有uri，没有其他有效数据，所以这类uri不被返回
	 * 此处返回有名称的作家的uri
	 * 对于包含在多个博物馆中的作家，此处仅返回smith中的uri
	 * @return TreeSet <String> 包含所有作家uri的集合，采用TreeSet可以去除重复元素
	 */
	public  TreeSet<String> getAllArtistURI()
	{
		
		String query = "select ?person ?appellation "+getGraph()+" where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."	
				+"}";
		
		TreeSet <String> result=getTreeSetValue(query,"person");
		
		for(String temp:result)
		{
		  //判断该作者是否已经在smithsonian中存在，如果存在则不添加
		  if (ifLinkToSmith(temp))
	         result.remove(temp);
		}
		
        return result;

	}
	
	/**
	 * 该函数用来获取所有作品的uri
	 * @return TreeSet <String> 包含所有作品uri的集合，采用TreeSet可以去除重复元素
	 */
	public TreeSet<String> getAllCollectionURI()
	{
	    String query = "select ?collection ?title "+getGraph()+"  where { "
				+ "?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>."
				+" ?collection <http://www.cidoc-crm.org/cidoc-crm/P102_has_title> ?title."	
				+"}";
		
		TreeSet <String> result=getTreeSetValue(query,"collection");
        return result;
	}
	
	/**
	 * 该函数用来获取所有作家的照片的uri
	 * @return TreeSet <String> 包含所有作家照片uri的集合，采用TreeSet可以去除重复元素
	 */
	public  TreeSet<String> getAllArtistImageURI()
	{
		String query = "select ?person ?image "+getGraph()+"  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+"?person <http://collection.americanart.si.edu/id/ontologies/PE_has_main_representation> ?image."
				+"}";
		
		TreeSet <String> result=getTreeSetValue(query,"image");
        return result;
     }

	
	/**
	 * 该函数用来获取所有作品的照片的uri
	 * @return TreeSet <String> 包含所有作品照片uri的集合，采用TreeSet可以去除重复元素
	 */
	public TreeSet<String> getAllCollectionImageURI()
	{
		String query = "select ?collection ?image "+getGraph()+"  where { "
				+ "?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>."
				+"?collection <http://www.cidoc-crm.org/cidoc-crm/P138i_has_representation> ?image."
				+"}";
		
		TreeSet <String> result=getTreeSetValue(query,"image");
		return result;
	}
    
	/**
	 * 该方法根据给定的Person的URI到triple store中检索出相应的值，并返回
	 * @param URI person的uri 
	 * @return Person 各项属性都已设置
	 */
	public  Person getPersonByURI(String personURI) {
		Person person = new Person();
		person.setURI(personURI);
		int first=personURI.indexOf("institution");
	    //System.out.println("*********uri:"+URI);
		person.setId(personURI.substring(first+12));//
		
		//System.out.println("*********id:"+person.getId());
		
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		String query = "select ?p ?s  "+getGraph()+"  where { "
			+ "<" + personURI + ">" + " ?p ?s . }";
		// System.out.println(query);

		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet results = vqe.execSelect();
		//System.out.println(results.getRowNumber());

		while (results.hasNext()) {
			//System.out.println("has result");
			QuerySolution result = results.nextSolution();
			RDFNode p = result.get("p");
			RDFNode s = result.get("s");
			//System.out.println(" ******************* " + "  " + p + "  " + s);

			if (p.toString().trim().indexOf("owl#sameAs") >= 0) {
				if (s.toString().trim().indexOf("dbpedia.org") >= 0)
					person.setDbpediaURI(s.toString().trim());
				else if (s.toString().trim().indexOf("data.nytimes.com") >= 0)
					person.setNytimesURI(s.toString().trim());
				else if (s.toString().trim().indexOf("rdf.freebase.com") >= 0)
					person.setFbaseURI(s.toString().trim());
				}

			if (p.toString().trim().indexOf("seeAlsoWikipedia") >= 0)
				person.setWikipediaURI(s.toString().trim());
			//System.out.println("****************wikepedia:"+person.getWikipediaURI());

			if (p.toString().trim().indexOf("PE_has_main_representation") >= 0)
				person.setMainRepresentationURI(s.toString().trim());

			if(s.toString().trim().indexOf(PageProperty.Smith_Graph)>=0&&p.toString().trim().indexOf("P1_is_identified_by") >= 0 &&s.toString().trim().indexOf("displayname")>=0)
			{
				person.setDisplayNameURI(s.toString().trim());
				//System.out.println("smith"+s.toString().trim());
			}
			else if (s.toString().trim().indexOf(PageProperty.Smith_Graph)<0&&p.toString().trim().indexOf("P1_is_identified_by") >= 0)
			{
				person.setDisplayNameURI(s.toString().trim());
				//System.out.println("not smith"+s.toString().trim());
			}

			if (p.toString().trim().indexOf("P98i_was_born") >= 0)
				person.setBirthURI(s.toString().trim());

			if (p.toString().trim().indexOf("P100i_died_in") >= 0)
				person.setDeathURI(s.toString().trim());

			if (p.toString().trim().indexOf("P69_is_associated_with") >= 0)
			{
				person.addAssociateEventURI(s.toString().trim());
				//System.out.println(s.toString().trim());
			}

			if (p.toString().trim().indexOf("PE_has_note_artistbio") >= 0)
				person.setArtistBio(s.toString().trim());

			if (p.toString().trim().indexOf("PE_has_note_primaryartistbio") >= 0)
				person.setPrimaryArtistBio(s.toString().trim());

			if (p.toString().trim().indexOf(
			"P107i_is_current_or_former_member_of") >= 0)
				person.setNationalityURI(s.toString().trim());
		}

		query = "select ?name "+getGraph()+" where { " + "<" + person.getDisplayNameURI()
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?name."
		+ " }";
		person.setDisplayName(getValue(query,"name"));
		

        query = "select ?place "+getGraph()+" where { " + "<" + person.getDeathURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";
        person.setDeathPlace(getValue(query,"place"));
       
        query = "select ?placeURI "+getGraph()+" where { " + "<" + person.getDeathURI()
        		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeURI."
        		+ " }";
        person.setDeathPlaceURI(getValue(query,"placeURI"));
        

        query = "select ?date "+getGraph()+" where { " + "<" + person.getDeathURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
		+ "?timespan <http://www.cidoc-crm.org/cidoc-crm/P82_at_some_time_within> ?date. "
		+ " }";
        person.setDeathDate(getValue(query,"date"));
        
  
        query = "select ?place "+getGraph()+" where { " + "<" + person.getBirthURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";
        person.setBirthPlace(getValue(query,"place"));
        //person.setDeathPlace(getDeathPlace(person.getDeathURI()));
        
        query = "select ?placeURI "+getGraph()+" where { " + "<" + person.getBirthURI()
        		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeURI."
        		+ " }";
        person.setBirthPlaceURI(getValue(query,"placeURI"));
        
   
        query = "select ?date "+getGraph()+" where { " + "<" + person.getBirthURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
		+ "?timespan <http://www.cidoc-crm.org/cidoc-crm/P82_at_some_time_within> ?date. "
		+ " }";
        person.setBirthDate(getValue(query,"date"));
       
        //System.out.println("AssociateEventURISet:"+person.getAssociateEventURISet());
		if (person.getAssociateEventURISet() != null) {
			for (String temp : person.getAssociateEventURISet()) {
				query = "select ?place  "
						+ getGraph()
						+ " where { "
						+ "<"
						+ temp
						+ ">"
						+ " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
						+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
						+ " }";
				HashSet<String> placeSet = getHashSetValue(query, "place");
				if (placeSet != null)
					for (String k : placeSet)
						person.addAssociatePlace(k);

				query = "select ?placeuri "
						+ getGraph()
						+ " where { "
						+ "<"
						+ temp
						+ ">"
						+ " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
						+ " }";
				HashSet<String> placeURISet = getHashSetValue(query, "placeuri");
				if (placeSet != null) {
					for (String p : placeURISet)
						person.addAssociatePlaceURI(p);
				}

				/*
				 * System.out.println(query); sparql =
				 * QueryFactory.create(query); vqe =
				 * VirtuosoQueryExecutionFactory.create(sparql, set); results =
				 * vqe.execSelect(); while (results.hasNext()) { QuerySolution
				 * result = results.nextSolution(); RDFNode s2 =
				 * result.get("placeuri");
				 * if(s2!=null&&s2.toString().trim().length()>0) {
				 * person.addAssociatePlaceURI(s2.toString().trim()); } }
				 */
			}
		} 
       
        query = "select ?place "+getGraph()+" where { " + "<" + person.getNationalityURI()
		+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?place."
		+ " }";
        person.setNationality(getValue(query,"place"));
        
        //速度太慢
        person.setCollectionList(getCollectionList(person.getURI()));
        return person;
	}
	
	
    /**
     * 从博物馆中根据用户URI检索数据藏品信息
     * @param query
     * @return
     */
	
    public  TreeSet<Work> getCollectionListFromMuseum(String query)
	{
		TreeSet<Work> collectionList=new TreeSet<Work>();
		//System.out.println("*******************ssss " + query);
		Query sparql = QueryFactory.create(query);
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode s = result.get("collection");
			if(s!=null&&s.toString().trim().length()>0)
			{ 
				//System.out.println("result:"+s.toString().trim());
				Work temp=new Work(s.toString().trim());
				String title=getCollectionTitleByUri(temp.getURI());
				temp.setTitle(title);
			    //System.out.println(title);
				temp.setScalarURI(PageProperty.BASE_URL+"/"+new PageContentGenerator().convertNameToUrl(title));
				String produceDate=getCollectionProduceDateByUri(temp.getURI());
				//System.out.println("date:"+produceDate);
				temp.setProduceDate(produceDate);
				String keeperUri=getCollectionKeeperUriByUri(temp.getURI());
				temp.setKeeperURI(keeperUri);
				if(keeperUri!=null&&keeperUri.length()>0)
				{
					temp.setKeeper(getMuseumNameByURI(keeperUri));
				}
				//System.out.println("keeperuri:"+keeperUri);
			    collectionList.add(temp);
			}
			
		}
		
		return collectionList;
	}
    
    /**
     * 根据博物馆的uri找到博物馆的名称
     */
    public String getMuseumNameByURI(String museumURI)
    {
    	String query="select ?museumname "+getGraph()+" where {"
    			+" ?museum <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E40_Legal_Body> "
    			+" FILTER regex( ?museum, \" *"+museumURI+"\")."
    			+" ?museum <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
    			+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?museumname. "
    			+"}";
    	return getValue(query,"museumname");
    }
    
    /**
     * 此处的personURI为smithsonian博物馆中的相关人员的uri，需要根据same as查找到其他博物馆中的数据
     * @param personURI
     * @return
     */
    public TreeSet<Work> getCollectionList(String personURISmithsonian)
    {
    	TreeSet<Work> collectionList=new TreeSet<Work>();
    	
    	ArrayList<String> queryList=new ArrayList<String>();
    	queryList.add("select ?collection "+getGraph()+" where { " 
		          + " ?production  <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> <" + personURISmithsonian+"> ."
		          + " ?production <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection."
	              + " }"
		          );
    	
    	//查找等同的作家
    	HashSet<String> personURISet=findSamePersonURI(personURISmithsonian);
    	if(personURISet!=null)
    	{
    		for(String temp:personURISet)
    		{
    			queryList.add("select ?collection "+getGraph()+" where { " 
    			          + " ?production  <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> <" + temp+"> ."
    			          + " ?production <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection."
    		              + " }");
    		}
    	}
    	
    	for(String temp:queryList)
    	{
    		//System.out.println(temp);
    		TreeSet<Work> collectionSet=getCollectionListFromMuseum(temp);
    		if(collectionSet!=null)
    			collectionList=unionSet(collectionList,collectionSet);
    	}
    	return collectionList;
    }
    
    
    /**
     * 该方法用来获取和smitshsonian博物馆中某个作者等同的carter博物馆中的uri
     * @param personURI
     * @return
     */
    public String findSamePersonURIFromCrystal(String personURISmithsonian)
    {
    	String query = "select ?personURI from <"+PageProperty.PersonLink_Graph+">  where { "
				+ "?personURI  <http://www.w3.org/2002/07/owl#sameAs> <"+personURISmithsonian+">  "
				+" Filter regex(?personURI, \" *crystalbridges*\")."
			    +"}";
		
		String result=getValue(query,"personURI");
	    return result;
    }
    
    /**
     * 该方法用来获取和smitshsonian博物馆中某个作者等同的其他博物馆中的作家uri
     * @param personURI
     * @return
     */
    public HashSet<String> findSamePersonURI(String personURISmithsonian)
    {
    	String query = "select ?personURI from <"+PageProperty.PersonLink_Graph+">  where { "
				+ "?personURI  <http://www.w3.org/2002/07/owl#sameAs> <"+personURISmithsonian+">  "
				+"}";
		
		return getHashSetValue(query,"personURI");
	 }
   
    /**
     * 该方法用来获取和smitshsonian博物馆中某个作者等同的carter博物馆中的uri
     * @param personURI
     * @return
     */
    public String findSamePersonURIFromCarter(String personURISmithsonian)
    {
		String query = "select ?personURI from <http://personLink.isi.edu>  where { "
				+ "?personURI  <http://www.w3.org/2002/07/owl#sameAs> <"+personURISmithsonian+">  "
				+" Filter regex(?personURI, \" *cartermuseum*\")."
			    +"}";
		
		return getValue(query,"personURI");
	  }
    
    /**
     * 用于合并两个集合
     */
    public TreeSet<Work> unionSet(TreeSet<Work> oneSet,TreeSet<Work> anotherSet)
    {
    	TreeSet<Work> result=new TreeSet<Work>();
    	for(Work temp:oneSet)
    	{
    		result.add(temp);
    	}
    	for(Work temp:anotherSet)
    	{
    		result.add(temp);
    	}
    	return result;
    }
   
    /**用于合并两个集合
     * 
     * @param oneSet
     * @param anotherSet
     * @return
     */
    public HashSet<String> unionSet(HashSet<String> oneSet,HashSet<String> anotherSet)
    {
    	HashSet<String> result=new HashSet<String>();
    	for(String temp:oneSet)
    	{
    		result.add(temp);
    	}
    	for(String temp:anotherSet)
    	{
    		result.add(temp);
    	}
    	return result;
    }
	
	/**
	 * 通过 productionURI获取collection
	 * @param productionURI
	 * @return
	 */

	public  Work getCollectionByProductionURI(String productionURI)
	{
		Work temp=null;
		String query="select ?collection "+getGraph()+" where { " 
			+ " <"+productionURI+"> "+ " <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection ." 
            + " }";
		
        
		String collectionURI=getValue(query,"collection");
		if(collectionURI!=null&&collectionURI.length()>0)
			temp=getCollectionByURI(collectionURI);
		return temp;
	}
	
	/**
	 * 通过collectionURI获取colleciton
	 * @param collectionURI
	 * @return
	 */
	public  Work getCollectionByURI(String collectionURI) 
	{
	   Work temp=new Work();
	   temp.setURI(collectionURI);
	   int first=collectionURI.indexOf("object");
	   temp.setObjnum(collectionURI.substring(first+7));
	   String value;
	   
	   //System.out.println("*********num:"+temp.getObjnum());

	   set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
	   String query = "select ?p ?s  "+getGraph()+"  where { "
			+ "<" + collectionURI + ">" + " ?p ?s . }";
	   //System.out.println(query);

	  	Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet results = vqe.execSelect();
		
        while (results.hasNext()) {
		    QuerySolution result = results.nextSolution();
			RDFNode p = result.get("p");
			RDFNode s = result.get("s");
			//System.out.println(" ******************* " + "  " + p + "  " + s);
			
			if (p.toString().trim().indexOf("P138i_has_representation") >= 0)
				temp.setRepresentationURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P1_is_identified_by") >= 0)
				temp.setIdURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P102_has_title") >= 0)
				temp.setTitleURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P30i_custody_transferred_through") >= 0)
				temp.setAcquisitionURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P48_has_preferred_identifier") >= 0)
				temp.setObjnumURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P50_has_current_keeper") >= 0)
				temp.setKeeperURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("PE_medium_description") >= 0)
				temp.setMedium(s.toString().trim());
			
			if (p.toString().trim().indexOf("PE_object_mainclass") >= 0)
				temp.setMainClassURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("PE_object_subsclass") >= 0)
				temp.setSubClassURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P55_has_current_locationn") >= 0)
				temp.setLocationURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P128_carries") >= 0)
			{
				temp.addKeywordURI(s.toString().trim());
			}
			
			if (p.toString().trim().indexOf("P43_has_dimension") >= 0)
				temp.setDimensionURI(s.toString().trim());
			
			if(p.toString().trim().indexOf("PE_medium_description")>=0)
				temp.setMedium(s.toString().trim());
		
		}
		


		//private String id;
		 query = "select ?id "+getGraph()+" where { " + "<" + temp.getIdURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?id."
			+ " }";
		 value=getValue(query,"id");
		 if(value!=null&&value.length()>0)
		    temp.setId(value);

		//private String title;
		 query = "select ?title "+getGraph()+" where { " + "<" + temp.getTitleURI()
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?title."
		+ " }";
		 value=getValue(query,"title");
		 if(value!=null&&value.length()>0)
		    temp.setTitle(value);
	
		//private String objnum;
		 query = "select ?num "+getGraph()+" where { " + "<" + temp.getTitleURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?num."
			+ " }";
		 value=getValue(query,"num");
		 if(value!=null&&value.length()>0)
		    temp.setObjnum(value);	
			 
		
	    //private String keeper;
		 int beginIndex=temp.getKeeperURI().indexOf("id/");
		 if(beginIndex>=0)
	        temp.setKeeper(temp.getKeeperURI().substring(beginIndex+3));
	
		//private String mainClass;
		 query = "select ?main "+getGraph()+" where { " + "<" + temp.getMainClassURI()
			+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?main."
			+ " }";
		 
		 value=getValue(query,"main");
		 if(value!=null&&value.length()>0)
		 {
			 temp.setMainClass(value);
		 }

		//private String subClass;
		 /*
		  query = "select ?sub "+getGraph()+" where { " + "<" + temp.getMainClassURI()
			+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?sub."
			+ " }";
		 temp.setSubClass(getValue(query,"sub"));
		 */
	
		//private String location;
		 query = "select ?loc "+getGraph()+" where { " + "<" + temp.getLocationURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?loc."
			+ " }";
		 value=getValue(query,"loc");
		 if(value!=null&&value.length()>0)
		    temp.setLocation(value);	
		 
		
	
	    //private ArrayList<String> keywordList;
		 if(temp.getKeywordURIList()!=null)
		 {
		     for(String t:temp.getKeywordURIList())
		    {
			     query = "select ?keyword "+getGraph()+" where { " + "<" + t
				    + ">" + " <http://www.cidoc-crm.org/cidoc-crm/P129_is_about> ?uri."
				    + "?uri <http://www.w3.org/2008/05/skos#prefLabel> ?keyword."
				    + " }";
			     value=getValue(query,"keyword");
			     if(value!=null&&value.length()>0)
			        temp.addKeyword(value);
		    }
		 }
		 

	    //private String width;
		 query="select ?width "+getGraph()+"  where { "
				 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
				 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
				 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P43_has_dimension> ?dimension"
				 +" Filter regex(?dimension, \" *dimension/width/cm\")."
				 +" ?dimension <http://www.cidoc-crm.org/cidoc-crm/P90_has_value> ?width."
				 + " }";
		 String w=getValue(query,"width");
		 //System.out.println(w);
		 if(w.indexOf("http")>=0)
			 w=w.substring(0,w.indexOf("http")-2);
		 temp.setWidth(w);
				
	    //private String height;
		 query="select ?height "+getGraph()+"  where { "
				 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
				 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
				 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P43_has_dimension> ?dimension"
				 +" Filter regex(?dimension, \" *dimension/height/cm\")."
				 +" ?dimension <http://www.cidoc-crm.org/cidoc-crm/P90_has_value> ?height."
				 + " }";
		 String h=getValue(query,"height");
		 //System.out.println(h);
		 if(h.indexOf("http")>=0)
			 h=h.substring(0,h.indexOf("http")-2);
		 temp.setHeight(h);
		
	    //private ArrayList<Person> personList;
		 query="select ?person "+getGraph()+" where { "
				 +" ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				 +" ?production <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> ?person."
				 +" ?production <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection."
				 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
				 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
				 + " }";
		 HashSet<String> personURISet=getHashSetValue(query,"person");
		 if(personURISet!=null)
		 {
		    for(String uri:personURISet)
		    {
		    	Person person=new Person(uri);//uri
				String displayName=getPersonNameByUri(person.getURI());
				if(displayName!=null&&displayName.length()>0)
				{
					person.setDisplayName(displayName);//displayname
				    person.setScalarURI(PageProperty.BASE_URL+"/"+new PageContentGenerator().convertNameToUrl(displayName));
				}
				temp.addPerson(person);//此时person中只有URI+displayname
		    }
		 
		 }
		
		 query = "select ?produceDate "+getGraph()+" where { " 
				    + " ?production <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection."
			    	+ " ?production <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
				    + " ?timespan <http://www.w3.org/2000/01/rdf-schema#label>  ?produceDate."
			    	+ " ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
				    + " FILTER regex( ?collection, \" *"+temp.getURI()+"\"). "
				    + " }";

		temp.setProduceDate(cleanProduceDate(getValue(query,"produceDate")));
		
		return temp;
	}
	
    public String getPersonNameByUri(String uri)
    {
    	String query="";
    	if(uri.indexOf(PageProperty.Smith_Graph)>=0)
    	   query="select ?name from <"+PageProperty.Smith_Graph+"> where { "
    			+" ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person> "
    			+" FILTER regex( ?person, \" *"+uri+"\")."
    			+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
    			+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name "
    			+" FILTER regex( ?appellation, \" *displayname*\")."
    			+"}";
    	else
    		query="select ?name "+getGraph()+" where { "
        			+" ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person> "
        			+" FILTER regex( ?person, \" *"+uri+"\")."
        			+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
        			+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name. "
        			+"}";
    	//System.out.println(query);
    	String name=getValue(query,"name");
    	return name;
    }
    
   /* public String getMuseumNameByUri(String museumURI)
    {
    	String name="";
    	
    	String query="";
    	
    	select ?name from
    			<http://personLink.isi.edu>
    			where 
    			{
    			?museumURI <http://www.w3.org/2000/01/rdf-schema#label> ?name
    			Filter regex(?museumURI," *000*").
    			}
    	  query="select ?name from <http://personLink.isi.edu> where { "
    			+" ?museumURI <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person> "
    			+" FILTER regex( ?person, \" *"+uri+"\")."
    			+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
    			+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name "
    			+" FILTER regex( ?appellation, \" *displayname*\")."
    			+"}";
    	
    	
    	
    	//System.out.println("query"+query);
    	if(query!=null&&query.length()>0)
    	{
    	   set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
    	   Query sparql = QueryFactory.create(query);
    	   System.out.println("query"+query);
		   VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		   ResultSet results = vqe.execSelect();
		   while (results.hasNext()) {
			   QuerySolution result = results.nextSolution();
			   RDFNode o = result.get("name");
			   name=o.toString().trim();
		   }
    	}
		return name;
    }
    */
   
    public String  getCollectionTitleByUri(String uri)
    {
    	String query="select ?title "+getGraph()+"  where { " 
    			+" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object> "
    			+" FILTER regex( ?collection, \" *"+uri+"\")."
    			+"  ?collection <http://www.cidoc-crm.org/cidoc-crm/P102_has_title> ?appellation."
    			+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?title."
    			+"}";
    			
    	return getValue(query,"title");
    }
    
    
    public String  getCollectionProduceDateByUri(String uri)
    {
    	String query= "select ?produceDate "+getGraph()+"  where { " 
    		    +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object> "
    			+" FILTER regex( ?collection, \" *"+uri+"\")."
    			+"  ?production <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?collection."
    			+ " ?production <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
			    + " ?timespan <http://www.w3.org/2000/01/rdf-schema#label>  ?produceDate."
    			+"}";
    	return getValue(query,"produceDate");
    }
    
    
    public String  getCollectionKeeperUriByUri(String collectionUri)
    {
    	
    	String query= "select ?keeper "+getGraph()+"  where { " 
    			+" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object> "
    			+" FILTER regex( ?collection, \" *"+collectionUri+"\")."
    			+"  ?collection <http://www.cidoc-crm.org/cidoc-crm/P50_has_current_keeper> ?keeper."
    			+"}";
    	
		return getValue(query,"keeper");
    }
	
	public  String getName(String displayNameURI)
	{
		String query = "select ?name "+getGraph()+" where { " + "<" + displayNameURI
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?name."
		+ " }";

		return getValue(query,"name");
	}
	

	public  String getDeathPlace(String deathURI)
	{
		String query = "select ?place "+getGraph()+" where { " + "<" + deathURI
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";

		return getValue(query,"place");
	}

    

	public  ArrayList<Person> searchByArtistName(String keyword) {
		ArrayList<Person> personList = new ArrayList<Person>();

		
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);

		String query = "select ?o ?p ?s "+getGraph()+" where { ?o ?p ?s. ?o  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
			+ " ?o <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation. "
			+ " ?appellation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.cidoc-crm.org/cidoc-crm/E82_Actor_Appellation>. "
			+ " ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name "
			+ "  FILTER regex( ?name, \" *" + keyword + "*\") . " + " }";

		//System.out.println(query);
		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		//System.out.println("begin:------------");
		ResultSet results = vqe.execSelect();
		HashSet<String> hashSet = new HashSet<String>();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			// RDFNode graph = result.get("graph");
			RDFNode o = result.get("o");
			RDFNode p = result.get("p");
			RDFNode s = result.get("s");
			//System.out.println(" ******************* " + o + "  " + p + "  "	+ s);

			if (!hashSet.contains(o.toString().trim())) {
				hashSet.add(o.toString().trim());
				Person person = new Person();
				person.setURI(o.toString().trim());
				personList.add(person);
			}

			for (Person t : personList) {
				if (t.getURI().equals(o.toString().trim())) {
					if (p.toString().trim().equals("http://collection.americanart.si.edu/id/ontologies/PE_has_main_representation"))
						t.setMainRepresentationURI(s.toString().trim());
					if (p.toString().trim().equals("http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by"))
						t.setDisplayNameURI(s.toString().trim());

					if (p.toString().trim().equals("http://www.cidoc-crm.org/cidoc-crm/P98i_was_born"))
						t.setBirthURI(s.toString().trim());

					if (p.toString().trim().equals("http://www.cidoc-crm.org/cidoc-crm/P100i_died_in"))
						t.setDeathURI(s.toString().trim());

					if (p.toString().trim().equals("http://www.cidoc-crm.org/cidoc-crm/P107i_is_current_or_former_member_of"))
						t.setNationalityURI(s.toString().trim());
            	}
			}

		}
		//System.out.println("end:------------");

		for (Person temp : personList) {
			query = "select ?name "+getGraph()+" where { "
				+ "<"
				+ temp.getDisplayNameURI()
				+ ">"
				+ " <http://www.w3.org/2000/01/rdf-schema#label> ?name."
				+ " }";

			//System.out.println("22222222222222" + query);
			sparql = QueryFactory.create(query);
			vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
			results = vqe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
				// RDFNode graph = result.get("graph");
				RDFNode o = result.get("name");
				//System.out.println(" ******************* " + o);
				temp.setDisplayName(o.toString().trim());
			}
		}
		//for (Person temp : personList) {
			//System.out.println("***********person:");
			//System.out.println(temp.getBirthURI());
			//System.out.println(temp.getDeathURI());
			//System.out.println(temp.getMainRepresentationURI());
			//System.out.println(temp.getDisplayName());
			//System.out.println(temp.getDisplayNameURI());
			//System.out.println(temp.getNationalityURI());
		//}

		return personList;
	}

	/*public  String search(String keyword, String searchType) {
		sparqlSearch("ok");
		return "ok";
	}*/


	/*public  String sparqlSearch(String query) {
		set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		Query sparql = QueryFactory
		.create("SELECT * "+getGraph()+" WHERE { ?s ?p ?o } limit 100");

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);

		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode graph = result.get("graph");
			RDFNode s = result.get("s");
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			System.out.println(graph + " { " + s + " " + p + " " + o + " . }");
		}
		return "ok";
	}*/
	
	private String cleanProduceDate(String date)
	{
		String result;
		String testreg = "[^0-9]";
        Pattern pattern = Pattern.compile(testreg);
        Matcher mp = pattern.matcher(date);
        result=mp.replaceAll("").trim(); 
        if(result.length()>=4)
        	result.substring(0,4);
        
		return result;
	}
	
	/*public  Work getCollectionByURIForCrystal(String collectionURI) 
	{
	   Work temp=new Work();
	   temp.setURI(collectionURI);
	   int first=collectionURI.indexOf("object");
	   temp.setObjnum(collectionURI.substring(first+7));
	   
	   //System.out.println("*********num:"+temp.getObjnum());

	   set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
	   String query = "select ?p ?s  from <"+PageProperty.Crystal_Graph+">  where { "
			+ "<" + collectionURI + ">" + " ?p ?s . }";
	   //System.out.println(query);

	  	Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet results = vqe.execSelect();
		
        while (results.hasNext()) {
		    QuerySolution result = results.nextSolution();
			RDFNode p = result.get("p");
			RDFNode s = result.get("s");
				
			if (p.toString().trim().indexOf("P102_has_title") >= 0)
				temp.setTitleURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P48_has_preferred_identifier") >= 0)
				temp.setObjnumURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P50_has_current_keeper") >= 0)
				temp.setKeeperURI(s.toString().trim());
			
			if (p.toString().trim().indexOf("P43_has_dimension") >= 0)
				temp.setDimensionURI(s.toString().trim());
		}
		

		 query = "select ?title from <"+PageProperty.Crystal_Graph+"> where { " + "<" + temp.getTitleURI()
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?title."
		+ " }";
		 temp.setTitle(getValue(query,"title"));
	     
		 int beginIndex=temp.getKeeperURI().indexOf("id/");
	     temp.setKeeper(temp.getKeeperURI().substring(beginIndex+3));
	
	    //private ArrayList<Person> personList;
		 query="select ?person from <"+PageProperty.Crystal_Graph+"> where { "
				 +" ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				 +" ?production <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> ?person."
				 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P108i_was_produced_by> ?production."
				 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
				 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
				 + " }";
		 sparql = QueryFactory.create(query);
		 System.out.println(query);
		 vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		 results = vqe.execSelect();
		 while (results.hasNext()) {
			    QuerySolution result = results.nextSolution();
				RDFNode p = result.get("person");
				Person person=new Person(p.toString().trim());//uri
				//System.out.println("uri:"+person.getURI());
				String displayName=getPersonNameByUri(person.getURI());
				person.setDisplayName(displayName);//displayname
				//System.out.println("name:"+displayName);
				person.setScalarURI(PageProperty.BASE_URL+"/"+new PageContentGenerator().convertNameToUrl(displayName));
				//System.out.println("url:"+person.getScalarURI());
				temp.addPerson(person);//此时person中只有URI+displayname
	    }
	
	   return temp;
	}
	*/
	/*
	public  Work getCollectionByURIForCarter(String collectionURI) 
	{
		   Work temp=new Work();
		   temp.setURI(collectionURI);
		   int first=collectionURI.indexOf("object");
		   temp.setObjnum(collectionURI.substring(first+7));
		   
		   //System.out.println("*********num:"+temp.getObjnum());

		   set= new VirtGraph(PageProperty.Virtuoso_URL, PageProperty.Virtuoso_User, PageProperty.Virtuoso_Password);
		   String query = "select ?p ?s  from <"+PageProperty.Carter_Graph+">  where { "
				+ "<" + collectionURI + ">" + " ?p ?s . }";
		   //System.out.println(query);

		  	Query sparql = QueryFactory.create(query);
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
					sparql, set);
			ResultSet results = vqe.execSelect();
			
	        while (results.hasNext()) {
			    QuerySolution result = results.nextSolution();
				RDFNode p = result.get("p");
				RDFNode s = result.get("s");
				//System.out.println(" ******************* " + "  " + p + "  " + s);
				
			
				if (p.toString().trim().indexOf("P102_has_title") >= 0)
					temp.setTitleURI(s.toString().trim());
				
				if (p.toString().trim().indexOf("P30i_custody_transferred_through") >= 0)
					temp.setAcquisitionURI(s.toString().trim());
				
				if (p.toString().trim().indexOf("P48_has_preferred_identifier") >= 0)
					temp.setObjnumURI(s.toString().trim());
				
				if (p.toString().trim().indexOf("P50_has_current_keeper") >= 0)
					temp.setKeeperURI(s.toString().trim());
				
				
				if (p.toString().trim().indexOf("P128_carries") >= 0)
				{
					temp.addKeywordURI(s.toString().trim());
				}
				
				if (p.toString().trim().indexOf("P43_has_dimension") >= 0)
					temp.setDimensionURI(s.toString().trim());
				
			}
			
            //private String title;
			 query = "select ?title from <"+PageProperty.Carter_Graph+"> where { " + "<" + temp.getTitleURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?title."
			+ " }";
			 temp.setTitle(getValue(query,"title"));
		
			
			 //private String keeper;
			 int beginIndex=temp.getKeeperURI().indexOf("id/");
		     temp.setKeeper(temp.getKeeperURI().substring(beginIndex+3));
		
			 
			 //private ArrayList<String> keywordList;
			 if(temp.getKeywordURIList()!=null)
			 {
			     for(String t:temp.getKeywordURIList())
			    {
				     query = "select ?keyword from <"+PageProperty.Carter_Graph+"> where { " + "<" + t
					    + ">" + " <http://www.cidoc-crm.org/cidoc-crm/P129_is_about> ?uri."
					    + "?uri <http://www.w3.org/2008/05/skos#prefLabel> ?keyword."
					    + " }";
				     temp.addKeyword(getValue(query,"keyword"));
			    }
			 }
			 

		    //private String width;
			 query="select ?width from <"+PageProperty.Carter_Graph+">  where { "
					 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
					 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
					 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P43_has_dimension> ?dimension"
					 +" Filter regex(?dimension, \" *width\")."
					 +" ?dimension <http://www.cidoc-crm.org/cidoc-crm/P90_has_value> ?width."
					 + " }";
			 String w=getValue(query,"width");
			 //System.out.println(w);
			 if(w.indexOf("http")>=0)
				 w=w.substring(0,w.indexOf("http")-2);
			 temp.setWidth(w);
					
		    //private String height;
			 query="select ?height from <"+PageProperty.Carter_Graph+">  where { "
					 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
					 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
					 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P43_has_dimension> ?dimension"
					 +" Filter regex(?dimension, \" *height\")."
					 +" ?dimension <http://www.cidoc-crm.org/cidoc-crm/P90_has_value> ?height."
					 + " }";
			 String h=getValue(query,"height");
			 //System.out.println(h);
			 if(h.indexOf("http")>=0)
				 h=h.substring(0,h.indexOf("http")-2);
			 temp.setHeight(h);
			
		    //private ArrayList<Person> personList;
			 query="select ?person from <"+PageProperty.Carter_Graph+"> where { "
					 +" ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
					 +" ?production <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> ?person."
					 +" ?collection <http://www.cidoc-crm.org/cidoc-crm/P108i_was_produced_by> ?production."
					 +" ?collection <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object>"
					 +" Filter regex(?collection, \" *"+temp.getURI()+"\")."
					 + " }";
			 sparql = QueryFactory.create(query);
			 System.out.println(query);
			 vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
			 results = vqe.execSelect();
			 while (results.hasNext()) {
				    QuerySolution result = results.nextSolution();
					RDFNode p = result.get("person");
					Person person=new Person(p.toString().trim());//uri
					//System.out.println("uri:"+person.getURI());
					String displayName=getPersonNameByUri(person.getURI());
					person.setDisplayName(displayName);//displayname
					//System.out.println("name:"+displayName);
					person.setScalarURI(PageProperty.BASE_URL+"/"+new PageContentGenerator().convertNameToUrl(displayName));
					//System.out.println("url:"+person.getScalarURI());
					temp.addPerson(person);//此时person中只有URI+displayname
		    }
			
		   return temp;
	}*/

}