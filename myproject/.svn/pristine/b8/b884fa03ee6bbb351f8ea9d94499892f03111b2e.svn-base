package com.dongao.core.mybase;

import com.dongao.core.myutil.StringUtil;

/**
 * 课程
 * @author zhengyongxiang
 *
 */
@SuppressWarnings("serial")
public class CourseDto implements java.io.Serializable {
	
	/** 课程编号 */
    private String courseId;
    /** 课程名称 */
    private String courseName;
    /** 主讲老师姓名（多个逗号分隔） */
    private String teacherName;
    /** 时长，单位是秒 (TODO 应该是Long类型的)*/
    private String timeLength;
    /** 免费试听地址 */
    private String freeUrl;
    /** 课件介绍链接 */
    private String courseIntroduction;
    /** 课件是否上传完 */
    private Integer createPlan;
    
    private String imgUrl;//课程图片
    
    private String teacherId;//老师Id
    
    private String mobileImgUrl;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTimeLength() {
    	if(timeLength==null||"".equals(timeLength)){
    		return "0";
    	}
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getFreeUrl() {
        return freeUrl;
    }

    public void setFreeUrl(String freeUrl) {
        this.freeUrl = freeUrl;
    }

    public String getCourseIntroduction() {
        return courseIntroduction;
    }

    public void setCourseIntroduction(String courseIntroduction) {
        this.courseIntroduction = courseIntroduction;
    }
    
    public Integer getCreatePlan() {
		return createPlan;
	}

	public void setCreatePlan(Integer createPlan) {
		this.createPlan = createPlan;
	}

	/**
     * 获得课程总时长
     * @return
     */
    public Integer getTotalSeconds() {
    	if (!StringUtil.isNotEmptyString(getTimeLength())) {
    		return 0;
    	}
    	
    	return Integer.valueOf(getTimeLength());
    }
    
    /**
     * 是否是已经完成状态
     * @return
     */
    public boolean isDone() {
    	return 1 == createPlan;
    }

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getMobileImgUrl() {
		return mobileImgUrl;
	}

	public void setMobileImgUrl(String mobileImgUrl) {
		this.mobileImgUrl = mobileImgUrl;
	}
}
