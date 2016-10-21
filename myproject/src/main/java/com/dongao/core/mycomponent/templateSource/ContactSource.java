package com.dongao.core.mycomponent.templateSource;

/**
 * 联系方式
 * @author zhangzhen
 *
 */
public class ContactSource implements ItemplateSource{
	/**文本内容*/
	private String content;
	/**图片链接地址*/
	private String url;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
