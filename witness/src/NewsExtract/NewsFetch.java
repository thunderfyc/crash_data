package NewsExtract;
import org.apache.http.client.fluent.*;
import org.apache.http.*;
import java.util.*;
import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

public class NewsFetch {
	
	public static String URLP = "http://www.soso.com/q?w=%B3%B5%BB%F6&site=news.qq.com&sd=5";
	public static Calendar DATEC = new GregorianCalendar(2012,05,01);
	public static int DATEV = 5265;
	
	private static long dayNum(int y, int m, int d) {
		Calendar c1 = new GregorianCalendar();
		c1.set(y, m-1, d, 0, 0, 0);
		return DATEV-1 + (c1.getTimeInMillis() - DATEC.getTimeInMillis())/(24*60*60*1000);
	}
	

	private static String getUrl(int y1, int m1, int y2, int m2) {
		if (m2 == 12) {
			m2 = 1;
			y2++;
		} else {
			m2++;
		}
		return URLP + "&min=" + dayNum(y1,m1,1) + "&max=" + (dayNum(y2,m2,1)-1);		
	}
	
	private static String getUrl(int y, int m, int d) {
		return URLP + "&min="+dayNum(y,m,d) + "&max="+dayNum(y,m,d);
	}
	private static String getUrl(int y) {
		int m = 12;
		int d = 31;
		if (y==2012) {
			m = 6;
			d = 30;
		}
		return URLP + "&min="+dayNum(y,1,1) + "&max="+dayNum(y,m,d);
	}
	
	public static String getPage(String url) {
		try {
			System.out.println(url);
			return Request.Get(url).viaProxy(new HttpHost("127.0.0.1", 8087)).execute().returnContent().asString();
			//System.out.println(Request.Get(url).execute().returnContent());
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}
	
	public static ArrayList<ArrayList<String>> getNews(String html) {
		ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>();
//		System.out.println("start to get news");
		html = html.replaceAll("<em>([^<]*)</em>", "$1");
		try {
			Document doc = Jsoup.parse(html);
			for (Node x: doc.getElementById("result").child(0).childNodes()) {
				//System.out.println(x.childNode(0).childNode(0).childNode(0).outerHtml());
				String link = x.childNode(0).childNode(0).childNode(0).attr("href");
				String title = x.childNode(0).childNode(0).childNode(0).childNode(0).toString();
				String date = x.childNode(0).childNode(2).childNode(0).childNode(0).toString().split("&nbsp;")[1].replace("</cite>", "");
				ans.add(new ArrayList<String>());
				ans.get(ans.size()-1).add(link);
				ans.get(ans.size()-1).add(title);
				ans.get(ans.size()-1).add(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	
	public static void getData(int y, int m, int d) {
		String url = getUrl(y,m,d);
		String doc;
		int pg = 1;
		do {
			doc = getPage(url+"&pg="+pg);
			ArrayList<ArrayList<String>> items = getNews(doc);
			try {
				FileWriter fw = new FileWriter("D:\\news\\"+y+"-"+m+".csv", true);
				for (ArrayList<String> t: items) {
					for(String x: t) {
						fw.write(x.replaceAll(",", "£¬")+",");
					}
					fw.write(y+"-"+m+"-"+d+"\n");
				}
				fw.close();
			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				Thread.sleep(1500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			pg++;
			//System.out.println(Jsoup.parse(doc).select(".next"));
		} while (!Jsoup.parse(doc).select(".next").isEmpty());	
	}
	
	public static void MonthOverView(int y, int m) {
		int d = 31;
		if (m == 4 || m == 6 || m == 9 || m == 11)
			d = 30;
		else if (y%4==0 && m == 2)
			d = 29;
		else if (m == 2)
			d = 28;
		for (int i = 1; i <= d; i++) {
			getData(y, m, i);
		}
	}
	
	public static void YearOverView(int y) {
		String url = getUrl(y);
		String doc;
		int pg = 1;
		do {
			doc = getPage(url+"&pg="+pg);
			ArrayList<ArrayList<String>> items = getNews(doc);
			try {
				FileWriter fw = new FileWriter("D:\\news\\"+y+".csv", true);
				for (ArrayList<String> t: items) {
					for(String x: t) {
						fw.write(x.replaceAll(",", "£¬")+",");
					}
					fw.write("\n");
				}
				fw.close();
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			pg++;
			//System.out.println(Jsoup.parse(doc).select(".next"));
		} while (!Jsoup.parse(doc).select(".next").isEmpty() || doc.indexOf("±§Ç¸£¬ÕÒ²»µ½") >= 0);
	}
	
	public static void main(String params[]) {
		YearOverView(2007);
		YearOverView(2008);
		YearOverView(2009);
		YearOverView(2010);
		YearOverView(2011);
		YearOverView(2012);
		
	}

}
