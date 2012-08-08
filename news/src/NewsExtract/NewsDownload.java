package NewsExtract;
import java.io.*;

import org.apache.http.client.fluent.*;
import org.jsoup.*;
import org.apache.http.*;

import java.net.*;
public class NewsDownload {
	
	public static String getUrl(String link) {
		try {
			String base = "http://www.soso.com/interface/yulan.q?load=1&url="+link;//URLEncoder.encode(link, "UTF-8");
			return base;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNews(String link) {
		try {
			return Request.Get(getUrl(link)).viaProxy(new HttpHost("127.0.0.1", 8087)).execute().returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String genContent(String doc) {
		return Jsoup.parse(doc).select("body").text();
	}
	
	public static void getDetail(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Dnews\\"+y+"-"+m+".csv");
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				String date = tmp[2];
				System.out.println(date+" "+title);
				x = br.readLine();
				
				String news = genContent(getNews(link)).replaceAll(",", "£¬");
				Thread.sleep(2400);
				fw.write(link+","+title+","+date+","+news+"\n");
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void check(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews2\\"+y+"-"+m+".csv");
			//FileWriter fw = new FileWriter("D:\\Projects\\Data\\Dnews2\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			//System.out.println(x.split(",")[3]);
			while (x != null) {
				String tmp[] = x.split(",");
				try {
					if (tmp[3].length() < 20) {
						//tmp[3] = genContent(getNews(tmp[0])).replaceAll(",", "£¬");
						//fw.write(tmp[0]+","+tmp[1]+","+tmp[2]+","+tmp[3]+"\n");
						System.out.println(tmp[0]+":"+tmp[3]);
					} else {
						//fw.write(tmp[0]+","+tmp[1]+","+tmp[2]+","+tmp[3]+"\n");
					}
				}catch (Exception e) {
					System.out.println(tmp[0]+":"+tmp[1]);
					//String doc = genContent(getNews(tmp[0])).replaceAll(",", "£¬");
					//fw.write(tmp[0]+","+tmp[1]+","+tmp[2]+","+doc+"\n");
				}
				x = br.readLine();
				
			}
			//fw.close();
			fr.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) {
		
		for (int i = 1; i <= 6; i++)
			check(2012, i);
	}

}
