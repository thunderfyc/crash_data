package NewsExtract;
import java.io.*;
import org.apache.http.client.fluent.*;
import org.apache.http.*;
import java.util.*;
public class AirportSet {
	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("dict\\airport");
			BufferedReader br = new BufferedReader(fr);
			HashSet<String> code = new HashSet<String>();
			String x = br.readLine();
			
			
			while (x != null) {
				//System.out.println(x);
				if (x.charAt(0) == '1') {
					String[] tmp = x.split(";");
					if (tmp[1].length() > 4 && tmp[1].charAt(4) == ' ')
						tmp[1] = tmp[1].substring(0,4);
					if (!code.contains(tmp[1])) {
						code.add(tmp[1]);
						String co = Request.Get("http://www.wunderground.com/history/airport/"+tmp[1]+"/2008/1/1/DailyHistory.html")
						.execute().returnContent().asString();
						if (co.contains("Not Found"))
							System.out.println(tmp[1]);
					}
				}
				x = br.readLine();
			}
			br.close();
			fr.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
