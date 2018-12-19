package com.leo.core.myutil;

import java.text.MessageFormat;

/**
 * 听课相关redis key
 * @author zhengyongxiang
 *
 */
public class WatchKeyUtils {
	static final String WATCHING = "watching:${0}";
	/** 正在听课参数-课件 */
	public static final String WATCHING_CW = "cw";
	/** 正在听课参数-最后访问时间 */
	public static final String WATCHING_LASTTIME = "lastTime";
	
	/** 最后一次听得课件参数-年度 */
	public static final String LAST_WATCHED_YEAR = "year";
	/** 最后一次听得课件参数-课程 */
	public static final String LAST_WATCHED_COURSEID = "courseId";
	/** 最后一次听得课件参数-课件 */
	public static final String LAST_WATCHED_COURSEWAREID = "coursewareId";
	
	/** Hash 已听时长-课程时长 ${0：周期编号} ${1：用户编号} ${2：年度}*/
	static final String WATCHED_COURSE = "watched:course:${0}:${1}:${2}";
	/** Hash 已听时长-课件时长 ${0：周期编号} ${1：用户编号} ${2：年度}*/
	static final String WATCHED_COURSEWARE = "watched:cw:${0}:${1}:${2}";
	/** Hash 记录上次听课的时间点(时间轴) ${0：用户编号} ${1：年度} */
	static final String WATCHED_COURSEWARE_LAST_TIME = "watchedLastTime:${0}:${1}";
	/** Hash 记录每门课最后听的是哪门课件 ${0：用户编号} ${1：年度}*/
	static final String WATCHED_COURSE_LAST_COURSEWARE = "watched:course:cw:${0}:${1}";
	/** Hash 最后一次听的课件信息 ${0:用户编号} */
	static final String WATCHED_LAST_COURSERWARE = "watched:lastcw:${0}";
	/** String 暂停在哪个时间 ${0:用户编号} ${1:课件编号} */
	static final String WATCH_PAUSE_AT = "watchPauseAt:${0}:${1}";
	/** String 已暂停的时间 ${0:用户编号} ${1:课件编号} */
	static final String WATCH_PAUSE_SECONDS = "watchPauseSeconds:${0}:${1}";
	/** Set 当天学习的人员 */
	static final String TODAY_STUDY_LIST = "today:study:{0}";
	
	
	
	/**
	 * 获得正在听课参数hash key
	 * @param userId
	 * @return
	 */
	public static String watching(String userId) {
		return MessageFormat.format(WATCHING, userId);
	}
	
	/**
	 * 获得已听课程时长 hash key${0：周期编号} ${1：用户编号} ${2：年度}
	 * @param params
	 * @return
	 */
	public static String watchedCourse(Object... params) {
		return MessageFormat.format(WATCHED_COURSE, params);
	}
	
	/**
	 * 获得已听课件时长 hash key${0：周期编号} ${1：用户编号} ${2：年度}
	 * @param params
	 * @return
	 */
	public static String watchedCourseware(Object... params) {
		return MessageFormat.format(WATCHED_COURSEWARE, params);
	}
	
	/**
	 * 暂停在哪个时间点的key
	 * @param params
	 * @return
	 */
	public static String watchPauseAt(Object... params) {
		return MessageFormat.format(WATCH_PAUSE_AT, params);
	}
	
	/**
	 * 已暂停时间的key
	 * @param params
	 * @return
	 */
	public static String watchPauseSeconds(Object... params) {
		return MessageFormat.format(WATCH_PAUSE_SECONDS, params);
	}
	
	/**
	 * 已听课件的播放时间点（播放器的时间）
	 * @param params
	 * @return
	 */
	public static String watchedCoursewareLastTime(Object... params) {
		return MessageFormat.format(WATCHED_COURSEWARE_LAST_TIME, params);
	}
	
	/**
	 * 每门课最后听的课件编号
	 * @param params
	 * @return
	 */
	public static String watchedCourseLastCourseware(Object... params) {
		return MessageFormat.format(WATCHED_COURSE_LAST_COURSEWARE, params);
	}
	
	/**
	 * 最后一次听的课件信息（包含年度、课程、课件）
	 * @param params
	 * @return
	 */
	public static String watchedLastCourseware(Object... params) {
		return MessageFormat.format(WATCHED_LAST_COURSERWARE, params);
	}
	
	/**
	 * 指定某天听课的学员
	 * @param dayStr
	 * @return
	 */
	public static String todayStudyList(String dayStr) {
		return MessageFormat.format(TODAY_STUDY_LIST, dayStr);
	}
}
