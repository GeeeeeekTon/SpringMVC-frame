package com.leo.core.mybase;

import java.lang.reflect.Field;

import com.leo.core.myutil.Reflections;


public class MyTest {
	enum Sex{	// 实现接口
		MALE("男"),FEMALE("女") ;
		private String title ;
		private Sex(String title){
			this.title = title ;
		}
		public String toString(){
			return this.title ;
		}
		public static Sex fromString(String value) {
			return Enum.valueOf(Sex.class, value.toUpperCase());
		}
	}
	public static class Person {
		private String name ;
		private int age ;
		private Sex sex ;
		private Integer test;
		public Person(String name,int age,Sex sex){
			this.name = name ;
			this.age = age ;
			this.sex = sex ;
		}
		public String toString(){
			return "姓名：" + this.name + "，年龄：" + this.age + "，性别：" + this.sex ;
		}
	}
	
	public static enum SourceKey{ 
		LOGO("logo配置"),CSS("css配置"),NOTICE("公告配置"),CONTACT("联系方式配置"),FRIENDLYLINK("友情链接配置") ;
		private String title ;
		private String value;
		private SourceKey(String title){
			this.title = title ;
			this.value=this.name();
		}
		public String toString(){
			return this.title ;
		}
		public static SourceKey fromString(String name) {
			return Enum.valueOf(SourceKey.class, name.toUpperCase());
		}
		public String getTitle() {
			return this.title;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	public static void main(String[] args) {
//		SourceKey[] values = SourceKey.values();
//		System.out.println(SourceKey.fromString("LOGO"));
//		System.out.println(SourceKey.values()[0].getValue()+":"+SourceKey.values()[0].name()+":"+SourceKey.values()[0]+":"+SourceKey.values()[0].ordinal());
//		
//		//=============================================
//		Person p=new Person("张三",20,Sex.MALE);
//		System.out.println(p);
//		System.out.println(Sex.fromString("female"));
//		
//		System.out.println(Sex.values()[0].name()+":"+Sex.values()[0]+":"+Sex.values()[0].ordinal());
		
		Field declaredField=Reflections.getField(Person.class, "test");
		String typeName = declaredField.getType().getName();
		System.out.println(typeName);
		System.out.println(declaredField.getType().isPrimitive());
		System.out.println(isWrapClass(declaredField.getType()));
		System.out.println(void.class.isPrimitive() );
		
			System.out.println((char)65);  
			 System.out.println(int.class.isPrimitive());  
	        System.out.println(isWrapClass(Long.class));  
	        System.out.println(isWrapClass(Integer.class));  
	        System.out.println(isWrapClass(String.class));   
	        System.out.println(isWrapClass(MyTest.class));
	}
	
    public static boolean isWrapClass(Class clz) {   
        try {   
           return ((Class) clz.getField("TYPE").get(null)).isPrimitive();  
        } catch (Exception e) {   
            return false;   
        }   
    } 

}
