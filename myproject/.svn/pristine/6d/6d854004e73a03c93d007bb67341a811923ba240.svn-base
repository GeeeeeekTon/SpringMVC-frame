package com.dongao.core.mycomponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectMapper.DefaultTyping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.AbstractFlashMapManager;
import org.springframework.web.util.WebUtils;

import com.dongao.core.myutil.Base64;
/**
 * @author zhangzhen
 *
 */
public class CookieFlashMapManager extends AbstractFlashMapManager implements
		InitializingBean {

	private static final String COOKIE_NAME = "FLASH";
	private static final String MAP_ATTR = "map";
	private static final String REQUEST_PATH_ATTR = "targetRequestPath";

	private ObjectMapper objectMapper = new ObjectMapper();

	private Logger logger = LoggerFactory.getLogger(getClass());

	public CookieFlashMapManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
	}

	@Override
	protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
		if (cookie == null) {
			return new ArrayList<FlashMap>();
		} else {
			String encodedValue = cookie.getValue();
			return decodeFlashMaps(encodedValue);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<FlashMap> decodeFlashMaps(String encryptedValue) {
		try {
			String decryptedJson = decrypt(encryptedValue);
			List<Map<String, Object>> maps = objectMapper.readValue(
					decryptedJson, List.class);
			List<FlashMap> flashMaps = rebuildFlashMap(maps);
			return flashMaps;
		} catch (IOException e) {
			throw new RuntimeException("Error decoding flash map", e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<FlashMap> rebuildFlashMap(List<Map<String, Object>> maps) {
		List<FlashMap> flashMaps = new ArrayList<FlashMap>();
		for (Map<String, Object> map : maps) {
			FlashMap flashMap = new FlashMap();
			flashMap.setTargetRequestPath((String) map.get(REQUEST_PATH_ATTR));
			flashMap.putAll((Map<String, Object>) map.get(MAP_ATTR));
			flashMaps.add(flashMap);
		}
		return flashMaps;
	}

	@Override
	protected void updateFlashMaps(List<FlashMap> flashMaps,
			HttpServletRequest request, HttpServletResponse response) {
		String encodedValue = encodeFlashMaps(flashMaps);
		Cookie cookie = new Cookie(COOKIE_NAME, encodedValue);
		String contextPath = request.getContextPath();
		if (StringUtils.isEmpty(contextPath)) {
			contextPath = "/";
		}
		cookie.setPath(contextPath);
		response.addCookie(cookie);
	}

	protected String encodeFlashMaps(List<FlashMap> flashMaps) {
		try {
			List<Map<String, Object>> disassembledFlashMaps = disassembleFlashMaps(flashMaps);
			String jsonValue = objectMapper
					.writeValueAsString(disassembledFlashMaps);
			//logger.trace("JSon encoded FlashMap : {}", jsonValue);
			String encryptedValue = encrypt(jsonValue);
			//logger.trace("Base64 encoded FlashMap size : {}", encryptedValue);
			return encryptedValue;
		} catch (IOException e) {
			throw new RuntimeException("Error encoding flash map", e);
		}
	}

	private List<Map<String, Object>> disassembleFlashMaps(
			List<FlashMap> flashMaps) {
		List<Map<String, Object>> disassembledFlashMaps = new ArrayList<Map<String, Object>>();
		for (FlashMap flashMap : flashMaps) {
			Map<String, Object> disassembledFlashMap = new HashMap<String, Object>();
			disassembledFlashMap.put(REQUEST_PATH_ATTR,
					flashMap.getTargetRequestPath());
			disassembledFlashMap.put(MAP_ATTR, new HashMap<String, Object>(
					flashMap));
			disassembledFlashMaps.add(disassembledFlashMap);
		}
		return disassembledFlashMaps;
	}

	private String encrypt(String message) {
		return Base64.encodeToString(message);
	}

	private String decrypt(String encryptedMessage) {
		return Base64.decodeToString(encryptedMessage);
	}
}