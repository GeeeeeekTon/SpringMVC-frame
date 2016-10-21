import com.alibaba.druid.filter.config.ConfigTools;
import com.dongao.core.myutil.Md5Util;


public class TestPassWord {
	public static void main(String[] args) throws Exception {
		System.out.println(ConfigTools.encrypt("root"));
	}
}
