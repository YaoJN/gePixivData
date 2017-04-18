package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constant.Constant;
import utils.Util;

public class GetPictureDate implements Runnable {
	// 链接数据
	private String pictureDateList = "";
	// 文件路径
	private String savePath = "";

	public GetPictureDate(String pictureDate, String savePath) {
		this.pictureDateList = pictureDate;
		this.savePath = savePath;
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 */
	@Override
	public void run() {
		List<String> pictureDate = new ArrayList<String>();
		// 图片名称取得
		// 0.ID
		Matcher matcher = Util.RegexString(pictureDateList, "(?<=data-id=\").*?(?=\")");
		String pictureID = Util.getListString(matcher).get(0);
		pictureDate.add(pictureID);
		// 1.TYPE
		matcher = Util.RegexString(pictureDateList, "(?<=data-attr=\").*?(?=\")");
		String pictureType = Util.getListString(matcher).get(0);
		pictureDate.add(pictureType);
		// 2.URL
		matcher = Util.RegexString(pictureDateList, "(?<=<a href=\").*?(?=\")");
		String pictureLink = Util.getListString(matcher).get(2);
		pictureDate.add(pictureLink);
		// 3.TITLE
		matcher = Util.RegexString(pictureDateList, "(?<=data-title=\").*?(?=\")");
		String pictureTitle = Util.getListString(matcher).get(0);
		pictureDate.add(pictureTitle);
		// 4.USER
		matcher = Util.RegexString(pictureDateList, "(?<=data-user-name=\").*?(?=\")");
		String pictureUser = Util.getListString(matcher).get(0);
		pictureDate.add(pictureUser);
		// 5.TAGS
		matcher = Util.RegexString(pictureDateList, "(?<=data-tags=\").*?(?=\")");
		String pictureTags = Util.getListString(matcher).get(0);
		pictureDate.add(pictureTags);
		this.analysisPicture(pictureDate);
	}

	// 下载图片
	private void analysisPicture(List<String> pictureDate) {
		String pictureID = pictureDate.get(0);
		String pictureType = pictureDate.get(1);
		String pictureTags = pictureDate.get(5);
		if (isNumeric(pictureID)) {
			if (!isNotBL(pictureType)) {
				if (isNotBLTag(pictureTags)) {
					// 访问链接并获取页面内容
					String result = Util.SendGet(Constant.HTTP + Constant.PIXIV + pictureDate.get(2));
					// 下载图片区域信息取得
					Matcher matcher = Util.RegexString(result, "original-image");
					String pictureArea = Util.getString(matcher);
					if (pictureArea != null) {
						// 获得单张图片
//						getPictureLink(result, pictureDate);
						Util.getPictureLink(result,savePath);
					} else {
						// 获得图片集
//						getPictureListLink(result, pictureDate);
						Util.getPictureListLink(result,savePath);
					}
				}
			}
		}
	}

	// 不下载BL和兽类的内容
	private boolean isNotBL(String pictureType) {
		if (null != pictureType && ("bl".indexOf(pictureType) != -1 || "furry".indexOf(pictureType) != -1)) {
			return false;
		}
		return true;
	}

	// 是BL标签
	private boolean isNotBLTag(String pictureTags) {
		boolean notBLTagflg = true;
		String[] tags = Constant.KILL_TAGS.split(",");
		for (String tag : tags) {
			if (pictureTags.indexOf(tag) != -1) {
				notBLTagflg = false;
				break;
			}
		}
		return notBLTagflg;
	}

	// 判断是否是全数字Id,并取得.
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

}
