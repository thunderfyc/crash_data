package NewsExtract;
import java.io.*;
import java.text.*;
import java.util.*;

public class DateUtil {
	private static DateUtil _instance = null;
	private HashSet<String> digit_set;
	private HashSet<String> time_set;
	
	public static DateUtil getInstance() {
		if (_instance == null) {
			_instance = new DateUtil();
		}
		return _instance;
	}
	protected DateUtil() {
		digit_set = new HashSet<String>();
		try {
			FileReader fr = new FileReader("dict\\digit");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
		
			while (x != null) {
				digit_set.add(x);
				x = br.readLine();
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isDigit(String x) {
		for (int i = 0; i < x.length(); i++) {
			if (!digit_set.contains(x.substring(i,i+1))) {
				return false;
			}
		}
		return true;
	}
	
	public String numCn2Ar(String str) {
		String s = str;
		s = s.replaceAll("��ʮһ", "31");s = s.replaceAll("��ʮ��", "32");s = s.replaceAll("��ʮһ", "21");
		s = s.replaceAll("��ʮ��", "22");s = s.replaceAll("��ʮ��", "23");s = s.replaceAll("��ʮ��", "24");
		s = s.replaceAll("��ʮ��", "25");s = s.replaceAll("��ʮ��", "26");s = s.replaceAll("��ʮ��", "27");
		s = s.replaceAll("��ʮ��", "28");s = s.replaceAll("��ʮ��", "29");s = s.replaceAll("ʮһ", "11");
		s = s.replaceAll("ʮ��", "12");s = s.replaceAll("ʮ��", "13");s = s.replaceAll("ʮ��", "14");
		s = s.replaceAll("ʮ��", "15");s = s.replaceAll("ʮ��", "16");s = s.replaceAll("ʮ��", "17");
		s = s.replaceAll("ʮ��", "18");s = s.replaceAll("ʮ��", "19");s = s.replaceAll("��ʮ", "20");
		s = s.replaceAll("��ʮ", "30");s = s.replaceAll("ʮ", "10");	s = s.replaceAll("һ", "1");
		s = s.replaceAll("��", "2");	s = s.replaceAll("��", "3");	s = s.replaceAll("��", "4");
		s = s.replaceAll("��", "5");	s = s.replaceAll("��", "6");	s = s.replaceAll("��", "7");
		s = s.replaceAll("��", "8");	s = s.replaceAll("��", "9");s = s.replaceAll("Ԫ", "1");
		s = s.replaceAll("��", "1");
		if (s.equals("��"))
			s = "100";
		return s;
	}
	
	public Date getDate(String str) {
		String s = "";
		for (int i = 0; i < str.length(); i++)
			if (!Character.isWhitespace(str.charAt(i)))
				s += str.charAt(i);
		s = numCn2Ar(s);
		SimpleDateFormat str2date = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return str2date.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Date findDate(String x,String report_day) {
		ArrayList<Date> can = new ArrayList<Date>();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		Date today = getDate(report_day); 
		can.add(today);
		pos.add(1000);
		
		/******************* Find all possible candidates********************/
		String[] tmp_report = report_day.split("-");
		for (int i = 1; i < x.length()/2; i++) {
			if (x.charAt(i) == '��') {
				int ri = x.substring(i+1, i+5).indexOf('��');
				if (isDigit(x.substring(i-1,i)) && ri > 0 && isDigit(x.substring(i+1, i+1+ri))) {
					if (isDigit(x.substring(i-2,i-1))) {
						tmp_report[1] = x.substring(i-2,i);
					} else {
						tmp_report[1] = x.substring(i-1,i);
					}
					tmp_report[2] = x.substring(i+1,i+1+ri);
					can.add(getDate(tmp_report[0]+"-"+tmp_report[1]+"-"+tmp_report[2]));
					pos.add(i);
				}
			} else if (x.charAt(i) == '��') {
				int start = 0;
				if (i > 10)
					start = i-10;
				if (x.substring(start, i).indexOf('��') < 0) {
					if (i > 1 && isDigit(x.substring(i-2,i))) {
						tmp_report[2] = x.substring(i-2, i);
						can.add(getDate(tmp_report[0]+"-"+tmp_report[1]+"-"+tmp_report[2]));
						pos.add(i);
					} else if (i > 0 && isDigit(x.substring(i-1, i))) {
						tmp_report[2] = x.substring(i-1, i);
						if (!tmp_report[2].equals("��")) {
							can.add(getDate(tmp_report[0]+"-"+tmp_report[1]+"-"+tmp_report[2]));
							pos.add(i);
						}
					}
				}
			} else if (x.charAt(i) == '��') {
				can.add(new Date(today.getTime()-24*3600*1000));
				pos.add(i);
			} else if (x.charAt(i) == '��') {
				String ngram = x.substring(i+1, i+4);
				if (ngram.indexOf('��') < 0 ||ngram.indexOf('Ѷ') < 0 ||ngram.indexOf('��') < 0 ) {
					can.add(today);
					pos.add(i);
				}
			} else if (x.charAt(i) == 'ǰ') {
				if (x.charAt(i+1) == '��' || x.charAt(i+1) == '��' || x.charAt(i+1) == '��') {
					can.add(new Date(today.getTime()-48*3600*1000));
					pos.add(i);
				}
			}
		}
		/****************Find the best fitted one************************/
		ArrayList<Integer> cnt = new ArrayList<Integer>();
		for (int i = 0; i < can.size(); i++) {
			cnt.add(1);
		}
		for (int i = 0; i < can.size()-1; i++) {
			int j = i+1;
			while (j < can.size()) {
				if (can.get(i).equals(can.get(j))) {
					if (pos.get(i) > pos.get(j)) {
						pos.set(i, pos.get(j));
					}
					cnt.set(i, cnt.get(i)+cnt.get(j));
					can.remove(j);
					pos.remove(j);
					cnt.remove(j);
				} else {
					j++;
				}
			}
		}
		
		Date ear = can.get(0); int ie = 0;
		Integer mos = cnt.get(0); int im = 0;
		Integer fir = pos.get(0); int ii = 0;
		
		for (int i = 1; i < can.size(); i++) {
			if (can.get(i).before(ear)) {
				ear = can.get(i);
				ie = i;
			}
			if (pos.get(i) < fir) {
				fir = pos.get(i);
				ii = i;
			}
			if (cnt.get(i) > mos) {
				mos = cnt.get(i);
				im = i;
			}
		}
		return ear;
	}
	public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,�����������˴����μݳ�ײ������ �������ŵ���,2007-1-8,�����������˴����μݳ�ײ������?�������ŵ��� 2007-01-08 08:53 �й������� �������������������س������������ľ���羰�������ߴ�Լ2�������һ���жιٴ�Ĵ�ׯ���˴�����˼ҽ�Ϊ����?�����Ϻ��ᡣԪ���������ڸô���Ӵ�ڷ�����һ��ͨ�¹ʣ����״����������ƽ������Ϊ�������Ǹ����˴����Ρ�����һ���Ӿ���羰�������ķ���ԽҰ��ͻȻ�ܵ���·���ײ��һ��Ħ�е����Ӻ���¶�������ʻ��30���ײ�ͣ���������¹ʵ��±�ײ���ӵ���������Ŀǰ�����н�����������չ�����顣? �����˴����μݳ�ײ���� ����Ԫ��������20ʱ���ιٴ�40�����μ�����ʻһ����ɫ����Ħ�г��Ӵ��Ӳ�·�ڳ�����׼����������һ��˽�˹����ϰ࣬��ʻ�Ϲ�·Լ����100����Զ���ͺ�һ���Ӿ��緽��ʻ���ķ��·�ԡ�ԽҰ����ײ��֮�������������µ�·�ұߵĶ��֮��ԽҰ���ڶ�����ɱ���30����Զ���������������ͣ�������ӵ������������ؽ������5��������������ҽԺһ�����ȳ�Ѹ�ٸϵ��ֳ��󣬾�֤ʵĦ�г���ʻ�Ŷμ����Ѿ���������������н���֧���й��쵼ҲѸ�ٸϵ��ֳ�չ�����顣�ιٴ�Ⱥ�ڳƣ��������������ص�һ���߹١� �����������ģ�?�ܺ��˵������� �����������磬������������120���������˽⵽��6��3����20ʱ20�֣������Ľӵ��ιٴ徳����ͨ�¹�����һ�˵ļ��ȵ绰�󣬼��ȳ�Ѹ?����20ʱ23�ֳ���������5����֮��ϵ��ֳ����Ρ�������Ա�ֳ�������֪���򳵻����˵��������жμ���������40�꣬�Ƕιٴ�����Ĵ��񡣡��������������ˣ�������û����ҽԺ�����ȣ�һ��Ů������Ա˵�� ����120�����¼��ʾ���������޺���������������������ʧ��˫��ͫ�׷Ŵ󣬶Թⷴ����ʧ����ǻ����ǻ����ѪҺ����������֫���ȡ�С�Ȼ��Ρ����Թؽڻ��������������������ֵ���ҡ��� ����Ⱥ�ڳ��Ǿƺ����� ��������϶��Ǿƺ����£�Ҫ��Ȼ��ײ���˺������ﻹ�Ὺ��30����Զ�����˽������������Ǿ�����ˣ�ƽʱ��Ⱦƣ�����ÿ�ٷ����벻���ơ���һ����Ը͸¶�����Ĵ���˵�����ιٴ����67��Ķ�ĳ���ʾ������26���ʻ��������������һ����͵ľƺ����¡��������ȾͲ�����Լ�Ӧ��������ʻ���ҳ��������󳵵�����Ϊ�����ս�ͨ���ŵĹ涨���ұߵĳ���������Ħ�г���������ʻ·������Ҫ���Ǿƺ��ʻ������ײ��������Ͳ෭�ڹ����ˣ���ô�����ڶ����￪����ôԶ����ô���������·�ǰ�Ƿ�ȹ����أ��������磬���������ؽ������ӣ��ڴ��ſڣ�����ӳ������˼��ߵļ򵥲ɷã�����ʾ���ڽ�ͨ�¹ʷ������������н���֧��һ����֧�ӳ����¹ʴ�����쵼�ͼ�ʱ�ϵ��ֳ����п��죬��Ŀǰ��ص�������û�г������Ƿ�Ⱦƣ�Ҫ�������յľƾ��������Ŀǰ�������½��ۣ� �����˴������������Ϣ ����Ϊ�ҵ������������˽���������������Ⱥ����ε����������˴󣬿�����ʱ��˫���գ�����Ҳ�޷��ҵ��й���Ա������������ί��ֵ?����Աȡ������ϵ������ȴ����֪�������������ε��ܻ����뻻�ˣ�������������Ҳû�а취���� �����������磬���������˴�칫¥��¥���ݿڣ�������������һ��������Ա����˵���¹ʷ��������������ξ�����һ�����ڵļٻؼ���Ϣ�ˡ�������Ҳ��֪�����ĵ绰�͵�ַ����?��������14ʱ30�֣������跨��ͨ���������˴�ί��칫�Ҹ�����������ֻ�������ȴ�ԡ�Ҫ�ɷô��±���ͨ��������ͬ�⡱Ϊ�ɼ��Ծܾ������߷��������Լ��Ѿ��ҹ����������Է���Ȼ���ɷֱ�͹��˵绰�� ������Դ�������±������ߣ��Ĺ���?������";
		System.out.println(DateUtil.getInstance().findDate(str, "2007-1-8"));
	}

}
