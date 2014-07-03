package scalar.xml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//解析scalar返回的数据

public class XMLParser {

	public static void getAllPageUrn(String s) {
		
		//添加根节点
		int firstPosition=s.indexOf("<rdf:Description");
		int lastPosition=s.indexOf("</rdf:RDF>");
		/*String xmlString=s.substring(0,firstPosition)
				+" <rdf:Description rdf:about=\"http://scalar.usc.edu/works/virtual-museum-of-american-art/\">"
				+s.substring(firstPosition,lastPosition)
				+"</rdf:Description>"
				+s.substring(lastPosition,s.length());
		*/
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader
					.read(new ByteArrayInputStream(s.getBytes()));
			writeXML(document);
			Element rootElement = document.getRootElement(); 
			System.out.println("********rootelement:"+rootElement.getText());
			Element e=rootElement.element("rdf:Description");
			System.out.println("********rdf:"+e.getText());
			
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public static void writeXML(Document document) throws IOException
	{
		OutputFormat out = new OutputFormat("    ",true);out.setEncoding("UTF-8");  
		XMLWriter xml = new XMLWriter(new FileOutputStream("/users/jwan/desktop/aaa.xml"),out);
		xml.write(document);
		xml.flush();
		xml.close();
		System.out.println("新的XML文件生成成功");
	}
	
	public static void main(String [] args) throws Exception
	{
		  SAXReader saxReader = new SAXReader();
	      //从文件构造一个Document，因为XML文件中已经指定了编码，所以这里不必了  

	      Document doc = saxReader.read(new FileInputStream("/users/jwan/desktop/test2.xml"),"UTF-8");
		  Element root = doc.getRootElement();  
	      System.out.println("根节点标记名：" + root.getName());  
	      System.out.println("根节点值：" + root.getText());
	      System.out.println("*****下面遍历XML元素*****");  
	      List nodes = root.elements();      
	      for (Iterator it = nodes.iterator(); it.hasNext();) 
	      {          
	    	  Element elm = (Element) it.next();         
	    	  System.out.println("节点标记名：" + elm.getName());  
	    	 
	    	  List subNodes=elm.elements();
	    	  for(Iterator t=subNodes.iterator();t.hasNext();)
	    	  {
	    		  Element e=(Element)t.next();
	    		  System.out.println("节点标记名：" +e.getName());//属性名称
	    		  System.out.println("节点值：" +e.getText());//属性值
	    	  }
	      }
	     
		
	}

}
