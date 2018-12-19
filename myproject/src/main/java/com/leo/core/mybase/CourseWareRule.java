package com.leo.core.mybase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 课件模板规则
 * @author zhangzhen
 *{
    "cwLogoUrl": "/image/image.jpg",
    "mouseEventTimeout": "5",
    "popQuestionsFlag": "true",
    "popSmsFlag": "true",
    "questions": [
        {
            "question": "357321",
            "verifySecond": "70"
        },
        {
            "question": "241207",
            "verifySecond": "200"
        },
        {
            "question": "357281",
            "verifySecond": "510"
        }
    ],
    "result": "success",
    "sms": [
        {
            "smsPopSecond": "65"
        },
        {
            "smsPopSecond": "185"
        },
        {
            "smsPopSecond": "305"
        }
    ]
}
 */
public class CourseWareRule implements Serializable{
	private static final long serialVersionUID = -8677983404955562058L;
	private String cwLogoUrl;
	private String mouseEventTimeout;
	private String popQuestionsFlag;
	private String popSmsFlag;
	private List<Map<String,String>> questions=new ArrayList<Map<String,String>>();//key[question;verifySecond]
	private List<Map<String,String>> sms=new ArrayList<Map<String,String>>();//key[smsPopSecond]
	private String result;
	public String getCwLogoUrl() {
		return cwLogoUrl;
	}
	public void setCwLogoUrl(String cwLogoUrl) {
		this.cwLogoUrl = cwLogoUrl;
	}
	public String getMouseEventTimeout() {
		return mouseEventTimeout;
	}
	public void setMouseEventTimeout(String mouseEventTimeout) {
		this.mouseEventTimeout = mouseEventTimeout;
	}
	public String getPopQuestionsFlag() {
		return popQuestionsFlag;
	}
	public void setPopQuestionsFlag(String popQuestionsFlag) {
		this.popQuestionsFlag = popQuestionsFlag;
	}
	public String getPopSmsFlag() {
		return popSmsFlag;
	}
	public void setPopSmsFlag(String popSmsFlag) {
		this.popSmsFlag = popSmsFlag;
	}
	public List<Map<String, String>> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Map<String, String>> questions) {
		this.questions = questions;
	}
	public List<Map<String, String>> getSms() {
		return sms;
	}
	public void setSms(List<Map<String, String>> sms) {
		this.sms = sms;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	

}
