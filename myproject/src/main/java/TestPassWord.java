import com.alibaba.druid.filter.config.ConfigTools;


public class TestPassWord {
	public static void main(String[] args) throws Exception {
		System.out.println(ConfigTools.encrypt("root"));
	}
}
