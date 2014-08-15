package scalar.entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface PageProperty {
	ArrayList <String> UrnScalarVersionList=new ArrayList<String> ();
	
	// 健，<art:url rdf:resource="http://americanart.si.edu/images/1909/1909.7.43_1a.jpg"/>
	// 值，<scalar:urn rdf:resource="urn:scalar:version:189826"/>
	HashMap <String,String> ImageResourceVersionMap=new HashMap<String,String>();
	
	//scalar账号
	String ID="wanjingbuct@gmail.com";
	//api key密码
	String API_KEY= "eWJV73kP";
	//图书的基本URL
	String BASE_URL="http://scalar.usc.edu/isi/virtual-museum-of-american-art";
    String Book_ID="urn:scalar:book:1";
    
    String Virtuoso_URL="jdbc:virtuoso://fusionrepository.isi.edu:1114";
    String Virtuoso_User="dba";
    String Virtuoso_Password="dba";
    
    //各博物馆的在virtuoso中的graph
    String Smith_Graph="http://collection.americanart.si.edu";
	String Crystal_Graph="http://collection.crystalbridges.org";
	String Carter_Graph="http://collection.cartermuseum.org";
	String PersonLink_Graph="http://personLink.isi.edu";
	
	//各博物馆名字
	String Smith_Name="Smithsonian American Art Museum";
	String Crystal_Name="Crystal Bridges Museum of American Art";
	String Carter_Name="Amon Carter Museum of American Art";
	
	String [ ] Museum_Graph_Array={"http://collection.americanart.si.edu",
		                           "http://collection.crystalbridges.org",
			                       "http://collection.cartermuseum.org"};
}
