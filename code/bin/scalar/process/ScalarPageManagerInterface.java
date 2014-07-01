package scalar.process;

import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;

public interface ScalarPageManagerInterface {
	/**
	 * 该方法用来向scalar平台中添加作家姓名索引页面，一共26个，从A－Z
	 */
	public void addArtistListPage();

	/**
	 * 该方法用来向scalar平台提交所有作家的照片，每个作家只提交一张照片
	 */
	public void addAllImage();

	/**
	 * 该方法用来向scalar平台上提交一个作者的照片 对于smithsonian American art
	 * museum，由于每个图片都有对应的url，该方法是有效的
	 * @param imageURI 作者照片的url，作者照片对应的网络上的url
	 */
	public void addImage(String imageURI);
	
	/**
	 * 该方法用来向scalar平台提交所有作品的照片，每个作品只提交一张照片
	 */
	public  void addAllCollectionImage() ;

	/**
	 * 该方法用来向scalar平台添加所有作家的页面
	 * 由于需要在页面上添加照片的链接，所以在该方法使用之前，所有的作家照片应该已经提交到了scalar平台上 该方法从平台上实时获取照片的信息
	 */
	public void addAllArtistPage();

	/**
	 * 该方法用来向scalar平台上添加一个作品页面 对于smithsonian American art museum，artisturi的例子为
	 *  http://collection.americanart.si.edu/id/object/1970.39 
     * @param collectionURI 作品的uri，来自triple store
     * @return boolean 如果添加成功，则返回true
	 */
	public boolean addCollectionPage(String collectionURI);
	
	/**
	 * 该方法用来向scalar平台添加所有作品的页面
	 * 由于需要在页面上添加照片的链接，所以在该方法使用之前，所有的作品照片应该已经提交到了scalar平台上 该方法从平台上实时获取照片的信息
	 */
	public void addAllCollectionPage();

	/**
	 * 该方法用来向scalar平台上添加一个作家页面 对于smithsonian American art museum，artisturi的例子为
	 * http://collection.americanart.si.edu/id/person-institution/4253
     * @param artistURI 作家的uri，来自triple store
     * @return boolean 如果添加成功，则返回true
	 */
	public boolean addArtistPage(String artistURI);

	/**
	 * 该方法用来删除Scalar上所有的已有页面
	 */
	public void deleteAllPages();

	/**
	 * 该方法用来删除scalar上一个指定的页面或图片
	 * @param urnScalarVersion
	 */
	public void deleteItem(String urnScalarVersion);
	
	/**
	 * 该方法用来删除Scalar上所有的作家的照片
	 */
	public void deleteAllArtistImage();

	// update a page
	public void update() throws IOException;

	/**
	 * 该方法用来向scalar平台提交数据
	 * @param urlString 提交数据的url
	 * @param content 提交的数据内容
	 * @return URLConnection url链接，用于获取平台返回信息
	 * @throws IOException
	 */
	public URLConnection post(String urlString, String content)
			throws IOException;

	/**
	 * 该方法用于在向scalar平台post数据之后，获取平台返回的信息
	 * @param urlConn post时建立的url connection
	 * @return scalar平台返回的数据
	 * @throws IOException
	 */
	public String getResponse(URLConnection urlConn) throws IOException;

	/**
	 * 该方法根据要提交的参数对，生成post的内容 
	 * @param data 要提交的属性名和值
	 * @return 生成的post的内容
	 */
	public String generateContent(HashMap<String, String> data);

}
