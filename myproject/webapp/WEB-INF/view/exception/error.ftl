<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统异常页面</title>
<#include "/common/global_css.ftl">
<link href="${request.getContextPath()}/css/jbox/jbox_orange.css" rel="stylesheet" type="text/css" id="jbox"/>
<style type="text/css">
	body{background:#f7f7f7;font-family:"微软雅黑";color:#000000;}
	body,h5,h4,ul,li {margin: 0px;padding: 0px;}
	a {color: #000000;outline: none;text-decoration:none;}
	a:hover{text-decoration:underline;}
	.clear {width: 0px;height: 0px;line-height: 0px;margin: 0px;padding: 0px;font-size: 0px;font-weight: normal;clear: both;}
	.full{width:100%;border-bottom:1px dashed #ccc;}
	.full h4{width:600px; margin:0px auto;padding:160px 0 77px 100px;font-size:40px;line-height:50px;color:#d6543b;background:url(${request.contextPath}/images/systemError.jpg) no-repeat 0 148px;font-weight:normal;}
	.systemError{width:700px; margin:0px auto; overflow:hidden;}
	.systemError div{width:300px;padding:10px 0 30px 50px;float:left;line-height:24px;}
	.systemError div h5{color:#d6543b;font-size:16px; line-height:40px;font-weight:normal;}
</style>
</head>
<body>
<div class="container">
	<div class="full"><h4> 业务处理违反规则，系统异常！ </h4></div>
	<div class="systemError">
		<input type="hidden" id="pageContext" value="${request.getContextPath()}"/>
        <div><h5>错误提示</h5><p>错误编号：${exceptionInfo.no!''}  <span style="padding-left:20px; font-size:14px; font-weight:bold; color:red;">${exceptionInfo.description!''}<span></p></div>
        <#if (Request.exceptionInfo.urls)??&&(Request.exceptionInfo.urls?size>0)>
        <div><h5>您可以</h5>
	        <ul>
	        	<#list Request.exceptionInfo.urls as obj>
					<li><a href="${obj.url}" target="_blank">${obj.name}</a></li>	
				</#list>
	        </ul>
        </div>
        </#if>
        <span class="clear"></span>
    </div>
</div>
<#include "/common/bottom.ftl">
</body>
<script src="${request.getContextPath()}/js/jquery-1.7.2.min.js"></script>
<script src="${request.getContextPath()}/js/utils/jbox/jquery.jBox.src.js"></script>
<script src="${request.getContextPath()}/js/utils/jbox/jquery.jBox-zh-CN.js"></script>
<script>
	$(function(){
		var browserHeight = document.documentElement.clientHeight-32;	
		$(".container").css("minHeight",browserHeight);
		window.onresize = function(){
	 		 var browserHeight = document.documentElement.clientHeight-32;	
			 $(".container").css("minHeight",browserHeight);
		}
	})
</script>
</html>