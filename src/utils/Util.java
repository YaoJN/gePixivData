package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import constant.Constant;
import logic.DownPicture;
import logic.GetCompanyData;
import logic.GetPictureDate;

public class Util {

	public static Map<Integer, String> userNameMap = new HashMap<Integer, String>();

	static {
		userNameMap.put(4872213, "N.G");
		userNameMap.put(297791, "Ϙ");
		userNameMap.put(2067821, "新屋敷");
		userNameMap.put(2774175, "しりー");
		userNameMap.put(1226647, "As109");
		userNameMap.put(660275, "ONC91");
		userNameMap.put(1854020, "ち");
		userNameMap.put(5201223, "LOLICEPT");
		userNameMap.put(13244881, "Hplay");
		userNameMap.put(2650491, "janong");
		userNameMap.put(617959, "くれりて");
		userNameMap.put(643512, "Redcomet");
		userNameMap.put(3618744, "ゆきうさぎ");
	}

	// 计数器
	private static Integer count_ = 1;

	/**
	 * 访问链接并获取页面内容
	 * 
	 * @param url
	 * @return
	 */
	public static String SendGet(String url) {
		// 定义一个字符串用来存储网页内容
		String result = "";
		// 定义一个缓冲字符输入流
		BufferedReader br = null;
		try {
			// HttpClient 超时配置
			RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
					.setConnectionRequestTimeout(30000).setConnectTimeout(30000).build();
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
			// 开始实际的连接
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
			httpGet.addHeader("cookie",
					"PHPSESSID=10476731_f85fb26d354cf41cf3935cf94c12c43b; p_ab_id=3; __utma=235335808.1683302790.1477053655.1492175054.1492248580.63; __utmz=235335808.1477053655.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^4=p_ab_id_2=6=1^5=gender=male=1^6=user_id=10476731=1^9=p_ab_id=3=1^10=p_ab_id_2=6=1^11=illust_tag_placeholder=yes=1^12=fanbox_subscribe_button=orange=1^13=fanbox_fixed_otodoke_naiyou=yes=1^14=hide_upload_form=yes=1^15=machine_translate_test=no=1; _ga=GA1.2.1683302790.1477053655; module_orders_mypage=%5B%7B%22name%22%3A%22recommended_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22fanbox%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22sensei_courses%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; p_ab_id_2=6; __utmb=235335808.9.10.1492248580; __utmc=235335808; __utmt=1");
			// 发送请求，并执行
			CloseableHttpResponse response = httpClient.execute(httpGet);
			InputStream in = response.getEntity().getContent();
			// 初始化 BufferedReader输入流来读取URL的响应
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			// 用来临时存储抓取到的每一行的数据
			String line;
			while ((line = br.readLine()) != null) {
				// 遍历抓取到的每一行并将其存储到result里面
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally来关闭输入流
		finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 定义一个样式模板，此中使用正则表达式
	 * 
	 * @param targetStr
	 * @param patternStr
	 * @return
	 */
	public static Matcher RegexString(String targetStr, String patternStr) {

		// 相当于埋好了陷阱匹配的地方就会掉下去
		Pattern pattern = Pattern.compile(patternStr);
		// 定义一个matcher用来做匹配
		Matcher matcher = pattern.matcher(targetStr);
		return matcher;
	}

	/**
	 * 利用匹配结果获得内容(List<String>)
	 * 
	 * @param matcher
	 * @return
	 */
	public static List<String> getListString(Matcher matcher) {
		// 匹配到的数据扔到List中;
		List<String> tableList = new ArrayList<>();
		while (matcher.find()) {
			tableList.add(matcher.group());
		}
		return tableList;
	}

	/**
	 * 利用匹配结果获得内容(String)
	 * 
	 * @param matcher
	 * @return
	 */
	public static String getString(Matcher matcher) {
		String str = null;
		while (matcher.find()) {
			str = matcher.group();
		}
		return str;
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String savePath, String fileName) {
		try {
			if (!new File(savePath).exists()) {
				new File(savePath).mkdirs();
			}
			File file = new File(savePath + File.separator + fileName);
			FileOutputStream fops = new FileOutputStream(file, true);
			fops.write(img);
			fops.flush();
			fops.close();
			System.out.println("第" + count_ + "个图片已写入");
			System.out.println("下载路径:" + file.getAbsolutePath());
			count_++;
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
	public static byte[] getImageFromNetByUrl(String savePath, String fileName, InputStream inStream) {
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

	// 查看当前的日期是否下载过
	public static boolean readFromFile(String date, String SaveFile) {
		File file = new File(SaveFile);
		StringBuilder result = new StringBuilder();
		try {
			// 构造一个BufferedReader类来读取文件
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			// 使用readLine方法，一次读一行
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
				if (date.equals(s)) {
					System.out.println("==========当前日期" + date + "已经下载过==========");
					br.close();
					return true;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 将下载的日期保存到文件中.
	public static void writeLinkToFile(String date, String saveFile) {
		File file = new File(saveFile);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(date, 0, date.length());
			out.write(System.getProperty("line.separator"));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获得下载天数集合
	public static List<String> getDateList() {
		List<String> dayList = new ArrayList<String>();
		String startDay = Constant.DOWN_YEAR + Constant.DOWN_MONTH + Constant.DOWN_START_DAY;
		String endDay = Constant.DOWN_YEAR + Constant.DOWN_MONTH + Constant.DOWN_END_DAY;
		do {
			dayList.add(startDay);
			Integer startDayInt = Integer.parseInt(startDay) + 1;
			startDay = startDayInt.toString();
		} while (endDay.compareTo(startDay) >= 0);
		return dayList;
	}

	/**
	 * 根据公司下载内容
	 * 
	 * @param company
	 * @throws InterruptedException
	 */
	public static void downCompany(String company, String savePath) throws InterruptedException {
		// 链接集合取得
		List<String> linkList = new ArrayList<>();
		linkList = new GetCompanyData().doMain(company);
		Thread thread = null;
		// 开启多线程下载
		for (String linkDate : linkList) {
			Thread.sleep(300);
			GetPictureDate getPictureDate = new GetPictureDate(linkDate, savePath);
			thread = new Thread(getPictureDate);
			thread.start();
		}
	}

	public static void getPictureListLink(String result, String savePath) {
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
//			System.out.println("图片路径:" + pictureLink);
			try {
				Thread.sleep(100);
				new Thread(new DownPicture(savePath, pictureName, pictureLink)).start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 取得单张图片的地址,并下载
	public static void getPictureLink(String result, String savePath) {
		// 单张图片地址取得
		Matcher matcher = Util.RegexString(result, "(?<=<div class=\"wrapper\">).*?(?=</div>)");
		String pictureLink = Util.getString(matcher);
		matcher = Util.RegexString(pictureLink, "(?<=data-src=\").*?(?=\")");
		pictureLink = Util.getString(matcher);
		String pictureName = getPictureName(pictureLink);
//		System.out.println("图片路径:" + pictureLink);
		new Thread(new DownPicture(savePath, pictureName, pictureLink)).start();
	}

	// 获得图片名
	private static String getPictureName(String pictureLink) {
		String pictureList[] = pictureLink.split("/");
		return pictureList[pictureList.length - 1];
	}

	public static String getUserName(String user) {
		return userNameMap.get(Integer.parseInt(user));
	}
}
