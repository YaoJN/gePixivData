package logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import constant.Constant;
import utils.Util;

public class DownPictureByUser implements Runnable {
	private String user = "";
	private String savePath = "";
	private boolean saveflg = false;

	public DownPictureByUser(String user, String savePath) {
		this.user = user;
		this.savePath = savePath;
	}

	// 计数器
	private static Integer count_ = 1;

	@Override
	public void run() {
		// 获得当前地址
		String url = Constant.USER_URL_LEFT + user + Constant.USER_URL_RIGHT;
		String userName = Util.getUserName(user);
		String fullSavePath = savePath + userName;
		for (int i = 1; i < 100; i++) {
			if (saveflg) {
				System.out.println(userName + "的作品已经下载完毕.");
				break;
			}
			// 访问链接并获取页面内容
			String result = Util.SendGet(url + i);
			// 图片区域信息List取得
			Matcher matcher = Util.RegexString(result, "(?<=\"image-item\"><a href=\").*?(?=\" class=\"work)");
			List<String> pictureList = Util.getListString(matcher);
			if (null == pictureList || pictureList.size() == 0) {
				break;
			} else {
				for (String string : pictureList) {
					if (saveflg) {
						break;
					}
					// 访问链接并获取页面内容
					String resultDate = Util.SendGet(Constant.HTTP + Constant.PIXIV + string);
					// 下载图片区域信息取得
					matcher = Util.RegexString(Util.SendGet(Constant.HTTP + Constant.PIXIV + string), "original-image");
					String pictureArea = Util.getString(matcher);
					if (pictureArea != null) {
						// 获得单张图片
						getPictureLink(resultDate, fullSavePath);
					} else {
						// 获得图片集
						getPictureListLink(resultDate, fullSavePath);
					}
				}
			}
		}
	}

	public void getPictureListLink(String result, String savePath) {
		// 图片集地址内容取得
		Matcher matcher = Util.RegexString(result, "(?<=<div class=\"works_display\"><a href=\").*?(?=\")");
		String pictureLink = Util.getString(matcher);
		// 图片详细地址页面取得
		String pictureLinkView = Util.SendGet(Constant.HTTP + Constant.PIXIV + pictureLink);
		// 图片集地址集取得
		matcher = Util.RegexString(pictureLinkView, "(?<=</script><a href=\").*?(?=\" target=\")");
		List<String> pictureLinkViewList = Util.getListString(matcher);
		for (String pictureViewLink : pictureLinkViewList) {
			pictureLinkView = Util.SendGet(Constant.HTTP + Constant.PIXIV + pictureViewLink);
			matcher = Util.RegexString(pictureLinkView, "(?<=<img src=\").*?(?=\")");
			pictureLink = Util.getString(matcher);
			String pictureName = getPictureName(pictureLink);
			try {
				Thread.sleep(100);
				downPicture(savePath, pictureName, pictureLink);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 取得单张图片的地址,并下载
	public void getPictureLink(String result, String savePath) {
		// 单张图片地址取得
		Matcher matcher = Util.RegexString(result, "(?<=<div class=\"wrapper\">).*?(?=</div>)");
		String pictureLink = Util.getString(matcher);
		matcher = Util.RegexString(pictureLink, "(?<=data-src=\").*?(?=\")");
		pictureLink = Util.getString(matcher);
		String pictureName = getPictureName(pictureLink);
		downPicture(savePath, pictureName, pictureLink);
	}

	// 获得图片名
	private static String getPictureName(String pictureLink) {
		String pictureList[] = pictureLink.split("/");
		return pictureList[pictureList.length - 1];
	}

	private void downPicture(String savePath, String pictureName, String pictureLink) {
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
			getImageFromNetByUrl(savePath, pictureName, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	public byte[] getImageFromNetByUrl(String savePath, String fileName, InputStream inStream) {
		try {
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			if (null != btImg && btImg.length > 0) {
				writeImageToDisk(btImg, savePath, fileName);
			} else {
				System.out.println("没有从该连接获得内容");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public void writeImageToDisk(byte[] img, String savePath, String fileName) {
		try {
			if (!new File(savePath).exists()) {
				new File(savePath).mkdirs();
			}
			File file = new File(savePath + File.separator + fileName);
			if (file.exists()) {
				saveflg = true;
				return;
			}
			FileOutputStream fops = new FileOutputStream(file, true);
			fops.write(img);
			fops.flush();
			fops.close();
			System.out.println("第" + count_ + "个图片已写入");
			System.out.println("图片路径:" + file.getAbsolutePath());
			count_++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
