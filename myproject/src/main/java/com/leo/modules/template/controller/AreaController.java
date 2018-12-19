package com.leo.modules.template.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.leo.core.base.pojo.Pagination;
import com.leo.core.common.utils.JsonUtil;
import com.leo.core.mybase.CommonDto;
import com.leo.core.mybase.Constants;
import com.leo.core.mybase.MyBaseController;
import com.leo.core.myutil.Collections3;
import com.leo.core.myutil.MyJsonUtil;
import com.leo.core.myutil.StringUtil;
import com.leo.modules.template.model.Area;
import com.leo.modules.template.service.AreaService;

/**
 *Area(继续教育地区)管理
 * autogenerate V1.0 by leo
 */
@Controller
@RequestMapping("/area")
public class AreaController extends MyBaseController {
    
    private Logger logger = LoggerFactory.getLogger(AreaController.class);
    
    @Autowired
    private AreaService areaService;
    
    /**初始化area管理页面*/
	@RequestMapping(value = "/areaList", method = RequestMethod.GET)
	public String arealist(Model model, HttpServletRequest request) {
		// 显示父节点下拉框，用于过滤
		model.addAttribute("areaList", areaService.selectAll());
		model.addAttribute("menuId", "areaManage");
		return "area/list_area";
	}
	
	/**
	 * 为添加城市做准备
	 */
	@RequestMapping(value = "/treeWin", method = RequestMethod.GET)
	public String treeWin(HttpServletResponse response , HttpServletRequest request){
		String partnerPeriodId = request.getParameter("partnerPeriodId");
		String partnerId = request.getParameter("partnerId");
		request.setAttribute("partnerPeriodId",partnerPeriodId);
		request.setAttribute("partnerId",partnerId);
		return "partnerPeriod/tree";
	}
	
	
	/**
	 * ajax分页查询
	 */
	@RequestMapping(value = "/findAreasAjax", method = RequestMethod.GET)
	public @ResponseBody
	Pagination<Area> findPayBanks(@ModelAttribute("area") Area area, HttpServletRequest request) {
		area.setPageParameter(getpagePageParameter());
		//TODO...
		Pagination<Area> pagination = areaService.findByPage(area);
		return pagination;
	}
	
	/**
	 * ajax全部查询
	 */
	@RequestMapping(value = "/findAllAreas", method = RequestMethod.GET)
	public @ResponseBody
	Pagination<Area> findAllAreas(@ModelAttribute("area") Area area, HttpServletRequest request) {
		//TODO...
		Pagination<Area> pagination = areaService.findByPage(area);
		return pagination;
	}
	
	
	/**
	 * ajax分页查询
	 */
	@ResponseBody
	@RequestMapping(value = "/findAreasAjaxNew", method = RequestMethod.GET, produces="text/plain;charset=utf-8")
	public String findAreasAjaxNew(@ModelAttribute("area") Area area, HttpServletRequest request,HttpServletResponse response) {
		area.setPageParameter(getpagePageParameter());
		Pagination<Area> pagination = areaService.findByPage(area);
		StringBuffer callback = new StringBuffer(request.getParameter("jsoncallback"));  
        try {
        	callback.append("(").append(JsonUtil.toJson(pagination)).append(")");
            //callback.append("({\"list\":").append(JsonUtil.toJson(pagination.getList())).append("})"); //array of objects as a data source
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return callback.toString();
	}    
	
	/**
	 * ajax 支持组合查询的单表分页：页面驱动命名规则参考springside
	 */
	@RequestMapping(value = "/customPageAjax")
	public @ResponseBody
	Pagination<Area> customPageAjax(HttpServletRequest request) {
		Pagination<Area> pagination = areaService.commonBySqlPage(CommonDto.build(super.getFilters(), super.getOrders(), Area.class, super.getpagePageParameter()));
		List<Area> areas = pagination.getList();
		List<Long> ids = new ArrayList<Long>();
		for (Area area : areas) {
			if (area.getParentId() != null) {
				ids.add(area.getParentId());
			}
		}
		Map<Long, Area> areaMap = areaService.getAreasMap(ids);
		for (Area area : areas) {
			if (area.getParentId() != null) {
				Area parentArea = areaMap.get(area.getParentId());
				if (parentArea != null) {
					area.setParentName(parentArea.getName());
				}
			}
		}
		
		return pagination;
	}
	
	/**增加前的准备*/
    @RequestMapping(value = "/toAddArea")
    public String toAddArea(HttpServletRequest request, Model model) {
    	request.setAttribute("areaList", areaService.selectAll());
    	request.setAttribute("statusList", getStatus());
    	request.setAttribute("enableValue", Constants.Status.ENABLE.getValue());
        return "area/add_area";
    }
    
    @RequestMapping(value = "/validaArea")
    public @ResponseBody String validaArea(HttpServletRequest request, Model model) {
    	String areaCode = request.getParameter("param");
    	String info = "验证通过！";
        String status = "y";
    	int count=areaService.countByAreaCode(areaCode);
    	if(count>0){
    		status = "n";
    		return "{ \"info\":\""+"编码已存在"+"\" }";
    	}else{
    		return "{ \"info\":\""+info+"\", \"status\":\""+status+"\" }";
    	}
    }       
    
    /**执行增加*/
    @RequestMapping(value = "/saveArea", method = RequestMethod.POST)
    public String saveArea(@ModelAttribute("area") Area area, HttpServletRequest request,RedirectAttributes redirectAttributes) {
        int existAreaCode = areaService.countByAreaCode(area.getAreaCode());
        if (existAreaCode > 0) {
        	redirectAttributes.addFlashAttribute("message", "地区编码已经存在");
            return "redirect:/area/areaList";
        }
        if (area.getParentId() != null) {
        	Area parentArea = areaService.load(area.getParentId());
        	if (parentArea.getAreaCode() != null && parentArea.getAreaCode().equalsIgnoreCase(area.getAreaCode())) {
        		redirectAttributes.addFlashAttribute("message", "父节点选择错误");
                return "redirect:/area/areaList";
        	}
        }
    	areaService.save(area);
        redirectAttributes.addFlashAttribute("message", "保存成功");
        return "redirect:/area/areaList";
    }
    
    /**更新之前的准备**/
    @RequestMapping(value = "/editArea/{id}")
    public String editArea(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
    	Area area = areaService.load(id);
        request.setAttribute("area", area);
        request.setAttribute("areaList", areaService.selectAll());
        request.setAttribute("statusList", getStatus());
        return "area/edit_area";
    }
    
    /**执行修改*/
    @RequestMapping(value = "/updateArea", method = RequestMethod.POST)
    public String updateArea(@ModelAttribute("area") Area area, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	if (StringUtil.isNotEmptyString(area.getAreaCode())) {
    		Area existArea = areaService.getAreaByCode(area.getAreaCode());
    		if (existArea != null && existArea.getId().equals(area.getId())) {
    			redirectAttributes.addFlashAttribute("message", "地区编码已经存在");
                return "redirect:/area/areaList";
    		}
    	}
    	if (area.getParentId() != null) {
        	Area parentArea = areaService.load(area.getParentId());
        	if (parentArea.getAreaCode() != null && parentArea.getAreaCode().equalsIgnoreCase(area.getAreaCode())) {
        		redirectAttributes.addFlashAttribute("message", "父节点选择错误");
                return "redirect:/area/areaList";
        	}
        }
    	areaService.update(area);
    	redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/area/areaList";
    }
    
    /**批量删除*/
    @RequestMapping(value = "/batchDeletetArea", method = RequestMethod.POST)
    public @ResponseBody 
    String batchDeleteArea(@RequestParam(value = "ids[]", required = true) Long[] ids) {
    	Collection<Long> existIds = areaService.deleteByIds(ids);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("isError", false);
    	if (!existIds.isEmpty()) {
    		resultMap.put("msg", Collections3.convertToString(existIds, ",") + "已被使用，无法删除");
    	}
    	
    	return MyJsonUtil.toJson(resultMap);
    }
    
    /**状态更新 */
    @RequestMapping(value = "/updatetStatus", method = RequestMethod.POST)
    public @ResponseBody 
    String batchUpdatetStatus(
    		@RequestParam(value = "status", required = true) Integer status,
			@RequestParam(value = "ids[]", required = true) Long[] ids) {
    	for (Long id:ids) {
    		Area area = areaService.load(id);
    		area.setStatus(status);
    		areaService.update(area);
    	}
        return "true";
    }
    
    
    /**
	 * 远程服务接口，获取地区
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/areaToJson", method = RequestMethod.GET, produces="text/plain;charset=utf-8")
    public @ResponseBody String areaToJson(HttpServletRequest request,HttpServletResponse response) {
        List<Area> areas = areaService.selectAll();
        StringBuffer callback = new StringBuffer(request.getParameter("jsoncallback"));  
        try {
            callback.append("(").append(JsonUtil.toJson(areas)).append(")");
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return callback.toString();
    }
	
	/**
	 * 远程服务接口，根据id获取地区
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loadArea/{id}", method = RequestMethod.GET, produces="text/plain;charset=utf-8")
    public @ResponseBody String loadArea(@PathVariable("id") String id, HttpServletRequest request,HttpServletResponse response) {
		Area area = areaService.load(Long.valueOf(id));
        StringBuffer callback = new StringBuffer(request.getParameter("jsoncallback"));  
        try {
            callback.append("(").append(JsonUtil.toJson(area)).append(")");
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return callback.toString();
    }
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getArea(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("area", areaService.load(id));
		}
	}
}