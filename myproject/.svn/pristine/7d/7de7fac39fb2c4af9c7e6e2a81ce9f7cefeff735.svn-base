/*author：zhangzhen*/
var da=da||{};//全局变量
da.retrain={//继教配置
		config:{
			base:'',
			other:'test'
		},
		otherServer:{
			business:'http://192.168.0.131:7000/business',
			resource:'http://192.168.0.131:7000/resource'
		},
		other:'test'
};
//TODO 其他管理模块的配置...

/**允许链式日志记录[使用Firebug和Firefox来记录jQuery事件日志]**/
//eg:$('#someDiv').hide().log('div hidden').addClass('someClass');
jQuery.log = jQuery.fn.log =function (msg) { 
    if(typeof(console) == "undefined"){
    }else{
    	console.log("%s: %o", msg, this);
    } 
    return this;
};