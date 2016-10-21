package com.dongao.core.mycomponent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.dongao.core.mybase.Constants;
import com.dongao.core.myutil.LogUtils;

import freemarker.template.Configuration;
/**
 * @author zhangzhen
 *
 */
@Component
public class MyStaticServiceImpl implements MyStaticService {


	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Override
	public int build(String templatePath, String staticPath,
			Map<String, Object> model) {
		Assert.hasText(templatePath);
		Assert.hasText(staticPath);

		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		Writer writer = null;
		try {
			Configuration cfg = freeMarkerConfigurer.getConfiguration();
			Configuration configuration=(Configuration) cfg.clone();
			configuration.setDirectoryForTemplateLoading(new File(Constants.TEMPLATE_PATH));// 取得FreeMarker的模版文件位置
			configuration.setAutoImports(new HashMap());
			configuration.clearTemplateCache(); //clear cache
			freemarker.template.Template template =configuration.getTemplate(templatePath);
			File staticFile = new File(staticPath);
			File staticDirectory = staticFile.getParentFile();
			if (!staticDirectory.exists()) {
				staticDirectory.mkdirs();
			}
			fileOutputStream = new FileOutputStream(staticFile);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, Constants.UTF8);
			writer = new BufferedWriter(outputStreamWriter);
			template.process(model, writer);
			writer.flush();
			configuration=null;
			return 1;
		} catch (Exception e) {
            //logger.error(e.getMessage());
			LogUtils.logError("静态化错误", e);
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(outputStreamWriter);
			IOUtils.closeQuietly(fileOutputStream);
		}
		return 0;
	}

	@Override
	public String buildReturnSorce(String templatePath, String staticPath,
			Map<String, Object> model) {
		Assert.hasText(templatePath);
		Assert.hasText(staticPath);

		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		Writer writer = null;
		try {
			Configuration cfg = freeMarkerConfigurer.getConfiguration();
			Configuration configuration=(Configuration) cfg.clone();
			configuration.setDirectoryForTemplateLoading(new File(Constants.TEMPLATE_PATH));// 取得FreeMarker的模版文件位置
			configuration.setAutoImports(new HashMap());
			configuration.clearTemplateCache(); //clear cache
			freemarker.template.Template template = configuration.getTemplate(templatePath);
			File staticFile = new File(staticPath);
			File staticDirectory = staticFile.getParentFile();
			if (!staticDirectory.exists()) {
				staticDirectory.mkdirs();
			}
			fileOutputStream = new FileOutputStream(staticFile);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, Constants.UTF8);
			writer = new BufferedWriter(outputStreamWriter);
			template.process(model, writer);
			writer.flush();
			configuration=null;
			String fileToString = FileUtils.readFileToString(staticFile, Constants.UTF8);
			return fileToString;
		} catch (Exception e) {
			LogUtils.logError("静态化错误", e);
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(outputStreamWriter);
			IOUtils.closeQuietly(fileOutputStream);
			File staticFile = new File(staticPath);
			if(staticFile.exists())staticFile.delete();
		}
		return "";
	}

}
