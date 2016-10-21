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
import com.dongao.modules.sample.model.SampleTest;
import com.dongao.modules.sample.service.SampleTestService;

/**
 *SampleTest(测试表（样例）)管理
 * autogenerate V1.0 by dongao
 */
@Controller
@RequestMapping("/sample/sampleTest")
public class SampleTestController extends MyBaseController {
    @Autowired
    private SampleTestService sampleTestService;
    
    /**初始化sampleTest管理页面*/
	@RequestMapping(value = {"/sampleTestList",""}, method = RequestMethod.GET)
	public String sampleTestlist(Model model, HttpServletRequest request) {
		//TODO...
		model.addAttribute("menuId", "sampleTestManage");
		return "modules/sample/sampleTest_list";
	}
	/**
	 * ajax分页查询
	 */
	@RequestMapping(value = "/findSampleTestsAjax", method = RequestMethod.GET)
	public @ResponseBody
	Pagination<SampleTest> findPayBanks(@ModelAttribute("sampleTest") SampleTest sampleTest, HttpServletRequest request) {
		sampleTest.setIsValid(IS_VALID.YES.getValue());
		sampleTest.setPageParameter(getpagePageParameter());
		//TODO...
		Pagination<SampleTest> pagination = sampleTestService.findByPage(sampleTest);
		return pagination;
	}
	/**
	 * ajax 支持组合查询的单表分页：页面驱动命名规则参考springside
	 */
	@RequestMapping(value = "/customPageAjax")
	public @ResponseBody
	Pagination<SampleTest> customPageAjax(HttpServletRequest request) {
		Pagination<SampleTest> pagination = sampleTestService.commonBySqlPage(CommonDto.build(super.getFilters(), super.getOrders(),SampleTest.class ,super.getpagePageParameter()));
		return pagination;
	}
	/**增加前的准备*/
    @RequestMapping(value = "/toAddSampleTest")
    public String toAddSampleTest(HttpServletRequest request,Model model) {
    	//TODO...
        return "modules/sample/sampleTest_add";
    }
    /**执行增加*/
    @RequestMapping(value = "/saveSampleTest", method = RequestMethod.POST)
    public String saveSampleTest(@ModelAttribute("sampleTest") SampleTest sampleTest, HttpServletRequest request,RedirectAttributes redirectAttributes) {
        //TODO...
        sampleTestService.save(sampleTest);
        redirectAttributes.addFlashAttribute("message", "增加成功");
        return "redirect:/sample/sampleTest/";
    }
    /**更新之前的准备**/
    @RequestMapping(value = "/editSampleTest/{id}")
    public String editSampleTest(@PathVariable("id") Long id, HttpServletRequest request,Model model) {
    	SampleTest sampleTest = sampleTestService.load(id);
        request.setAttribute("sampleTest", sampleTest);
        return "modules/sample/sampleTest_edit";
    }
    /**执行修改*/
    @RequestMapping(value = "/updateSampleTest", method = RequestMethod.POST)
    public String updateSampleTest(@ModelAttribute("sampleTest") SampleTest sampleTest, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	sampleTestService.update(sampleTest);
    	redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/sample/sampleTest/";
    }
    /**批量删除*/
    @RequestMapping(value = "/batchDeleteSampleTest")
    @ResponseBody
    public String batchDeleteSampleTest(@RequestParam(value = "ids[]", required = true) Long[] ids,RedirectAttributes redirectAttributes) {
        for(Long id:ids){
    		SampleTest sampleTest=sampleTestService.load(id);
    		sampleTest.setIsValid(IS_VALID.NO.getValue());
    		sampleTestService.update(sampleTest);
        }
        return "true";
    }
/*  //状态更新,如果有需要,请打开注释
    @RequestMapping(value = "/updatetStatus", method = RequestMethod.POST)
    public String batchUpdatetStatus(
    		@RequestParam(value = "status", required = true) Integer status,
			@RequestParam(value = "ids[]", required = true) Long[] ids) {
    	for(Long id:ids){
    		SampleTest sampleTest=sampleTestService.load(id);
    		sampleTest.setStatus(status);
    		sampleTestService.update(sampleTest);
    	}
        return "true";
    }*/
    
    /**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出SampleTest对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getSampleTest(
			@RequestParam(value = "id", defaultValue = "-1") Long id,
			Model model) {
		if (id != -1) {
			model.addAttribute("sampleTest", sampleTestService.load(id));
		}
	}
	

}