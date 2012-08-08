package NewsExtract;
import java.io.*;
import java.text.*;
import java.util.*;

public class PeopleUtil {
	private static PeopleUtil _instance = null;
	
	public static PeopleUtil getInstance() {
		if (_instance == null) {
			_instance = new PeopleUtil();
		}
		return _instance;
	}
	protected PeopleUtil() {
	}
	
	public boolean hasRich(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("�Ҳ�") || doc.substring(i,i+2).equals("����") || doc.substring(i,i+2).equals("����")
					|| doc.substring(i,i+2).equals("�ܳ�") || doc.substring(i,i+3).equals("������") || doc.substring(i,i+2).equals("����")
					|| doc.substring(i,i+2).equals("�ʲ�") || doc.substring(i,i+3).equals("�ܾ���") || doc.substring(i,i+3).equals("���³�")) {
						conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasBureau(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("��ϯ") || doc.substring(i,i+2).equals("���") 
				|| doc.substring(i,i+2).equals("�ֳ�") || doc.substring(i,i+2).equals("����")
				|| doc.substring(i,i+2).equals("�г�") || doc.substring(i,i+2).equals("�س�")
				|| doc.substring(i,i+2).equals("�峤") || doc.substring(i,i+2).equals("��")
				|| doc.substring(i,i+2).equals("����") || doc.substring(i,i+2).equals("�ɲ�")
				|| doc.substring(i,i+2).equals("��Ա")) {
				conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasChild(String doc) {
		int conf = 0;
		for (int i = 4; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+1).equals("ͯ") || doc.substring(i,i+1).equals("��") || doc.substring(i,i+1).equals("Ӥ")) {
				conf++;
			} else if (doc.substring(i, i+1).equals("��") || doc.substring(i, i+1).equals("��")) {
				String t1 = doc.substring(i-1, i);
				String t2 = doc.substring(i-2, i);
				int age = -1;
				if (TimeUtil.getInstance().isDigit(t1)) {
					if (TimeUtil.getInstance().isDigit(t2)) {
						age = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t2));
					}
					
					age = Integer.parseInt(TimeUtil.getInstance().numCn2Ar(t1));
				}
				if (age < 20)
					conf++;
				
			}
		}
		return conf>0;
	}
	
	public boolean hasGreen(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+1).equals("����")) {
					conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasLabor(String doc) {
		return doc.contains("��������") || doc.contains("����Ů��") || doc.contains("·������");
	}
	
	public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,�����������˴����μݳ�ײ������ �������ŵ���,2007-1-8,�����������˴����μݳ�ײ������?�������ŵ��� 2007-01-08 08:53 �й������� �������������������س������������ľ���羰�������ߴ�Լ2�������һ���жιٴ�Ĵ�ׯ���˴�����˼ҽ�Ϊ����?�����Ϻ��ᡣԪ���������ڸô���Ӵ�ڷ�����һ��ͨ�¹ʣ����״����������ƽ������Ϊ�������Ǹ����˴����Ρ�����һ���Ӿ���羰�������ķ���ԽҰ��ͻȻ�ܵ���·���ײ��һ��Ħ�е����Ӻ���¶�������ʻ��30���ײ�ͣ���������¹ʵ��±�ײ���ӵ���������Ŀǰ�����н�����������չ�����顣? �����˴����μݳ�ײ���� ����Ԫ��������20ʱ���ιٴ�40�����μ�����ʻһ����ɫ����Ħ�г��Ӵ��Ӳ�·�ڳ�����׼����������һ��˽�˹����ϰ࣬��ʻ�Ϲ�·Լ����100����Զ���ͺ�һ���Ӿ��緽��ʻ���ķ��·�ԡ�ԽҰ����ײ��֮�������������µ�·�ұߵĶ��֮��ԽҰ���ڶ�����ɱ���30����Զ���������������ͣ�������ӵ������������ؽ������5��������������ҽԺһ�����ȳ�Ѹ�ٸϵ��ֳ��󣬾�֤ʵĦ�г���ʻ�Ŷμ����Ѿ���������������н���֧���й��쵼ҲѸ�ٸϵ��ֳ�չ�����顣�ιٴ�Ⱥ�ڳƣ��������������ص�һ���߹١� �����������ģ�?�ܺ��˵������� �����������磬������������120���������˽⵽��6��3����20ʱ20�֣������Ľӵ��ιٴ徳����ͨ�¹�����һ�˵ļ��ȵ绰�󣬼��ȳ�Ѹ?����20ʱ23�ֳ���������5����֮��ϵ��ֳ����Ρ�������Ա�ֳ�������֪���򳵻����˵��������жμ���������40�꣬�Ƕιٴ�����Ĵ��񡣡��������������ˣ�������û����ҽԺ�����ȣ�һ��Ů������Ա˵�� ����120�����¼��ʾ���������޺���������������������ʧ��˫��ͫ�׷Ŵ󣬶Թⷴ����ʧ����ǻ����ǻ����ѪҺ����������֫���ȡ�С�Ȼ��Ρ����Թؽڻ��������������������ֵ���ҡ��� ����Ⱥ�ڳ��Ǿƺ����� ��������϶��Ǿƺ����£�Ҫ��Ȼ��ײ���˺������ﻹ�Ὺ��30����Զ�����˽������������Ǿ�����ˣ�ƽʱ��Ⱦƣ�����ÿ�ٷ����벻���ơ���һ����Ը͸¶�����Ĵ���˵�����ιٴ����67��Ķ�ĳ���ʾ������26���ʻ��������������һ����͵ľƺ����¡��������ȾͲ�����Լ�Ӧ��������ʻ���ҳ��������󳵵�����Ϊ�����ս�ͨ���ŵĹ涨���ұߵĳ���������Ħ�г���������ʻ·������Ҫ���Ǿƺ��ʻ������ײ��������Ͳ෭�ڹ����ˣ���ô�����ڶ����￪����ôԶ����ô���������·�ǰ�Ƿ�ȹ����أ��������磬���������ؽ������ӣ��ڴ��ſڣ�����ӳ������˼��ߵļ򵥲ɷã�����ʾ���ڽ�ͨ�¹ʷ������������н���֧��һ����֧�ӳ����¹ʴ�����쵼�ͼ�ʱ�ϵ��ֳ����п��죬��Ŀǰ��ص�������û�г������Ƿ�Ⱦƣ�Ҫ�������յľƾ��������Ŀǰ�������½��ۣ� �����˴������������Ϣ ����Ϊ�ҵ������������˽���������������Ⱥ����ε����������˴󣬿�����ʱ��˫���գ�����Ҳ�޷��ҵ��й���Ա������������ί��ֵ?����Աȡ������ϵ������ȴ����֪�������������ε��ܻ����뻻�ˣ�������������Ҳû�а취���� �����������磬���������˴�칫¥��¥���ݿڣ�������������һ��������Ա����˵���¹ʷ��������������ξ�����һ�����ڵļٻؼ���Ϣ�ˡ�������Ҳ��֪�����ĵ绰�͵�ַ����?��������14ʱ30�֣������跨��ͨ���������˴�ί��칫�Ҹ�����������ֻ�������ȴ�ԡ�Ҫ�ɷô��±���ͨ��������ͬ�⡱Ϊ�ɼ��Ծܾ������߷��������Լ��Ѿ��ҹ����������Է���Ȼ���ɷֱ�͹��˵绰�� ������Դ�������±������ߣ��Ĺ���?������";
		System.out.println(DateUtil.getInstance().findDate(str, "2007-1-8"));
	}

}
