package NewsExtract;
import java.util.*;


public class TimeUtil extends DateUtil {

	private HashMap<String, Integer> time_set = null;
	private static TimeUtil _instance;
	private TimeUtil() {
		super();
		time_set = new HashMap<String, Integer>();
		time_set.put("��", 0);time_set.put("�賿", 4);time_set.put("�峿", 0);time_set.put("�糿",0);
		time_set.put("����",0);time_set.put("����",1);time_set.put("����",1);time_set.put("����",1);
		time_set.put("����",2);time_set.put("ҹ",3);time_set.put("ҹ��",3);time_set.put("ҹ��",3);
		time_set.put("����",0);time_set.put("���",1);time_set.put("��ҹ",4);time_set.put("��ҹ",4);
		time_set.put("�ϰ�",0);time_set.put("�°�",2);time_set.put("��",4);time_set.put("ͨ��",4);
	}
	public static TimeUtil getInstance() {
		if (_instance == null) {
			_instance = new TimeUtil();
		}
		return _instance;
	}
	
	public int normalizeTime(int hour, int per) {
		int t = hour;
		if (per == 1) {
			if (t <= 7) {
				t += 12;
			}
		} else if (per == 2 || per == 3) {
			if (t <= 12) {
				t+= 12;
			}
		} else if (per == 4) {
			if (t >=7 && t < 12) {
				t+= 12;
			} else if (t==12) {
				t = 0;
			}
		}
		return t;
	}
	
	public int findTime(String x) {
		// 0: 7am-10am; 1: 10am - 4pm; 2: 4pm-7pm; 3: 7pm-22pm; 4: 22pm-7am;
		ArrayList<Integer> time = new ArrayList<Integer>();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		ArrayList<Integer> cnt = new ArrayList<Integer>();
		/**********Find All candidates******************/
		for (int i = 2; i < x.length() / 2; i++) {
			if (time_set.containsKey(x.substring(i,i+2))) {
				String ngram = x.substring(i+2, i+7);
				if (!ngram.contains("��Ѷ��")) {
					time.add(time_set.get(x.substring(i,i+2)));
					pos.add(i);
					cnt.add(1);
				}
			} else if (time_set.containsKey(x.substring(i,i+1)) &&
					!time_set.containsKey(x.substring(i-1,i+1)) &&
					!time_set.containsKey(x.substring(i,i+2))) {
				String ngram = x.substring(i+1, i+6);
				if (!ngram.contains("��") && !ngram.contains("Ѷ") && !ngram.contains("��")) {
					time.add(time_set.get(x.substring(i,i+1)));
					pos.add(i);
					cnt.add(1);
				}
			} else if (x.charAt(i) == 'ʱ' || x.charAt(i) == '��') {
				int t = -1;
				if (isDigit(x.substring(i-1, i))) {
					if (isDigit(x.substring(i-2, i))) {
						t = Integer.parseInt(numCn2Ar(x.substring(i-2,i)));
					} else {
						t = Integer.parseInt(numCn2Ar(x.substring(i-1,i)));
					}
				} 
				if (t == -1)
					continue;
				if (time_set.containsKey(x.substring(i-2,i))) {
					t = normalizeTime(t, time_set.get(x.substring(i-2,i)));
				} else if (time_set.containsKey(x.substring(i-1,i))) {
					t = normalizeTime(t, time_set.get(x.substring(i-1,i)));
				}
				if (t >= 7 && t <= 10) {
					time.add(0);
				} else if (t <= 16 && t > 10) {
					time.add(1);
				} else if (t <= 19 && t > 16) {
					time.add(2);
				} else if (t <=22 && t > 19) {
					time.add(3);
				} else {
					time.add(4);
				}
				pos.add(i);
				cnt.add(1);
			}
		}
		/**********Find best fitted one******************/
		for (int i = 0; i < time.size()-1; i++) {
			int j = i+1;
			while (j < time.size()) {
				if (time.get(i).equals(time.get(j))) {
					cnt.set(i, cnt.get(i)+1);
					if (pos.get(j) < pos.get(i)) {
						pos.set(i, pos.get(j));
					}
					cnt.remove(j);
					pos.remove(j);
					time.remove(j);
				} else {
					j++;
				}
			}
		}
		if (cnt.size() == 0)
			return -1;
		int mos = cnt.get(0);
		int im = 0;
		int fir = pos.get(0);
		for (int i = 0; i < time.size()-1; i++) {
			if (cnt.get(i) > mos) {
				im = i;
				mos = cnt.get(i);
			} else if (cnt.get(i) == mos) {
				if (pos.get(i) < fir) {
					im = i;
					fir = pos.get(i);
				}
			}
		}
		return time.get(im);
	}
	

	public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,�����������˴����μݳ�ײ������ �������ŵ���,2007-1-8,�����������˴����μݳ�ײ������?�������ŵ��� 2007-01-08 08:53 �й������� �������������������س������������ľ���羰�������ߴ�Լ2�������һ���жιٴ�Ĵ�ׯ���˴�����˼ҽ�Ϊ����?�����Ϻ��ᡣԪ���������ڸô���Ӵ�ڷ�����һ��ͨ�¹ʣ����״����������ƽ������Ϊ�������Ǹ����˴����Ρ�����һ���Ӿ���羰�������ķ���ԽҰ��ͻȻ�ܵ���·���ײ��һ��Ħ�е����Ӻ���¶�������ʻ��30���ײ�ͣ���������¹ʵ��±�ײ���ӵ���������Ŀǰ�����н�����������չ�����顣? �����˴����μݳ�ײ���� ����Ԫ��������20ʱ���ιٴ�40�����μ�����ʻһ����ɫ����Ħ�г��Ӵ��Ӳ�·�ڳ�����׼����������һ��˽�˹����ϰ࣬��ʻ�Ϲ�·Լ����100����Զ���ͺ�һ���Ӿ��緽��ʻ���ķ��·�ԡ�ԽҰ����ײ��֮�������������µ�·�ұߵĶ��֮��ԽҰ���ڶ�����ɱ���30����Զ���������������ͣ�������ӵ������������ؽ������5��������������ҽԺһ�����ȳ�Ѹ�ٸϵ��ֳ��󣬾�֤ʵĦ�г���ʻ�Ŷμ����Ѿ���������������н���֧���й��쵼ҲѸ�ٸϵ��ֳ�չ�����顣�ιٴ�Ⱥ�ڳƣ��������������ص�һ���߹١� �����������ģ�?�ܺ��˵������� �����������磬������������120���������˽⵽��6��3����20ʱ20�֣������Ľӵ��ιٴ徳����ͨ�¹�����һ�˵ļ��ȵ绰�󣬼��ȳ�Ѹ?����20ʱ23�ֳ���������5����֮��ϵ��ֳ����Ρ�������Ա�ֳ�������֪���򳵻����˵��������жμ���������40�꣬�Ƕιٴ�����Ĵ��񡣡��������������ˣ�������û����ҽԺ�����ȣ�һ��Ů������Ա˵�� ����120�����¼��ʾ���������޺���������������������ʧ��˫��ͫ�׷Ŵ󣬶Թⷴ����ʧ����ǻ����ǻ����ѪҺ����������֫���ȡ�С�Ȼ��Ρ����Թؽڻ��������������������ֵ���ҡ��� ����Ⱥ�ڳ��Ǿƺ����� ��������϶��Ǿƺ����£�Ҫ��Ȼ��ײ���˺������ﻹ�Ὺ��30����Զ�����˽������������Ǿ�����ˣ�ƽʱ��Ⱦƣ�����ÿ�ٷ����벻���ơ���һ����Ը͸¶�����Ĵ���˵�����ιٴ����67��Ķ�ĳ���ʾ������26���ʻ��������������һ����͵ľƺ����¡��������ȾͲ�����Լ�Ӧ��������ʻ���ҳ��������󳵵�����Ϊ�����ս�ͨ���ŵĹ涨���ұߵĳ���������Ħ�г���������ʻ·������Ҫ���Ǿƺ��ʻ������ײ��������Ͳ෭�ڹ����ˣ���ô�����ڶ����￪����ôԶ����ô���������·�ǰ�Ƿ�ȹ����أ��������磬���������ؽ������ӣ��ڴ��ſڣ�����ӳ������˼��ߵļ򵥲ɷã�����ʾ���ڽ�ͨ�¹ʷ������������н���֧��һ����֧�ӳ����¹ʴ�����쵼�ͼ�ʱ�ϵ��ֳ����п��죬��Ŀǰ��ص�������û�г������Ƿ�Ⱦƣ�Ҫ�������յľƾ��������Ŀǰ�������½��ۣ� �����˴������������Ϣ ����Ϊ�ҵ������������˽���������������Ⱥ����ε����������˴󣬿�����ʱ��˫���գ�����Ҳ�޷��ҵ��й���Ա������������ί��ֵ?����Աȡ������ϵ������ȴ����֪�������������ε��ܻ����뻻�ˣ�������������Ҳû�а취���� �����������磬���������˴�칫¥��¥���ݿڣ�������������һ��������Ա����˵���¹ʷ��������������ξ�����һ�����ڵļٻؼ���Ϣ�ˡ�������Ҳ��֪�����ĵ绰�͵�ַ����?��������14ʱ30�֣������跨��ͨ���������˴�ί��칫�Ҹ�����������ֻ�������ȴ�ԡ�Ҫ�ɷô��±���ͨ��������ͬ�⡱Ϊ�ɼ��Ծܾ������߷��������Լ��Ѿ��ҹ����������Է���Ȼ���ɷֱ�͹��˵绰�� ������Դ�������±������ߣ��Ĺ���?������";
		System.out.println(TimeUtil.getInstance().findTime(str));
	}
	
}
