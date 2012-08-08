package NewsExtract;
import java.io.*;
import java.util.*;
public class SeverityExtract {
	
	public ArrayList<String> dates;
	public ArrayList<String> news;
	
	public SeverityExtract(int y, int m) {
		dates = new ArrayList<String>();
		news = new ArrayList<String>();
		int cnt = 0;
		int tot = 0;
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Severity\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				tot++;
				String[] tmps = x.split(",");
				dates.add(tmps[2]);
				news.add(tmps[3]);
				int ans = SeverityUtil.getInstance().findSeverity(tmps[3]);
				String a = "普通";
				if (ans == 1)
					a = "轻伤";
				else if (ans == 2)
					a = "重伤";
				else if (ans == 3)
					a = "死亡";
				else if (ans == 4)
					a = "特大";
				if (ans > 0)
					cnt++;
				fw.write(tmps[0]+","+tmps[1]+","+a+"\n");
				x = br.readLine();
			}
			br.close();
			fr.close();
			fw.close();
			System.out.println(y+"-"+m+": "+cnt+"/"+tot);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				SeverityExtract es = new SeverityExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				SeverityExtract es = new SeverityExtract(i, j);
			}
		}
	}

}
