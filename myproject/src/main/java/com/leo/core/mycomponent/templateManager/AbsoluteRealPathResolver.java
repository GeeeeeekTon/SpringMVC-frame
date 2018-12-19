package com.leo.core.mycomponent.templateManager;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author zhangzhen
 *
 */
@Component
public class AbsoluteRealPathResolver implements RealPathResolver{
	@Override
	public String get(String path) {
		Assert.hasLength(path,"path lenght must > 0");
		return path.replace("/", File.separator);
	}
}
