;(function(){
if(typeof openCourseBox!="undefined"&&openCourseBox){//课程分类
	$(".create").click(function(){
		JXJY.showBox("新增继教课程分类",500,0,"弹窗--增加课程分类.html")
	})
	$(".editicon").click(function(){
		var id=$(this).attr("cid")
		JXJY.showBox("修改继教课程分类",500,0,"弹窗--修改课程分类.html")
	})
}	
if(typeof openSortBox!="undefined"&&openSortBox){//继教版本
	$(".create").click(function(){
		JXJY.showBox("新增继教版本",500,0,"弹窗--增加继教版本.html")
	})
	$(".editicon").click(function(){
		JXJY.showBox("修改继教版本",500,0,"弹窗--修改继教版本.html")
	})
}
if(typeof CoursewareBox!="undefined"&&CoursewareBox){//课件管理
	$(".create").click(function(){
		JXJY.showBox("新增课件",540,0,"弹窗--新增课件.html")
	})
}
if(typeof showTip!="undefined"&&showTip){//鼠标经过标题
	$(".data_Table tr:not('.header_title')").each(function(){
		$("td:eq(2) span",this).live("mouseover",function(){
			$(this).find(".title_tip").show();
			$(this).css("zIndex",2)
		}).live("mouseout",function(){
			$(this).find(".title_tip").hide();
			$(".tip_container span").css("zIndex",1);
		})
															   
	})
}
})()