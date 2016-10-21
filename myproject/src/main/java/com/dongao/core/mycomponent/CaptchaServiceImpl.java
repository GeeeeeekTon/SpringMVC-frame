package com.dongao.core.mycomponent;

import java.awt.image.BufferedImage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongao.core.mybase.Constants.CaptchaType;
/**
 * @author zhangzhen
 *
 */
@Component
public class CaptchaServiceImpl implements CaptchaService {
	@Autowired
	private com.dongao.core.mycomponent.RbcManageableImageCaptchaService imageCaptchaService;
	@Override
	public BufferedImage buildImage(String captchaId) {
		try{
			imageCaptchaService.remove(captchaId);
			return (BufferedImage) imageCaptchaService.getChallengeForID(captchaId);
		}catch (Exception e) {//ignore :if remove fail happend exception
			return null;
		}
		
	}

	@Override
	public boolean isValid(CaptchaType captchaType, String captchaId,
			String captcha) {
			if (StringUtils.isNotEmpty(captchaId) && StringUtils.isNotEmpty(captcha)) {
				try {
					return imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
	}
}