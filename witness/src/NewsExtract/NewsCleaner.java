package NewsExtract;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class NewsCleaner {
	
	private  HashSet<String> link_set;
	private  HashSet<String> title_set;
	private  HashSet<String> symbols;
	private  HashSet<String> numbers;
	private  HashMap<String, Integer> dict;
	private ArrayList<String> vocab;
	private ArrayList<String> dates;
	public void init() {
		symbols.add("!");symbols.add("！");
		symbols.add(".");symbols.add("。");
		symbols.add("?");symbols.add("？");
		symbols.add("=");symbols.add("+");
		symbols.add(",");symbols.add("，");
		symbols.add(";");symbols.add("；");
		symbols.add(":");symbols.add("：");
		symbols.add("\"");symbols.add("“");symbols.add("”");
		symbols.add("'");symbols.add("‘");symbols.add("’");
		symbols.add("-");symbols.add("—");
		symbols.add("%");symbols.add("∶");
		symbols.add("#");//symbols.add("");
		symbols.add("$");symbols.add("￥");
		symbols.add("&");//symbols.add("");
		symbols.add("*");//symbols.add("");
		symbols.add("(");symbols.add("（");
		symbols.add(")");symbols.add("）");
		symbols.add("|");symbols.add("/");
		symbols.add("[");symbols.add("【");
		symbols.add("]");symbols.add("】");
		symbols.add("\\");symbols.add("、");
		numbers.add("0");numbers.add("零");numbers.add("０");
		numbers.add("1");numbers.add("一");numbers.add("１");
		numbers.add("2");numbers.add("二");numbers.add("２");
		numbers.add("3");numbers.add("三");numbers.add("３");
		numbers.add("4");numbers.add("四");numbers.add("４");
		numbers.add("5");numbers.add("五");numbers.add("５");
		numbers.add("6");numbers.add("六");numbers.add("６");
		numbers.add("7");numbers.add("七");numbers.add("７");
		numbers.add("8");numbers.add("八");numbers.add("８");
		numbers.add("9");numbers.add("九");numbers.add("９");
	}
	
	public NewsCleaner() {
		link_set = new HashSet<String>();
		title_set = new HashSet<String>();
		symbols = new HashSet<String>();
		numbers = new HashSet<String>();
		dict = new HashMap<String, Integer>();
		vocab = new ArrayList<String>();
		dates = new ArrayList<String>();
		this.init();
	}
	
	protected void addtoDict(String title) {
		String ts = title;
		int t = title.indexOf("图");
		if (t > 0) {
			if (t == title.length()-1) {
				ts = ts.substring(0, t);
			} else if (symbols.contains(ts.charAt(t-1)) && symbols.contains(ts.charAt(t+1))) {
				ts = ts.substring(0, t-1) + ts.substring(t+2);
			} else if (symbols.contains(ts.charAt(t+1))) {
				ts = ts.substring(0, t) + ts.substring(t+2);
			}
		}
		
		for (int i = 0; i < ts.length(); i++) {
			String c = ts.substring(i,i+1); 
			if (symbols.contains(c) || numbers.contains(c) || c.matches("[a-zA-Z]") || !Character.isLetter(ts.charAt(i)))
				continue;
			if (dict.containsKey(ts.substring(i, i+1))) {
				Integer tmp = dict.get(ts.substring(i,i+1)); 
				dict.put(ts.substring(i,i+1), new Integer(tmp+1));
			} else {
				dict.put(ts.substring(i,i+1), new Integer(1));
			}
		}
	}
	
	
	public boolean isLinkOK(String link) {
		if (link.indexOf("_")>0)
			return false;
		if (link.indexOf("htm") <0)
			return false;
		if (link.indexOf("http://news.qq.com") < 0)
			return false;
		if (link.indexOf("http://news.qq.com/b") >= 0)
			return false;
		String tmp;
		if (link.indexOf("?") >= 0) 
			tmp = link.substring(0, link.indexOf("?"));
		else
			tmp = link;
		if (link_set.contains(tmp))
			return false;
		else {
			link_set.add(tmp);
			return true;
		}
	}
	public boolean isTitleOK(String title) {
		if (title.indexOf("视频") == 0 || title.indexOf("组图") == 0 || title.indexOf("英国") >= 0 || title.indexOf("美国") >= 0)
			return false;
		if (title.indexOf("国际") >= 0)
			return false;
		if (title_set.contains(title))
			return false;
		else {
			title_set.add(title);
			return true;
		}
	}
	
	protected double calcDist(String a, String b) {
		double ans = 0;
		for (int i = 0; i < b.length(); i++) {
			boolean isin = a.indexOf(b.charAt(i)) >= 0;
 			if (isin) {
 				if (dict.containsKey(b.substring(i,i+1))) {
 					ans += Math.exp(-(new Double(dict.get(b.substring(i,i+1))))/8);
 				}
 			}
		}
		//System.out.println(a+" - " + b + " : " + ans);
		return ans;
	}
	
	public void check(int y, int m, boolean deep) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews2\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				if (tmp[1].indexOf("_") > 0)
					title = title.substring(0, tmp[1].indexOf("_"));
				String date = tmp[2];
				if (!deep) {
					if (isLinkOK(link) && isTitleOK(title)) {
						addtoDict(title);
						fw.write(link+","+title+","+date+"\n");
					}
				} else {
					boolean isEq = false;
					for (String sd: vocab) {
						double ans = calcDist(sd, title);
						if (ans > 0.002) {
							isEq = true;
							System.out.println(sd+" - " + title+ " : " + ans);
							break;
						}
					}
					if (!isEq)
						fw.write(link+","+title+","+date+"\n");
					vocab.add(title);
				}
					
				
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public String trimDec(String title) {
		String t = title;
		int delta = 0;
		t = t.replaceAll("\\(", "（");
		t = t.replaceAll("\\)", "）");
		t = t.replaceAll("\\[", "【");
		t = t.replaceAll("\\]", "】");
		t = t.replaceAll(":", "：");
		t = t.replaceAll("∶", "：");
		t = t.replaceAll("（图）", "");
		t = t.replaceAll("【图】", "");
		t = t.replaceAll("（组图）", "");
		t = t.replaceAll("【组图】", "");
		t = t.replaceAll("（视频）", "");
		t = t.replaceAll("【视频】", "");
		t = t.replaceAll("【图文】", "");
		t = t.replaceAll("【附图】", "");
		t = t.replaceAll("图片故事：", "");
		t = t.replaceAll("图片讲述", "");
		t = t.replaceAll("图文：", "");
		t = t.replaceAll("高清图：", "");
		t = t.replaceAll("图：", "");
		return t;
	}
	
	public void check2(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				String date = tmp[2];
				boolean isOK = true;
				if (title.indexOf("骗") >= 0 || title.indexOf("续") >= 0 || title.indexOf("追踪") >=0 || title.indexOf("人物") >=0
						|| title.indexOf("打人") >=0 || title.indexOf("感动") >=0 ||title.indexOf("讹") >=0  || title.indexOf("戴安娜") >=0 || title.indexOf("戴妃") >=0
						|| title.indexOf("印度") >=0 || title.indexOf("日本") >=0 || title.indexOf("南非") >=0  || title.indexOf("苏醒") >=0 || title.indexOf("残疾") >=0
						|| title.indexOf("回放") >=0 ||  title.indexOf("华人") >=0  || title.indexOf("华裔") >=0
						||  title.indexOf("在美") >=0 ||  title.indexOf("赴美") >=0 ) {
					isOK = false;
				} else if (link.indexOf("/a/") < 0) {
					isOK = false;
				}
				if (isOK == false) {
					x = br.readLine();
					continue;
				}
				
				title = trimDec(title);
				
				for (int z = 0; z < vocab.size(); z++) {
					String v = vocab.get(z);
					String d = dates.get(z);
					int cnt = 0;
					int len = 0;
					int len2 = 0;
					for (int i = 0; i < title.length(); i++) {
						if (title.charAt(i) != '死' && title.charAt(i) != '伤' && title.charAt(i) != '车' 
							&& title.charAt(i) != '祸' && title.charAt(i) != '发' && title.charAt(i) != '生' &&  
							symbols.contains(title.substring(i,i+1))==false
									&&numbers.contains(title.substring(i,i+1))==false) {
							if (v.indexOf(title.charAt(i)) >= 0)
								cnt++;
							len++;
						}
					}
					for (int i = 0; i < v.length(); i++) {
						if (v.charAt(i) != '死' && v.charAt(i) != '伤' && v.charAt(i) != '车' 
								&& v.charAt(i) != '祸' &&symbols.contains(v.substring(i,i+1))==false
								&& v.charAt(i) != '发' && v.charAt(i) != '生'   
								&&numbers.contains(v.substring(i,i+1))==false) {
							len2++;
						}
					}
					if (cnt >= len / 2 && cnt >= len2 / 2 && date.equals(d)) {
						isOK = false;
						//System.out.println(date+":"+title + " - " + d+":"+v);
						break;
					}
				}
				if (isOK) {
					vocab.add(title);
					dates.add(date);
					fw.write(link+","+title+","+date+"\n");
				}
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void check3(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				title = title.replace("&quot;", "");
				String date = tmp[2];
				boolean isOK = true;
				if (title.indexOf("非洲") >= 0 || title.indexOf("尼日利亚") >= 0
					//|| title.indexOf("风采") >= 0 || title.indexOf("道德") >= 0
					//|| title.indexOf("困扰") >= 0 || title.indexOf("灾区") >= 0
					//|| title.indexOf("理解") >= 0 || title.indexOf("维权") >= 0
					//|| title.indexOf("变动") >= 0 || title.indexOf("走访") >= 0
					//|| title.indexOf("专访") >= 0 || title.indexOf("法国") >= 0 || title.indexOf("蒋介石") >= 0 
					//|| title.indexOf("德国") >= 0 || title.indexOf("意大利") >= 0 || title.indexOf("俄罗斯") >= 0
					//|| title.indexOf("马来西亚") >= 0 || title.indexOf("印尼") >= 0 || title.indexOf("泰国") >= 0
					//|| title.indexOf("越南") >= 0 || title.indexOf("柬埔寨") >= 0 || title.indexOf("菲律宾") >= 0 || title.indexOf("老挝") >= 0
					//|| title.indexOf("墨西哥") >= 0 || title.indexOf("加拿大") >= 0 || title.indexOf("高雄") >= 0 
					//|| title.indexOf("阿根廷") >= 0 || title.indexOf("澳大利亚") >= 0 || title.indexOf("巴西") >= 0 
					//|| title.indexOf("智利") >= 0 || title.indexOf("巴基斯坦") >= 0 || title.indexOf("新西兰") >= 0 
					//|| title.indexOf("北美") >= 0 || title.indexOf("沙特") >= 0 || title.indexOf("阿联酋") >= 0 || title.indexOf("暴风") >= 0 || title.indexOf("洪水") >= 0
					//|| title.indexOf("医患") >= 0 || title.indexOf("打理") >= 0 || title.indexOf("尼泊尔") >= 0 || title.indexOf("津巴布韦") >= 0
					//|| title.indexOf("南非") >= 0 || title.indexOf("文明") >= 0 || title.indexOf("乌克兰") >= 0 || title.indexOf("秘鲁") >= 0
					//|| (title.indexOf("安") >= 0&&title.indexOf("妃")>=0) || title.indexOf("危地马拉") >= 0
					//|| title.indexOf("阿富汗") >= 0 || title.indexOf("涨") >= 0 || title.indexOf("榜") >= 0
					//|| title.indexOf("开盘") >= 0 || (title.indexOf("股") >= 0&&title.indexOf("屁股")<0)
					//|| title.indexOf("移植") >= 0|| title.indexOf("艾滋") >= 0|| title.indexOf("音乐") >= 0
					//|| title.indexOf("亚洲") >= 0|| title.indexOf("爆料") >= 0|| title.indexOf("暴雪") >= 0
					//|| title.indexOf("潜规则") >= 0|| title.indexOf("预警") >= 0|| title.indexOf("警报") >= 0
					//|| title.indexOf("中毒") >= 0|| title.indexOf("法治") >= 0|| title.indexOf("灾民") >= 0
					//|| title.indexOf("十大") >= 0|| title.indexOf("希拉里") >= 0|| title.indexOf("奥巴马") >= 0
					//|| title.indexOf("事迹") >= 0|| title.indexOf("壮举") >= 0|| title.indexOf("善举") >= 0
					//|| title.indexOf("理赔") >= 0|| title.indexOf("奉献") >= 0|| title.indexOf("波兰") >= 0
					//|| title.indexOf("焦点") >= 0|| title.indexOf("杀死") >= 0|| title.indexOf("伪造") >= 0
					//|| title.indexOf("骚扰") >= 0|| title.indexOf("葬礼") >= 0|| title.indexOf("作案") >= 0
					//|| title.indexOf("诋毁") >= 0|| title.indexOf("免费") >= 0|| title.indexOf("解答") >= 0
					//|| title.indexOf("爱心") >= 0|| title.indexOf("西班牙") >= 0|| title.indexOf("芬兰") >= 0
					//|| title.indexOf("委内瑞拉") >= 0|| title.indexOf("哥伦比亚") >= 0|| title.indexOf("埃及") >= 0
					//|| title.indexOf("伦敦") >= 0|| title.indexOf("咨询") >= 0|| title.indexOf("考试") >= 0
					//|| title.indexOf("敲诈") >= 0|| title.indexOf("召开")  0|| title.indexOf("芬兰") >= 0
					//	|| title.indexOf("保加利亚") >= 0|| title.indexOf("鲜血") >= 0|| title.indexOf("伊朗") >= 0
					//	|| title.indexOf("绑架") >= 0|| title.indexOf("斯坦") >= 0|| title.indexOf("的故事") >= 0
					//	|| title.indexOf("索要") >= 0|| title.indexOf("预交") >= 0|| title.indexOf("错过") >= 0
					//	|| title.indexOf("抚养") >= 0|| title.indexOf("爱情") >= 0
					//	|| title.indexOf("历史") >= 0|| title.indexOf("金牌") >= 0|| title.indexOf("熊宁") >= 0
					//	|| title.indexOf("我是谁") >= 0|| title.indexOf("温情") >= 0|| title.indexOf("萨科") >= 0
						|| title.indexOf("自杀") >= 0|| title.indexOf("提示") >= 0|| title.indexOf("提醒") >= 0
						|| title.indexOf("盗") >= 0|| title.indexOf("离婚") >= 0|| title.indexOf("美军") >= 0
						|| title.indexOf("新加坡") >= 0|| title.indexOf("离婚") >= 0|| title.indexOf("美军") >= 0
						|| title.indexOf("跳楼") >= 0|| title.indexOf("赴台") >= 0|| title.indexOf("溺") >= 0
						|| title.indexOf("殴") >= 0|| title.indexOf("赴台") >= 0|| title.indexOf("溺") >= 0
						|| title.indexOf("假象") >= 0|| title.indexOf("规定") >= 0|| title.indexOf("老虎") >= 0
						|| title.indexOf("票") >= 0|| title.indexOf("交流") >= 0|| title.indexOf("火化") >= 0
						|| title.indexOf("孟加拉") >= 0|| title.indexOf("虎") >= 0|| title.indexOf("殡葬") >= 0
						|| title.indexOf("专家") >= 0|| title.indexOf("暴打") >= 0
						){
					isOK = false;
					System.out.println(title);
				}
				if (title.indexOf("客车") <0 && title.indexOf("货车") <0 && title.indexOf("车祸") < 0 && title.indexOf("驾") < 0&& title.indexOf("肇事") < 0 && title.indexOf("撞") <0) {
					if (title.indexOf("精神") >= 0 || title.indexOf("代表") >= 0 || title.indexOf("关注") >= 0
						|| title.indexOf("救治") >= 0 || title.indexOf("主席") >= 0 || title.indexOf("陌生") >= 0
						|| title.indexOf("贼") >= 0|| title.indexOf("抢劫") >= 0 || title.indexOf("献血") >= 0
						|| title.indexOf("地震") >= 0|| title.indexOf("救灾") >= 0|| title.indexOf("英雄") >= 0
							) {
						isOK = false;
						System.out.println(title);
					}
				}
				if (isOK == false) {
					x = br.readLine();
					continue;
				}
				fw.write(link+","+title+","+date+"\n");
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String args[]) {
		NewsCleaner nc = new NewsCleaner();
		for (int y = 2010; y <= 2011; y++) {
			for (int m = 1; m <= 12; m++) {
				nc.check3(y, m);
			}
		}
	}
}
