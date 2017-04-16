package logic;

import java.io.InputStream;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import utils.Util;

public class DownPicture implements Runnable {

	// 文件路径
	private String savePath = "";

	// 文件名
	private String pictureName = "";

	// 链接数据
	private String pictureLink = "";

	DownPicture(String savePath, String pictureName, String pictureLink) {
		this.savePath = savePath;
		this.pictureName = pictureName;
		this.pictureLink = pictureLink;
	}

	@Override
	public void run() {
		// HttpClient 超时配置
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
				.setConnectionRequestTimeout(30000).setConnectTimeout(30000).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
		// 创建一个GET请求
		HttpGet httpGet = new HttpGet(pictureLink);
		httpGet.addHeader("Host", "i.pximg.net");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
		httpGet.addHeader("Referer", "http://www.pixiv.net/member_illust.php?mode=manga_big&illust_id=62370641&page=0");
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			InputStream in = response.getEntity().getContent();
			Util.getImageFromNetByUrl(savePath, pictureName, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
