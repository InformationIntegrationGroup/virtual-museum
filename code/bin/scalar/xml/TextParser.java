package scalar.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TextParser {
	public static void main(String args[]) throws Exception
	{
		getUrnScalarVersionList();
	}
	
	public static ArrayList<String>  getUrnScalarVersionList() throws IOException
	{
		ArrayList <String> result=new ArrayList<String>();
		InputStream is = new FileInputStream("/users/jwan/desktop/aaa.xml");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            //System.out.println(line);
            int beginPosition=line.indexOf("urn:scalar:version");
            int endPosition=line.lastIndexOf("\"");
            if(beginPosition>=0)
            {
                System.out.println(line.substring(beginPosition,endPosition));
                result.add(line.substring(beginPosition,endPosition));
            }
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
		return result;
	}
	
	public static ArrayList<String>  getUrnScalarVersionList(String s) throws IOException
	{
		ArrayList <String> result=new ArrayList<String>();

		InputStream is=new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        int i=0;
        while (line != null) { // 如果 line 为空说明读完了
           
            int beginPosition=line.indexOf("urn:scalar:version");
            int endPosition=line.indexOf(">");
            if(beginPosition>=0)
            {
            	System.out.println(line);
                System.out.println(""+i+":"+line.substring(beginPosition,endPosition-2));
                result.add(line.substring(beginPosition,endPosition));
            }
            line = reader.readLine(); // 读取下一行
            i++;
        }
        reader.close();
        is.close();
		return result;
	}

}
