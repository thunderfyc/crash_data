package NewsExtract;
import java.io.*;
import java.text.*;
import java.util.*;

public class RoadUtil {
	private static RoadUtil _instance = null;
	
	public static RoadUtil getInstance() {
		if (_instance == null) {
			_instance = new RoadUtil();
		}
		return _instance;
	}
	protected RoadUtil() {
	}
	
	public boolean hasCross(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("ʮ��") || doc.substring(i,i+2).equals("���") || doc.substring(i,i+2).equals("�Ƶ�")
					|| doc.substring(i,i+2).equals("����") || doc.substring(i,i+2).equals("����") || doc.substring(i,i+2).equals("����")
					|| doc.substring(i,i+2).equals("·��")) {
						conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasHighway(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("����")) {
				conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasHill(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+1).equals("��") || doc.substring(i,i+1).equals("ɽ")) {
				String ngramPrev = doc.substring(i+3,i+8);
				if (i > 5)
					ngramPrev = ngramPrev + doc.substring(i-5, i);
				
				if (ngramPrev.contains("��") ||ngramPrev.contains("��") || ngramPrev.contains("׹")) {
					conf++;
				}
			}
		}
		return conf>0;
	}
	
	public boolean hasBridge(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+1).equals("��")) {
				String ngramPrev = doc.substring(i+3,i+8);
				if (i > 5)
					ngramPrev = ngramPrev + doc.substring(i-5, i);
				
				if (ngramPrev.contains("����") ||ngramPrev.contains("����") || ngramPrev.contains("����")) {
					conf++;
				}
			}
		}
		return conf>0;
	}
	
	public boolean hasRural(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("С��") || doc.substring(i,i+2).equals("С·")) {
				conf++;
//				String ngramPrev = doc.substring(i+2, i+7);
	//			if (i > 5)
		//			ngramPrev = doc.substring(i-5, i);
				
			//	if (ngramPrev.contains("��") || ngramPrev.contains("��") || ngramPrev.contains("��")) {
				//	conf++;
			//	}
			}
		}
		return conf>0;
	}
	
	public boolean hasSpeed(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("����")) {
				String ngramPrev = "";
				if (i > 5)
					ngramPrev = doc.substring(i-5, i);
				
				if (ngramPrev.indexOf("û") < 0 && ngramPrev.indexOf("����") < 0 && ngramPrev.indexOf("δ") < 0) {
					conf++;
				}
			} else if (doc.substring(i, i+2).equals("�ٶ�") || doc.substring(i, i+2).equals("����")) {
				String ngramAfter = doc.substring(i+2,i+7);
				if ((ngramAfter.indexOf("��") >= 0 || ngramAfter.indexOf("��") >= 0) && !ngramAfter.contains("��")  && !ngramAfter.contains("δ")  && !ngramAfter.contains("û")) {
					conf++;
				}
			}
		}
		return conf>1;
	}
	
	public boolean hasTired(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() -10; i++) {
			if (doc.substring(i,i+2).equals("ƣ��") || doc.substring(i,i+2).equals("˯��")) {
					conf++;
			}
		}
		return conf>1;
		
	}
	
	public boolean hasTireBomb(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() -10; i++) {
			if (doc.substring(i,i+2).equals("��̥")) {
					conf++;
			} else if (doc.substring(i, i+2).equals("����") || doc.substring(i, i+1).equals("��̥")) {
				String ngram = doc.substring(i+2,i+7);
				if (i > 5)
					ngram += doc.substring(i-5, i);
				if (ngram.indexOf("����") >= 0 || ngram.indexOf("����") >= 0) {
					conf++;
				}
			}
		}
		return conf>0;
	}
	
	public boolean hasRoad(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() -10; i++) {
			if (doc.substring(i,i+2).equals("·��")) {
				String ngramAfter = doc.substring(i+2, i+7);
				if (ngramAfter.contains("����") || ngramAfter.contains("����") ||ngramAfter.contains("����"))
					conf++;
			}
		}
		return conf>1;
		
	}
	public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,�����������˴����μݳ�ײ������ �������ŵ���,2007-1-8,�����������˴����μݳ�ײ������?�������ŵ��� 2007-01-08 08:53 �й������� �������������������س������������ľ���羰�������ߴ�Լ2�������һ���жιٴ�Ĵ�ׯ���˴�����˼ҽ�Ϊ����?�����Ϻ��ᡣԪ���������ڸô���Ӵ�ڷ�����һ��ͨ�¹ʣ����״����������ƽ������Ϊ�������Ǹ����˴����Ρ�����һ���Ӿ���羰�������ķ���ԽҰ��ͻȻ�ܵ���·���ײ��һ��Ħ�е����Ӻ���¶�������ʻ��30���ײ�ͣ���������¹ʵ��±�ײ���ӵ���������Ŀǰ�����н�����������չ�����顣? �����˴����μݳ�ײ���� ����Ԫ��������20ʱ���ιٴ�40�����μ�����ʻһ����ɫ����Ħ�г��Ӵ��Ӳ�·�ڳ�����׼����������һ��˽�˹����ϰ࣬��ʻ�Ϲ�·Լ����100����Զ���ͺ�һ���Ӿ��緽��ʻ���ķ��·�ԡ�ԽҰ����ײ��֮�������������µ�·�ұߵĶ��֮��ԽҰ���ڶ�����ɱ���30����Զ���������������ͣ�������ӵ������������ؽ������5��������������ҽԺһ�����ȳ�Ѹ�ٸϵ��ֳ��󣬾�֤ʵĦ�г���ʻ�Ŷμ����Ѿ���������������н���֧���й��쵼ҲѸ�ٸϵ��ֳ�չ�����顣�ιٴ�Ⱥ�ڳƣ��������������ص�һ���߹١� �����������ģ�?�ܺ��˵������� �����������磬������������120���������˽⵽��6��3����20ʱ20�֣������Ľӵ��ιٴ徳����ͨ�¹�����һ�˵ļ��ȵ绰�󣬼��ȳ�Ѹ?����20ʱ23�ֳ���������5����֮��ϵ��ֳ����Ρ�������Ա�ֳ�������֪���򳵻����˵��������жμ���������40�꣬�Ƕιٴ�����Ĵ��񡣡��������������ˣ�������û����ҽԺ�����ȣ�һ��Ů������Ա˵�� ����120�����¼��ʾ���������޺���������������������ʧ��˫��ͫ�׷Ŵ󣬶Թⷴ����ʧ����ǻ����ǻ����ѪҺ����������֫���ȡ�С�Ȼ��Ρ����Թؽڻ��������������������ֵ���ҡ��� ����Ⱥ�ڳ��Ǿƺ����� ��������϶��Ǿƺ����£�Ҫ��Ȼ��ײ���˺������ﻹ�Ὺ��30����Զ�����˽������������Ǿ�����ˣ�ƽʱ��Ⱦƣ�����ÿ�ٷ����벻���ơ���һ����Ը͸¶�����Ĵ���˵�����ιٴ����67��Ķ�ĳ���ʾ������26���ʻ��������������һ����͵ľƺ����¡��������ȾͲ�����Լ�Ӧ��������ʻ���ҳ��������󳵵�����Ϊ�����ս�ͨ���ŵĹ涨���ұߵĳ���������Ħ�г���������ʻ·������Ҫ���Ǿƺ��ʻ������ײ��������Ͳ෭�ڹ����ˣ���ô�����ڶ����￪����ôԶ����ô���������·�ǰ�Ƿ�ȹ����أ��������磬���������ؽ������ӣ��ڴ��ſڣ�����ӳ������˼��ߵļ򵥲ɷã�����ʾ���ڽ�ͨ�¹ʷ������������н���֧��һ����֧�ӳ����¹ʴ�����쵼�ͼ�ʱ�ϵ��ֳ����п��죬��Ŀǰ��ص�������û�г������Ƿ�Ⱦƣ�Ҫ�������յľƾ��������Ŀǰ�������½��ۣ� �����˴������������Ϣ ����Ϊ�ҵ������������˽���������������Ⱥ����ε����������˴󣬿�����ʱ��˫���գ�����Ҳ�޷��ҵ��й���Ա������������ί��ֵ?����Աȡ������ϵ������ȴ����֪�������������ε��ܻ����뻻�ˣ�������������Ҳû�а취���� �����������磬���������˴�칫¥��¥���ݿڣ�������������һ��������Ա����˵���¹ʷ��������������ξ�����һ�����ڵļٻؼ���Ϣ�ˡ�������Ҳ��֪�����ĵ绰�͵�ַ����?��������14ʱ30�֣������跨��ͨ���������˴�ί��칫�Ҹ�����������ֻ�������ȴ�ԡ�Ҫ�ɷô��±���ͨ��������ͬ�⡱Ϊ�ɼ��Ծܾ������߷��������Լ��Ѿ��ҹ����������Է���Ȼ���ɷֱ�͹��˵绰�� ������Դ�������±������ߣ��Ĺ���?������";
		System.out.println(DateUtil.getInstance().findDate(str, "2007-1-8"));
	}

}
