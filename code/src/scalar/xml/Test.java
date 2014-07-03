package scalar.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String [] args)
	{
		String teststr = "%%%private999999-String//";
        String testreg = "[^0-9]";
        Pattern pattern = Pattern.compile(testreg);
        Matcher mp = pattern.matcher(teststr);
        System.out.println(mp.replaceAll("").trim()); 
	}

}
