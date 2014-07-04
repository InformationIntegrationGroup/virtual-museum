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
	
	//图书的基本URL
	String BASE_URL="http://scalar.usc.edu/isi/virtual-museum-of-american-art";
    String Book_ID="urn:scalar:book:1";
}
