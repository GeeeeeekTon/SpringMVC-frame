<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>添加用户表（样例）</title>
	<#include "/common/global_css.ftl">
	<link href="${request.getContextPath()}/css/validform/style.css" rel="stylesheet" type="text/css" media="all"/>
	<link href="${request.getContextPath()}/css/chosen/chosen.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		body{margin:0px;font-size:12px;color:#999;}
		.form{overflow:auto;background:#F7F7F7;height:500px;}
	</style>
</head>
<body>
	<div class="form">
		<form class="addForm" target="_top" id="sampleUserAddForm" action="${request.getContextPath()}/sample/sampleUser/saveSampleUser" method="post">
			<input type="hidden" id="pageContext" value="${request.getContextPath()}" />
			<table border="0" cellspacing="0" cellpadding="0" width="100%" style="margin-top:20px;" class="boxTable boxmar">
					<tr>
		            	<td align="right" style="width:80px"><span class="check">*</span>登录名：</td>
		      			<td style="width:150px">
		      				<input name="loginName" id="loginName" class="cword" value="${(sampleUser.loginName)!''}"  datatype="s2-16" nullmsg="请输入登录名！" errormsg="登录名至少2个字符,最多16个字符！"/>
		            	</td>
		            	<td>
	    					<div class="Validform_checktip"></div>
	    				</td>
		            </tr>
					<tr>
		            	<td align="right" style="width:80px"><span class="check">*</span>密码：</td>
		      			<td style="width:150px">
		      				<input name="password" id="password" class="cword" value="${(sampleUser.password)!''}"  datatype="s2-16" nullmsg="请输入密码！" errormsg="密码至少2个字符,最多16个字符！"/>
		            	</td>
		            	<td>
	    					<div class="Validform_checktip"></div>
	    				</td>
		            </tr>
					<tr>
		            	<td align="right" style="width:80px"><span class="check">*</span>登录时间：</td>
		      			<td style="width:150px">
		      				<input name="loginDate" id="loginDate" class="cword" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" />
		            	</td>
		            	<td>
	    					<div class="Validform_checktip"></div>
	    				</td>
		            </tr>
		            <tr>
		            	<td align="right" style="width:80px"><span class="check">*</span>类型：</td>
		      			<td style="width:150px">
		      				<select  name="type" class="chosen" data-placeholder="选择类型" datatype="s1-2" nullmsg="请输入类型！">
								<option value="" selected></option>
								<option value="1" >系统管理员</option>
								<option value="2" >学员</option>
							</select>
		            	</td>
		            	<td>
	    					<div class="Validform_checktip"></div>
	    				</td>
		            </tr>
	            <tr align="center">
    				<td colspan="3">
    					<input class="btn" type="button" id="saveBtn" value="保存">
    					<input type="button" class="btn dis cancel" id="cancelBtn" value="取消">
    				</td>
  				</tr>
	  		</table>
		</form>
	</div>
	<script type="text/javascript" src="${request.getContextPath()}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/allPage.js" contextPath="${request.getContextPath()}"  id="allPageJs"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/validform/Validform_v5.3.2.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/chosen/chosen.jquery.min.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/my97date/WdatePicker.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#sampleUserAddForm').Validform({
				btnSubmit:"#saveBtn", 
				tiptype:2,
				showAllError:true
			});
			$('.chosen').chosen({
				"no_results_text":'未找到匹配数据!',
				"width":"120px",
				"allow_single_deselect":true
			});
			$('#cancelBtn').click(function(){
				parent.jBox.close(true);
			});
		});
	</script>
</body>
</html>