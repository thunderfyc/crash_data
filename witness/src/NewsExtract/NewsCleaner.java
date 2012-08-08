package NewsExtract;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class NewsCleaner {
	
	private  HashSet<String> link_set;
	private  HashSet<String> title_set;
	private  HashSet<String> symbols;
	private  HashSet<String> numbers;
	private  HashMap<String, Integer> dict;
	private ArrayList<String> vocab;
	private ArrayList<String> dates;
	public void init() {
		symbols.add("!");symbols.add("��");
		symbols.add(".");symbols.add("��");
		symbols.add("?");symbols.add("��");
		symbols.add("=");symbols.add("+");
		symbols.add(",");symbols.add("��");
		symbols.add(";");symbols.add("��");
		symbols.add(":");symbols.add("��");
		symbols.add("\"");symbols.add("��");symbols.add("��");
		symbols.add("'");symbols.add("��");symbols.add("��");
		symbols.add("-");symbols.add("��");
		symbols.add("%");symbols.add("��");
		symbols.add("#");//symbols.add("");
		symbols.add("$");symbols.add("��");
		symbols.add("&");//symbols.add("");
		symbols.add("*");//symbols.add("");
		symbols.add("(");symbols.add("��");
		symbols.add(")");symbols.add("��");
		symbols.add("|");symbols.add("/");
		symbols.add("[");symbols.add("��");
		symbols.add("]");symbols.add("��");
		symbols.add("\\");symbols.add("��");
		numbers.add("0");numbers.add("��");numbers.add("��");
		numbers.add("1");numbers.add("һ");numbers.add("��");
		numbers.add("2");numbers.add("��");numbers.add("��");
		numbers.add("3");numbers.add("��");numbers.add("��");
		numbers.add("4");numbers.add("��");numbers.add("��");
		numbers.add("5");numbers.add("��");numbers.add("��");
		numbers.add("6");numbers.add("��");numbers.add("��");
		numbers.add("7");numbers.add("��");numbers.add("��");
		numbers.add("8");numbers.add("��");numbers.add("��");
		numbers.add("9");numbers.add("��");numbers.add("��");
	}
	
	public NewsCleaner() {
		link_set = new HashSet<String>();
		title_set = new HashSet<String>();
		symbols = new HashSet<String>();
		numbers = new HashSet<String>();
		dict = new HashMap<String, Integer>();
		vocab = new ArrayList<String>();
		dates = new ArrayList<String>();
		this.init();
	}
	
	protected void addtoDict(String title) {
		String ts = title;
		int t = title.indexOf("ͼ");
		if (t > 0) {
			if (t == title.length()-1) {
				ts = ts.substring(0, t);
			} else if (symbols.contains(ts.charAt(t-1)) && symbols.contains(ts.charAt(t+1))) {
				ts = ts.substring(0, t-1) + ts.substring(t+2);
			} else if (symbols.contains(ts.charAt(t+1))) {
				ts = ts.substring(0, t) + ts.substring(t+2);
			}
		}
		
		for (int i = 0; i < ts.length(); i++) {
			String c = ts.substring(i,i+1); 
			if (symbols.contains(c) || numbers.contains(c) || c.matches("[a-zA-Z]") || !Character.isLetter(ts.charAt(i)))
				continue;
			if (dict.containsKey(ts.substring(i, i+1))) {
				Integer tmp = dict.get(ts.substring(i,i+1)); 
				dict.put(ts.substring(i,i+1), new Integer(tmp+1));
			} else {
				dict.put(ts.substring(i,i+1), new Integer(1));
			}
		}
	}
	
	
	public boolean isLinkOK(String link) {
		if (link.indexOf("_")>0)
			return false;
		if (link.indexOf("htm") <0)
			return false;
		if (link.indexOf("http://news.qq.com") < 0)
			return false;
		if (link.indexOf("http://news.qq.com/b") >= 0)
			return false;
		String tmp;
		if (link.indexOf("?") >= 0) 
			tmp = link.substring(0, link.indexOf("?"));
		else
			tmp = link;
		if (link_set.contains(tmp))
			return false;
		else {
			link_set.add(tmp);
			return true;
		}
	}
	public boolean isTitleOK(String title) {
		if (title.indexOf("��Ƶ") == 0 || title.indexOf("��ͼ") == 0 || title.indexOf("Ӣ��") >= 0 || title.indexOf("����") >= 0)
			return false;
		if (title.indexOf("����") >= 0)
			return false;
		if (title_set.contains(title))
			return false;
		else {
			title_set.add(title);
			return true;
		}
	}
	
	protected double calcDist(String a, String b) {
		double ans = 0;
		for (int i = 0; i < b.length(); i++) {
			boolean isin = a.indexOf(b.charAt(i)) >= 0;
 			if (isin) {
 				if (dict.containsKey(b.substring(i,i+1))) {
 					ans += Math.exp(-(new Double(dict.get(b.substring(i,i+1))))/8);
 				}
 			}
		}
		//System.out.println(a+" - " + b + " : " + ans);
		return ans;
	}
	
	public void check(int y, int m, boolean deep) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews2\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				if (tmp[1].indexOf("_") > 0)
					title = title.substring(0, tmp[1].indexOf("_"));
				String date = tmp[2];
				if (!deep) {
					if (isLinkOK(link) && isTitleOK(title)) {
						addtoDict(title);
						fw.write(link+","+title+","+date+"\n");
					}
				} else {
					boolean isEq = false;
					for (String sd: vocab) {
						double ans = calcDist(sd, title);
						if (ans > 0.002) {
							isEq = true;
							System.out.println(sd+" - " + title+ " : " + ans);
							break;
						}
					}
					if (!isEq)
						fw.write(link+","+title+","+date+"\n");
					vocab.add(title);
				}
					
				
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public String trimDec(String title) {
		String t = title;
		int delta = 0;
		t = t.replaceAll("\\(", "��");
		t = t.replaceAll("\\)", "��");
		t = t.replaceAll("\\[", "��");
		t = t.replaceAll("\\]", "��");
		t = t.replaceAll(":", "��");
		t = t.replaceAll("��", "��");
		t = t.replaceAll("��ͼ��", "");
		t = t.replaceAll("��ͼ��", "");
		t = t.replaceAll("����ͼ��", "");
		t = t.replaceAll("����ͼ��", "");
		t = t.replaceAll("����Ƶ��", "");
		t = t.replaceAll("����Ƶ��", "");
		t = t.replaceAll("��ͼ�ġ�", "");
		t = t.replaceAll("����ͼ��", "");
		t = t.replaceAll("ͼƬ���£�", "");
		t = t.replaceAll("ͼƬ����", "");
		t = t.replaceAll("ͼ�ģ�", "");
		t = t.replaceAll("����ͼ��", "");
		t = t.replaceAll("ͼ��", "");
		return t;
	}
	
	public void check2(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				String date = tmp[2];
				boolean isOK = true;
				if (title.indexOf("ƭ") >= 0 || title.indexOf("��") >= 0 || title.indexOf("׷��") >=0 || title.indexOf("����") >=0
						|| title.indexOf("����") >=0 || title.indexOf("�ж�") >=0 ||title.indexOf("��") >=0  || title.indexOf("������") >=0 || title.indexOf("����") >=0
						|| title.indexOf("ӡ��") >=0 || title.indexOf("�ձ�") >=0 || title.indexOf("�Ϸ�") >=0  || title.indexOf("����") >=0 || title.indexOf("�м�") >=0
						|| title.indexOf("�ط�") >=0 ||  title.indexOf("����") >=0  || title.indexOf("����") >=0
						||  title.indexOf("����") >=0 ||  title.indexOf("����") >=0 ) {
					isOK = false;
				} else if (link.indexOf("/a/") < 0) {
					isOK = false;
				}
				if (isOK == false) {
					x = br.readLine();
					continue;
				}
				
				title = trimDec(title);
				
				for (int z = 0; z < vocab.size(); z++) {
					String v = vocab.get(z);
					String d = dates.get(z);
					int cnt = 0;
					int len = 0;
					int len2 = 0;
					for (int i = 0; i < title.length(); i++) {
						if (title.charAt(i) != '��' && title.charAt(i) != '��' && title.charAt(i) != '��' 
							&& title.charAt(i) != '��' && title.charAt(i) != '��' && title.charAt(i) != '��' &&  
							symbols.contains(title.substring(i,i+1))==false
									&&numbers.contains(title.substring(i,i+1))==false) {
							if (v.indexOf(title.charAt(i)) >= 0)
								cnt++;
							len++;
						}
					}
					for (int i = 0; i < v.length(); i++) {
						if (v.charAt(i) != '��' && v.charAt(i) != '��' && v.charAt(i) != '��' 
								&& v.charAt(i) != '��' &&symbols.contains(v.substring(i,i+1))==false
								&& v.charAt(i) != '��' && v.charAt(i) != '��'   
								&&numbers.contains(v.substring(i,i+1))==false) {
							len2++;
						}
					}
					if (cnt >= len / 2 && cnt >= len2 / 2 && date.equals(d)) {
						isOK = false;
						//System.out.println(date+":"+title + " - " + d+":"+v);
						break;
					}
				}
				if (isOK) {
					vocab.add(title);
					dates.add(date);
					fw.write(link+","+title+","+date+"\n");
				}
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void check3(int y, int m) {
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Snews\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Snews3\\"+y+"-"+m+".csv");
			vocab.clear();
			dates.clear();
			
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			while (x != null) {
				String tmp[] = x.split(",");
				String link = tmp[0];
				String title = tmp[1];
				title = title.replace("&quot;", "");
				String date = tmp[2];
				boolean isOK = true;
				if (title.indexOf("����") >= 0 || title.indexOf("��������") >= 0
					//|| title.indexOf("���") >= 0 || title.indexOf("����") >= 0
					//|| title.indexOf("����") >= 0 || title.indexOf("����") >= 0
					//|| title.indexOf("���") >= 0 || title.indexOf("άȨ") >= 0
					//|| title.indexOf("�䶯") >= 0 || title.indexOf("�߷�") >= 0
					//|| title.indexOf("ר��") >= 0 || title.indexOf("����") >= 0 || title.indexOf("����ʯ") >= 0 
					//|| title.indexOf("�¹�") >= 0 || title.indexOf("�����") >= 0 || title.indexOf("����˹") >= 0
					//|| title.indexOf("��������") >= 0 || title.indexOf("ӡ��") >= 0 || title.indexOf("̩��") >= 0
					//|| title.indexOf("Խ��") >= 0 || title.indexOf("����կ") >= 0 || title.indexOf("���ɱ�") >= 0 || title.indexOf("����") >= 0
					//|| title.indexOf("ī����") >= 0 || title.indexOf("���ô�") >= 0 || title.indexOf("����") >= 0 
					//|| title.indexOf("����͢") >= 0 || title.indexOf("�Ĵ�����") >= 0 || title.indexOf("����") >= 0 
					//|| title.indexOf("����") >= 0 || title.indexOf("�ͻ�˹̹") >= 0 || title.indexOf("������") >= 0 
					//|| title.indexOf("����") >= 0 || title.indexOf("ɳ��") >= 0 || title.indexOf("������") >= 0 || title.indexOf("����") >= 0 || title.indexOf("��ˮ") >= 0
					//|| title.indexOf("ҽ��") >= 0 || title.indexOf("����") >= 0 || title.indexOf("�Ჴ��") >= 0 || title.indexOf("��Ͳ�Τ") >= 0
					//|| title.indexOf("�Ϸ�") >= 0 || title.indexOf("����") >= 0 || title.indexOf("�ڿ���") >= 0 || title.indexOf("��³") >= 0
					//|| (title.indexOf("��") >= 0&&title.indexOf("��")>=0) || title.indexOf("Σ������") >= 0
					//|| title.indexOf("������") >= 0 || title.indexOf("��") >= 0 || title.indexOf("��") >= 0
					//|| title.indexOf("����") >= 0 || (title.indexOf("��") >= 0&&title.indexOf("ƨ��")<0)
					//|| title.indexOf("��ֲ") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0|| title.indexOf("��ѩ") >= 0
					//|| title.indexOf("Ǳ����") >= 0|| title.indexOf("Ԥ��") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("�ж�") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("ʮ��") >= 0|| title.indexOf("ϣ����") >= 0|| title.indexOf("�°���") >= 0
					//|| title.indexOf("�¼�") >= 0|| title.indexOf("׳��") >= 0|| title.indexOf("�ƾ�") >= 0
					//|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("����") >= 0|| title.indexOf("ɱ��") >= 0|| title.indexOf("α��") >= 0
					//|| title.indexOf("ɧ��") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("ڮ��") >= 0|| title.indexOf("���") >= 0|| title.indexOf("���") >= 0
					//|| title.indexOf("����") >= 0|| title.indexOf("������") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("ί������") >= 0|| title.indexOf("���ױ���") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("�׶�") >= 0|| title.indexOf("��ѯ") >= 0|| title.indexOf("����") >= 0
					//|| title.indexOf("��թ") >= 0|| title.indexOf("�ٿ�")  0|| title.indexOf("����") >= 0
					//	|| title.indexOf("��������") >= 0|| title.indexOf("��Ѫ") >= 0|| title.indexOf("����") >= 0
					//	|| title.indexOf("���") >= 0|| title.indexOf("˹̹") >= 0|| title.indexOf("�Ĺ���") >= 0
					//	|| title.indexOf("��Ҫ") >= 0|| title.indexOf("Ԥ��") >= 0|| title.indexOf("���") >= 0
					//	|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//	|| title.indexOf("��ʷ") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
					//	|| title.indexOf("����˭") >= 0|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0
						|| title.indexOf("��ɱ") >= 0|| title.indexOf("��ʾ") >= 0|| title.indexOf("����") >= 0
						|| title.indexOf("��") >= 0|| title.indexOf("���") >= 0|| title.indexOf("����") >= 0
						|| title.indexOf("�¼���") >= 0|| title.indexOf("���") >= 0|| title.indexOf("����") >= 0
						|| title.indexOf("��¥") >= 0|| title.indexOf("��̨") >= 0|| title.indexOf("��") >= 0
						|| title.indexOf("Ź") >= 0|| title.indexOf("��̨") >= 0|| title.indexOf("��") >= 0
						|| title.indexOf("����") >= 0|| title.indexOf("�涨") >= 0|| title.indexOf("�ϻ�") >= 0
						|| title.indexOf("Ʊ") >= 0|| title.indexOf("����") >= 0|| title.indexOf("��") >= 0
						|| title.indexOf("�ϼ���") >= 0|| title.indexOf("��") >= 0|| title.indexOf("����") >= 0
						|| title.indexOf("ר��") >= 0|| title.indexOf("����") >= 0
						){
					isOK = false;
					System.out.println(title);
				}
				if (title.indexOf("�ͳ�") <0 && title.indexOf("����") <0 && title.indexOf("����") < 0 && title.indexOf("��") < 0&& title.indexOf("����") < 0 && title.indexOf("ײ") <0) {
					if (title.indexOf("����") >= 0 || title.indexOf("����") >= 0 || title.indexOf("��ע") >= 0
						|| title.indexOf("����") >= 0 || title.indexOf("��ϯ") >= 0 || title.indexOf("İ��") >= 0
						|| title.indexOf("��") >= 0|| title.indexOf("����") >= 0 || title.indexOf("��Ѫ") >= 0
						|| title.indexOf("����") >= 0|| title.indexOf("����") >= 0|| title.indexOf("Ӣ��") >= 0
							) {
						isOK = false;
						System.out.println(title);
					}
				}
				if (isOK == false) {
					x = br.readLine();
					continue;
				}
				fw.write(link+","+title+","+date+"\n");
				x = br.readLine();
			}
			fr.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String args[]) {
		NewsCleaner nc = new NewsCleaner();
		for (int y = 2010; y <= 2011; y++) {
			for (int m = 1; m <= 12; m++) {
				nc.check3(y, m);
			}
		}
	}
}
