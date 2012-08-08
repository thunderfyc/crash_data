package NewsExtract;
import java.io.*;
import java.util.*;
public class CharTransform {
	public static void Transform(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<String> doc = new ArrayList<String>();
			String x = br.readLine();
			while (x != null) {
				x = new String(x.getBytes("UTF-8"), "UTF-8");
				doc.add(x);
				x = br.readLine();
			}
			br.close();
			fr.close();
			
			FileWriter fw = new FileWriter(filename);
			for (String tmp: doc) {
				fw.write(tmp+"\n");
				
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++)
				Transform("D:\\Projects\\Data\\Dnews_F\\"+i+"-"+j+".csv");
		}
		
	}

}
