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
					if (city_name.charAt(city_name.length()-1) == '��') {
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
					if (lc == '��' || lc == '��') {
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
		if (ngram.contains("��") || ngram.contains("��") || ngram.contains("˾")) {
			return false;
		} else
			return true;
	}
	private boolean checkPrev(String ngram) {
		if (!(ngram.contains("��")||ngram.contains("��")) &&
			(ngram.contains("��") || ngram.contains("��"))) {
			return false;
		} else
			return true;
	}
	private boolean enhanceAfter(String ngram) {
		if (ngram.contains("��")) {
			return true;
		} else 
			return false;
	}
	private boolean enhancePrev(String ngram) {
		if (ngram.contains("��") || ngram.contains("��") ||ngram.contains("��")) {
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
			return "û��";
	}

	public static void main(String[] args) {
		String doc = "http://news.qq.com/a/20070101/000401.htm,����ͣ·������������ ���׷β���賵,2007-1-1,����ͣ·������������?���׷β���賵 2007-01-01 05:52 ��һ�� ������һ��Ѷһ������������������ɳ�ۿ��ߣ����У�����êͣ��·�У���������ͨ�¹ʣ�����һ�����׷β���賵��һ���γ�ʧ�س廤�����¹��нγ�˾�����ˣ���ͳ��ϣ��������ܾ��� �����·���������Ǹڳ���Լ���������ʱ����һ����ͨ���ȳ�����һ����ɫС�������룬����һ�����ݿ�����ɽ�Ĵ��ǰ���粣���Ѿ�ȫ���ˣ�·��ͣ��һ��ˮ����賵��? �������賵˾��˵����ʱ������ǰ��·��ͣ����ɫС����������ɲ����û�뵽�����׷β�� �������ڽ�����������׷β�¹�ʱ��һ���γ�������ʮ���״�ʱ��ʧ�س�������˾�����ˡ� ����·����Ա���ܣ���ɫС������˾����Υ��ͣ���ܵ��˽��������� �����༭���麣�� �������Ϸ����б���";
		System.out.println(GeoUtil.getInstance().findCity("����ע�ⰲȫ?����һ����С�Ļ���ײ���˰�ˮ��", doc));
	}
}
