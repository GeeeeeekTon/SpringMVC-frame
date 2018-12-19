package com.leo.core.mycomponent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leo.core.common.utils.tedis.TedisUtil;
import com.leo.core.myutil.Collections3;
import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;
/**
 * @author zhangzhen
 *
 */
@Component
public class RedisCaptchaStore implements CaptchaStore{
	public static String REDIS_CAPTCHA="redis_captcha";  
	@Autowired
	private TedisUtil tedisUtil;
	public RedisCaptchaStore(){
	}
	public Map<String,CaptchaAndLocale> getStore(){
		Map<String,CaptchaAndLocale> store = tedisUtil.getHashCommands().entries(tedisUtil.getNameSpace(), REDIS_CAPTCHA);
		if(store!=null){
			return store;
		}
		return new HashMap<String,CaptchaAndLocale>();
	}
	@Override
	public void cleanAndShutdown() {
		Set<Object> keys = tedisUtil.getHashCommands().keys(tedisUtil.getNameSpace(), REDIS_CAPTCHA);
		if(Collections3.isNotEmpty(keys)){
			for(Object key:keys){
				tedisUtil.getHashCommands().delete(tedisUtil.getNameSpace(), REDIS_CAPTCHA, key);
			}
		}
	}

	@Override
	public void empty() {
		Set<Object> keys = tedisUtil.getHashCommands().keys(tedisUtil.getNameSpace(), REDIS_CAPTCHA);
		if(Collections3.isNotEmpty(keys)){
			for(Object key:keys){
				tedisUtil.getHashCommands().delete(tedisUtil.getNameSpace(), REDIS_CAPTCHA, key);
			}
		}
	}

	@Override
	public Captcha getCaptcha(String id) throws CaptchaServiceException {
		Object captchaAndLocale=tedisUtil.getHashCommands().get(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id);
	    return captchaAndLocale == null ? null : ((CaptchaAndLocale)captchaAndLocale).getCaptcha();
	}

	@Override
	public Collection getKeys() {
		Set<String> set = tedisUtil.getHashCommands().keys(tedisUtil.getNameSpace(), REDIS_CAPTCHA);
		return set;
	}

	@Override
	public Locale getLocale(String id) throws CaptchaServiceException {
		Object captchaAndLocale=tedisUtil.getHashCommands().get(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id);
	    return captchaAndLocale == null ? null : ((CaptchaAndLocale)captchaAndLocale).getLocale();
	}

	@Override
	public int getSize() {
		return tedisUtil.getHashCommands().size(tedisUtil.getNameSpace(), REDIS_CAPTCHA).intValue();
	}

	@Override
	public boolean hasCaptcha(String id) {
		Boolean hasKey = tedisUtil.getHashCommands().hasKey(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id);
		if(hasKey!=null){
			return hasKey.booleanValue();
		}
		return false;
	}

	@Override
	public void initAndStart() {
		Set<Object> keys = tedisUtil.getHashCommands().keys(tedisUtil.getNameSpace(), REDIS_CAPTCHA);
		if(Collections3.isNotEmpty(keys)){
			for(Object key:keys){
				tedisUtil.getHashCommands().delete(tedisUtil.getNameSpace(), REDIS_CAPTCHA, key);
			}
		}
	}

	@Override
	public boolean removeCaptcha(String id) {
		Boolean hasKey = tedisUtil.getHashCommands().hasKey(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id);
		if(hasKey!=null&&hasKey.booleanValue()){
			tedisUtil.getHashCommands().delete(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id);
			return true;
		}
		return false;
	}

	@Override
	public void storeCaptcha(String id, Captcha captcha)
			throws CaptchaServiceException {		
		tedisUtil.getHashCommands().put(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id, new CaptchaAndLocale(captcha));
	}

	@Override
	public void storeCaptcha(String id, Captcha captcha, Locale locale)
			throws CaptchaServiceException {
		tedisUtil.getHashCommands().put(tedisUtil.getNameSpace(), REDIS_CAPTCHA, id,new CaptchaAndLocale(captcha, locale));
	}
//	@Override
//	public String toString() {
//		return "RedisCaptchaStore []"+getStore();
//	}
	

}
