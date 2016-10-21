package com.dongao.core.mycomponent.templateSource;
/**
 * 特殊的javascript设置
 * @author zhangzhen
 *
 */
public class JsSource implements ItemplateSource{
	/**路径地址*/
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return url+"";
	}
	
	
}