package NewsExtract;
import java.io.*;
import java.util.*;
public class ReasonExtract {
	
	public ArrayList<String> dates;
	public ArrayList<String> news;
	
	public ReasonExtract(int y, int m) {
		dates = new ArrayList<String>();
		news = new ArrayList<String>();
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Reason\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			int cnt = 0;
			int tot = 0;
			while (x != null) {
				tot++;
				cnt++;
				String[] tmps = x.split(",");
				dates.add(tmps[2]);
				news.add(tmps[3]);
				String a1 = "无";
				String a2 = a1;
				String a3 = a1;
				String a4 = a1;
				String a5 = a1;
				String a6 = a1;
				String a7 = a1;
				String a8 = a1;
				String a9 = a1;
				if (ReasonUtil.getInstance().hasAlcohol(tmps[3]))
					a1 = "酒精";
				if (ReasonUtil.getInstance().hasOverload(tmps[3]))
					a2 = "超载";
				if (ReasonUtil.getInstance().hasRoad(tmps[3]))
					a3 = "路面";
				if (ReasonUtil.getInstance().hasSpeed(tmps[3]))
					a4 = "超速";
				if (ReasonUtil.getInstance().hasThrottle(tmps[3]))
					a5 = "油门";
				if (ReasonUtil.getInstance().hasTireBomb(tmps[3]))
					a6 = "爆胎";
				if (ReasonUtil.getInstance().hasTired(tmps[3]))
					a7 = "疲劳";
				if (ReasonUtil.getInstance().hasVisible(tmps[3]))
					a8 = "可视度";
				if (ReasonUtil.getInstance().hasTurnover(tmps[3]))
					a9 = "侧翻";
				fw.write(tmps[0]+","+tmps[1]+","+a1+","+a2+","+a3+","+a4+","+a5+","+a6+","+a7+","+a8+","+a9+"\n");
				if (a1.equals("无") && a2.equals("无") && a3.equals("无") 
					&& a4.equals("无") && a5.equals("无") && a6.equals("无") 
					&& a7.equals("无") && a8.equals("无") && a9.equals("无"))
					cnt--;
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
				ReasonExtract es = new ReasonExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				ReasonExtract es = new ReasonExtract(i, j);
			}
		}

	}

}
