package com.dongao.core.mybase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dongao.core.myutil.StringUtil;

/**
 * @author zhangzhen
 *
 */
public class LogbackStatusServlet extends
		ch.qos.logback.classic.ViewStatusMessagesServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String openKey = req.getParameter("openKey");
		if (StringUtil.isEmptyString(openKey)
				|| !Constants.OpenKey.equals(openKey)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "no open right");
			return;
		}
		super.service(req, resp);
	}
}
