package com.dongao.core.mycomponent.templateSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * List支持多个，否则仅支持一个
 * @author zhangzhen
 */
public class TemplateSourceDto implements Serializable{
	private static final long serialVersionUID = 6711849116636150642L;
	
	/**group1: 只有url属性 但是不能href*/
	private List<CssSource> css=new ArrayList<CssSource>();
	private List<JsSource> js=new ArrayList<JsSource>();
	private BannerSource banner;
	
	/**group2:只有url属性 且可以选择性href*/
	private LogoSource logo;
	private MobileSpreadSource mobilespread;
	private StudyFlowSource studyflow;
	
	/**group3:有content属性或者url属性  url优先  但是不能href*/
	private ContactSource contact;
	
	/**group4:只有content属性:支持文本编辑器*/
	private AnswerSource  answer;
	private DialogSource dialog;
	private ExplainSource explain;
	private OtherSource other;
	private RightLowerSource rightlower;
	private CourseSpreadSource coursespread;
	
	/**group5:具有多属性的model*/
	private List<BookSpreadSource> bookspread=new ArrayList<BookSpreadSource>();
	private List<NoticeSource> notice=new ArrayList<NoticeSource>();
	private List<FriendlyLinkSource> friendlylink=new ArrayList<FriendlyLinkSource>();
	
	private SeoSource seo;
	public List<CssSource> getCss() {
		return css;
	}
	public void setCss(List<CssSource> css) {
		this.css = css;
	}
	public List<JsSource> getJs() {
		return js;
	}
	public void setJs(List<JsSource> js) {
		this.js = js;
	}
	public BannerSource getBanner() {
		return banner;
	}
	public void setBanner(BannerSource banner) {
		this.banner = banner;
	}
	public LogoSource getLogo() {
		return logo;
	}
	public void setLogo(LogoSource logo) {
		this.logo = logo;
	}
	public MobileSpreadSource getMobilespread() {
		return mobilespread;
	}
	public void setMobilespread(MobileSpreadSource mobilespread) {
		this.mobilespread = mobilespread;
	}
	public StudyFlowSource getStudyflow() {
		return studyflow;
	}
	public void setStudyflow(StudyFlowSource studyflow) {
		this.studyflow = studyflow;
	}
	public ContactSource getContact() {
		return contact;
	}
	public void setContact(ContactSource contact) {
		this.contact = contact;
	}
	public AnswerSource getAnswer() {
		return answer;
	}
	public void setAnswer(AnswerSource answer) {
		this.answer = answer;
	}
	public DialogSource getDialog() {
		return dialog;
	}
	public void setDialog(DialogSource dialog) {
		this.dialog = dialog;
	}
	public ExplainSource getExplain() {
		return explain;
	}
	public void setExplain(ExplainSource explain) {
		this.explain = explain;
	}
	public OtherSource getOther() {
		return other;
	}
	public void setOther(OtherSource other) {
		this.other = other;
	}
	public RightLowerSource getRightlower() {
		return rightlower;
	}
	public void setRightlower(RightLowerSource rightlower) {
		this.rightlower = rightlower;
	}
	public CourseSpreadSource getCoursespread() {
		return coursespread;
	}
	public void setCoursespread(CourseSpreadSource coursespread) {
		this.coursespread = coursespread;
	}
	public List<BookSpreadSource> getBookspread() {
		return bookspread;
	}
	public void setBookspread(List<BookSpreadSource> bookspread) {
		this.bookspread = bookspread;
	}
	public List<NoticeSource> getNotice() {
		return notice;
	}
	public void setNotice(List<NoticeSource> notice) {
		this.notice = notice;
	}
	public List<FriendlyLinkSource> getFriendlylink() {
		return friendlylink;
	}
	public void setFriendlylink(List<FriendlyLinkSource> friendlylink) {
		this.friendlylink = friendlylink;
	}
	public SeoSource getSeo() {
		return seo;
	}
	public void setSeo(SeoSource seo) {
		this.seo = seo;
	}
	
	
}
