package scalar.process;



public class Main {
	public static void main(String args[]) throws Exception
	{
		ScalarPageManagerInterface m=new ScalarPageManager();
		//m.deleteAllPages();//获取最新页面数据，删除所有页面,已测试，仅仅隐藏页面，可登录到scalar平台，dashboard中的pages，进行完全删除
		//m.deleteAllArtistImage();//获取最新图片数据，删除所有图片，已测试，可登录到scalar平台，dashboard中的media，进行完全删除
		//m.addArtistListPage();//添加姓名索引页面,已测试
		//m.addAllImage();//添加所有图片,已测试
		m.addAllArtistPage();// 添加作者页面
		
	}
	

}
