package com.dongao.modules.sample.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dongao.core.base.pojo.Pagination;
import com.dongao.core.mybase.CommonDto;
import com.dongao.core.mybase.Constants.IS_VALID;
import com.dongao.core.mybase.MyBaseController;
import com.dongao.modules.sample.model.SampleUser;
import com.dongao.modules.sample.service.SampleUserService;

/**
 *SampleUser(用户表（样例）)管理
 * autogenerate V1.0 by dongao
 */
@Controller
@RequestMapping("/sample/sampleUser")
public class SampleUserController extends MyBaseController {
    @Autowired
    private SampleUserService sampleUserService;
    
    /**初始化sampleUser管理页面*/
	@RequestMapping(value = {"/sampleUserList",""}, method = RequestMethod.GET)
	public String sampleUserlist(Model model, HttpServletRequest request) {
		//TODO...
		model.addAttribute("menuId", "sampleUserManage");
		return "modules/sample/sampleUser_list";
	}
	/**
	 * ajax分页查询
	 */
	@RequestMapping(value = "/findSampleUsersAjax", method = RequestMethod.GET)
	public @ResponseBody
	Pagination<SampleUser> findPayBanks(@ModelAttribute("sampleUser") SampleUser sampleUser, HttpServletRequest request) {
		sampleUser.setIsValid(IS_VALID.YES.getValue());
		sampleUser.setPageParameter(getpagePageParameter());
		//TODO...
		Pagination<SampleUser> pagination = sampleUserService.findByPage(sampleUser);
		return pagination;
	}
	/**
	 * ajax 支持组合查询的单表分页：页面驱动命名规则参考springside
	 */
	@RequestMapping(value = "/customPageAjax")
	public @ResponseBody
	Pagination<SampleUser> customPageAjax(HttpServletRequest request) {
		Pagination<SampleUser> pagination = sampleUserService.commonBySqlPage(CommonDto.build(super.getFilters(), super.getOrders(),SampleUser.class ,super.getpagePageParameter()));
		return pagination;
	}
	/**增加前的准备*/
    @RequestMapping(value = "/toAddSampleUser")
    public String toAddSampleUser(HttpServletRequest request,Model model) {
    	//TODO...
        return "modules/sample/sampleUser_add";
    }
    /**执行增加*/
    @RequestMapping(value = "/saveSampleUser", method = RequestMethod.POST)
    public String saveSampleUser(@ModelAttribute("sampleUser") SampleUser sampleUser, HttpServletRequest request,RedirectAttributes redirectAttributes) {
        //TODO...
        sampleUserService.save(sampleUser);
        redirectAttributes.addFlashAttribute("message", "增加成功");
        return "redirect:/sample/sampleUser/";
    }
    /**更新之前的准备**/
    @RequestMapping(value = "/editSampleUser/{id}")
    public String editSampleUser(@PathVariable("id") Long id, HttpServletRequest request,Model model) {
    	SampleUser sampleUser = sampleUserService.load(id);
        request.setAttribute("sampleUser", sampleUser);
        return "modules/sample/sampleUser_edit";
    }
    /**执行修改*/
    @RequestMapping(value = "/updateSampleUser", method = RequestMethod.POST)
    public String updateSampleUser(@ModelAttribute("sampleUser") SampleUser sampleUser, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	sampleUserService.update(sampleUser);
    	redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/sample/sampleUser/";
    }
    /**批量删除*/
    @RequestMapping(value = "/batchDeleteSampleUser")
    @ResponseBody
    public String batchDeleteSampleUser(@RequestParam(value = "ids[]", required = true) Long[] ids,RedirectAttributes redirectAttributes) {
        for(Long id:ids){
    		SampleUser sampleUser=sampleUserService.load(id);
    		sampleUser.setIsValid(IS_VALID.NO.getValue());
    		sampleUserService.update(sampleUser);
        }
        return "true";
    }
/*  //状态更新,如果有需要,请打开注释
    @RequestMapping(value = "/updatetStatus", method = RequestMethod.POST)
    public String batchUpdatetStatus(
    		@RequestParam(value = "status", required = true) Integer status,
			@RequestParam(value = "ids[]", required = true) Long[] ids) {
    	for(Long id:ids){
    		SampleUser sampleUser=sampleUserService.load(id);
    		sampleUser.setStatus(status);
    		sampleUserService.update(sampleUser);
    	}
        return "true";
    }*/
    
    /**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出SampleUser对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getSampleUser(
			@RequestParam(value = "id", defaultValue = "-1") Long id,
			Model model) {
		if (id != -1) {
			model.addAttribute("sampleUser", sampleUserService.load(id));
		}
	}
	

}