package logic;

import java.util.List;
import java.util.regex.Matcher;
import utils.Util;

public class GetCompanyData {
	/**
	 * 获得该公司下商品信息链接集合
	 * 
	 * @param companyList
	 */
	public List<String> doMain(String company) {
		System.out.println("正在获取链接列表,请稍等......");
		// 定义即将访问的链接
		String companyName = company;
		System.out.println("目标地址:" + companyName);
		// 访问链接并获取页面内容
		String result = Util.SendGet(companyName);
		// 图片区域信息List取得
		Matcher matcher = Util.RegexString(result, "(<section id=).*?(</section>)");
		List<String> pictureAreaD = Util.getListString(matcher);
		// 返回图片信息
		return pictureAreaD;
	}

}
