package NewsExtract;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.*;
import java.util.*;
import java.io.*;
public class WeatherUtil {
	private static WeatherUtil _instance = null;
	private HashMap<String, String> airport;
	protected WeatherUtil() {
		try {
			FileReader fr = new FileReader("dict\\airport");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			airport = new HashMap<String, String> ();
			while (x != null) {
				if (x.charAt(0) == '1') {
					String[] tmp = x.split(";");
					tmp[0] = tmp[0].substring(1);
					if (tmp[0].length() > 4)
						tmp[0] = tmp[0].substring(0, 4);
					airport.put(tmp[0], tmp[1]);
				}
				x = br.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static WeatherUtil getInstance() {
		if (_instance == null) {
			_instance = new WeatherUtil();
		}
		return _instance;
	}
	
	private String getData(Elements t) {
		for (Element x: t) {
			if (x.tagName().equals("tr") && x.childNodes().size() > 3) {
				try {
					return x.child(1).child(0).child(0).text();
				} catch (Exception e) {
					return "-";
				}
			}
		}
		return null;
	}
	
	public String getWeather(String location, String date) {
		String tmp = location;
		if (location.length() > 4)
			tmp = location.substring(0, 4);
		if (!airport.containsKey(tmp))
			return null;
		String airCode = airport.get(tmp);
		if (airCode.length() > 4)
			airCode = airCode.substring(0, 4);
		String[] dates = date.split("-");
		try {
			Document doc = null;
			try {
				doc = Jsoup.connect("http://www.wunderground.com/history/airport/"+airCode+"/"+dates[0]+"/"+dates[1]+"/"+dates[2]+"/DailyHistory.html").get();
			} catch (Exception e) {
				Thread.sleep(5000);
				try {
					if (airCode.equals("ZSHC")) {
					
						doc = Jsoup.connect("http://www.wunderground.com/history/airport/"+"ZSSS"+"/"+dates[0]+"/"+dates[1]+"/"+dates[2]+"/DailyHistory.html").get();
					}
					else
						doc = Jsoup.connect("http://www.wunderground.com/history/airport/"+airCode+"/"+dates[0]+"/"+dates[1]+"/"+dates[2]+"/DailyHistory.html").get();
				} catch (Exception e2) {
					System.out.println(location+":  "+date);
				}
			}
			if (doc == null)
				return "!,!,!,!,!";
			Elements t1 = doc.getElementById("historyTable").getElementsContainingText("Mean Temperature");
			Elements t2 = doc.getElementById("historyTable").getElementsContainingText("Precipitation");
			Elements t3 = doc.getElementById("historyTable").getElementsContainingText("Wind Speed");
			Elements t4 = doc.getElementById("historyTable").getElementsContainingText("Visibility");
			Elements t5 = doc.getElementById("historyTable").getElementsContainingText("Events");
			return getData(t1)+","+getData(t2)+","+getData(t3)+","+getData(t4)+","+getData(t5); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-,-,-,-,-";		
	}
	
	public static void main(String[] args) {
		System.out.println(WeatherUtil.getInstance().getWeather("º¼ÖÝ", "2011-11-28"));
	}
	
}
