package com.dongao.core.mycomponent.templateSource;

/**
 * 友情链接
 * @author zhangzhen
 *
 */
public class FriendlyLinkSource implements ItemplateSource{
	/**名称*/
	private String titile;
	/**图片地址*/
	private String url;
	/**对应的链接*/
	private String href;
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	@Override
	public String toString() {
		return titile+"";
	}
	
	

}
