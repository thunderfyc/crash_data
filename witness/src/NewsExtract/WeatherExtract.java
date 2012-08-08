package NewsExtract;
import java.util.*;
import java.io.*;
public class WeatherExtract {
	public WeatherExtract(int y, int m) {
		try {
			System.out.println(y+"-"+m);
			FileReader fr = new FileReader("D:\\Projects\\Data\\GeoTime\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Weather\\"+y+"-"+m+".csv");
			String x = br.readLine();
			while (x != null) {
				String[] tmp = x.split(",");
				String date = tmp[2];
				String loc = tmp[4];
				String ans = "";
				if (loc != null)
					ans = WeatherUtil.getInstance().getWeather(loc, date);
				fw.write(tmp[0]+","+tmp[1]+","+ans+"\n");
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
				WeatherExtract es = new WeatherExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				WeatherExtract es = new WeatherExtract(i, j);
			}
		}
	}
}
