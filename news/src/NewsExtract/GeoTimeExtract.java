package NewsExtract;
import java.io.*;
import java.util.*;
public class GeoTimeExtract {
	
	public ArrayList<String> dates;
	public ArrayList<String> news;
	
	public GeoTimeExtract(int y, int m) {
		dates = new ArrayList<String>();
		news = new ArrayList<String>();
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\GeoTime\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				dates.add(tmps[2]);
				news.add(tmps[3]);
				String city = GeoUtil.getInstance().findCity(tmps[1], tmps[3]);
				
				Date date = DateUtil.getInstance().findDate(tmps[3], tmps[2]);
				int time = TimeUtil.getInstance().findTime(tmps[3]);
				fw.write(tmps[0]+","+tmps[1]+","+(date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate()+","+time+","+city+"\n");
				x = br.readLine();
			}
			br.close();
			fr.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				GeoTimeExtract es = new GeoTimeExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				GeoTimeExtract es = new GeoTimeExtract(i, j);
			}
		}
	}

}
