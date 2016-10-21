;(function(){
	var BOX={
		hideBox:function(box){
			box.hide();
		},
		hideMask:function(mask){
			mask.hide();	
		},
		deleteBox:function(exit){
			exit.click(function(){
				var box=$(".box_container",parent.document)
				var mask=$(".mask",parent.document)
				BOX.hideBox(box)
				BOX.hideMask(mask)
			})
		},
		success:function(word,delay){
			BOX.hideBox($(".box_container",parent.document));
			var str='<div class="result"><div class="rcont">'+word+'</div></div>';
			$("body",parent.document).append(str);
			$(".result",parent.document).show();
			
			setTimeout(function(){
				$(".result",parent.document).hide();
				parent.location.reload();
			},delay)	
		}
	}
	window.BOX=BOX;	   
})()
BOX.deleteBox($(".cancel"))
$(".save").click(function(){
	BOX.success("Ìí¼Ó³É¹¦£¡",3000);
})