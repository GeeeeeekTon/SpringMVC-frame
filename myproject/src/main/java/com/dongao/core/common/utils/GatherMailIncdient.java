package com.dongao.core.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;



/**
 * 有一封邮件就需要建立一个ReciveMail对象
 */
public class GatherMailIncdient {
	
	private MimeMessage mimeMessage = null;
	/**
	 * 附件下载后的存放目录
	 */
	private String saveAttachPath = ""; 
	/**
	 * 存放邮件内容
	 */
	private StringBuffer bodytext = new StringBuffer();
	/**
	 * 默认的日前显示格式
	 */
	private String dateformat = "yy-MM-dd HH:mm"; 
	
	private static GatherMailIncdient gatherMailIncdient;
	/**
	 * 邮件链接状态TRUE可以使用、false 不能使用
	 */
	private boolean gmStart = true; 
	public static Folder folder;
	public static Store store;

	static{
		gatherMailIncdient = new GatherMailIncdient();
    }
	
	private GatherMailIncdient(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}
	private GatherMailIncdient() {
	}
	
	public synchronized  static GatherMailIncdient getGatherMailIncdient() {
        if (gatherMailIncdient == null) {  
        	gatherMailIncdient = new GatherMailIncdient();
        }  
       return gatherMailIncdient;
   }


	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/**
	 * 获得发件人的地址和姓名
	 * @return String
	 * @throws Exception
	 */
	public String getFrom() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null)
			from = "";
		String personal = address[0].getPersonal();
		if (personal == null)
			personal = "";
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	/**
	 * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
	 * @param type
	 * @return String
	 * @throws Exception
	 */
	public String getMailAddress(String type) throws Exception {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + email + ">";
					mailaddr += "," + compositeto;
				}
				if(mailaddr!= null && !"".equals(mailaddr)){
					mailaddr = mailaddr.substring(1);
				}
			}
		} else {
			throw new Exception("Error emailaddr type!");
		}
		return mailaddr;
	}

	/**
	 * 获得邮件主题
	 * @return String
	 * @throws MessagingException
	 */
	public String getSubject() throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception exce) {
		}
		return subject;
	}

	/**
	 * 获得邮件发送日期
	 * @return String
	 * @throws Exception
	 */
	public String getSentDate() throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(sentdate);
	}

	/**
	 * 获得邮件正文内容
	 * @return String
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 * @param part
	 * @throws Exception
	 */
	public void getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			//bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		}
	}

	/**
	 * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
	 * @return boolean
	 * @throws MessagingException
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage
				.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * 获得此邮件的Message-ID
	 * @return String
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * 判断此邮件是否已读，如果未读返回返回false,反之返回true
	 * @return boolean
	 * @throws MessagingException
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
	//	System.out.println("flags's length: " + flag.length);
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
	//			System.out.println("seen Message.......");
				break;
			}
		}
		return isnew;
	}

	/**
	 * 判断此邮件是否包含附件
	 * @param part
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
//		@SuppressWarnings("unused")
//		String contentType = part.getContentType();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE))))
					attachflag = true;
				else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * 保存附件
	 * @param part
	 * @throws Exception
	 */
	public void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName.toLowerCase().indexOf("GBK") != -1) {
					     fileName = MimeUtility.decodeWord(fileName);
					} else {
					     fileName = MimeUtility.decodeText(fileName);
					}
//					if(fileName.indexOf("?=")>0 ){
//						fileName = MimeUtility.decodeWord(fileName);
//					}
//					fileName=this.base64Decoder(fileName);

					if (fileName.toLowerCase().indexOf("gb2312") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mpart.getInputStream());
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart);
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null)
							&& (fileName.toLowerCase().indexOf("GB2312") != -1)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent());
		}
	}

	/**
	 * 设置附件存放路径
	 * @param attachpath
	 */
	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * 设置日期显示格式
	 * @param format
	 * @throws Exception
	 */
	public void setDateFormat(String format) throws Exception {
		this.dateformat = format;
	}

	/**
	 * 获得附件存放路径
	 * @return String
	 */
	public String getAttachPath() {
		return saveAttachPath;
	}
	
	/**
	 * 判断是否为已读
	 * @param message
	 * @return boolean
	 * @throws MessagingException
	 */
	private boolean isRead(Message message) throws MessagingException {
	  String [] flags = message.getFlags().getUserFlags();
	  for (String f : flags) {
	   if (f.equals(Flag.SEEN))
	    return true;
	  }
	  return false;
	 }


	/**
	 * 真正的保存附件到指定目录里
	 * @param fileName
	 * @param in
	 * @throws Exception
	 */
	private void saveFile(String fileName, InputStream in) throws Exception {
		String osName = System.getProperty("os.name");
		String storedir = getAttachPath();
		String separator = "";
		if (osName == null)
			osName = "";
		if (osName.toLowerCase().indexOf("win") != -1) {
			separator = "\\";
			if (storedir == null || storedir.equals(""))
				storedir = "c:\\";
		} else {
			separator = "/";
			storedir = "/tmp";
		}
		File storefile = new File(storedir + separator + fileName);
	//	System.out.println("storefile's path: " + storefile.toString());
		// for(int i=0;storefile.exists();i++){
		// storefile = new File(storedir+separator+fileName+i);
		// }
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception("文件保存失败!");
		} finally {
			bos.close();
			bis.close();
		}
	}
	
	/**
	 * 接收邮件 (服务器地址、邮件地址、用户名、密码、附件路径)
	 * @param host
	 * @param emailUrl
	 * @param userName
	 * @param pwd
	 * @param rubbishIsDel
	 * @return List<GatherMailIncdient>
	 * @throws Exception
	 */
	public List<GatherMailIncdient> receiveMail(String host, String emailUrl,
			String userName, String pwd,Integer rubbishIsDel) throws Exception {
		List<GatherMailIncdient> pomList = new ArrayList<GatherMailIncdient>();
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, null);
			URLName urln = new URLName("pop3", host, 110, null, userName, pwd);
			store = session.getStore(urln);
			store.connect();
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);			
			Message message[] = folder.getMessages();
			GatherMailIncdient pmm = null;
			for (int i = 0;i< message.length ;i++) {
				pmm = new GatherMailIncdient((MimeMessage) message[i]);					
				pmm.getMailContent((Part) message[i]);
//				pmm.saveAttachMent((Part) message[i]);
				if(!isRead(message[i])){
					pomList.add(pmm);
				}
				message[i].getFlags().add(Flags.Flag.SEEN);//标记为已读
				//if(rubbishIsDel.equals(1)){
					message[i].setFlag(Flags.Flag.DELETED, true); //删除
				//}
			}
		} catch (Exception e) {			
//			throw new Exception("连接邮件服务失败！");
//			System.out.println("连接邮件服务失败！");
			e.printStackTrace();
		}
		return pomList;
	}

	/**
	 * 接收邮件测试
	 * @param host
	 * @param emailUrl
	 * @param userName
	 * @param pwd
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean checkoutMail(String host, String emailUrl,
			String userName, String pwd) throws Exception {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, null);
			URLName urln = new URLName("pop3", host, 110, null, userName, pwd);
			Store store = session.getStore(urln);
			store.connect();
			store.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean isGmStart() {
		return gmStart;
	}
	public void setGmStart(boolean gmStart) {
		this.gmStart = gmStart;
	}
	
}