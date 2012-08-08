package NewsExtract;
//http://api.map.baidu.com/?qt=s&wd=%E5%A6%82%E4%B8%9C
import java.io.*;
import java.util.*;
public class GeoUtil {
	private static GeoUtil _instance = null;
	
	public static GeoUtil getInstance() {
		if (_instance == null) {
			_instance = new GeoUtil();
		}
		return _instance;
	}
	
	public ArrayList<HashMap<String, String>> county;
	public HashMap<String, String> city;
	public ArrayList<String> province;
	
	protected GeoUtil() {
		province = new ArrayList<String>();
		city = new HashMap<String, String>();
		county = new ArrayList<HashMap<String, String>>();
		try {
			FileReader fr = new FileReader("dict\\geo");
			BufferedReader x = new BufferedReader(fr);
			String s = x.readLine();
			String current_province = null;
			String current_city = null;
			while (s != null) {
				if (s.charAt(0) == '0') {
					current_province = s.substring(1);
					province.add(current_province);
					county.add(new HashMap<String, String>());
				} else if (s.charAt(0) == '1') {
					String city_name = s.substring(1);
					if (city_name.charAt(city_name.length()-1) == '市') {
						if (city_name.length() > 2)
							city_name = city_name.substring(0, city_name.length()-1);
					}
					if (city_name.length() > 5) {
						city_name.substring(0, 5);
					}
					current_city = city_name;
					if (city.containsKey(city_name)) {
						System.out.println(city_name +":" + current_province);
					} else {
						city.put(city_name, current_province);
					}
				} else if (s.charAt(0) == '2') {
					
					String county_name = s.substring(1);
					Character lc =county_name.charAt(county_name.length()-1); 
					if (lc == '县' || lc == '市') {
						if (county_name.length() > 2) {
							county_name = county_name.substring(0, county_name.length()-1);
						}
					}
					if (county_name.length() > 5) {
						county_name.substring(0, 5);
					}
					HashMap<String, String> city_c = county.get(county.size()-1);
					if (!city_c.containsKey(county_name)) {
						city_c.put(county_name, current_city);
					}
				}
				s = x.readLine();
			}
			x.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkAfter(String ngram) {
		if (ngram.contains("人") || ngram.contains("孩") || ngram.contains("司")) {
			return false;
		} else
			return true;
	}
	private boolean checkPrev(String ngram) {
		if (!(ngram.contains("记")||ngram.contains("警")) &&
			(ngram.contains("自") || ngram.contains("从"))) {
			return false;
		} else
			return true;
	}
	private boolean enhanceAfter(String ngram) {
		if (ngram.contains("警")) {
			return true;
		} else 
			return false;
	}
	private boolean enhancePrev(String ngram) {
		if (ngram.contains("警") || ngram.contains("在") ||ngram.contains("于")) {
			return true;
		} else
			return false;
	}
	
	public String findCity(String title, String doc) {
		ArrayList<String> can = new ArrayList<String>();
		ArrayList<Integer> cnt = new ArrayList<Integer>();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for (int i = 5; i < doc.length()-20; i++) {
			for (int len = 2; len <= 5; len++) {
				String x = doc.substring(i, i+len);
				if (city.containsKey(x)) {
					String ngramAfter = doc.substring(i+len, i+len+5);
					String ngramPrev = doc.substring(i-5, i);
					if (checkAfter(ngramAfter) && checkPrev(ngramPrev)) {
						int tmp_cnt = 1;
						if (enhanceAfter(ngramAfter))
							tmp_cnt++;
						if (enhancePrev(ngramPrev))
							tmp_cnt++;
						can.add(x);
						cnt.add(tmp_cnt);
						pos.add(i);
					}
				} else {
					for (int k = 0; k < county.size(); k++) {
						if (county.get(k).containsKey(x)) {
							String ngramAfter = doc.substring(i+len, i+len+5);
							String ngramPrev = doc.substring(i-5, i);
							if (checkAfter(ngramAfter) && checkPrev(ngramPrev)) {
								int tmp_cnt = 1;
								if (enhanceAfter(ngramAfter))
									tmp_cnt++;
								if (enhancePrev(ngramPrev))
									tmp_cnt++;
								String tmp = county.get(k).get(x);
								can.add(tmp);
								cnt.add(tmp_cnt);
								pos.add(i);
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < can.size()-1; i++) {
			int j = i+1;
			while (j < can.size()) {
				if (can.get(i).equals(can.get(j))) {
					cnt.set(i, cnt.get(i)+cnt.get(j));
					if (pos.get(j) < pos.get(i))
						pos.set(i, pos.get(j));
					can.remove(j);
					cnt.remove(j);
					pos.remove(j);
				} else {
					j++;
				}
			}
		}
		if (can.size() == 0)
			return null;
		int mos = cnt.get(0);
		int im = 0;
		int fir = pos.get(0);
		for (int i = 1; i < can.size(); i++) {
			if (cnt.get(i) > mos) {
				mos = cnt.get(i);
				fir = pos.get(i);
				im = i;
			} else if (cnt.get(i) == mos) {
				if (pos.get(i) < fir) {
					fir = pos.get(i);
					im = i;
				}
			}
			
		}
		return can.get(im);
	}
	
	public String city2prov(String s) {
		if (city.containsKey(s)) {
			return city.get(s);
		} else
			return "没有";
	}

	public static void main(String[] args) {
		String doc = "http://news.qq.com/a/20070101/000401.htm,货车停路中引发两车祸 大巴追尾搅拌车,2007-1-1,货车停路中引发两车祸?大巴追尾搅拌车 2007-01-01 05:52 奥一网 　　奥一网讯一辆货车昨日上午在南沙港快线（南行）上抛锚停在路中，引发两起交通事故，导致一辆大巴追尾搅拌车，一辆轿车失控冲护栏，事故中轿车司机受伤，大巴车上３０多人受惊。 　　事发点距离七星岗出口约４公里。１１时半许，一辆交通拯救车正将一辆蓝色小货车拖离，还有一辆广州开往中山的大巴前挡风玻璃已经全碎了，路边停着一辆水泥搅拌车。? 　　搅拌车司机说，当时他发现前方路上停有蓝色小货车，于是刹车，没想到被大巴追尾。 　　就在交警处理这起追尾事故时，一辆轿车开至后方十来米处时，失控冲向护栏，司机受伤。 　　路政人员介绍，蓝色小货车的司机因违章停车受到了交警处罚。 　　编辑：洪海宁 　　（南方都市报）";
		System.out.println(GeoUtil.getInstance().findCity("出行注意安全?又是一个不小心货车撞破了氨水罐", doc));
	}
}
