package com.leo.core.mybase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.leo.core.myutil.StringUtil;

/**
 * @author zhangzhen
 *
 */
public class SimplePermissionControlle extends MyBaseController{
	@ModelAttribute
	public void permissionlimit(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String openKey = request.getParameter("openKey");
		if(StringUtil.isEmptyString(openKey)||!Constants.OpenKey.equals(openKey)){
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "no open right");
			return;
		}
	}
}
