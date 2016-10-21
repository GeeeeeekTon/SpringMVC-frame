package com.dongao.core.mycomponent;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.dongao.core.mybase.MySwitch;
import com.dongao.core.mycache.CacheChannel;
import com.dongao.core.mycache.annotation.Cachable;
import com.dongao.core.mycache.annotation.CachePut;
import com.dongao.core.myutil.ArrayUtil;
import com.dongao.core.myutil.ClassUtil;
import com.dongao.core.myutil.Collections3;
import com.dongao.core.myutil.JedisTemplate;
import com.dongao.core.myutil.LogUtils;
import com.dongao.core.myutil.Md5Util;
import com.dongao.core.myutil.Reflections;
import com.dongao.modules.LoadService;
import com.dongao.modules.LoadServiceImpl;

/**
 * @author zhangzhen
 *
 */
@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

	/** servletContext */
	private ServletContext servletContext;
	private final static String projectPackage="com.dongao";
    
	private final static String daoPackage="com.dongao";
	
	private final static String daoSuffix="Mapper";
	
	private final static String projectInfo=
				"\r\n|========================================================|" +
			   "\r\n\r\n\r\n|***************myproject successful start!!!!!!***************|\r\n\r\n\r\n" +
				"|========================================================|\r\n";
	private final static String projectCheckInfo=
			"\r\n|========================================================|" +
		   "\r\n\r\n\r\n|************error:please check myproject config !!!!!!***************|\r\n\r\n\r\n" +
			"|========================================================|\r\n";

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (servletContext != null && event.getApplicationContext().getParent() == null) {
			if(!checkStartBefore()){
				System.out.println(projectCheckInfo);
				System.exit(1);
			}
			CacheChannel.getInstance();
			JedisTemplate.getInstance();
//			SimpleEmailMessage message = new SimpleEmailMessage();
//            message.getMailTo().add("retraindev@dongao.com");
//            message.setMessage(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+rbcInfo);
//            message.setTitle(MySwitch.getAppInfo()+"启动成功。");
//			emailService.send(message);
			LogUtils.getEmailLog().info(LogUtils.getNotifyAdminMarker(),"{} myproject 启动成功!",MySwitch.getAppInfo());
			System.out.println(projectInfo);//输出到控制台
			LogUtils.BUSINESS_LOG.info(projectInfo);
		}
	}
	
	public boolean checkStartBefore(){
		List<Class<?>> classList = Reflections.getClassList(daoPackage,true);
		//检测load方法
		if(Collections3.isNotEmpty(classList)){
			for(Class c:classList){
				if(c.getName().endsWith(daoSuffix)&&!c.getName().contains("JsonMapper")){
					String clsName=c.getSimpleName();
					int endLength=clsName.length()-daoSuffix.length();
					String entityName=clsName.substring(0, endLength);
					LoadServiceImpl loadService=new LoadServiceImpl();
					Method loadMethod = Reflections.getAccessibleMethodByName(loadService, CacheAspect.loadPrefix+entityName);
					if(loadMethod==null){
						System.out.println(CacheAspect.loadPrefix+entityName+"在类:"+LoadService.class.getName()+"未定义!");
						LogUtils.getErrorLog().error(CacheAspect.loadPrefix+entityName+"在类:"+LoadService.class.getName()+"未定义!");
						return false;
					}
				}
			}
		}
		try {
			//String[] paramNames = JavaassistUtil.getParamNames(MyMath.class, "add");
			//System.err.println(paramNames[0]+paramNames[1]);
			//找到类中使用@CachePut的注解，并将其放到propertysNameCaches
			List<Class<?>> clss = ClassUtil.getClassListByAnnotation(projectPackage, Cachable.class);
			if(Collections3.isNotEmpty(clss)){
				for(Class<?> cls : clss){
					Method[] methods = cls.getDeclaredMethods();
					 if (ArrayUtil.isNotEmpty(methods)) {
						 for (Method method : methods) {
							 if (method.isAnnotationPresent(CachePut.class)) {
								 CachePut cachePut = method.getAnnotation(CachePut.class);
								 String[] relPropertys = cachePut.relPropertys();
								 if(ArrayUtil.isNotEmpty(relPropertys)){
									 StringBuffer typeString=new StringBuffer();
									 Class<?>[] parameterTypes = method.getParameterTypes();//支持方法重载
									 if(ArrayUtil.isNotEmpty(parameterTypes)){
										 for(Class<?> c:parameterTypes){
											 typeString.append(c.getName());
										 }
									 }
									 String key=Md5Util.encrypt(cls.getName()+method.getName()+typeString);
									 CacheAspect.propertysNameCaches.put(key,relPropertys);
								 }
							 }
						 }
					 }
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			LogUtils.logError("Javaassist 取得方法参数名称引发了异常：", e);
			return false;
		}
		return true;
	}
}