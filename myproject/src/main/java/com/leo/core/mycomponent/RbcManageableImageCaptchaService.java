package com.leo.core.mycomponent;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

/**
 * @author zhangzhen
 */
public class RbcManageableImageCaptchaService extends DefaultManageableImageCaptchaService {

    public RbcManageableImageCaptchaService(com.octo.captcha.service.captchastore.CaptchaStore captchaStore, com.octo.captcha.engine.CaptchaEngine captchaEngine, int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    //验证,不移除 apply ajax
    public boolean hasCapcha(String id, String userCaptchaResponse) {
        return store.getCaptcha(id).validateResponse(userCaptchaResponse);
    }

    public boolean remove(String id) {
        return store.removeCaptcha(id);
    }
}
