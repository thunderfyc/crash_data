package NewsExtract;
import java.io.*;
import java.text.*;
import java.util.*;

public class SeverityUtil {
	private static SeverityUtil _instance = null;
	
	public static SeverityUtil getInstance() {
		if (_instance == null) {
			_instance = new SeverityUtil();
		}
		return _instance;
	}
	protected SeverityUtil() {
	}
	
	public int findSeverity(String doc) {
		int ans = 0;
		for (int i = 3; i < doc.length() - 10; i++) {
			if (doc.charAt(i) == '��' || doc.charAt(i) == 'ɥ') {
				int d = 0;
				if (doc.charAt(i-1) == '��') {
					d = 1;
				}
				String t1 = doc.substring(i-1-d, i-d);
				String t2 = doc.substring(i-2-d, i-d);
				int s = 0;
				if (TimeUtil.getInstance().isDigit(t1)) {
					if (TimeUtil.getInstance().isDigit(t2)) {
						s = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t2));
					}
					s = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t1));
				}
				if (s > 1)
					ans = 4;
				if (s == 1 && ans < 3)
					ans = 3;
				String ngram = "";
				if (i >= 5)
					ngram = doc.substring(i-5,i);
				else
					ngram = doc.substring(0, i);
				if (!ngram.contains("��")) {
					if (ans < 3)
						ans = 3;
				}
			} else if (doc.charAt(i) == '��') {
				int d = 0;
				if (doc.charAt(i-1) == '��')
					d = 1;
				else if (doc.charAt(i-1) == '��' || doc.charAt(i-1) == '��' || doc.charAt(i-1) == '��') {
					if (doc.charAt(i-1) == '��') {
						d = 2;
					}
				}
				String t1 = doc.substring(i-1-d, i-d);
				String t2 = doc.substring(i-2-d, i-d);
				int s = 0;
				if (TimeUtil.getInstance().isDigit(t1)) {
					if (TimeUtil.getInstance().isDigit(t2)) {
						s = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t2));
					}
					s = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t1));
				}
				String t = doc.substring(i-2-d, i);
				if (s >= 1 && ans < 1 && t.contains("��"))
					ans = 1;
				else if (s >= 1 && ans < 2)
					ans = 2; 
			} else if (doc.substring(i, i+2) == "����") {
				if (doc.contains("��") || doc.contains("ȥ��") || doc.contains("ɥ��")) {
					if (ans < 3)
						ans = 3;
				} else {
					if (ans < 2)
						ans = 2;
				}
			} else if (doc.substring(i, i+2) == "��֫") {
				if (doc.contains("��") || doc.contains("ȥ��") || doc.contains("ɥ��")) {
					if (ans < 3)
						ans = 3;
				} else {
					if (ans < 2)
						ans = 2;
				}
			} else if (doc.substring(i, i+2) == "�ش�") {
				if (doc.substring(i+2, i+6).contains("�¹�")) {
					ans = 4;
				}
			} else if (doc.substring(i, i+2) == "�ش�") {
				if (doc.substring(i+2, i+6).contains("�¹�")) {
					if (ans <3)
						ans = 3;
				}
			} else if (doc.charAt(i) == '��') {
				String ngram = "";
				if (i >= 5)
					ngram = doc.substring(i-5, i);
				else
					ngram = doc.substring(0, i);
				if (!ngram.contains("��")) {
					if (ans < 3)
						ans = 3;
				}
			} else if (doc.charAt(i) == 'ʬ') {
				if (ans < 3)
					ans = 3;
			} else if (doc.charAt(i) == '��') {
				String ngram = "";
				if (i >= 8)
					ngram = doc.substring(i-8, i);
				else
					ngram = doc.substring(0,i);
				if (ngram.contains("ʧ") || ngram.contains("ɥ") ||ngram.contains("��"))
					if (ans < 3)
						ans = 3;
			}
		}
		return ans;
	}
	
		public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,�����������˴����μݳ�ײ������ �������ŵ���,2007-1-8,�����������˴����μݳ�ײ������?�������ŵ��� 2007-01-08 08:53 �й������� �������������������س������������ľ���羰�������ߴ�Լ2�������һ���жιٴ�Ĵ�ׯ���˴�����˼ҽ�Ϊ����?�����Ϻ��ᡣԪ�����������ڸô���Ӵ�ڷ�����һ��ͨ�¹ʣ����״����������ƽ������Ϊ�������Ǹ����˴����Ρ�������һ���Ӿ���羰�������ķ���ԽҰ��ͻȻ�ܵ���·���ײ��һ��Ħ�е����Ӻ���¶�������ʻ��30���ײ�ͣ���������¹ʵ��±�ײ���ӵ���������Ŀǰ�����н�����������չ�����顣? �����˴����μݳ�ײ���� ����Ԫ��������20ʱ�����ιٴ�40�����μ�����ʻһ����ɫ����Ħ�г��Ӵ��Ӳ�·�ڳ�����׼����������һ��˽�˹����ϰ࣬��ʻ�Ϲ�·Լ����100����Զ���ͺ�һ���Ӿ��緽��ʻ���ķ��·�ԡ�ԽҰ����ײ��֮�������������µ�·�ұߵĶ��֮��ԽҰ���ڶ�����ɱ���30����Զ���������������ͣ�������ӵ������������ؽ������5��������������ҽԺһ�����ȳ�Ѹ�ٸϵ��ֳ��󣬾�֤ʵĦ�г���ʻ�Ŷμ����Ѿ���������������н���֧���й��쵼ҲѸ�ٸϵ��ֳ�չ�����顣�ιٴ�Ⱥ�ڳƣ��������������ص�һ���߹١� �����������ģ�?�ܺ��˵������� �����������磬������������120���������˽⵽��6��3����20ʱ20�֣������Ľӵ��ιٴ徳����ͨ�¹�����һ�˵ļ��ȵ绰�󣬼��ȳ�Ѹ?����20ʱ23�ֳ���������5����֮��ϵ��ֳ����Ρ�������Ա�ֳ�������֪���򳵻����˵��������жμ���������40�꣬�Ƕιٴ�����Ĵ��񡣡��������������ˣ�������û����ҽԺ�����ȣ�һ��Ů������Ա˵�� ����120�����¼��ʾ���������޺���������������������ʧ��˫��ͫ�׷Ŵ󣬶Թⷴ����ʧ����ǻ����ǻ����ѪҺ����������֫���ȡ�С�Ȼ��Ρ����Թؽڻ��������������������ֵ���ҡ��� ����Ⱥ�ڳ��Ǿƺ����� ��������϶��Ǿƺ����£�Ҫ��Ȼ��ײ���˺������ﻹ�Ὺ��30����Զ�����˽������������Ǿ�����ˣ�ƽʱ��Ⱦƣ�����ÿ�ٷ����벻���ơ���һ����Ը͸¶�����Ĵ���˵�����ιٴ����67��Ķ�ĳ���ʾ������26���ʻ��������������һ����͵ľƺ����¡��������ȾͲ�����Լ�Ӧ��������ʻ���ҳ��������󳵵�����Ϊ�����ս�ͨ���ŵĹ涨���ұߵĳ���������Ħ�г���������ʻ·������Ҫ���Ǿƺ��ʻ������ײ��������Ͳ෭�ڹ����ˣ���ô�����ڶ����￪����ôԶ����ô���������·�ǰ�Ƿ�ȹ����أ��������磬���������ؽ������ӣ��ڴ��ſڣ�����ӳ������˼��ߵļ򵥲ɷã�����ʾ���ڽ�ͨ�¹ʷ��������������н���֧��һ����֧�ӳ����¹ʴ�����쵼�ͼ�ʱ�ϵ��ֳ����п��죬��Ŀǰ��ص�������û�г������Ƿ�Ⱦƣ�Ҫ�������յľƾ��������Ŀǰ�������½��ۣ� �����˴������������Ϣ ����Ϊ�ҵ������������˽���������������Ⱥ����ε����������˴󣬿�����ʱ��˫���գ�����Ҳ�޷��ҵ��й���Ա������������ί��ֵ?����Աȡ������ϵ������ȴ����֪�������������ε��ܻ����뻻�ˣ�������������Ҳû�а취���� �����������磬���������˴�칫¥��¥���ݿڣ�������������һ��������Ա����˵���¹ʷ��������������ξ�����һ�����ڵļٻؼ���Ϣ�ˡ�������Ҳ��֪�����ĵ绰�͵�ַ����?��������14ʱ30�֣������跨��ͨ���������˴�ί��칫�Ҹ�����������ֻ�������ȴ�ԡ�Ҫ�ɷô��±���ͨ��������ͬ�⡱Ϊ�ɼ��Ծܾ������߷��������Լ��Ѿ��ҹ����������Է���Ȼ���ɷֱ�͹��˵绰�� ������Դ�������±������ߣ��Ĺ���?������";
		System.out.println(DateUtil.getInstance().findDate(str, "2007-1-8"));
	}

}