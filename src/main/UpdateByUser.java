package main;

import java.util.Map;
import java.util.Map.Entry;

import constant.Constant;
import logic.DownPictureByUser;
import utils.Util;

public class UpdateByUser {

	public static void main(String[] args) throws InterruptedException {
		// 放置作者
		Map<Integer, String> map = Util.userNameMap;
		// 下载路径
		String savePath = Constant.SAVE_PATH_USER;
		// 根据公司下载内容
		for (Entry<Integer, String> useEntry : map.entrySet()) {
			Thread.sleep(500);
			new Thread(new DownPictureByUser(useEntry.getKey().toString(), savePath)).start();
		}
	}
}
