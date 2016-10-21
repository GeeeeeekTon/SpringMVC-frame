package com.dongao.core.myutil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangzhen
 *
 */
public class MyMath {
	/**
	 * 小数位精确方式
	 */
	public enum RoundType {

		/** 四舍五入 */
		roundHalfUp("四舍五入"),

		/** 向上取整 */
		roundUp("向上取整 "),

		/** 向下取整 */
		roundDown("向下取整");
		private String key;
		private String value;
		private RoundType(String value){
			this.key = this.name();
			this.value=value;
		}
		public static RoundType fromString(String key) {
			return Enum.valueOf(RoundType.class, key);
		}
		public String getKey() {
			return this.key;
		}
		public String getValue() {
			return this.value;
		}
		
		public static Map<String, String> getAllRoundType() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("1", RoundType.roundHalfUp.getValue());
			map.put("2", RoundType.roundUp.getValue());
			map.put("3", RoundType.roundDown.getValue());
			return map;
		}
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */

	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 带精度的除法运算，小数位按照四舍五入方式精确
	 * @param num 除数
	 * @param divide 被除数
	 * @param scale 保留位数
	 * @return
	 */
	public static double divide(double num,double divide, int scale) {
		return divide(num,divide,scale,RoundType.roundHalfUp);
	}
	/**
	 * 带精度的除法运算
	 * @param num 除数
	 * @param divide 被除数
	 * @param scale 保留位数
	 * @param roundType  小数位精确方式
	 * @return
	 */
	public static double divide(double num,double divide,int scale,RoundType roundType) {		
		return divide(new BigDecimal(num),new BigDecimal(divide),scale,roundType).doubleValue();
	}
	/**
	 * 带精度的除法运算，小数位按照四舍五入方式精确
	 * @param num 除数
	 * @param divide 被除数
	 * @param scale 保留位数
	 * @return
	 */
	public static BigDecimal divide(BigDecimal num,BigDecimal divide, int scale) {
		return divide(num,divide,scale,RoundType.roundHalfUp);
	}
	/**
	 * 带精度的除法运算
	 * @param num 除数
	 * @param divide 被除数
	 * @param scale 保留位数
	 * @param roundType  小数位精确方式
	 * @return
	 */
	public static BigDecimal divide(BigDecimal num,BigDecimal divide,int scale,RoundType roundType) {
		if(divide!=null&&divide.equals(new BigDecimal(0))){
			divide=new BigDecimal(1);
		}
		int roundingMode;
		if (roundType == RoundType.roundUp) {
			roundingMode = BigDecimal.ROUND_UP;
		} else if (roundType == RoundType.roundDown) {
			roundingMode = BigDecimal.ROUND_DOWN;
		} else {
			roundingMode = BigDecimal.ROUND_HALF_UP;
		}
		return num.divide(divide, scale,
				roundingMode);
	}
	public static void main(String[] args) {
//		System.out.println("四舍五入：" + MyMath.divide(1,1,1));//default
//		System.out.println("四舍五入：" + MyMath.divide(1,1,3,RoundType.roundHalfUp));
//		System.out.println("向上取整：" + Integer.valueOf((int)MyMath.divide(90.474,1,0,RoundType.roundUp)));
//		System.out.println("向下取整：" + MyMath.divide(90.579,1,0,RoundType.roundDown));
//		
//		System.out.println(MyMath.divide(10,45,0));
//		
//        System.out.println(0.05 + 0.01);  
//        System.out.println(1.0 - 0.42);  
//        System.out.println(4.015 * 100);  
//        System.out.println(123.3 / 100);  
//        double sum=0;
//        String[] d={"0.05","0.01","0.05","0.02","1"};
//        for(int i=0;i<d.length;i++){
//        	sum=(sum+(Double.valueOf(d[i])).doubleValue());
//        }
//        System.out.println(sum+"");
		
	}
}
