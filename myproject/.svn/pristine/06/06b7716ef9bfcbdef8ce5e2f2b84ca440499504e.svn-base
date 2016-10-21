package com.dongao.core.myutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongao.core.mybase.Constants;

import java.security.MessageDigest;

public class Md5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);

    private static String compute(String inStr) {
    	try {
			char[] charArray = inStr.toCharArray();
	
			byte[] byteArray = new byte[charArray.length];
	
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(byteArray);
	
			StringBuffer hexValue = new StringBuffer();
	
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
    	}catch (Exception e) {
    		LOGGER.error("MD5 Error...", e);
    		return "";
    	}
		
	}
	
	/**
	 * 按传入编码进行MD5加密
	 * @param charsetName
	 * @return
	 */
	private static String compute(String inStr, String charsetName){
		try{
			byte[] byteArray = inStr.getBytes(charsetName);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(byteArray);
	
			StringBuffer hexValue = new StringBuffer();
	
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
	
			return hexValue.toString();
		}catch (Exception e) {
			LOGGER.error("MD5 Error...", e);
			return "";
		}
	}
	
	public static String encrypt(final String key) {
		return compute(key);
	}
	
	/**
	 * 按传入编码进行MD5加密
	 * @param key
	 * @param charsetName
	 * @return
	 */
	public static String encrypt(final String key, final String charsetName){
		return compute(key, charsetName);
	}
	
	public static void main(String[] args) {
		System.out.println(Md5Util.encrypt("142427199002272444" + "dakjzx"));
	}
}