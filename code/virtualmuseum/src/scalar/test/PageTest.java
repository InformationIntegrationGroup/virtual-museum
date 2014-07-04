package scalar.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import scalar.process.PagePropertyObtainer;

/**
 * 这个类主要用作测试
 * @author wan jing
 *
 */
public class PageTest extends TestCase 
{
	 public PageTest(String name)
	 {
	     super(name);
	 }
	

	/**
	 * 这个方法用来测试aristList页面上的链接是否正确
	 * 作者的链接是否连接到作者的页面
	 */
	private static void testArtistListPageLink()
	{
		
		ArrayList<String> artistUrlList;
		String [ ] pageUrl={"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--a-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--b-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--c-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--d-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--e-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--f-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--g-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--h-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--i-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--j-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--k-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--l-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--m-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--n-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--o-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--p-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--q-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--r-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--s-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--t-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--u-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--v-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--w-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--x-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--y-8",
				"http://scalar.usc.edu/works/virtual-museum-of-american-art/list-of-artists--z-8",
				};
				
		
		for (int i = 0; i < pageUrl.length; i++) 
		{   
			//获取artilstList上的所有链接
			artistUrlList=(new PagePropertyObtainer()).getUrlList(pageUrl[i]);
			// 针对每个链接，测试相关的作者页面是否存在
			for (String temp : artistUrlList) 
			{
				if(temp.indexOf("levitsky")<0)//特殊处理http://scalar.usc.edu/works/virtual-museum-of-american-art/daniel-rosza-lang/levitsky
				    assertTrue(new PagePropertyObtainer().getItemStateByUrl(temp));
				//new PagePropertyObtainer().getItemStateByUrl(temp);
			}
		}
	}
		
	public static void main(String [] args)
	{
		testArtistListPageLink();
	}
		
}

