package com.dongao.core.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 发送手机短信的工具类
 * @author chenzhiguo
 *
 */
public class SmsUtil {

	private static final Logger logger=LoggerFactory.getLogger(SmsUtil.class);
	//http://pi.noc.cn/SendSMS.aspx?Msisdn=18610369103&SMSContent=12345667【东奥】&MSGType=5&ECECCID=100387&Password=dongao1107 
	private static final String baseUrl="http://pi.noc.cn/SendSMS.aspx?MSGType=5&ECECCID=100387&Password=dongao1107&";
	
	//次通道，100387001 dao1211 9588另一通道的用户名 密码
	private static final String slaveUrl = "http://pi.noc.cn/SendSMS.aspx?MSGType=5&ECECCID=100387001&Password=dao1211&";
	private static boolean isDebug=false;
	
	/**
	 * 批量发送短信
	 * @param mobiles 手机号码集合
	 * @param message 短信内容
	 */
//	public static void sendMessage(String[] mobiles,String message){
//		for (String mobile : mobiles) {
//			sendMessage(mobile,message);
//		}
//	}
	/**
	 * 发送手机短信
	 * @param mobile 手机号码
	 * @param message 短信内容
	 */
	public static void sendMessage(String mobile,String message,String connectionType){
		try {
			String str=message.replaceAll("\r", "");
			str=str.replaceAll("\n", "");
			str=str.replaceAll("\\s", "");
			//str=URLEncoder.encode(str,"utf8");
			if(isDebug){
				logger.info("系统发送短信给:"+mobile+",短信内容:"+message);
			}else{
			    String urlStr = "";
			    if("2".equals(connectionType)){
			        urlStr=slaveUrl+"Msisdn="+mobile+"&SMSContent="+str;
			    }else {
			        urlStr=baseUrl+"Msisdn="+mobile+"&SMSContent="+str;
                }
				
				logger.info(urlStr);
				URL url = new URL(urlStr);
				URLConnection connection = url.openConnection();
				HttpURLConnection httpConnection = (HttpURLConnection)connection;  
				int responseCode = httpConnection.getResponseCode();  
				if (responseCode == HttpURLConnection.HTTP_OK) {  
					InputStream urlStream = httpConnection.getInputStream();  
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));  
					String sCurrentLine = "";  
					String sTotalString = "";  
					while ((sCurrentLine = bufferedReader.readLine()) != null) {  
						sTotalString += sCurrentLine;  
					}  
					urlStream.close();
					if(sTotalString.contains("1|")){
						logger.info("给"+mobile+"发短信\""+message+"\"成功!");
					}else{
						logger.error("给"+mobile+"发短信\""+message+"\"失败!");
					}
				}else{  
					logger.error("给"+mobile+"发短信\""+message+"\"时短信网关未能正确返回!");
				}  
			}
		} catch (Exception e) {
			logger.error("给"+mobile+"发短信\""+message+"\"时出现错误!", e);
		}
		
	}
	
	public static void main(String[] args) {
	    SmsUtil.sendMessage("13716220360", "验证码：644272","2");
	}
}
