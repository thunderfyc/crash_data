package DataMining;
import java.io.*;
import java.text.*;
import java.util.*;
public class CompareWithGov {
	
	HashMap<String, Integer> geoDeath;
	HashMap<String, Integer> geoAcc;
	HashMap<String, Integer> reaDeath;
	HashMap<String, Integer> reaAcc;
	HashMap<String, String> city;
	HashMap<String, Integer> newsDeath;
	HashMap<String, Integer> newsAcc;
	HashMap<String, Integer> reason;
	int accTot;
	
	public void readinFiles() {
		try {
			FileReader fr = new FileReader("data\\08sta.csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				geoAcc.put(tmps[0], Integer.parseInt(tmps[1]));
				geoDeath.put(tmps[0], Integer.parseInt(tmps[2]));
				x = br.readLine();
			}
			br.close();
			fr.close();
			
			fr = new FileReader("data\\09sta.csv");
			br = new BufferedReader(fr);
			x = br.readLine();
			x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				geoAcc.put(tmps[0], (geoAcc.get(tmps[0])+Integer.parseInt(tmps[1]))>>1);
				geoDeath.put(tmps[0], (geoDeath.get(tmps[0])+Integer.parseInt(tmps[2]))>>1);
				x = br.readLine();
			}
			br.close();
			fr.close();
			
			fr = new FileReader("data\\08reason.csv");
			br = new BufferedReader(fr);
			x = br.readLine();
			x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				reaAcc.put(tmps[0], Integer.parseInt(tmps[1]));
				reaDeath.put(tmps[0], Integer.parseInt(tmps[2]));
				x = br.readLine();
			}
			br.close();
			fr.close();
			
			fr = new FileReader("data\\09reason.csv");
			br = new BufferedReader(fr);
			x = br.readLine();
			x = br.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				try {
				reaAcc.put(tmps[0], (reaAcc.get(tmps[0])+Integer.parseInt(tmps[1]))>>1);
				} catch (Exception e) {
					System.out.println(tmps[0]);
				}
				reaDeath.put(tmps[0], (reaDeath.get(tmps[0])+Integer.parseInt(tmps[2]))>>1);
				x = br.readLine();
			}
			br.close();
			fr.close();
			
			fr = new FileReader("dict\\geo");
			br = new BufferedReader(fr);
			city = new HashMap<String, String>();
			x = br.readLine();
			String cur = null;
			while (x != null) {
				if (x.charAt(0) == '0') {
					cur = x.substring(1);
				} else if (x.charAt(0) == '1') {
					city.put(x.substring(1), cur);
				}
				x = br.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void countProvince(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\GeoTime\\"+y+"-"+m+".csv");
			FileReader dr = new FileReader("D:\\Projects\\Data\\Severity\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			BufferedReader cr = new BufferedReader(dr);
			String x = br.readLine();
			String z = cr.readLine();
			while (x != null) {
				String[] tmps = x.split(",");
				if (city.containsKey(tmps[4])) {
					String prov = city.get(tmps[4]);
					if (newsAcc.containsKey(prov)) {
						newsAcc.put(prov, newsAcc.get(prov)+1);
					} else {
						newsAcc.put(prov, 1);
					}
					String[] tmp2 = z.split(",");
					int delta = 0;
					if (tmp2[2].equals("死亡")) {
						delta = 1;
					} else if (tmp2[2].equals("特大")) {
						delta = 1;
					}
					if (newsDeath.containsKey(prov)) {
						newsDeath.put(prov, newsDeath.get(prov)+delta);
					} else {
						newsDeath.put(prov, delta);
					}
				}
				x = br.readLine();
				z = cr.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CompareWithGov() {
		geoDeath = new HashMap<String, Integer>();
		geoAcc = new HashMap<String, Integer>();
		reaDeath = new HashMap<String, Integer>();
		reaAcc = new HashMap<String, Integer>();
		newsDeath = new HashMap<String, Integer>();
		newsAcc = new HashMap<String, Integer>();
		reason = new HashMap<String, Integer>();
		accTot = 0;
		readinFiles();
		//System.out.println(geoDeath);
	}
	public String shorten(double t) {
		return new DecimalFormat("0.000").format(t);
	}

	public void showDiffer() {
		int tot = geoAcc.get("合计");
		int tot2 = 0;
		int dtot = geoDeath.get("合计");
		int dtot2 = 0;
		for (String k: newsAcc.keySet()) {
			tot2 += newsAcc.get(k);
			dtot2 += newsDeath.get(k);
		}
		for (String k: newsAcc.keySet()) {
			double c1 = (double)(geoAcc.get(k))/tot;
			double c2 = (double)(newsAcc.get(k))/tot2;
			System.out.println(k + ": "+shorten((c1-c2)/c1));
		}
		System.out.println("-------------------------------\n");
		for (String k: newsAcc.keySet()) {
			double c1 = (double)(geoDeath.get(k))/dtot;
			double c2 = (double)(newsDeath.get(k))/dtot2;
			System.out.println(k + ": "+shorten((c1-c2)/c1));
		}
		System.out.println("---------------------------------");
		System.out.println(shorten((double)dtot/tot) + "   " + shorten((double)dtot2 / tot2));
	}
	
	public void countReason(int i, int j) {
		try {
			
			FileReader fr = new FileReader("D:\\Projects\\Data\\Reason\\"+i+"-"+j+".csv");
			BufferedReader br = new BufferedReader(fr);
			
			String x = br.readLine();
			
			while (x != null) {
				String[] tmps = x.split(",");
				accTot++;
				for (int k = 2; k < tmps.length; k++) {
					if (reason.containsKey(tmps[k])) {
						reason.put(tmps[k], reason.get(tmps[k])+1);
					} else {
						reason.put(tmps[k], 1);
					}
				}
				
				x = br.readLine();
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
		}
	}
	
	public void reasonDiffer() {
		System.out.println(reason);
		System.out.println(accTot);
		for (String k: reason.keySet()) {
			System.out.println(k+": "+(double)(reason.get(k))/accTot);
		}
	}
	
	public static void main(String[] args) {
		CompareWithGov ins = new CompareWithGov();
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				ins.countProvince(i, j);
				ins.countReason(i, j);
			}
		}
		for (int j = 1; j <= 6; j++) {
			ins.countProvince(2012, j);
			ins.countReason(2012, j);
		}
		//System.out.println(ins.newsDeath);
		//System.out.println(ins.newsAcc);
		ins.reasonDiffer();
	}
	
}
