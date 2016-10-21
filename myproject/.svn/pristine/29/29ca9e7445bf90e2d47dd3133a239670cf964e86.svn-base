<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户表（样例）管理</title>
	<#include "/common/global_css.ftl">
	<link href="${request.getContextPath()}/css/jbox/jbox_blue.css" rel="stylesheet" type="text/css" id="jbox"/>
	<link href="${request.getContextPath()}/css/datatable/datatable.css" rel="stylesheet" type="text/css"/>
	<link href="${request.getContextPath()}/css/chosen/chosen.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<#include "/common/top.ftl">
	<div class="container">
		<#include "/common/left.ftl">
	    <div id="main">
	    	<div class="position"><a>基础数据管理</a> > 用户表（样例）列表</div>
	    	<div class="search_container">
        		<form class="search_c" id="searchForm">
        			<input type="hidden" id="userButton" value="${userButton}"/>
        				<div class="sc">
	            			编号：<input type="text" class="word" name="search_EQ_id">
	            		</div>
        				<div class="sc">
	            			登录名：<input type="text" class="word" name="search_EQ_loginName">
	            		</div>
        				<div class="sc">
	            			密码：<input type="text" class="word" name="search_EQ_password">
	            		</div>
        				<div class="sc">
	            			登录时间：<input type="text" class="word" name="search_EQ_loginDate">
	            		</div>
        				<div class="sc">
	            			类型：
	            			<select  name="search_EQ_type" class="chosen" data-placeholder="选择类型">
								<option value="" selected></option>
								<option value="1" >系统管理员</option>
								<option value="2" >学员</option>
							</select>
	            		</div>
                	<i class="search_btn" id="searchBtn"></i>
                	<i class="reset_btn" id="searchResetBtn"></i>
            	</form>
        	</div>
	    	<div class="header header_no_martop">
	        	<div class="create"><a href="javascript:;" onclick="toAdd()">新增</a></div>
	        	<div class="del"><a href="javascript:;" id="delBtn" targetUrl="${request.getContextPath()}/sample/sampleUser/batchDeleteSampleUser">删除</a></div>
	     	</div>
	        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="data_Table" id="sampleUser_list">
            	<thead>
            		<tr class="header_title">
		            	<td width="3%"><input type="checkbox" name="checkAllT" id="checkALLT" /></td>
		                	<td width="10%">编号</td>
		                	<td width="10%">登录名</td>
		                	<td width="10%">密码</td>
		                	<td width="10%">登录时间</td>
		                	<td width="10%">类型</td>
						<td width="8%">操作</td>
	                </tr>
	            </thead>
	        </table>
	    </div>
	</div>
	<#include "/common/bottom.ftl">
	<script type="text/javascript" src="${request.getContextPath()}/js/datatable/jquery.dataTables.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/jbox/jquery.jBox.src.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/jbox/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/rbcList.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/strUtils/dateUtil.js"></script>
	<script type="text/javascript" src="${request.getContextPath()}/js/utils/chosen/chosen.jquery.min.js"></script>
	<script type="text/javascript">
		function toAdd() {
			jBox("iframe:${request.getContextPath()}/sample/sampleUser/toAddSampleUser", {
				title: "添加用户表（样例）",
			    width: 600,
				id:"sampleUserAddForm",
			    height: 600,
				buttons: {}
			});
		}
		
		function toEdit(id) {
			jBox("iframe:${request.getContextPath()}/sample/sampleUser/editSampleUser/" + id, {
				title: "修改用户表（样例）",
			    width: 600,
				id:"sampleUserEditForm",
			    height: 600,
				buttons: {}
			});
		}
		var userButton;

		$(function() {
			userButton = $("#userButton").val();
		
			var columns = [
		    	{"mDataProp" : null, "bSortable" : false,
					"fnCreatedCell" : function(nTd, sData,oData, iRow, iCol){
						$(nTd).html("<input type=\"checkbox\" class=\"chk_list\" value=\"" + oData.id + "\">");
					}
				},
						{"mDataProp" : "id", "bSortable" : false},					
						{"mDataProp" : "loginName", "bSortable" : false},					
						{"mDataProp" : "password", "bSortable" : false},					
					{"mDataProp" : "loginDate", "bSortable" : false,
	            	  "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
							var d = new Date();
							d.setTime(oData.loginDate);
							$(nTd).html(d.format("yyyy-MM-dd  hh:mm"));
						}
	            	},
						{"mDataProp" : "type", "bSortable" : false},					
				{"mDataProp" : null, "bSortable" : false,
					"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
							$(nTd).html("<a class=\"editicon\"  href='javascript:;'  onclick=\"toEdit('"+oData.id+"')\"  title=\"修改\"></a>");
					}
				}
			];
			$.rbc({
				"targetTable" : '#sampleUser_list',
				"tableDataUrl" : '${request.getContextPath()}/sample/sampleUser/customPageAjax',
				"tableColumns" : columns,
				"checkAll" : '#checkALLT',
				"tableHeaderOpers" : '#delBtn',
				"msg" : '${message}'
			});
			
			$('.chosen').chosen({
				"no_results_text":'未找到匹配数据!',
				"width":"160px",
				"allow_single_deselect":true
			});
		});
	</script>
</body>
</html>