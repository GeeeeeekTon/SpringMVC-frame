package com.leo.core.common.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author yanglin
 * 
 */
public class StringUtils {

	/** 金额为分的格式 */
	private static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	/** 卡段号码长度：该批卡的卡号都已其作为开头 */
	private static final int CARD_BATCH_CODE_LENGTH = 3;

	/** 卡号里除了卡段之外的数字部分的长度7位 */
	private static final int CARD_NO_LENGTH = 7;

	/** 优惠码长度 */
	public static final int COUPON_CODE_LENGTH = 13;

	/** 手机号长度 */
	private static final int VALID_MOBILE_NO_LENGTH = 11;

	/** HTTP头 */
	private static final String HTTP_METHOD_STR = "http://";

	private static final char[] DIGITAL_PWD_TABLE = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };

	private static final char[] CARDBATCH_CODE_TABLE = { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final char[] COUPONCODE_TABLE = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z' };

	private static final String REGEX_DOMAIN_PATTERN = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";

	// private static final String MOBILE_REGEX_PATTERN ="^(1\\d{10})|(\\d{6,11})$";
	private static final String REGEX_IP_PATTERN = "((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}";

	//private static final String REGEX_NUMBER_PATTERN = "(^|^-)\\d*(\\.\\d*)?$";

	private static final String REGEX_EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	private static final String REGEX_DATE_PATTERN = "^([1-9][0-9]{3}-([0][1-9]|[1][0-2])-([0][1-9]|[1][0-9]|[2][0-9]|[3][0-1]))$";

	// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
	private static final Pattern REGEX_PATTERN_HTML_SCRIPT = Pattern.compile(
			"<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>",
			Pattern.CASE_INSENSITIVE);
	// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
	private static final Pattern REGEX_PATTERN_HTML_STYLE = Pattern.compile(
			"<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>",
			Pattern.CASE_INSENSITIVE);
	// 定义style的正则表达式{或<form[^>]*?>[\\s\\S]*?<\\/form> }
	private static final Pattern REGEX_PATTERN_HTML_FORM = Pattern.compile(
			"<[\\s]*?form[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?form[\\s]*?>",
			Pattern.CASE_INSENSITIVE);
	// 定义a的正则表达式{或<a[^>]*?>[\\s\\S]*?<\\/a> }
	private static final Pattern REGEX_PATTERN_HTML_A = Pattern.compile(
			"<[\\s]*?a[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?a[\\s]*?>",
			Pattern.CASE_INSENSITIVE);
	/**定义秒 */
	private static final Long SECONDS_IN_MINUTE = 60L; 
	/**定义分钟*/
	private static final Long MINUTES_IN_HOUR = 60L; 
	
	/** 页面文字显示字数，可用于截取字符串，多余字符用...代替*/
	private static final int TITLE_LENGTH_IN_LIST = 20;

	/**
	 * 判断字符串是否为空, null、"" 返回false
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null) && (str.trim().length() > 0);
	}

	/**
	 * 处理null，返回空串
	 * 
	 * @param str
	 * @return String
	 */
	public static String dealNull(String str) {
		return (null == str) || ("null".equals(str)) ? "" : str.trim();
	}

	/**
	 * 从字符串正向开始截取字符串,长度取决于aLen
	 * 
	 * @param aStr
	 * @param aLen
	 * @return String
	 */
	public static String left(String aStr, int aLen) {
		if (aLen < 0) {
			throw new IllegalArgumentException("Requested String length "
					+ aLen + " is less than zero");
		}
		if ((aStr == null) || (aStr.length() <= aLen)) {
			return aStr;
		}
		return aStr.substring(0, aLen);
	}

	/**
	 * 从字符串反向开始截取字符串,长度取决于aLen
	 * 
	 * @param aStr
	 * @param aLen
	 * @return String
	 */
	public static String right(String aStr, int aLen) {
		if (aLen < 0) {
			throw new IllegalArgumentException("Requested String length "
					+ aLen + " is less than zero");
		}
		if ((aStr == null) || (aStr.length() <= aLen)) {
			return aStr;
		}
		return aStr.substring(aStr.length() - aLen);
	}

	/**
	 * 字符串根据索引，截取aLen位字符串
	 * 
	 * @param aStr
	 * @param pos
	 * @param aLen
	 * @return String
	 */
	public static String mid(String aStr, int pos, int aLen) {
		if ((pos < 0) || ((aStr != null) && (pos > aStr.length()))) {
			throw new StringIndexOutOfBoundsException("String index " + pos
					+ " is out of bounds");
		}
		if (aLen < 0) {
			throw new IllegalArgumentException("Requested String length "
					+ aLen + " is less than zero");
		}
		if (aStr == null) {
			return null;
		}
		if (aStr.length() <= pos + aLen) {
			return aStr.substring(pos);
		}
		return aStr.substring(pos, pos + aLen);
	}

	/**
	 * 数组转字符串，不含连接符
	 * 
	 * @param aArray
	 * @return String
	 */
	public static String concatenate(Object[] aArray) {
		return join(aArray, "");
	}

	/**
	 * 数组转字符串，包含连接符
	 * 
	 * @param aArray
	 * @param aSeparator
	 * @return String
	 */
	public static String join(Object[] aArray, String aSeparator) {
		if (aSeparator == null) {
			aSeparator = "";
		}
		int tArraySize = aArray.length;
		int tBufSize = tArraySize == 0 ? 0
				: (aArray[0].toString().length() + aSeparator.length())
						* tArraySize;
		StringBuffer tBuf = new StringBuffer(tBufSize);

		for (int i = 0; i < tArraySize; i++) {
			if (i > 0) {
				tBuf.append(aSeparator);
			}
			tBuf.append(aArray[i]);
		}
		return tBuf.toString();
	}

	/**
	 * 字符串转大写
	 * 
	 * @param aStr
	 * @return String
	 */
	public static String upperCase(String aStr) {
		if (aStr == null) {
			return null;
		}
		return aStr.toUpperCase();
	}

	/**
	 * 字符串转小写
	 * 
	 * @param aStr
	 * @return String
	 */
	public static String lowerCase(String aStr) {
		if (aStr == null) {
			return null;
		}
		return aStr.toLowerCase();
	}

	/**
	 * 判断字符串是否由数字组成
	 * 
	 * @param num
	 * @return boolean
	 */
	public static boolean validateNum(String num) {
		boolean isMatch = false;
		Pattern tMask = Pattern.compile("^[0-9]*[1-9][0-9]*$");
		if (isNotEmpty(num)) {
			Matcher tMatch = tMask.matcher(num);
			if (tMatch.matches()) {
				isMatch = true;
			}
		}
		return isMatch;
	}

	/**
	 * 判断字符串数组是否包含检索字符,默认返回-1，包含返回 1
	 * 
	 * @param arrayList
	 * @param chkStr
	 * @return int
	 */
	public static int checkArray(String[] arrayList, String chkStr) {
		int isValidate = -1;

		for (int i = 0; i < arrayList.length; i++) {
			if (arrayList[i].equals(chkStr)) {
				isValidate = 1;
				break;
			}
		}
		return isValidate;
	}

	/**
	 * 去掉回车符
	 * 
	 * @param str
	 * @return String
	 */
	public static String removeEnterNewLine(String str) {
		String newStr = "";
		try {
			if (isNotEmpty(str)) {
				newStr = str.replaceAll("\r\n", "");
				newStr = newStr.replaceAll("\r", "");
				newStr = newStr.replaceAll("\n", "");
			}
		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		return newStr;
	}

	/**
	 * 将分为单位的转换为元并返回金额格式的字符串 （除100） 例如 111123133L 转换完为 1,111,231.33
	 * 
	 * @param amount
	 * @return String
	 */
	public static String changeF2Y(Long amount) {
		if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
			return "金额格式有误";
		}
		int flag = 0;
		String amString = amount.toString();
		if (amString.charAt(0) == '-') {
			flag = 1;
			amString = amString.substring(1);
		}
		StringBuffer result = new StringBuffer();
		if (amString.length() == 1) {
			result.append("0.0").append(amString);
		} else if (amString.length() == 2) {
			result.append("0.").append(amString);
		} else {
			String intString = amString.substring(0, amString.length() - 2);
			for (int i = 1; i <= intString.length(); i++) {
				if ((i - 1) % 3 == 0 && i != 1) {
					result.append(",");
				}
				result.append(intString.substring(intString.length() - i,
						intString.length() - i + 1));
			}
			result.reverse().append(".").append(
					amString.substring(amString.length() - 2));
		}
		if (flag == 1) {
			return "-" + result.toString();
		} else {
			return result.toString();
		}
	}

	/**
	 * 将分为单位的转换为元 （除100） 例如 111123133 转换完为 1111231.33
	 * 
	 * @param amount
	 * @return String
	 */
	public static String changeF2Y(String amount) {
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			return "金额格式有误";
		}
		return BigDecimal.valueOf(Long.valueOf(amount)).divide(
				new BigDecimal(100)).toString();
	}

	/**
	 * 将元为单位的转换为分 （乘100） 例如 111123133L转换完为 11112313300
	 * 
	 * @param amount
	 * @return String
	 */
	public static String changeY2F(Long amount) {
		return BigDecimal.valueOf(amount).multiply(new BigDecimal(100))
				.toString();
	}

	/**
	 * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额 例如1,111,231.33 转换完为 111123133
	 * 1111231.33转换完为111123133 111123133转换完为11112313300
	 * 
	 * @param amount
	 * @return String
	 */
	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
		// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "")
					+ "00");
		}
		return amLong.toString();
	}

	/**
	 * 信息格式化 例如 msg="${0} is good" params = new Object[] {"funky"} return $funky
	 * is good
	 * 
	 * @param msg
	 * @param params
	 * @return String
	 */
	public static String msgFormat(final String msg, final Object[] params) {
		return MessageFormat.format(msg, params);
	}

	/**
	 * 随机生成数字的UUID
	 * 
	 * @return String
	 */
	public static String generateDigitalUUID() {
		UUID uuid = UUID.randomUUID();
		long high = uuid.getMostSignificantBits();
		if (high < 0) {
			high += Long.MAX_VALUE;
		}
		long low = uuid.getLeastSignificantBits();
		if (low < 0) {
			low += Long.MAX_VALUE;
		}
		return new StringBuilder().append(high).append(low).toString();
	}

	/**
	 * 随机生成UUID
	 * 
	 * @return String
	 */
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase(Locale.getDefault());
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return String
	 */
	public static String getFileExtension(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int i = fileName.lastIndexOf('.');
			if ((i >= 0) && (i < (fileName.length() - 1))) {
				return fileName.substring(i + 1);
			}
		}
		return "";
	}

	/**
	 * 根据url，取得一级域名
	 * 例如url=http://www.dongaoyx.com/adfadf/adf.shtml，返回dongaoyx.com
	 * 
	 * @param url
	 * @return String
	 */
	public static String getTopDomainNameFromUrl(final String url) {
		Pattern p = Pattern.compile(
				"(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(url);
		matcher.find();
		return matcher.group();
	}

	/**
	 * 根据url，取得完整的主机名
	 * 例如url=http://www.dongaoyx.com/adfadf/adf.shtml，返回www.dongaoyx.com
	 * 
	 * @param url
	 * @return String
	 */
	public static String getHostnameFromUrl(final String url) {
		Pattern p = Pattern.compile(
				"[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(url);
		matcher.find();
		return matcher.group();
	}

	/**
	 * 完成类似php里的addslashes功能
	 * 
	 * @param str
	 * @return String
	 */
	public static String addSlashes(final String str) {
		if (str == null) {
			return "";
		}

		StringBuffer s = new StringBuffer(str);
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\'') {
				s.insert(i++, '\\');
			}
		}

		return s.toString();

	}

	/**
	 * 字符串转集合类 ids=1,2,3,55,5，则返回生成Long型的List
	 * 
	 * @param ids
	 * @return List<Long>
	 */
	public static List<Long> idsToList(final String ids) {
		if (ids == null || ids.trim().equals("")) {
			return null;
		}

		String idsClean = stripLastStr(ids, ",");
		String[] elements = idsClean.split(",");
		if (elements == null || elements.length < 1) {
			return null;
		}

		List<Long> ret = new ArrayList<Long>();

		for (String e : elements) {
			e = e.trim();
			Long val = Long.valueOf(e);
			if (!ret.contains(val)) {
				ret.add(val);
			}
		}

		return ret;
	}

	/**
	 * 字符串转集合类 ids=1,2,3,55,5，则返回生成Integer型的List
	 * 
	 * @param ids
	 * @return List<Integer>
	 */
	public static List<Integer> idsToIntList(final String ids) {
		if (ids == null || ids.trim().equals("")) {
			return null;
		}

		String idsClean = stripLastStr(ids, ",");
		String[] elements = idsClean.split(",");
		if (elements == null || elements.length < 1) {
			return null;
		}
		List<Integer> ret = new ArrayList<Integer>();
		for (String e : elements) {
			ret.add(Integer.valueOf(e));
		}
		return ret;
	}

	/**
	 * 去掉某个字符串里包含的最后某个字符串
	 * 
	 * @param srcStr
	 * @param toStrip
	 * @return String
	 */
	public static String stripLastStr(final String srcStr, final String toStrip) {
		if (!isNotEmpty(srcStr) || !isNotEmpty(toStrip)) {
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

	/**
	 * 去掉某个字符串里包含的第一个某个字符串
	 * 
	 * @param srcStr
	 * @param toStrip
	 * @return String
	 */
	public static String stripFirstStr(final String srcStr, final String toStrip) {
		if (!isNotEmpty(srcStr) || !isNotEmpty(toStrip)) {
			return null;
		}
		if (srcStr.length() <= toStrip.length()) {
			return null;
		}
		return srcStr.substring(toStrip.length(), srcStr.length());
	}

	/**
	 * 对url进行编码，采用UTF-8
	 * 
	 * @param url
	 * @return String
	 */
	public static String encodeUrl(final String url) {
		return encodeUrl(url, null);
	}

	/**
	 * 通过指定编码，对url进行编码
	 * 
	 * @param url
	 * @param encoding
	 * @return
	 */
	public static String encodeUrl(String url, String encoding) {
		if (url == null || url.trim().equals("")) {
			return url;
		}
		if (encoding == null) {
			encoding = "UTF-8";
		}
		String ret = null;
		try {
			ret = URLEncoder.encode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 对url进行解码，按照指定的字符集
	 * 
	 * @param url
	 * @param encoding
	 * @return String
	 */
	public static String decodeUrl(final String url, final String encoding) {
		if (url == null || url.trim().equals("")) {
			return url;
		}
		// java规范的url编码在%后应该是十六进制数。%u1234这种情况可能是用javascript的escape编码导致的。
		// 如果在%后既不是十六进制数也不是u的，那么需要另找原因另作处理。
		if (url.indexOf("%u") >= 0) {
			return urlUnescape(url);
		}
		String ret = null;
		try {
			ret = URLDecoder.decode(url, encoding == null ? "utf-8" : encoding);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	/**
	 * 采用utf-8 对url进行解码
	 * 
	 * @param url
	 * @return String
	 */
	public static String decodeUrl(final String url) {
		return decodeUrl(url, null);
	}

	// only used by method urlUnescape
	private static final byte[] UNICODE_VAL = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	/**
	 * 用于将javascript的escape方法产生的unicode编码转换为utf-8编码。例如：“%u4E2Du%u6587”转换为“中文”。
	 * 
	 * @param s
	 * @return String
	 */
	private static String urlUnescape(final String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if (ch == '+') { // + : map to ' '
				sbuf.append(' ');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-'
					|| ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*'
					|| ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 1)];
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 2)];
					i += 2;
				} else { // %uXXXX : map to unicode(XXXX)
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 2)];
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 3)];
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 4)];
					cint = (cint << 4) | UNICODE_VAL[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			}
			i++;
		}
		return sbuf.toString();
	}

	/**
	 * 生成学习卡和赠卡的随即密码 同时，学员在找回密码时，系统自动为其重新设定随机密码 为了简单，随机密码都采用数字，并且固定长度为6位
	 * 
	 * @return String
	 */
	public static String createPWD() {
		StringBuilder pwd = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			pwd.append(DIGITAL_PWD_TABLE[(int) (DIGITAL_PWD_TABLE.length * Math
					.random())]);
		}
		return pwd.toString();
	}

	/**
	 * 生成新卡的前3位卡段号码，只有在生成新卡时才使用
	 * 
	 * @return String
	 */
	public static String createCardBatchCode() {
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < CARD_BATCH_CODE_LENGTH; i++) {
			code
					.append(CARDBATCH_CODE_TABLE[(int) (CARDBATCH_CODE_TABLE.length * Math
							.random())]);
		}
		return code.toString();
	}

	/**
	 * 生成优惠码 13位 YH YYYY MM XXXXX
	 * 
	 * @return String
	 */
	public static String createCouponCode() {
		StringBuilder code = new StringBuilder();
		code.append("YH").append(CalendarUtil.getYear()).append(
				String.format("%1$,02d", CalendarUtil.getMonth()));
		for (int i = 0; i < COUPON_CODE_LENGTH - 8; i++) {
			code.append(COUPONCODE_TABLE[(int) (COUPONCODE_TABLE.length * Math
					.random())]);
		}
		return code.toString();
	}

	/**
	 * 把秒转换成 时:分:秒 格式的时长信息
	 * 
	 * @param seconds
	 * @return String
	 */
	public static String getTimeDurationLabel(final Integer seconds) {
		if (seconds == null || seconds.intValue() == 0) {
			return "0:00:00";
		} else {
			return String.format("%01d:%02d:%02d", seconds
					/ (SECONDS_IN_MINUTE * MINUTES_IN_HOUR),
					(seconds % (SECONDS_IN_MINUTE * MINUTES_IN_HOUR))
							/ SECONDS_IN_MINUTE, seconds % SECONDS_IN_MINUTE);
		}
	}

	/**
	 * 根据某张卡的次序，生成卡号<br/>
	 * 例如，卡次序为1，返回0000001 生成固定长度的卡号 我们的赠卡的规则是 2位年号 ＋ a或者b(a表示职称|b表示注会) ＋ 6位数字
	 * 我们的学习卡规则是 3位卡段号 ＋ 1位的数字（表示它是哪种面值） ＋ 7位数字
	 * 
	 * @param sequence
	 * @return String
	 */
	public static String createCardNo(final String sequence) {
		StringBuffer cardNo = new StringBuffer();
		for (int i = 0; i < CARD_NO_LENGTH - sequence.length(); i++) {
			cardNo.append("0");
		}
		cardNo.append(sequence);

		return cardNo.toString();
	}

	public static String getTxtWithoutHTMLElement(final String element) {
		if (null == element || "".equals(element.trim())) {
			return element;
		}

		Pattern pattern = Pattern.compile("<[^<|^>]*>");
		Matcher matcher = pattern.matcher(element);
		StringBuffer txt = new StringBuffer();
		while (matcher.find()) {
			String group = matcher.group();
			if (group.matches("<[\\s]*>")) {
				matcher.appendReplacement(txt, group);
			} else {
				matcher.appendReplacement(txt, "");
			}
		}
		matcher.appendTail(txt);
		return txt.toString();
	}

	public static String getTxtWithoutHTMLElementsExceptIMG(final String element) {
		if (null == element || "".equals(element.trim())) {
			return element;
		}

		Pattern pattern = Pattern.compile("<[^<|^>]*>");
		Matcher matcher = pattern.matcher(element);
		StringBuffer txt = new StringBuffer();
		while (matcher.find()) {
			String group = matcher.group();
			if (group.matches("<table[^<][^>]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("<tbody[^>]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("<tr[^>]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("<td[^>]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("</td>") || group.matches("</tr>")
					|| group.matches("</tbody>") || group.matches("</table>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("<img[^>]*>")) {
				matcher.appendReplacement(txt, group);
			} else if (group.matches("<[\\s]*>")) {
				matcher.appendReplacement(txt, group);
			} else {
				matcher.appendReplacement(txt, "");
			}
		}
		matcher.appendTail(txt);
		return txt.toString();
	}

	public static String getTxtFromHTMLWithoutScriptStyleForm(
			final String htmlString) {
		String textStr = htmlString;

		try {
			textStr = REGEX_PATTERN_HTML_STYLE.matcher(textStr).replaceAll(""); // 过滤STYLE标签
			textStr = REGEX_PATTERN_HTML_SCRIPT.matcher(textStr).replaceAll(""); // 过滤script标签
			textStr = REGEX_PATTERN_HTML_FORM.matcher(textStr).replaceAll(""); // 过滤FORM标签
			textStr = REGEX_PATTERN_HTML_A.matcher(textStr).replaceAll("");// 过滤A标签
		} catch (Exception e) {
			e.getMessage();
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 去除html代码，目前还没有被使用，仅仅是个sample code
	 * 
	 * @param inputString
	 * @return String
	 */
	public static String htmltoText(final String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern pScript;
		java.util.regex.Matcher mScript;
		java.util.regex.Pattern pStyle;
		java.util.regex.Matcher mStyle;
		java.util.regex.Pattern pHtml;
		java.util.regex.Matcher mHtml;
		java.util.regex.Pattern pBa;
		java.util.regex.Matcher mBa;

		try {
			String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regExHtml = "<[^>]+>"; // 定义HTML标签的正则表达式
			String patternStr = "&nbsp;|[\\t\\n\\f\\r]";// 过滤掉\html空格，但保留文中空格（所以不用"\\s+"）。但其实中文字符之间的空格也可以去掉
			// \F\IXME;

			pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
			mScript = pScript.matcher(htmlStr);
			htmlStr = mScript.replaceAll(""); // 过滤script标签
			pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
			mStyle = pStyle.matcher(htmlStr);
			htmlStr = mStyle.replaceAll(""); // 过滤style标签

			pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
			mHtml = pHtml.matcher(htmlStr);
			htmlStr = mHtml.replaceAll(""); // 过滤html标签

			pBa = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
			mBa = pBa.matcher(htmlStr);
			htmlStr = mBa.replaceAll(""); // 过滤空格

			textStr = htmlStr;

		} catch (Exception e) {
			e.getMessage();
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 判断是否合法域名
	 * 
	 * @param domain
	 * @return boolean
	 */
	public static boolean isValidDomain(final String domain) {
		if (domain == null || domain.trim().equals("")) {
			return false;
		}

		return isRegexMatch(domain, REGEX_DOMAIN_PATTERN);
	}

	/**
	 * 根据pattern(正则)，判断input合法性
	 * 
	 * @param input
	 * @param pattern
	 * @return boolean
	 */
	private static boolean isRegexMatch(final String input, final String pattern) {
		if (input == null || pattern == null) {
			return false;
		}
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		return m.matches();
	}

	/**
	 * 判断某个值是否是合法的邮件地址
	 * 
	 * @param buf
	 * @return boolean
	 */
	public static boolean isEmail(final String buf) {
		if (buf == null || buf.trim().equals("")) {
			return false;
		}

		return isRegexMatch(buf, REGEX_EMAIL_PATTERN);
	}

	/**
	 * 判断某个值是否是合法的日期格式 (格式例如：2010-01-01)
	 * 
	 * @param buf
	 * @return isDate
	 */
	public static boolean isDate(final String buf) {
		if (buf == null || buf.trim().equals("")) {
			return false;
		}

		return isRegexMatch(buf, REGEX_DATE_PATTERN);
	}

	/**
	 * 判断IP是否合法
	 * 
	 * @param buf
	 * @return boolean
	 */
	public static boolean isValidIp(final String buf) {
		if (buf == null || buf.trim().equals("")) {
			return false;
		}

		return isRegexMatch(buf, REGEX_IP_PATTERN);
	}

	/**
	 * 判断某个值是否是个合法的手机号码
	 * 
	 * @param buf
	 * @return boolean
	 */
	public static boolean isMobile(final String buf) {
		if (buf == null) {
			return false;
		}
		if (!validateNum(buf)) {
			return false;
		}
		if (buf.length() == VALID_MOBILE_NO_LENGTH) {
			if (buf.startsWith("13")) {
				return true;
			} else if (buf.startsWith("15")) {
				return true;
			} else if (buf.startsWith("14")) {
				return true;
			} else {
				return buf.startsWith("18");
			}
		} else {
			return false;
		}
		// else if (buf.length() == 8) { // 小灵通的号是8位，但我们目前短信通道不支持小灵通
		// return true;
		// } else if (buf.length() == VALID_MOBILE_NO_LENGTH + 1) {
		// return buf.startsWith("0");
		// }
	}

	/**
	 * 把url "http://test.dongao.com//aa/bb/c.html"中多余的/符号去掉， 返回
	 * "http://test.dongao.com/aa/bb/c.html"
	 * 
	 * @param url
	 * @return String
	 */
	public static String stripUrlSlash(final String url) {

		if (url.indexOf(HTTP_METHOD_STR) == -1) {
			return url;
		}

		String m = url.substring(url.indexOf(HTTP_METHOD_STR)
				+ HTTP_METHOD_STR.length());

		String ret = HTTP_METHOD_STR + m.replaceAll("//", "/");

		return ret;

	}

	/**
	 * 隐藏邮箱
	 * 
	 * @param email
	 * @return String
	 */
	public static String getEmailSecret(final String email) {
		if (email != null) {
			int p1 = email.indexOf("@");
			if (p1 > 0) {
				return email.substring(0, p1) + "@xxxx";
			}
		}
		return email;
	}

	/**
	 * 隐藏手机号
	 * 
	 * @param phone
	 * @return String
	 */
	public static String getPhoneSecret(final String phone) {
		if (phone == null || phone.length() < 7) {
			return phone;
		} else {
			return phone.substring(0, phone.length() - 7) + "xxxx"
					+ phone.substring(phone.length() - 3, phone.length());
		}
	}

	/**
	 * 隐藏卡号
	 * 
	 * @param ssn
	 * @return String
	 */
	public static String getIdCardSecret(final String ssn) {
		if (!isNotEmpty(ssn)) {
			return ssn;
		} else if (ssn.length() < 5) {
			return ssn;
		} else {
			if (ssn.length() == 15 || ssn.length() == 18) {
				return ssn.substring(0, ssn.length() - 12) + "xxxx"
						+ ssn.substring(ssn.length() - 4, ssn.length());
			} else {
				return ssn.substring(0, ssn.length() - 5) + "xxxx"
						+ ssn.substring(ssn.length() - 3, ssn.length());
			}
		}
	}

	/**
	 * 隐藏地址
	 * 
	 * @param address
	 * @return String
	 */
	public static String getAddressSecret(final String address) {
		if (address == null || address.length() < 8) {
			return address;
		}
		return address.substring(0, 8) + "xxxxxxxxxx";
	}

	/**
	 * 将 hh:mm:ss(时：分：秒) 格式的时间转化为秒数
	 * 
	 * @param hms
	 * @return Long
	 */
	public static Long convertHMSToSeconds(final String hms) {
		Long totalSeconds = 0L;
		if (hms != null) {
			String[] s = hms.split(":");
			totalSeconds += Long.parseLong(s[0]) * SECONDS_IN_MINUTE
					* MINUTES_IN_HOUR;
			totalSeconds += Long.parseLong(s[1]) * SECONDS_IN_MINUTE;
			totalSeconds += Long.parseLong(s[2]);
		}
		return totalSeconds;
	}

	/**
	 * 字符串前缀补0，长度达不到指定位数时前面补0
	 * 
	 * @param str
	 * @param totalBits
	 *            总位数
	 * @return String
	 */
	public static String fillPrefixZero(String str, int totalBits) {
		if (str == null) {
			str = "";
		}

		StringBuilder result = new StringBuilder();
		while (result.length() < (totalBits - str.length())) {
			result.append("0");
		}

		return result.append(str).toString();
	}

	/**
	 * 去除全部空格（包括<b>中文全角空格</b>，英文空格，换行，html空格即&nbsp;）
	 * 
	 * @param str
	 *            原字符串
	 * @return String
	 */
	public static String trimQaSpaces(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return str.replaceAll("\\s*|\u3000|&nbsp;", ""); // \u3000指中文全角空格,
		// \s指英文空格和换行(\r\n)
	}

	/**
	 * 去除前后的空格（包括<b>中文全角空格</b>，英文空格，换行）
	 * 
	 * @param str
	 *            原字符串
	 * @return String
	 */
	public static String trimWhiteSpaces(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return str.replaceAll("^[\u3000,\\s]*|[\u3000,\\s]*$", ""); // \u3000指中文全角空格,
		// \s指英文空格和换行(\r\n)
	}

	/**
	 * 去除字符串中间的空格
	 * 
	 * @param str
	 *            原字符串
	 * @return String
	 */
	public static String trimMiddleSpaces(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.replaceAll("\\s*|\u3000", "");
	}

	/**
	 * 将集合转换成用指定的分隔符分隔的字符串
	 * 
	 * @param coll
	 * @param delimiter
	 *            (分隔符)
	 * @return String
	 */
	public static String converToDelimitedStr(final Collection<?> coll,
			final String delimiter) {
		return converToDelimitedStr(coll, delimiter, "", "");
	}

	/**
	 * 将集合转换成用指定的分隔符分隔的字符串 例如 (list,",","#","!")输出为 #1!,#2!,#3!
	 * 
	 * @param coll
	 * @param delimiter
	 *            (分隔符)
	 * @param prefix
	 *            (头)
	 * @param suffix
	 *            (尾)
	 * @return String
	 */
	public static String converToDelimitedStr(final Collection<?> coll,
			final String delimiter, final String prefix, final String suffix) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}
		StringBuilder resultBuilder = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			resultBuilder.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				resultBuilder.append(delimiter);
			}
		}
		return resultBuilder.toString();
	}

	/**
	 * 截取字符串前面若干长度，并可选择性增加“后缀”（如 '...'）
	 * 
	 * @param src
	 *            源字符串
	 * @param limitLength
	 *            限制长度
	 * @param surfix
	 *            截取后添加的“后缀”， 如果源字符串小于限制长度，将不添加此“后缀”。null表示，无后缀
	 * @return String
	 */
	public static String truncateString(String src, int limitLength,
			String surfix) {
		if (isNotEmpty(src) && limitLength > 0 && src.length() > limitLength) {
			String ret = src.substring(0, limitLength);

			if (isNotEmpty(surfix)) {
				ret += surfix;
			}

			return ret;
		}

		return src;
	}

	/**
	 * 截取字符串前面若干长度，并可选择性增加（ '...'），可供列表页显示用
	 * 
	 * @param src
	 *            源字符串
	 * @return String
	 */
	public static String truncateTiltle(String src) {
		return truncateString(src, TITLE_LENGTH_IN_LIST, "...");
	}

	/**
	 * 转码 从charsetFrom编码 转到 charsetTo编码
	 * 
	 * @param src
	 * @param charsetFrom
	 * @param charsetTo
	 * @return String
	 */
	public static String convertCharset(String src, String charsetFrom,
			String charsetTo) {
		if (isNotEmpty(src) && isNotEmpty(charsetFrom) && isNotEmpty(charsetTo)) {
			String ret = null;

			try {
				ret = new String(src.getBytes(charsetFrom), charsetTo);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (ret != null) {
				return ret;
			}
		}
		return src;
	}

	/**
	 * 查找字符串中是否包含给定的关键字正则
	 * 
	 * @param oriStr
	 * @param keywordRegEx
	 * @return boolean
	 */
	public static boolean containKeyword(final String oriStr,
			final String keywordRegEx) {
		if (oriStr == null || keywordRegEx == null) {
			return false;
		}
		Pattern pat = Pattern.compile(keywordRegEx, Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(oriStr);

		return mat.find();
	}
}
