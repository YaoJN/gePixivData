package main;

import java.util.List;
import java.util.regex.Matcher;

import constant.Constant;
import logic.GetPictureLink;
import utils.Util;

public class DownLoadByUser {

	public static void main(String[] args) throws InterruptedException {

		// 下载路径
		String savePath = Constant.SAVE_PATH_USER;

		String user = "3532395";
		// 获得当前地址
		String url = Constant.USER_URL_LEFT + user + Constant.USER_URL_RIGHT;
		String fullSavePath = savePath + Util.getUserName(user);
		for (int i = 1; i < 100; i++) {
			// 访问链接并获取页面内容
			String result = Util.SendGet(url + i);
			// 图片区域信息List取得
			Matcher matcher = Util.RegexString(result, "(?<=\"image-item\"><a href=\").*?(?=\" class=\"work)");
			List<String> pictureList = Util.getListString(matcher);
			if (null == pictureList || pictureList.size() == 0) {
				break;
			} else {
				for (String string : pictureList) {
					GetPictureLink getPictureLink = new GetPictureLink(string, fullSavePath);
					Thread thread = new Thread(getPictureLink);
					thread.start();
				}
			}
		}
	}
}
