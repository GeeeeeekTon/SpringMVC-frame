package com.dongao.core.mycomponent.templateSource;
/**
 * html中的:title 和  meta 信息
 * @author zhangzhen
 *
 */
public class SeoSource implements ItemplateSource{
	private String title;
	private String robots;
	private String author;
	private String description;
	private String keywords;
	private boolean isIncludeDaHead;
	private boolean isIncludeDaTail;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRobots() {
		return robots;
	}
	public void setRobots(String robots) {
		this.robots = robots;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public boolean isIncludeDaHead() {
		return isIncludeDaHead;
	}
	public void setIncludeDaHead(boolean isIncludeDaHead) {
		this.isIncludeDaHead = isIncludeDaHead;
	}
	public boolean isIncludeDaTail() {
		return isIncludeDaTail;
	}
	public void setIncludeDaTail(boolean isIncludeDaTail) {
		this.isIncludeDaTail = isIncludeDaTail;
	}
	
	

}
