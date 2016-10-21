(function ($) {
    $.rbc = function (options) {
    	var defaults = {
    		searchForm: "#searchForm", // 搜索表单
    		searchBtn: "#searchBtn", // 搜索按钮
    		searchResetBtn : "#searchResetBtn", // 搜索重置
    		targetTable: null, // 表格ID
    		checkAll : "#checkAll", // 表头中的checkbox
    		tableColumns : [],
    		tableDataUrl : null,
    		dataTableObj : null,
    		tableHeaderOpers : null,
    		checkMaxItem :null
        };
        options = $.extend({}, defaults, options);
        
        jBox.setDefaults({ defaults: { top: '30%', border: 0, opacity: 0.2} });
		
  		if (options.msg != "") {
			$.jBox.tip(options.msg, 'info');
  	  	}
        
        var getCheckArray = function() {
			var tempArray = new Array();
			$("input:checkbox:checked", options.dataTableObj.fnGetNodes()).each(function(i){
				tempArray[i] = $(this).val();
			});
			return tempArray;
		};
		var reloadTable = function() {
			options.dataTableObj.fnClearTable(0);
			options.dataTableObj.fnDraw();
		};
		
        options.dataTableObj = $(options.targetTable).dataTable({
			"bProcessing" : true,
			"bServerSide" : true,
			"sServerMethod" : 'post',
			"bPaginate" : true,
			"bSort" : true,
			"bFilter" : false,
			"bJQueryUI" : false,
			"sPaginationType" : "full_numbers",
			"sDom" : 'rt <"dongao_page"flpi>',
			"iDisplayLength" : 10,
			"aaData" : "list",
			"aLengthMenu" : [ [ 10, 50, 100 ], [ 10, 50, 100 ] ],
			"aoColumns" : options.tableColumns,
	        "sAjaxSource" : options.tableDataUrl + "?" + $(options.searchForm).serialize(),
	        "fnDrawCallback":function(){
	        	JXJY.dataTableCallback();
	        },
			"oLanguage" : {
				"sLengthMenu" : "每页 _MENU_ 条",
				"sZeroRecords" : "",
				"sInfo" : "当前从 _START_ 到 _END_ 条,共 _TOTAL_ 条记录",
				"sInfoEmpty" : "没有找到记录",
				"oPaginate" : {
					"sFirst" : "首页",
					"sPrevious" : "上一页",
					"sNext" : "下一页",
					"sLast" : "尾页"
				}
			}
		});
        $(options.checkAll).live('click', function() {
			$(".chk_list", options.dataTableObj.fnGetNodes()).prop("checked", $(this).prop("checked"));
		});
		
        $(options.searchBtn).click(function() {
			var oSettings = options.dataTableObj.fnSettings();
			oSettings._iDisplayStart = 0;
			oSettings.sAjaxSource = options.tableDataUrl + "?" + $(options.searchForm).serialize();
			options.dataTableObj.fnPageChange("first", true);
		});
        $(options.searchResetBtn).click(function(){
        	$(options.searchForm)[0].reset();
        	$('.chosen').val('').trigger('chosen:updated');
		});
        
        $(options.tableHeaderOpers).click(function(){
			var checkedArray = getCheckArray();
			if(checkedArray.length == 0) {
				$.jBox.info('请先选择要操作的数据', '提示');
				return false;
			}
			if (options.checkMaxItem) {
				if (checkedArray.length > options.checkMaxItem) {
					$.jBox.info('选择要操作的数据不能超过' + options.checkMaxItem + "条", '提示');
					return false;
				}
			}
			var tipText = $(this).html();
			var param = $(this).attr('param');
	    	
	    	var targetUrl = $(this).attr('targetUrl');
			var submit = function (v, h, f) {
			    if (v == 'ok') {
			    	var params = {};
					params.ids = checkedArray;
			    	if (param && param != '') {
			    		param = eval('('+param+')');
			    		params = $.extend({}, params, param);
			    	}
			   
			    	$.ajax({
			    		type : "post",
			    		url : targetUrl,
			    		dataType : "json",
			    		data : params,
			    		success : function(data) {
			    			if (typeof(data.success) != "undefined") {
			    				var success = data.success;
			    				if (success == "true") {
			    					var tipHtml = tipText + "成功";
				    				jBox.tip(tipHtml, "success");
				    				$(options.checkAll).removeAttr("checked");
				    			} else {
				    				var tipHtml = tipText + "失败";
				    				if (data.msg) {
				    					tipHtml += "<br/>" + data.msg;
				    				}
				    				jBox.tip(tipHtml, "error");
				    			}
			    			} else {	
				    			if (data.isError) {
				    				jBox.tip(tipText + "失败", "error");
				    			} else {
				    				var tipHtml = tipText + "成功";
				    				if (data.msg) {
				    					tipHtml += "<br/>" + data.msg;
				    				}
				    				jBox.tip(tipHtml, "success");
				    				$(options.checkAll).removeAttr("checked");
				    			}
			    			}
							reloadTable(); // 重新加载数据
			    		},
			    		error :function(XMLHttpRequest, textStatus, errorThrown) {
			    			alert(textStatus);
			    			jBox.tip(tipText + "失败","error");
			    			reloadTable(); // 重新加载数据
			    		}
			    	});
			    } else if (v == 'cancel') {
					return true;
			    }
			    return true; 
			};
			jBox.confirm("确定要" + tipText + "选择的" + checkedArray.length + "行数据吗？", "提示", submit);
		});
    };
})(jQuery);