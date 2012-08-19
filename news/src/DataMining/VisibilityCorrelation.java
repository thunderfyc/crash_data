package DataMining;

import java.io.*;
import java.util.*;
import NewsExtract.*;

public class VisibilityCorrelation {
	int cnt;
	HashMap<Integer, Double> vis;
	HashMap<Integer, Double> rain;
	HashMap<Integer, Double> temp;
	HashMap<String, String> citi2air;
	HashMap<String, Integer> loccnt;
	HashMap<String, ArrayList<Integer>> recRain;
	HashMap<String, ArrayList<Integer>> recVis;
	HashMap<String, ArrayList<Integer>> recTemp;
	HashSet<String> airport;
	
	

	public VisibilityCorrelation() {
		vis = new HashMap<Integer, Double>();
		rain = new HashMap<Integer, Double>();
		temp = new HashMap<Integer, Double>();
		airport = new HashSet<String>();
		loccnt = new HashMap<String, Integer>();
		citi2air = new HashMap<String, String>();
		recRain = new HashMap<String, ArrayList<Integer>>();
		recVis = new HashMap<String, ArrayList<Integer>>();
		recTemp = new HashMap<String, ArrayList<Integer>>();
		cnt = 0;
	}
	
	public int vis2int(String ans) {
		if (ans.equals("-") || ans.equals("!") || ans.equals("null")) {
			return -1;
		} else {
			double tmp = Double.parseDouble(ans);
			if (tmp <= 1) {
				return 0;
			} else if (tmp <= 4) {
				return 1;
			} else if (tmp <= 10) {
				return 2;
			} else if (tmp <= 20) {
				return 3;
			} else {
				return 4;
			}
		}
	}
	public int rain2int(String ans) {
		if (ans.equals("-") || ans.equals("!") || ans.equals("null")) {
			return -1;
		} else {
			double tmp = Double.parseDouble(ans);
			if (tmp <= 1) {
				return 0;
			} else if (tmp <= 10) {
				return 1;
			} else if (tmp <= 25) {
				return 2;
			} else if (tmp <= 50) {
				return 3;
			} else if (tmp <= 100) {
				return 4;
			} else {
				return 5;
			}
		}
	}
	public int temp2int(String ans) {
		if (ans.equals("-") || ans.equals("!") || ans.equals("null")) {
			return -1;
		} else {
			double tmp = Double.parseDouble(ans);
			if (tmp <= 0) {
				return 0;
			} else if (tmp <= 10) {
				return 1;
			} else if (tmp <= 20) {
				return 2;
			} else if (tmp <= 30) {
				return 3;
			} else {
				return 4;
			}
		}
	}
	
	public void getAirRecord(String code) {
		try {
			FileWriter fw= new FileWriter("D:\\vis_record.csv", true);
			String c = code;
			for (int i = 2008; i <= 2012; i++) {
				int jmax = 12;
				if (i == 2012)
					jmax = 6;
				for (int j = 1; j <= jmax; j++) {
					int kmax = 31;
					if (j == 4 || j == 6 || j == 9 || j == 11)
						kmax = 30;
					else if (j == 2) {
						if (i == 2008 || i == 2012)
							kmax = 29;
						else 
							kmax= 28;
					}
					for (int k = 1; k <= kmax; k++) {
						try {
							String ans = WeatherUtil.getInstance().getWeather(c, i+"-"+j+"-"+k);
							if (ans == null)
								continue;
							String[] tmps = ans.split(",");
							int v = vis2int(tmps[3]);
							if (v >= 0)
								recVis.get(c).set(v, recVis.get(c).get(v)+1);
							v = rain2int(tmps[1]);
							if (v >= 0)
								recRain.get(c).set(v, recRain.get(c).get(v)+1);
							v = temp2int(tmps[0]);
							if (v >= 0)
								recTemp.get(c).set(v, recTemp.get(c).get(v)+1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
			System.out.println(c+": "+recVis.get(c));
			System.out.println(c+": "+recTemp.get(c));
			System.out.println(c+": "+recRain.get(c));
			fw.write(c+","+recVis.get(c).get(0)+","+recVis.get(c).get(1)+","+recVis.get(c).get(2)+","
					+recVis.get(c).get(3)+","+recVis.get(c).get(4)+"\n");
			fw.write(c+","+recTemp.get(c).get(0)+","+recTemp.get(c).get(1)+","+recTemp.get(c).get(2)+","
					+recTemp.get(c).get(3)+","+recTemp.get(c).get(4)+"\n");
			fw.write(c+","+recRain.get(c).get(0)+","+recRain.get(c).get(1)+","+recRain.get(c).get(2)+","
					+recRain.get(c).get(3)+","+recRain.get(c).get(4)+"\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getRecord() {
		try {
			FileReader fr = new FileReader("D:\\vis_record.csv");
			BufferedReader br = new BufferedReader(fr);
			recVis = new HashMap<String, ArrayList<Integer>>();
			recTemp = new HashMap<String, ArrayList<Integer>>();
			recRain = new HashMap<String, ArrayList<Integer>>();
			
			String x = br.readLine();
			while (x != null) {
				String[] tmp = x.split(",");
				recVis.put(tmp[0], new ArrayList<Integer>());
				for (int i = 1; i < tmp.length; i++)
					recVis.get(tmp[0]).add(Integer.parseInt(tmp[i]));
				x = br.readLine();
				
				tmp = x.split(",");
				recTemp.put(tmp[0], new ArrayList<Integer>());
				int tot = 0;
				for (int i = 1; i < tmp.length; i++) {
					recTemp.get(tmp[0]).add(Integer.parseInt(tmp[i]));
					tot += Integer.parseInt(tmp[i]);
				}
				x = br.readLine();
				
				tmp = x.split(",");
				recRain.put(tmp[0], new ArrayList<Integer>());
				int rt = 0;
				for (int i = 1; i < tmp.length; i++) {
					recRain.get(tmp[0]).add(Integer.parseInt(tmp[i]));
					rt += Integer.parseInt(tmp[i]);
				}
				
				recRain.get(tmp[0]).add(tot-rt);
				x = br.readLine();
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void getAllAirport() {
		try {
			FileReader fr = new FileReader("dict\\airport");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				if (x.charAt(0) == '0') {
					
				} else if (x.charAt(0) == '1') {
					String[] tmps = x.split(";");
					citi2air.put(tmps[0].substring(1), tmps[1]);
					airport.add(tmps[1]);
				}
				x = br.readLine();
			}
			
			br.close();
			fr.close();
			//System.out.println(airport);
			for (String c: airport) {
				recVis.put(c, new ArrayList<Integer>());
				for (int i = 0; i < 5; i++) {
					recVis.get(c).add(0);
				}
				recRain.put(c, new ArrayList<Integer>());
				for (int i = 0; i < 6; i++) {
					recRain.get(c).add(0);
				}
				recTemp.put(c, new ArrayList<Integer>());
				for (int i = 0; i < 5; i++) {
					recTemp.get(c).add(0);
				}
				//getAirRecord(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void countLoc(int i, int j) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\GeoTime\\"+i+"-"+j+".csv");
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String code = null;
				String[] tmp2 = x.split(",");
				if (tmp2[4] != null && !tmp2[4].equals("null")) {
					if (citi2air.containsKey(tmp2[4])) {
						code = citi2air.get(tmp2[4]);
						if (loccnt.containsKey(code)) {
							loccnt.put(code, loccnt.get(code)+1);
						} else {
							loccnt.put(code, 1);
						}
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
	
	
	
	public void countVis(int i, int j) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Weather\\"+i+"-"+j+".csv");
			FileReader fr2 = new FileReader("D:\\Projects\\Data\\GeoTime\\"+i+"-"+j+".csv");
			BufferedReader br = new BufferedReader(fr);
			BufferedReader br2 = new BufferedReader(fr2);
			
			String x = br.readLine();
			String x2 = br2.readLine();
			while (x != null) {
				
				String code = null;
				String[] tmp2 = x2.split(",");
				if (tmp2[4] != null && !tmp2[4].equals("null")) {
					if (citi2air.containsKey(tmp2[4])) {
						code = citi2air.get(tmp2[4]);
					}
				}
				if (code == null) {
					x = br.readLine();
					x2 = br2.readLine();
					continue;
				}
				
				cnt++;
				String[] tmps = x.split(",");
				if (tmps.length < 6) {
					x = br.readLine();
					x2 = br2.readLine();
					continue;
				}
				Integer k = 0;
				k = vis2int(tmps[5]);
				if (vis.containsKey(k)) {
					vis.put(k, vis.get(k)+1);
				} else {
					vis.put(k, 1.);
				}
				k = temp2int(tmps[2]);
				if (temp.containsKey(k)) {
					temp.put(k, temp.get(k)+1);
				} else {
					temp.put(k, 1.);
				}
				k = rain2int(tmps[3]);
				if (rain.containsKey(k)) {
					rain.put(k, rain.get(k)+1);
				} else {
					rain.put(k, 1.);
				}
				
				
				
				x = br.readLine();
				x2 = br2.readLine();
			}
			br.close();
			fr.close();
			br2.close();
			fr2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		VisibilityCorrelation ins = new VisibilityCorrelation();
		ins.getAllAirport();
		ins.getRecord();
		
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				ins.countLoc(i, j);
			}
		}
		for (int j = 1; j <= 6; j++)
			ins.countLoc(2012, j);
		
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				ins.countVis(i, j);
			}
		}
		for (int j = 1; j <= 6; j++)
			ins.countVis(2012, j);
		
		
		//
		System.out.println(ins.vis);
		System.out.println(ins.rain);
		System.out.println(ins.temp);
	}
}
