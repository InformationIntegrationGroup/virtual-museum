package scalar.xml;
import javax.xml.parsers.*;  
import org.w3c.dom.*;  
import java.io.*; 

public class DomParser {
	
	public static void main(String [] args)
	{
		//parseNormal();
		parseSpecial();
	}
	
	public static void parseNormal()
	{
 
		    try {  
		      //创建解析工厂  
		      DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();  
		      //指定DocumentBuilder  
		      DocumentBuilder builder = dbfactory.newDocumentBuilder();  
		      //从文件构造一个Document，因为XML文件中已经指定了编码，所以这里不必了  
		      Document doc = builder.parse(new File("/users/jwan/desktop/test.xml"));  
		      //得到Document的根（节点名：book）  
		      Element root = doc.getDocumentElement();  
		      System.out.println("根节点标记名：" + root.getTagName());  
		      System.out.println("*****下面遍历XML元素*****");  
		      //获得page元素  
		      NodeList list = root.getElementsByTagName("content");  
		      System.out.println(list.getLength());  
		      //遍历page元素  
		      for (int i=0; i < list.getLength() ; i++) {  
		     //获得page的元素  
		        Element element = (Element)list.item(i);  
		        //获得ID属性  
		        NodeList titleid = element.getElementsByTagName("id");  
		        //获得id元素  
		        Element idElement = (Element)titleid.item(0);  
		        //获得id元素的第一个值  
		        String id = idElement.getFirstChild().getNodeValue();  
		        System.out.println("ID :" + "   " + id  + "title :"  + "   " + titleid );  
		      }  
		    } catch (Exception e) {  
		      e.printStackTrace();  
		    }  
		    
	}
	
	public static void parseSpecial()
	{
		try {  
		       DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();  
		       DocumentBuilder builder = dbfactory.newDocumentBuilder();  
		       Document doc = builder.parse(new File("/users/jwan/desktop/aaa.xml"));     
		       Element root = doc.getDocumentElement();  
		       System.out.println("root name:"+root.getNodeName());
	
		       NodeList list = root.getChildNodes();    
		       Node node = null;   
	 
		       for(int i=0;i<list.getLength();i++){  
		           node = list.item(i);  
		           NodeList subList = node.getChildNodes();
		           for(int j=0;j<subList.getLength();j++)
		           {
		        	   Node subNode=subList.item(j);
		        	   String value=subNode.getAttributes().getNamedItem("rdf:type").getNodeValue();
		        	   System.out.println("值:"+value);
		           }
		           
		      }  
		     } catch (Exception e) {  
		       e.printStackTrace();  
		     }  
	}
		  
	

}
