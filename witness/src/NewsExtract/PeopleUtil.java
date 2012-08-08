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
			if (doc.substring(i,i+2).equals("家产") || doc.substring(i,i+2).equals("宝马") || doc.substring(i,i+2).equals("奔驰")
					|| doc.substring(i,i+2).equals("跑车") || doc.substring(i,i+3).equals("富二代") || doc.substring(i,i+2).equals("豪车")
					|| doc.substring(i,i+2).equals("资产") || doc.substring(i,i+3).equals("总经理") || doc.substring(i,i+3).equals("董事长")) {
						conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasBureau(String doc) {
		int conf = 0;
		for (int i = 0; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+2).equals("主席") || doc.substring(i,i+2).equals("书记") 
				|| doc.substring(i,i+2).equals("局长") || doc.substring(i,i+2).equals("厅长")
				|| doc.substring(i,i+2).equals("市长") || doc.substring(i,i+2).equals("县长")
				|| doc.substring(i,i+2).equals("村长") || doc.substring(i,i+2).equals("镇长")
				|| doc.substring(i,i+2).equals("主任") || doc.substring(i,i+2).equals("干部")
				|| doc.substring(i,i+2).equals("官员")) {
				conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasChild(String doc) {
		int conf = 0;
		for (int i = 4; i < doc.length() - 10; i++) {
			if (doc.substring(i,i+1).equals("童") || doc.substring(i,i+1).equals("孩") || doc.substring(i,i+1).equals("婴")) {
				conf++;
			} else if (doc.substring(i, i+1).equals("岁") || doc.substring(i, i+1).equals("龄")) {
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
			if (doc.substring(i,i+1).equals("新手")) {
					conf++;
			}
		}
		return conf>0;
	}
	
	public boolean hasLabor(String doc) {
		return doc.contains("环卫工人") || doc.contains("环卫女工") || doc.contains("路政工人");
	}
	
	public static void main(String[] args) {
		String str = "http://news.qq.com/a/20070108/000980.htm,云南宜良县人大主任驾车撞死村民 交警部门调查,2007-1-8,云南宜良县人大主任驾车撞死村民?交警部门调查 2007-01-08 08:53 中国新闻网 　　从昆明市宜良县县城驱车往著名的九乡风景区方向走大约2公里，就是一个叫段官村的村庄，此村段姓人家皆为大理?国段氏后裔。元月三日晚，在该村五队村口发生了一起交通事故，彻底大破了这里的平静。因为肇事者是该县人大主任。当晚，一辆从九乡风景区开来的丰田越野车突然窜到道路左边撞倒一骑摩托的男子后飞下豆田又行驶了30多米才停下来，次事故导致被撞男子当场死亡。目前昆明市交警部门正在展开调查。? 　　人大主任驾车撞死人 　　元月三日晚20时许，段官村40岁村民段继明驾驶一辆红色两轮摩托车从村子岔路口出来，准备到附近的一家私人工厂上班，刚驶上公路约走了100多米远，就和一辆从九乡方向驶来的丰田“路霸”越野车相撞，之后两辆车都飞下道路右边的豆田，之后越野车在豆田里飞奔了30多米远，横跨了两丘豆田后才停下来。接到报警后，宜良县交警大队5辆警车和宜良县医院一辆急救车迅速赶到现场后，经证实摩托车驾驶着段继明已经死亡。随后昆明市交警支队有关领导也迅速赶到现场展开调查。段官村群众称，肇事者是宜良县的一名高官。 　　急救中心：?受害人当场死亡 　　昨日下午，记者在宜良县120急救中心了解到，6月3日晚20时20分，该中心接到段官村境内因交通事故重伤一人的急救电话后，急救车迅?速在20时23分出发，并于5分钟之后赶到现场救治。急救人员现场经过得知，因车祸致伤的男子名叫段继明，现年40岁，是段官村四组的村民。“他当场就死亡了，根本就没拉到医院来抢救１一名女工作人员说。 　　120出诊记录显示：“患者无呼吸、心跳。大动脉搏动消失，双侧瞳孔放大，对光反射消失；口腔、鼻腔内有血液流出，左下肢大腿、小腿畸形。假性关节活动，患者已死亡，报告总值班室。” 　　群众称是酒后肇事 　　“这肯定是酒后肇事，要不然车撞到人后冲进豆田还会开出30多米远，我了解祁文生，他是九乡的人，平时最爱喝酒，几乎每顿饭都离不开酒。”一名不愿透露姓名的村民说。而段官村村民67岁的段某则表示，以他26年驾驶经验来看，这是一起典型的酒后肇事。否则首先就不会从自己应该正常行驶的右车道抢过左车道，因为，按照交通部门的规定，右边的车道正好是摩托车的正常行驶路径。“要不是酒后驾驶，车在撞到树后早就侧翻在沟里了，怎么可能在豆田里开出那么远１那么肇事者在事发前是否喝过酒呢？昨日上午，记者来到县交警察大队，在大门口，副大队长接受了记者的简单采访，他表示，在交通事故发生当晚，昆明市交警支队一名副支队长和事故处相关领导就及时赶到现场进行勘察，“目前相关调查结果还没有出来，是否喝酒，要根据最终的酒精检测结果，目前还不好下结论１ 　　人大主任已请假休息 　　为找到祁文生本人了解情况，记者昨日先后两次到达宜良县人大，可由于时逢双休日，最终也无法找到有关人员。随后记者与县委办值?班人员取得了联系，最终却被告知，“由于祁主任的受机号码换了，就连我们找他也没有办法”。 　　昨日中午，在宜良县人大办公楼九楼电梯口，记者终于遇到一名工作人员，他说，事故发生后，祁文生主任就请了一个星期的假回家休息了。“我们也不知道他的电话和地址！”?昨日下午14时30分，记者设法打通了宜良县人大常委会办公室副主任李麒的手机，李麒却以“要采访此事必须通过宣传部同意”为由加以拒绝，记者反复表明自己已经找过宣传部，对方仍然不由分辨就挂了电话。 　　来源：生活新报；作者：夏光龙?周晓晖";
		System.out.println(DateUtil.getInstance().findDate(str, "2007-1-8"));
	}

}
