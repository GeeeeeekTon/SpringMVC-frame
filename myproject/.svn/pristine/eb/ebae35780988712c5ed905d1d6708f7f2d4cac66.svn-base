package com.dongao.core.mycache.util;

import com.taobao.common.tedis.serializer.HessianSerializer;

/**
 * @author zhangzhen
 *
 */
public class SerializationUtils {
	private static HessianSerializer serializer = new HessianSerializer();

	public static byte[] serialize(Object obj) {
		return serializer.serialize(obj);
	}

	public static Object deserialize(byte[] bytes) {
		return serializer.deserialize(bytes);
	}
	
	public static void main(String[] args) {
//		Area a=new Area();
//		a.setId(1l);a.setName("1");
//		Area b=new Area();
//		b.setId(1l);b.setName("1");
//		byte[] serialize = serialize(a);
//		String encodeToString = Base64.encodeToString(serialize);
//		System.out.println(encodeToString);
//		byte[] decode = Base64.decode(encodeToString);
//		System.out.println(((Area)deserialize(decode)).getId());
		String str=(String) deserialize(serialize(null));
		System.out.println(str.length());
	}
}
