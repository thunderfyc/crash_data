package NewsExtract;
import java.io.*;
import java.util.*;
public class SimpleCount {
	
	private static HashSet<String> link_set = new HashSet<String>();
	private static HashSet<String> title_set = new HashSet<String>();
	
	
	public static boolean isLinkOK(String link) {
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
	public static boolean isTitleOK(String title) {
		if (title.indexOf("视频") == 0 || title.indexOf("组图") == 0)
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
	
	public static int cnt = 0;
	
	public static void check(int y, int m) {
		int v = 0;
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			//FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				String date = tmp[2];
				if (title.indexOf("上海") >= 0 || title.indexOf("申")>=0 || title.indexOf("沪") >= 0) {
					//System.out.println(date+" "+title);
					cnt++;
				}
				v++;
				x = br.readLine();
			}
			fr.close();
			System.out.println(y+"-"+m+":  "+v);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String args[]) {
		 check(2007, 1);
		 check(2007, 2);
		 check(2007, 3);
		 check(2007, 4);
		 check(2007, 5);
		 check(2007, 6);
		 check(2007, 7);
		 check(2007, 8);
		 check(2007, 9);
		 check(2007, 10);
		 check(2007, 11);
		 check(2007, 12);
		 check(2008, 1);
		 check(2008, 2);
		 check(2008, 3);
		 check(2008, 4);
		 check(2008, 5);
		 check(2008, 6);
		 check(2008, 7);
		 check(2008, 8);
		 check(2008, 9);
		 check(2008, 10);
		 check(2008, 11);
		 check(2008, 12);
		 check(2009, 1);
		 check(2009, 2);
		 check(2009, 3);
		 check(2009, 4);
		 check(2009, 5);
		 check(2009, 6);
		 check(2009, 7);
		 check(2009, 8);
		 check(2009, 9);
		 check(2009, 10);
		 check(2009, 11);
		 check(2009, 12);
		 check(2010, 1);
		 check(2010, 2);
		 check(2010, 3);
		 check(2010, 4);
		 check(2010, 5);
		 check(2010, 6);
		 check(2010, 7);
		 check(2010, 8);
		 check(2010, 9);
		 check(2010, 10);
		 check(2010, 11);
		 check(2010, 12);
		 check(2011, 1);
		 check(2011, 2);
		 check(2011, 3);
		 check(2011, 4);
		 check(2011, 5);
		 check(2011, 6);
		 check(2011, 7);
		 check(2011, 8);
		 check(2011, 9);
		 check(2011, 10);
		 check(2011, 11);
		 check(2011, 12);
		 check(2012, 1);
		 check(2012, 2);
		 check(2012, 3);
		 check(2012, 4);
		 check(2012, 5);
		 check(2012, 6);		
		System.out.println(cnt);
	}

}
