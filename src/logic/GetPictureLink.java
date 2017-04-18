package logic;

import java.util.regex.Matcher;

import constant.Constant;
import utils.Util;

public class GetPictureLink implements Runnable {
	//链接地址
	private String link = "";
	private String fullSavePath = "";
	
	public GetPictureLink(String link,String fullSavePath){
		this.link = link;
		this.fullSavePath = fullSavePath;
	}
	
	@Override
	public void run() {
		// 访问链接并获取页面内容
		String resultDate = Util.SendGet(Constant.HTTP + Constant.PIXIV + link);
		// 下载图片区域信息取得
		Matcher matcher = Util.RegexString(Util.SendGet(Constant.HTTP + Constant.PIXIV + link), "original-image");
		String pictureArea = Util.getString(matcher);
		if (pictureArea != null) {
			// 获得单张图片
			 Util.getPictureLink(resultDate, fullSavePath);
		} else {
			// 获得图片集
			 Util.getPictureListLink(resultDate, fullSavePath);
		}

	}

}
