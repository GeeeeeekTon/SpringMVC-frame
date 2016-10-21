$(function(){
	$(".loginCont:eq(0)").show()
	$(".scheduleCont:eq(0)").show()
	$(".toogle span.hover").hide()
	if($(".loginTit h2").length==2){
		$(".loginTit h2").css("cursor","pointer")
		//alert($(".loginTit h2").eq(0).html());
		if($(".loginTit h2").html()==$(".loginTit h2").eq(0).html()){
			$(".loginCont:eq(0)").show()
		}
		else{
			$(".loginCont").hide()
			$(".loginCont:eq(1)").show()
			$(".loginTit h2").removeClass("borL")
			$(".loginTit h2").addClass("hover")
		}
	}
	if($(".loginTit h2").length==1){
		$(".loginTit h2").css({float:"none",width:"auto"})
		$(".loginCont:eq(0)").show()
	}
	$(".loginTit h2").each(function(i){
		$(this).click(function(){
			$(".loginTit .hover").removeClass("hover")
			$(this).addClass("hover")
			$(".loginCont").hide()
			$(".loginCont:eq("+i+")").show()
		})
	})
	$(".scheduleTit h2").each(function(i){
		$(this).click(function(){
			$(".scheduleTit .hover").removeClass("hover")
			$(this).addClass("hover")
			$(".scheduleCont").hide()
			$(".scheduleCont:eq("+i+")").show()
		})
	})
	$(".schedules").each(function(i){
		$(this).find("ul:even").addClass("even")
	})
	$(".toogle span").each(function(i){
		$(this).click(function(){
			$(".toogle span").show()
			$(this).hide()
			$(".scheduleCont").toggleClass("hide")
		})
	})
	if($(".loginTit h2").length==2){
		$(".loginTit h2").mouseover(function(){
			$(this).addClass("hovers")
		}).mouseout(function(){
			$(this).removeClass("hovers")		
		})
	}
	$(".loginB").mouseover(function(){
		$(this).addClass("loginBs")
	}).mouseout(function(){
		$(this).removeClass("loginBs")		
	})
})