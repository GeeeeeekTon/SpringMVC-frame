package com.dongao.core.mycomponent.templateSource;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dongao.core.common.utils.JsonDateSerializer;

/**
 * 公告
 * @author zhangzhen
 *
 */
public class NoticeSource implements ItemplateSource{
	/**标题*/
	private String titile;
	/**超链接*/
	private String href;
	/**显示排序*/
	private Integer order;
	/**order第一的公告，有个简介*/
	private String content;
	/**是否特殊 New图标*/
	private boolean isSpecial;
	/**创建时间*/
	private  Date createDate=new Date();
	
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public boolean isSpecial() {
		return isSpecial;
	}
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		if(createDate!=null){
			this.createDate = createDate;
		}else{
			this.createDate=new Date();
		}
	}
	@Override
	public String toString() {
		return titile+"";
	}
	
}
