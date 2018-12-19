package com.leo.core.myutil;

import java.lang.reflect.Modifier;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leo.core.common.utils.SsistUtils;

/**
 * @author zhangzhen
 *
 */
public class JavaassistUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(SsistUtils.class);

	public static String[] getParamNames(Class<?> obj, String methodName)
			throws Exception {
		String[] paramNames = null;

		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(SsistUtils.class));
		CtClass ctClass = classPool.get(obj.getName());
		CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

		// 使用javaassist的反射方法获取方法的参数名
		MethodInfo methodInfo = ctMethod.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
				.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			//logger.error("通过javaassist反射方法参数名称时异常，LocalVariableAttribute is null");
			throw new RuntimeException("通过javaassist反射方法参数名称时异常，LocalVariableAttribute is null");
		} else {
			paramNames = new String[ctMethod.getParameterTypes().length];
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++) {
				paramNames[i] = attr.variableName(i + pos);
			}
		}
		return paramNames;
	}
	
	public static void main(String[] args) throws Exception{
		String[] paramNames = JavaassistUtil.getParamNames(MyMath.class, "add");
		System.out.print(paramNames[0]);;
	}
}
