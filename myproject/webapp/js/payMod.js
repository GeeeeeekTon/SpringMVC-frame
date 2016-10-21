;(function(){
	JXJY.bankTemplate=function(){//选择支付银行模板
		var temTr=$(".trTemp");
		if(!temTr.is(":visible")) temTr.show();
		JXJY.delBankTemplate();
		//ajax
		//temTr.append()
	}
	JXJY.delBankTemplate=function(){
		$(".trTemp").children(".cintemplate").children("tr:gt(0)").remove();//隐藏增加名称和值的行，并将ajax获取到的内容删除	
	}
	
	JXJY.bankReset=function(obj){
		obj.live("click",function(){
			
			$(".addForm").html(html)
		})	
	}
	
	JXJY.selectBank=function(obj){
			$(".bankTable :checkbox[checked=checked]").each(function(){
				var i=$(".bankTable :checkbox").index(this)
				$(".bankTable").parent().append('<input type="hidden" value='+$(this).val()+' id=cbox'+i+'>')														 
			})
			JXJY.hideBox($(".box_container"),$(".mask"))
			$(".addBtn").show();
			
	}
	JXJY.addBank=function(obj){
			$(".boxGroup input[type=hidden]").each(function(){
				var html='<tr>'+
				'<td><input type="text"></td>'+
				'<td>'+$(".cx").val()+'</td>'+
				'<td>'+$(this).val()+'</td>'+
				'<td>东奥</td>'+
				'<td><a class="delBankBtn">删除</a></td>'+
				'</tr>'
				$(".bankList").append(html)
			})
			
			$(".bankList").show();
	}
	
})()
$(".btn_cont .edit").click(function(){//修改内容的弹窗
	JXJY.showBox("修改支付方式",500,0,"../支付/修改支付方式.html")
})
$(".create").click(function(){
	JXJY.showBox("新增支付方式",500,0,"../支付/新增支付方式.html")
})
$(".addbtn").click(function(){
	JXJY.addBank($(this))						
})
$(".delBankBtn").click(function(){
							
})	
	
