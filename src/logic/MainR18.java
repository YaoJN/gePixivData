package logic;

import java.util.List;

import constant.Constant;
import utils.Util;

public class MainR18 {
	// 下载路径
	private String savePath = Constant.SAVE_PATH_R18;

	/**
	 * R18主程
	 * 
	 * @param companyList
	 * @throws InterruptedException
	 */
	public void doMainR18() throws InterruptedException {
		// 下载日期取得
		List<String> dateList = Util.getDateList();
		// 根据公司下载内容
		for (String date : dateList) {
			// 查看当前的日期是否下载过
			if (!Util.readFromFile(date,Constant.SAVE_R18_FILE)) {
				Util.downCompany(Constant.R18_DATE_URL + date, savePath);
				System.out.println("R18==========日期" + date + "的内容下载结束,正在写入文件.==========");
				// 将下载的日期保存到文件中.
				Util.writeLinkToFile(date, Constant.SAVE_R18_FILE);
			}
		}
	}
}
