package com.dongao.core.common.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMail {

	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

	/**
	 * MIME邮件对象
	 */
	private MimeMessage mimeMsg; 
	/**
	 * 邮件会话对象
	 */
	private Session session; 
	/**
	 * 系统属性
	 */
	private Properties props; 
	@SuppressWarnings("unused")
	/**
	 * smtp是否需要认证
	 */
	private boolean needAuth = false;  
	/**
	 * smtp认证用户名
	 */
	private String username = "";
	/**
	 * smtp认证密码
	 */
	private String password = "";
	/**
	 * Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
	 */
	private Multipart mp;  

	public SendMail() {
		// setSmtpHost(getConfig.mailHost);//如果没有指定邮件服务器,就从getConfig类中获取
		// createMimeMessage();
	}

	/**
	 * 设置服务属性
	 * 
	 * @param hostName
	 */
	public void setSmtpHost(String hostName) {
		// System.out.println("设置系统属性：mail.smtp.host = " + hostName);
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
	}

	/**
	 * 创建邮件会话对象
	 * 
	 * @return boolean
	 */
	public boolean createMimeMessage() {
		try {
			// System.out.println("准备获取邮件会话对象！");
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
		} catch (Exception e) {
			// System.err.println("获取邮件会话对象时发生错误！" + e);
			return false;
		}

		// System.out.println("准备创建MIME邮件对象！");
		try {
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();

			return true;
		} catch (Exception e) {
			// System.err.println("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	/**
	 * 设置smtp身份验证
	 * 
	 * @param need
	 */
	public void setNeedAuth(boolean need) {
		// System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	/**
	 * 设置名称和密码
	 * 
	 * @param name
	 * @param pass
	 */
	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param mailSubject
	 * @return boolean
	 */
	public boolean setSubject(String mailSubject) {
		// System.out.println("设置邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			// System.err.println("设置邮件主题发生错误！");
			return false;
		}
	}

	/**
	 * 设置邮件内容
	 * 
	 * @param mailBody
	 * @return boolean
	 */
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);

			return true;
		} catch (Exception e) {
			// System.err.println("设置邮件正文时发生错误！" + e);
			return false;
		}
	}

	/**
	 * 设置邮件附件
	 * 
	 * @param filename
	 * @return boolean
	 */
	public boolean addFileAffix(String filename) {

		// System.out.println("增加邮件附件：" + filename);
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());

			mp.addBodyPart(bp);

			return true;
		} catch (Exception e) {
			// System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
			return false;
		}
	}

	/**
	 * 设置邮件发件人
	 * 
	 * @param from
	 * @return boolean
	 */
	public boolean setFrom(String from) {
		// System.out.println("设置发信人！");
		try {
			mimeMsg.setFrom(new InternetAddress(from)); // 设置发信人
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 设置收件人
	 * 
	 * @param to
	 * @return boolean
	 */
	public boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 设置抄送地址
	 * 
	 * @param copyto
	 * @return boolean
	 */
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,
					(Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @return boolean
	 */
	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			// System.out.println("正在发送邮件....");

			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(Message.RecipientType.TO));
			// transport.send(mimeMsg);
			logger.info("发送邮件成功！");
			transport.close();
			return true;
		} catch (Exception e) {
			logger.error("发送邮件时出错!", e);
			return false;
		}
	}

	/**
	 * 设置邮件信息:服务器地址、邮件地址、用户名、密码、附件路径
	 * 
	 * @param title
	 * @param text
	 * @param host
	 * @param outEmail
	 * @param toEmail
	 * @param userName
	 * @param pwd
	 * @param attachPath
	 */
	public void sendAllMail(String title, String text, String host,
			String outEmail, String toEmail, String userName, String pwd,
			String attachPath) {
		try {
			String mailbody = text;
			setSmtpHost(host);
			this.createMimeMessage();
			this.setNeedAuth(true);
			if (this.setSubject(title) == false)
				return;
			if (this.setBody(mailbody) == false)
				return;
			if (this.setTo(toEmail) == false)
				return;
			if (this.setFrom(outEmail) == false)
				return;
			if (this.addFileAffix(attachPath) == false)
				return;
			this.setNamePass(userName, pwd);

			if (this.sendout() == false)
				return;
		} catch (Exception e) {
			logger.error("设置邮件时出错!", e);
		}
	}

	/**
	 * 设置邮件信息:服务器地址、邮件地址、用户名、密码
	 * 
	 * @param title
	 * @param text
	 * @param host
	 * @param outEmail
	 * @param toEmail
	 * @param userName
	 * @param pwd
	 */
	public void sendAllMail(String title, String text, String host,
			String outEmail, String toEmail, String userName, String pwd) {
		try {
			String mailbody = text;
			setSmtpHost(host);
			this.createMimeMessage();
			this.setNeedAuth(true);
			if (this.setSubject(title) == false)
				return;
			if (this.setBody(mailbody) == false)
				return;
			if (this.setTo(toEmail) == false)
				return;
			if (this.setFrom(outEmail) == false)
				return;
			this.setNamePass(userName, pwd);

			if (this.sendout() == false)
				return;
		} catch (Exception e) {
			logger.error("设置邮件时出错!", e);
		}
	}
}