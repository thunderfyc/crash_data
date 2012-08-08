package NewsExtract;
import java.io.*;
import java.util.*;
public class PeopleExtract {
	
	public ArrayList<String> dates;
	public ArrayList<String> news;
	
	public PeopleExtract(int y, int m) {
		dates = new ArrayList<String>();
		news = new ArrayList<String>();
		int tot = 0;
		int cnt = 0;
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\People\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				tot++;
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
				if (PeopleUtil.getInstance().hasBureau(tmps[3]))
					a1 = "官员";
				if (PeopleUtil.getInstance().hasRich(tmps[3]))
					a2 = "富人";
				if (PeopleUtil.getInstance().hasGreen(tmps[3]))
					a3 = "新手";
				if (PeopleUtil.getInstance().hasChild(tmps[3]))
					a4 = "儿童";
				if (PeopleUtil.getInstance().hasLabor(tmps[3]))
					a5 = "工人";
				//if (PeopleUtil.getInstance().hasRural(tmps[3]))
					//a5 = "小路";
	/*			if (RoadUtil.getInstance().hasTireBomb(tmps[3]))
					a6 = "爆胎";
				if (RoadUtil.getInstance().hasTired(tmps[3]))
					a7 = "疲劳";
				if (RoadUtil.getInstance().hasVisible(tmps[3]))
					a8 = "可视度";*/
				//Date date = DateUtil.getInstance().findDate(tmps[3], tmps[2]);
				//int time = TimeUtil.getInstance().findTime(tmps[3]);
				fw.write(tmps[0]+","+tmps[1]+","+a1+","+a2+","+a3+","+a4+"\n");
				cnt++;
				if (a1.equals(a8) && a2.equals(a8) && a3.equals(a8) && a4.equals(a8) && a5.equals(a8))
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
				PeopleExtract es = new PeopleExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				PeopleExtract es = new PeopleExtract(i, j);
			}
		}
	}

}
