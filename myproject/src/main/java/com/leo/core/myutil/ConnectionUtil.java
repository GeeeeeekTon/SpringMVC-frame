package com.leo.core.myutil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * @author zhangzhen
 *
 */
public class ConnectionUtil {
	private static final int TIMEOUT_SECONDS = 20;
	public static  String  getReturnString(String contentUrl){
		HttpURLConnection connection =null; 
		InputStream input = null;
		String returnString="";
		try {
			connection=(HttpURLConnection) new URL(contentUrl).openConnection();
			connection.setReadTimeout(TIMEOUT_SECONDS * 1000);
			connection.connect();
			input = connection.getInputStream();
			returnString=IOUtils.toString(input, "UTF-8");
		} catch (Exception e) {
			//e.printStackTrace();
			//return  returnString;
			LogUtils.getErrorLog().error(contentUrl+"出现了异常",e);
			returnString="";
		}finally {
			if(input!=null)
			IOUtils.closeQuietly(input);
			if(connection!=null)
			connection.disconnect();
		}
		return  returnString;
	}
	public static void main(String[] args) {
		System.out.println(getReturnString("http://ip.taobao.com/service/getIpInfo.php?ip=211.151.33.44"));
		System.out.println(100/3);
		System.out.println(Double.parseDouble("0.110")*2+2);
		Boolean flag=true;
		System.out.println(flag+"");
	}
}
