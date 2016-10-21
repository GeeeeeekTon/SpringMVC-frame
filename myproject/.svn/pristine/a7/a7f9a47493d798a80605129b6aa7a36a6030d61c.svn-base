package com.dongao.core.myutil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author zhangzhen
 * 
 *  封装各种随机相关操作
 *
 *  v1.0
 */
public class RandomDataUtil {

	protected static final Random rnd = new Random();

	/**
	 * 返回随机名称, prefix字符串+count位随机数字.
	 */
	public static String randomCountStrByPrefix(String prefix,int count) {
		return prefix + randomNumeric(count);
	}
	/**
	 * 返回随机ID.
	 */
	public static long randomId() {
		return rnd.nextLong();
	}
	/**
	 * 从输入list中随机返回一个对象.
	 */
	public static <T> T randomOne(List<T> list) {
		Collections.shuffle(list);
		return list.get(0);
	}

	/**
	 * 从输入list中随机返回n个对象.
	 */
	public static <T> List<T> randomSome(List<T> list, int n) {
		Collections.shuffle(list);
		return list.subList(0, n);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 */
	public static <T> List<T> randomSome(List<T> list) {
		int size = rnd.nextInt(list.size());
		if (size == 0) {
			size = 1;
		}
		return randomSome(list, size);
	}

	// ---------------------------------------------------------------- string

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the set of characters specified.
	 */
	public static String random(int count, char[] chars) {
		if (count == 0) {
			return "";
		}
		char[] result = new char[count];
		while (count-- > 0) {
			result[count] = chars[rnd.nextInt(chars.length)];
		}
		return new String(result);
	}

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the set of characters specified.
	 */
	public static String random(int count, String chars) {
		return random(count, chars.toCharArray());
	}

	// ---------------------------------------------------------------- range

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the provided range.
	 */
	public static String random(int count, char start, char end) {
		if (count == 0) {
			return "";
		}
		char[] result = new char[count];
		int len = end - start + 1;
		while (count-- > 0) {
			result[count] = (char) (rnd.nextInt(len) + start);
		}
		return new String(result);
	}

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the set of characters whose
	 * ASCII value is between <code>32</code> and <code>126</code> (inclusive).
	 */
	public static String randomAscii(int count) {
		return random(count, (char)32, (char)126);
	}

	/**
	 * Creates random string that consist only of numbers.
	 */
	public static String randomNumeric(int count) {
		return random(count, '0', '9');
	}

	/**
	 * Creates random string whose length is the number of characters specified.
	 * Characters are chosen from the multiple sets defined by range pairs.
	 * All ranges must be in acceding order.
	 */
	public static String randomRanges(int count, char... ranges) {
		if (count == 0) {
			return "";
		}
		int i = 0;
		int len = 0;
		int lens[] = new int[ranges.length];
		while (i < ranges.length) {
			int gap = ranges[i + 1] - ranges[i] + 1;
			len += gap;
			lens[i] = len;
			i += 2;
		}

		char[] result = new char[count];
		while (count-- > 0) {
			char c = 0;
			int r = rnd.nextInt(len);
			for (i = 0; i < ranges.length; i += 2) {
				if (r < lens[i]) {
					r += ranges[i];
					if (i != 0) {
						r -= lens[i - 2];
					}
					c = (char) r;
					break;
				}
			}
			result[count] = c;
		}
		return new String(result);
	}

	protected static final char[] ALPHA_RANGE = new char[] {'A', 'Z', 'a', 'z'};
	protected static final char[] ALPHA_NUMERIC_RANGE = new char[] {'0', '9', 'A', 'Z', 'a', 'z'};

	/**
	 * Creates random string of characters.
	 */
	public static String randomAlpha(int count) {
		return randomRanges(count, ALPHA_RANGE);
	}

	/**
	 * Creates random string of characters and digits. 
	 */
	public static String randomAlphaNumeric(int count) {
		return randomRanges(count, ALPHA_NUMERIC_RANGE);
	}
	
	public static void main(String[] args) {
		Set<String> set=new HashSet<String>();
		int count=10000;
		for(int i=0;i<count;i++){
			set.add(randomCountStrByPrefix("ewfwe",8));
		}
		System.out.println(count==set.size());
	}

}