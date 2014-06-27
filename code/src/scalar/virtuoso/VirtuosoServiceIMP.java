package scalar.virtuoso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import scalar.entity.Person;
import scalar.entity.Work;
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
	
    private VirtGraph set = new VirtGraph("jdbc:virtuoso://fusionrepository.isi.edu:1114", "dba", "dba");
	

	/**
	 * 该函数用来得到所有的artist的uri,名字和firstname。
	 * 使用HashMap用来去掉uri重复的artist.
	 * @return HashMap<String,String> 其中一条数据的格式为，uri为键，值为name##firstname
	 * 此处firsname主要用于在页面显示的时候排序，因为name中有的包括Mr等内容
	 */
	public  HashMap<String,String> getAllArtistName()
	{
		HashMap<String,String> result=new HashMap<String,String>();
		
		String query = "select ?person ?name  ?firstname from <http://collection.americanart.si.edu>  where { "
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
		
		System.out.println(result.size());
  
        return result;
	}
	

	/**
	 * 该函数用来得到firstname以特定字母开头的所有的artist的uri,名字和firstname。
	 * 使用HashMap用来去掉uri重复的artist.
	 * @return HashMap<String,String> 其中一条数据的格式为，uri为键，值为name##firstname
	 * 此处firsname主要用于在页面显示的时候排序，因为name中有的包括Mr等内容
	 */
	public  HashMap<String,String> getAllArtistName(String startString)
	{
		HashMap<String,String> result=new HashMap<String,String>();
		
		String query = "select ?person ?name  ?firstname ?appellation from <http://collection.americanart.si.edu>  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+" ?person <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?appellation."
				+" ?appellation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E82_Actor_Appellation>."
				+" ?appellation <http://www.w3.org/2000/01/rdf-schema#label> ?name. "
				+ "?appellation <http://collection.americanart.si.edu/id/ontologies/PE_firstname> ?firstname "
				+" filter regex(?firstname,\"^"+startString+"\")."
				+"}";
		
		System.out.println(query);
		
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
			//仅将displayname放入
			if ( a.toString().trim().indexOf("displayname")>=0)
			{
			    result.put(p.toString().trim(), s.toString().trim()+"##"+f.toString().trim());
			}
		}
		
		System.out.println(result.size());
  
        return result;

	}
	
	
	/**
	 * 该函数用来获取所有作家的uri
	 * @return TreeSet <String> 包含所有作家uri的集合，采用TreeSet可以去除重复元素
	 */
	public  TreeSet<String> getAllArtistURI()
	{
		TreeSet <String> result=new TreeSet<String>();
	
		String query = "select ?person from <http://collection.americanart.si.edu>  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+"}";
		
		System.out.println(query);
		
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{
			//System.out.println("has result");
			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get("person");
		    result.add(p.toString().trim());
	    }
		
		System.out.println(result.size());
        return result;

	}
	
	
	/**
	 * 该函数用来获取所有作家的照片的uri
	 * @return TreeSet <String> 包含所有作家照片uri的集合，采用TreeSet可以去除重复元素
	 */
	public  TreeSet<String> getAllArtistImageURI()
	{
		TreeSet <String> result=new TreeSet<String>();
		
		String query = "select ?person ?image from <http://collection.americanart.si.edu>  where { "
				+ "?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
				+"?person <http://collection.americanart.si.edu/id/ontologies/PE_has_main_representation> ?image."
				+"}";
		
		System.out.println(query);
		
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet queryResult = vqe.execSelect();
		
		while (queryResult.hasNext()) 
		{
			//System.out.println("has result");
			QuerySolution r= queryResult.nextSolution();
			RDFNode p = r.get("image");
		    result.add(p.toString().trim());
	    }
		
		System.out.println(result.size());
        return result;

	}

    
	/**
	 * 该方法根据给定的Person的URI到triple store中检索出相应的值，并返回
	 * @param URI person的uri 
	 * @return Person 各项属性都已设置
	 */
	public  Person getPersonByURI(String URI) {
		Person person = new Person();
		person.setURI(URI);
		int first=URI.indexOf("institution");
	    //System.out.println("*********uri:"+URI);
		person.setId(URI.substring(first+12));//
		
		//System.out.println("*********id:"+person.getId());
		
		
		String query = "select ?p ?s  from <http://collection.americanart.si.edu>  where { "
			+ "<" + URI + ">" + " ?p ?s . }";
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

    		if (p.toString().trim().indexOf("P1_is_identified_by") >= 0 &&s.toString().trim().indexOf("displayname")>=0)
				person.setDisplayNameURI(s.toString().trim());

			if (p.toString().trim().indexOf("P98i_was_born") >= 0)
				person.setBirthURI(s.toString().trim());

			if (p.toString().trim().indexOf("P100i_died_in") >= 0)
				person.setDeathURI(s.toString().trim());

			if (p.toString().trim().indexOf("P69_is_associated_with") >= 0)
				person.setAssociateEventURI(s.toString().trim());

			if (p.toString().trim().indexOf("PE_has_note_artistbio") >= 0)
				person.setArtistBio(s.toString().trim());

			if (p.toString().trim().indexOf("PE_has_note_primaryartistbio") >= 0)
				person.setPrimaryArtistBio(s.toString().trim());

			if (p.toString().trim().indexOf(
			"P107i_is_current_or_former_member_of") >= 0)
				person.setNationalityURI(s.toString().trim());
		}

		query = "select ?name from <http://collection.americanart.si.edu> where { " + "<" + person.getDisplayNameURI()
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?name."
		+ " }";
		person.setDisplayName(getValue(query,"name"));
		//System.out.println("displayname:"+person.getDisplayName());
		

        query = "select ?place from <http://collection.americanart.si.edu> where { " + "<" + person.getDeathURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";
        person.setDeathPlace(getValue(query,"place"));
        //person.setDeathPlace(getDeathPlace(person.getDeathURI()));
        

        query = "select ?date from <http://collection.americanart.si.edu> where { " + "<" + person.getDeathURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
		+ "?timespan <http://www.cidoc-crm.org/cidoc-crm/P82_at_some_time_within> ?date. "
		+ " }";
        person.setDeathDate(getValue(query,"date"));
        
  
        query = "select ?place from <http://collection.americanart.si.edu> where { " + "<" + person.getBirthURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";
        person.setBirthPlace(getValue(query,"place"));
        //person.setDeathPlace(getDeathPlace(person.getDeathURI()));
        
   
        query = "select ?date from <http://collection.americanart.si.edu> where { " + "<" + person.getBirthURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P4_has_time-span> ?timespan."
		+ "?timespan <http://www.cidoc-crm.org/cidoc-crm/P82_at_some_time_within> ?date. "
		+ " }";
        person.setBirthDate(getValue(query,"date"));
        

        query = "select ?place from <http://collection.americanart.si.edu> where { " + "<" + person.getAssociateEventURI()
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";
        person.setAssociatePlace(getValue(query,"place"));
        //person.setDeathPlace(getDeathPlace(person.getDeathURI()));
        
     
        query = "select ?place from <http://collection.americanart.si.edu> where { " + "<" + person.getNationalityURI()
		+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?place."
		+ " }";
        person.setNationality(getValue(query,"place"));
        
 
        person.setCollectionList(getCollectionList(person.getURI()));
        
        
		return person;
	}
	

    public  ArrayList<Work> getCollectionList(String URI)
	{
		ArrayList<Work> collectionList=new ArrayList<Work>();

		String query = "select ?s  from <http://collection.americanart.si.edu> where { ?s " 
			          + " <http://www.cidoc-crm.org/cidoc-crm/P14_carried_out_by> <" + URI+"> ."
		+ " }";

		//System.out.println("*******************ssss " + query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode s = result.get("s");
			//System.out.println(" *******************ssss " + s);
			//name=s.toString().trim();
			if(s!=null&&s.toString().trim().length()>0)
			{
				Work temp=getCollectionByProductionURI(s.toString().trim());
			    collectionList.add(temp);
			}
			
		}
		return collectionList;
	}
	
	

	public  Work getCollectionByProductionURI(String productionURI)
	{
		Work temp=null;

		String query="select ?o from <http://collection.americanart.si.edu> where { " 
			+ " <"+productionURI+"> "+ " <http://www.cidoc-crm.org/cidoc-crm/P108_has_produced> ?o ." 
            + " }";
		
        //System.out.println("************collection " + query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode o = result.get("o");
			temp=SearchByCollectionURI(o.toString().trim());
		}
		
		return temp;
	}
	
	
	public  Work SearchByCollectionURI(String collectionURI) 
	{
	   Work temp=new Work();
	   temp.setURI(collectionURI);
	   int first=collectionURI.indexOf("object");
	   temp.setObjnum(collectionURI.substring(first+7));
	   
	   //System.out.println("*********num:"+temp.getObjnum());

	   String query = "select ?p ?s  from <http://collection.americanart.si.edu>  where { "
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
				temp.setDescription(s.toString().trim());
			
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
		
		}
		


		//private String id;
		 query = "select ?id from <http://collection.americanart.si.edu> where { " + "<" + temp.getIdURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?id."
			+ " }";
		 temp.setId(getValue(query,"id"));

		//private String title;
		 query = "select ?title from <http://collection.americanart.si.edu> where { " + "<" + temp.getTitleURI()
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?title."
		+ " }";
		 temp.setTitle(getValue(query,"title"));
	
		//private String objnum;
		 query = "select ?num from <http://collection.americanart.si.edu> where { " + "<" + temp.getTitleURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?num."
			+ " }";
		 temp.setObjnum(getValue(query,"num"));	
			 
		
	    //private String keeper;
		 int beginIndex=temp.getKeeperURI().indexOf("id/");
	     temp.setKeeper(temp.getKeeperURI().substring(beginIndex+3));
	
		//private String mainClass;
		 query = "select ?main from <http://collection.americanart.si.edu> where { " + "<" + temp.getMainClassURI()
			+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?main."
			+ " }";
		 temp.setMainClass(getValue(query,"main"));

		//private String subClass;
		 /*
		  query = "select ?sub from <http://collection.americanart.si.edu> where { " + "<" + temp.getMainClassURI()
			+ ">" + " <http://www.w3.org/2008/05/skos#prefLabel> ?sub."
			+ " }";
		 temp.setSubClass(getValue(query,"sub"));
		 */
	
		//private String location;
		 query = "select ?loc from <http://collection.americanart.si.edu> where { " + "<" + temp.getLocationURI()
			+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?loc."
			+ " }";
		 temp.setLocation(getValue(query,"loc"));	
		 
		
	
	    //private ArrayList<String> keywordList;
		 if(temp.getKeywordURIList()!=null)
		 {
		     for(String t:temp.getKeywordURIList())
		    {
			     query = "select ?keyword from <http://collection.americanart.si.edu> where { " + "<" + t
				    + ">" + " <http://www.cidoc-crm.org/cidoc-crm/P129_is_about> ?uri."
				    + "?uri <http://www.w3.org/2008/05/skos#prefLabel> ?keyword."
				    + " }";
			     temp.addKeyword(getValue(query,"keyword"));
		    }
		 }
		 

	    //private String width;
	    //private String height;
	    
	    //private ArrayList<Person> personList;
		 
		System.out.println("****************title:"+temp.getTitle());
	   
	   
	   
	   return temp;
	}
	

	
	public  String getName(String displayNameURI)
	{
		String name="";

		String query = "select ?name where { " + "<" + displayNameURI
		+ ">" + " <http://www.w3.org/2000/01/rdf-schema#label> ?name."
		+ " }";

		//System.out.println("22222222222222" + query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode o = result.get("name");
			//System.out.println(" ******************* " + o);
			name=o.toString().trim();
		}
		return name;
	}
	

	public  String getDeathPlace(String deathURI)
	{
		String place="";

		String query = "select ?place from <http://collection.americanart.si.edu> where { " + "<" + deathURI
		+ ">" + " <http://www.cidoc-crm.org/cidoc-crm/P7_took_place_at> ?placeuri."
		+ "?placeuri <http://www.w3.org/2008/05/skos#prefLabel> ?place. "
		+ " }";

		//System.out.println("************place" + query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode o = result.get("place");
			place=o.toString().trim();
		}
		//System.out.println("************place result:" + place);
		return place;
	}

    public  String getValue(String query,String name)
	{
		String value="";
        System.out.println("************place" + query);
        Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode o = result.get(name);
			value=o.toString().trim();
		}
		System.out.println("************place result:" + value);
		return value;
	}

	public  ArrayList<Person> searchByArtistName(String keyword) {
		ArrayList<Person> personList = new ArrayList<Person>();

		/*
		 * String query="PREFIX rdf:
		 * <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+ "PREFIX rdfs:
		 * <http://www.w3.org/2000/01/rdf-schema#> \n"+ "PREFIX crm:
		 * <http://www.cidoc-crm.org/cidoc-crm/> \n"+ "select ?person ?name\n"+"
		 * where { \n"+ " ?person rdf:type crm:E21_Person.\n"+ " ?person
		 * crm:P1_is_identified_by ?appellation.\n"+ " ?appellation rdf:type
		 * crm:E82_Actor_Appellation.\n"+ " ?appellation rdfs:label ?name"+ "
		 * FILTER regex(?name, \" *George Biddle*\")."+ " }";
		 */
		/*
		 * String query="select ?person "+" where { "+ " ?person
		 * <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
		 * <http://www.cidoc-crm.org/cidoc-crm/E21_Person>. "+ " }";
		 */

		String query = "select ?o ?p ?s from <http://collection.americanart.si.edu> where { ?o ?p ?s. ?o  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.cidoc-crm.org/cidoc-crm/E21_Person>."
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
					if (p
							.toString()
							.trim()
							.equals(
							"http://collection.americanart.si.edu/id/ontologies/PE_has_main_representation"))
						t.setMainRepresentationURI(s.toString().trim());
					if (p
							.toString()
							.trim()
							.equals(
							"http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by"))
						t.setDisplayNameURI(s.toString().trim());

					if (p.toString().trim().equals(
					"http://www.cidoc-crm.org/cidoc-crm/P98i_was_born"))
						t.setBirthURI(s.toString().trim());

					if (p.toString().trim().equals(
					"http://www.cidoc-crm.org/cidoc-crm/P100i_died_in"))
						t.setDeathURI(s.toString().trim());

					if (p
							.toString()
							.trim()
							.equals(
							"http://www.cidoc-crm.org/cidoc-crm/P107i_is_current_or_former_member_of"))
						t.setNationalityURI(s.toString().trim());

				}
			}

		}
		//System.out.println("end:------------");

		for (Person temp : personList) {
			query = "select ?name from <http://collection.americanart.si.edu> where { "
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

	public  String search(String keyword, String searchType) {
		sparqlSearch("ok");
		return "ok";
	}


	public  String sparqlSearch(String query) {
		
		Query sparql = QueryFactory
		.create("SELECT * from <http://collection.americanart.si.edu> WHERE { ?s ?p ?o } limit 100");

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
	}

}