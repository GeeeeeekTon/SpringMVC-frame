package com.leo.core.myutil;

import org.springframework.util.Assert;

import com.leo.core.mybase.Constants;

public class StringUtil {
	/**
	 * @author zhangzhen 
	 * 
	 * @param parentCode 
	 * @param currentLevelMaxCode 当前层级下（parent.getLevel()+1）的最大code ---- 如果没有父亲传入或者有父亲但是一个孩子也没有的时候请传入""
	 * @param defaultStart
	 * @return 新的code
	 */
	public static String getRuleCode(String parentCode, String currentLevelMaxCode,String defaultStart) {
		Assert.notNull(parentCode, "parentCode not null");
		Assert.notNull(currentLevelMaxCode, "currentLevelMaxCode not null");
		Assert.notNull(defaultStart, "defaultStart not null");
		int level=defaultStart.length();
		long intCode;
		if ("".equals(currentLevelMaxCode)) {
			intCode = 1;
		} else {
			Assert.state(currentLevelMaxCode.length()==defaultStart.length(), "currentLevelMaxCode.length()==defaultStart.length()");
			int len = currentLevelMaxCode.length();
			intCode = Long.valueOf(currentLevelMaxCode.substring(len - level, len)).longValue() + 1;
		}
		return parentCode + leftZero(String.valueOf(intCode),level);
	}
	public static String leftZero(String code,int level) {
		Assert.notNull(code, "code not null");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < level - code.length(); i++) {
			sb = sb.append("0");
		}
		sb = sb.append(code);
		return sb.toString();
	}
	/**
	 * 是否非空字符串
	 * @param input
	 * @return
	 */
	public static boolean isNotEmptyString(final String input) {
		return input != null && !"".equals(input.trim());
	}
	public static boolean isEmptyString(final String input) {
		return input == null || "".equals(input.trim());
	}
	/**
	 * 把秒转换成 时:分:秒 格式的时长信息
	 * @param seconds
	 * @return
	 */
	public static String getTimeDurationLabel(final Long seconds) {
		if (seconds == null || seconds.intValue() == 0) {
			return "00:00:00";
		} else {
			return String.format("%02d:%02d:%02d", seconds / Constants.SECONDS_IN_HOUR, (seconds % Constants.SECONDS_IN_HOUR) / Constants.SECONDS_IN_MINUTE, seconds % Constants.SECONDS_IN_MINUTE);
		}		
	}
	
	/**
	 * 去掉某个字符串里包含的最后某个字符串
	 * 
	 * @param srcStr
	 * @param toStrip
	 * @return
	 */
	public static String stripLastStr(final String srcStr, final String toStrip) {
		if (!isNotEmptyString(srcStr) || !isNotEmptyString(toStrip)) {
			return null;
		}
		if (srcStr.length() <= toStrip.length()) {
			return null;
		}

		if (!srcStr.endsWith(toStrip)) {
			return srcStr;
		} else {
			return srcStr.substring(0, srcStr.length() - toStrip.length());
		}
	}
	
	public static void main(String[] args) {
		String defaultStart="000";
		System.out.println(getRuleCode("","",defaultStart));
		System.out.println(getRuleCode("","001",defaultStart));
		System.out.println(getRuleCode("","009",defaultStart));
		System.out.println(getRuleCode("","011",defaultStart));
		System.out.println(getRuleCode("","099",defaultStart));
		System.out.println("======================================");
		System.out.println(getRuleCode("099","",defaultStart));
		System.out.println(getRuleCode("099","001",defaultStart));
		System.out.println(getRuleCode("099","009",defaultStart));
		System.out.println(getRuleCode("099","011",defaultStart));
		System.out.println(getRuleCode("099","099",defaultStart));
		System.out.println("======================================");
		String defaultStart1="0000";
		System.out.println(getRuleCode("","",defaultStart1));
		System.out.println(getRuleCode("","0001",defaultStart1));
		System.out.println(getRuleCode("","0009",defaultStart1));
		System.out.println(getRuleCode("","0011",defaultStart1));
		System.out.println(getRuleCode("","0099",defaultStart1));
		System.out.println("======================================");
		System.out.println(getRuleCode("0099","",defaultStart1));
		System.out.println(getRuleCode("0099","0001",defaultStart1));
		System.out.println(getRuleCode("0099","0009",defaultStart1));
		System.out.println(getRuleCode("0099","0011",defaultStart1));
		System.out.println(getRuleCode("0099","0099",defaultStart1));
		System.out.println("======================================");
		System.out.println(getRuleCode("999","",defaultStart1));
		System.out.println(getRuleCode("999","0001",defaultStart1));
		System.out.println(getRuleCode("999","0009",defaultStart1));
		System.out.println(getRuleCode("999","0011",defaultStart1));
		System.out.println(getRuleCode("999","0099",defaultStart1));
		System.out.println("======================================");
		System.out.println(leftZero("011002", 15));
		System.out.println(leftZero("", 15));
		
	}
}
