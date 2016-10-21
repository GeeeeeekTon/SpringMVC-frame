<div class="top">
	<div class="logo"></div>
    <div class="func">
        <a href="${ucenter_ip}/sysUser/toPortal" title="首页" class="block first"></a>
        <a href="#" title="用户" class="block second"></a>
        <a href="${ucenter_ip}/sysUser/logout" title="退出" class="block third"></a>
        <a href="javascript:;" title="换肤" class="block last skinBtn"></a>
        <div class="skinCont">
            <i class="orange"></i>
            <i class="blue"></i>
            <i class="green"></i>
            <i class="red"></i>
            <i class="purple"></i>
            <i class="pink"></i>
        </div>
    </div>
    <ul id="menu">
    	<li><a href="${ucenter_ip}/sysUser/toPortal" <#if selCode=="000">class="sel"</#if> >首页</a></li>
    	<#list firstFunction as function>  
    	<li><a <#if selCode==function.functionCode>class="sel"</#if> href="${ucenter_ip}/sysUser/main/${function.functionCode?default('&nbsp;')}" >${function.functionName?default('&nbsp;')}</a></li>
    	</#list>
    </ul>
</div>