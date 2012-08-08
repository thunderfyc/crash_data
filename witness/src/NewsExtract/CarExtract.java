package NewsExtract;
import java.io.*;
import java.util.*;
public class CarExtract {
	
	public ArrayList<String> dates;
	public ArrayList<String> news;
	
	public CarExtract(int y, int m) {
		dates = new ArrayList<String>();
		news = new ArrayList<String>();
		try {
			FileReader fr = new FileReader("D:\\Projects\\Data\\Dnews_F\\"+y+"-"+m+".csv");
			FileWriter fw = new FileWriter("D:\\Projects\\Data\\Car\\"+y+"-"+m+".csv");
			BufferedReader br = new BufferedReader(fr);
			String x = br.readLine();
			int tot = 0;
			int cnt = 0;
			while (x != null) {
				tot++;
				cnt++;
				String[] tmps = x.split(",");
				dates.add(tmps[2]);
				news.add(tmps[3]);
				String a1 = "��";
				String a2 = a1;
				String a3 = a1;
				String a4 = a1;
				String a5 = a1;
				String a6 = a1;
				String a7 = a1;
				String a8 = a1;
				if (CarUtil.getInstance().hasBus(tmps[3]))
					a1 = "����";
				if (CarUtil.getInstance().hasDistant(tmps[3]))
					a2 = "��;";
				if (CarUtil.getInstance().hasSchool(tmps[3]))
					a3 = "У��";
				if (CarUtil.getInstance().hasTaxi(tmps[3]))
					a4 = "����";
				if (CarUtil.getInstance().hasTruck(tmps[3]))
					a5 = "������";
			//	if (CarUtil.getInstance().has(tmps[3]))
				//	a6 = "��̥";
			/*if (RoadUtil.getInstance().hasTired(tmps[3]))
					a7 = "ƣ��";
				if (RoadUtil.getInstance().hasVisible(tmps[3]))
					a8 = "���Ӷ�";*/
				//Date date = DateUtil.getInstance().findDate(tmps[3], tmps[2]);
				//int time = TimeUtil.getInstance().findTime(tmps[3]);
				fw.write(tmps[0]+","+tmps[1]+","+a1+","+a2+","+a3+","+a4+","+a5+"\n");
				if (a1.equals("��") && a2.equals("��") && a3.equals("��") 
						&& a4.equals("��") && a5.equals("��"))
						cnt--;
				x = br.readLine();
			}
			br.close();
			fr.close();
			fw.close();
			System.out.println(y+"-"+m+": "+cnt+"/"+tot);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		for (int i = 2008; i <= 2011; i++) {
			for (int j = 1; j <= 12; j++) {
				CarExtract es = new CarExtract(i, j);
			}
		}
		for (int i = 2012; i <= 2012; i++) {
			for (int j = 1; j <= 6; j++) {
				CarExtract es = new CarExtract(i, j);
			}
		}
	}

}
