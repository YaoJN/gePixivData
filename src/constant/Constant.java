package constant;

public class Constant {

	// R18按日期下载部分
	public final static String R18_DATE_URL = "http://www.pixiv.net/ranking.php?mode=male_r18&date=";

	// R15按日期下载部分
	public final static String R15_DATE_URL = "http://www.pixiv.net/ranking.php?mode=male&date=";
	
	// 按作者下载左部分
	public final static String USER_URL_LEFT = "http://www.pixiv.net/member_illust.php?id=";
	
	// 按作者下载右部分
	public final static String USER_URL_RIGHT = "&type=all&p=";

	// 下载年
	public final static String DOWN_YEAR = "2017";

	// 下载月
	public final static String DOWN_MONTH = "04";

	// 下载开始日期(日)
	public final static String DOWN_START_DAY = "18";

	// 下载终了日期(日)
	public final static String DOWN_END_DAY = "19";

	// HTTP
	public final static String HTTP = "http://";

	// pixiv
	public final static String PIXIV = "www.pixiv.net/";

	// 下载地址
	public final static String SAVE_PATH_R18 = "E:\\壁纸\\pixiv\\" + DOWN_YEAR + "\\R18";

	// 下载地址
	public final static String SAVE_PATH_R15 = "E:\\壁纸\\pixiv\\" + DOWN_YEAR + "\\R15";
	
	// 下载地址
	public final static String SAVE_PATH_USER = "E:\\壁纸\\pixiv\\USER\\";

	// 下载记录地址
	public final static String SAVE_R18_FILE = SAVE_PATH_R18 + "\\SAVE.txt";

	// R15下载记录地址
	public final static String SAVE_R15_FILE = SAVE_PATH_R15 + "\\SAVE.txt";

	// 过滤的漫画标签
	public final static String KILL_TAGS = "腐向け,腐,ヴィク勇,BL松,唐唐,ケモノ,ケモショタ,りついず,ユーリオンアイス,漫画,男の子,ゲイ,ホモ,Gay";

	// 没有找到匹配内容.
	public final static String DATA_NOFIND = "没有找到匹配内容.";
}
