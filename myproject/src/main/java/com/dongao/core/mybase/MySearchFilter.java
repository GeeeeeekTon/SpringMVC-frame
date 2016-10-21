package com.dongao.core.mybase;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.dongao.core.myutil.Reflections;

/**
 * 搜索过滤器:pojo 和 table 约定的命名规则必须是驼峰风格
 * 
 * pojo是领域模型  dto是数据传输模型  尽量保持pojo的整洁  
 * 
 * 如果特殊需要  为pojo 定义对应的  dto(可以直接包含pojo,在增加额外的属性 关联定义)
 * 
 * apply:单表各种组合条件的查询 、统计   其中查询支持各种排序
 * 
 * @author zhangzhen 
 * 
 * v1.0
 */
public class MySearchFilter implements Serializable{
	private static final long serialVersionUID = 481803238318622265L;
	private static final Logger logger = LoggerFactory.getLogger(MySearchFilter.class);
	private static final Boolean showSql = true;
	private static final String tablePerfix="da_retrain_";
	private static final String defalutOrder=" order by id desc ";

	public enum Operator {
		EQ, LIKE, CONTAIN, STARTWITH, ENDWITH, GT, LT, GTE, LTE, IN,
		NOT_EQ, NOT_LIKE,NOT_CONTAIN,NOT_IN,ISNULL,ISNOTNULL;
		/**忽略大小写*/
		public static Operator fromString(String value) {
			return Operator.valueOf(value.toUpperCase());
		}
	}
	public String fieldName;
	public String[] value;
	public Operator operator;

	private MySearchFilter(String fieldName, Operator operator, String[] value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}
	public static MySearchFilter filter(String fieldName, Operator operator, String[] value){
		return new MySearchFilter(fieldName,operator,value);
	}
	
	private static String getOrderSql(Collection<Order> orders,Class cls){
		Assert.notNull(cls,"class not allow Null");
		Assert.notNull(orders,"orders not allow Null");
		StringBuilder sb=new StringBuilder();
		//sb.append(" order by "); bug fix
		boolean flag=false;
		if(orders.size()>0){
			for(Order order:orders){
				Field declaredField=null;
//				try {
//					 declaredField = cls.getDeclaredField(order.getProperty());
//				} catch (Exception e) {
//					logger.error(e.getMessage()+" sql 攻击！");
//					continue;
//				}
				declaredField=Reflections.getField(cls, order.getProperty());
				if(declaredField==null){
					logger.error("发现 sql 攻击！");
					continue;
				}
				//检测declaredField数据类型
				if(!isPrimitive(declaredField.getType())){
					logger.error("不支持的数据类型...");
					continue;
				}
				if(!flag){
					sb.append(" order by ");
				}
				if(flag){
					sb.append(",");
				}
				String paramName=camelhumpToUnderline(declaredField.getName());
				sb.append(paramName).append(" ").append(order.getDirection().toString());
				flag=true;
			}
		}else{
			sb.append(defalutOrder);
		}
		if(sb.toString().length()==0){//no match use default
			sb.append(defalutOrder);
		}
		return sb.toString();
	}
	public static String getSelectSqlAndOrder(Collection<MySearchFilter> filters,Collection<Order> orders,Class cls){
		Assert.notNull(cls,"class not allow Null");
		String className = cls.getSimpleName();
		String tableName=camelhumpToUnderline(className);
		StringBuilder sb=new StringBuilder();
		sb.append("select * from ").append(tablePerfix).append(tableName).append(" where is_valid=1 ");
		String sql = getSql(filters,cls,sb.toString());
		String orderSql = getOrderSql(orders,cls);
		if(showSql){
			logger.info(sql+orderSql);
		}
		return sql+orderSql;
	}
	
	
	public static String getSelectSql(Collection<MySearchFilter> filters,Class cls){
		Assert.notNull(cls,"class not allow Null");
		String className = cls.getSimpleName();
		String tableName=camelhumpToUnderline(className);
		StringBuilder sb=new StringBuilder();
		sb.append("select * from ").append(tablePerfix).append(tableName).append(" where is_valid=1 ");
		String sql = getSql(filters,cls,sb.toString());
		String orderSql = getOrderSql(new ArrayList<Order>(),cls);
		if(showSql){
			logger.info(sql+orderSql);
		}
		return sql+orderSql;
	}
	public static String getCountSql(Collection<MySearchFilter> filters,Class cls){
		Assert.notNull(cls,"class not allow Null");
		String className = cls.getSimpleName();
		String tableName=camelhumpToUnderline(className);
		StringBuilder sb=new StringBuilder();
		sb.append("select count(1) from ").append(tablePerfix).append(tableName).append(" where is_valid=1 ");
		String sql = getSql(filters,cls,sb.toString());
		if(showSql){
			logger.info(sql);
		}
		return sql;
	}
	public static String getSql(Collection<MySearchFilter> filters,Class cls,String str){
		StringBuilder sb=new StringBuilder();
		sb.append(str);
		Assert.notNull(filters,"filters not allow Null");
		for(MySearchFilter filter :filters){
//			String[] names = StringUtils.split(filter.fieldName,
//					".");
			Field declaredField=null;
//			try {
//				 declaredField = cls.getDeclaredField(filter.fieldName);
//			} catch (Exception e) {
//				logger.error(e.getMessage()+" sql 攻击！");
//				continue;
//			}
			declaredField=Reflections.getField(cls, filter.fieldName);
			if(declaredField==null){
				logger.error("发现 sql 攻击！");
				continue;
			}
			//检测declaredField数据类型
			if(!isPrimitive(declaredField.getType())){
				logger.error("不支持的数据类型...");
				continue;
			}
			String paramName=camelhumpToUnderline(declaredField.getName());
			Assert.notEmpty(filter.value,"filters not allow empty");
			String paramValue=process(filter.value[0]);
			
			switch (filter.operator) {
			case EQ:
				sb.append(" and ").append(paramName).append(" = ").append("'").append(paramValue).append("'");
				break;
			case LIKE:
				sb.append(" and ").append(paramName).append(" like ").append("'").append(paramValue).append("'");
				break;
			case CONTAIN:
				sb.append(" and ").append(paramName).append(" like ").append("'").append("%").append(paramValue).append("%").append("'");
				break;
			case STARTWITH:
				sb.append(" and ").append(paramName).append(" like ").append("'").append(paramValue).append("%").append("'");
				break;
			case ENDWITH:
				sb.append(" and ").append(paramName).append(" like ").append("'").append("%").append(paramValue).append("'");
				break;
			case GT:
				sb.append(" and ").append(paramName).append(" > ").append("'").append(paramValue).append("'");
				break;
			case LT:
				sb.append(" and ").append(paramName).append(" < ").append("'").append(paramValue).append("'");
				break;
			case GTE:
				sb.append(" and ").append(paramName).append(" >= ").append("'").append(paramValue).append("'");
				break;
			case LTE:
				sb.append(" and ").append(paramName).append(" <= ").append("'").append(paramValue).append("'");
				break;
			case IN:
				sb.append(" and ").append(paramName).append(" in ").append(dealArray(filter.value));
				break;
			case NOT_EQ:
				sb.append(" and ").append(paramName).append(" != ").append("'").append(paramValue).append("'");
				break;
			case NOT_LIKE:
				sb.append(" and ").append(paramName).append(" not like ").append("'").append(paramValue).append("'");
				break;
			case NOT_CONTAIN:
				sb.append(" and ").append(paramName).append(" not like ").append("'").append("%").append(paramValue).append("%").append("'");
				break;
			case NOT_IN:
				sb.append(" and ").append(paramName).append(" not in ").append(dealArray(filter.value));
				break;
			case ISNULL:
				sb.append(" and ").append(paramName).append(" is null ");
				break;
			case ISNOTNULL:
				sb.append(" and ").append(paramName).append(" is not null ");
				break;
			}
			
			
		}
		return sb.toString();
	}
	public static String dealArray(String[] value){
		Assert.notEmpty(value,"value not allow Null");
		StringBuilder sb=new StringBuilder();
		boolean flag=false;
		for(String o:value){
			if(!flag){
				sb.append("(");
			}
			if(flag){
				sb.append(",");
			}
			flag=true;
			sb.append("'").append(process(o)).append("'");
		}
		if(flag){
			sb.append(")");
		}
		return sb.toString();
	}
	/**
	 * 过滤单个单引号为双引号的SafeSqlFilter<p>
	 * 适用数据库(MS SqlServer,Oracle,DB2)
	 * Mysql还需过滤反斜框
	 */
	public static String process(String value) {
	        Assert.notNull(value,"value not allow Null");
	        return value.replaceAll("'", "''").replaceAll("\\\\","\\\\\\\\"); 
	}

	public static Map<String, MySearchFilter> parse(Map<String, String[]> params) {
		if (params == null || params.isEmpty()) return Collections.emptyMap();
		Map<String, MySearchFilter> filters = new HashMap<String, MySearchFilter>();
		for (Entry<String, String[]> entry : params.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (ArrayUtils.isEmpty(values)) {
				continue;
			}
			boolean flag=true;
			for(String str:values){
				if(str!=null&&!"".equals(str)){
					flag=false;//只要数组中一个有值，就放过
					break;
				}
			}
			if(flag){
				continue;
			}
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length < 2) {
				throw new IllegalArgumentException(key
						+ " is not a valid search filter name");
			}
			Operator operator = Operator.fromString(names[0]);
			String filedName = names[1];
			// 创建mySearchFilter
			MySearchFilter filter = new MySearchFilter(filedName, operator, values);
			filters.put(key, filter);
		}
		return filters;
	}
    /**将驼峰风格替换为下划线风格*/
    public static String camelhumpToUnderline(String str) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
        }
        if (builder.charAt(0) == '_') {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
    /**
     * 检测数据类型是否基本数据类型或者对应的包装类以及其他允许的数据类型
     * @param cls
     * @return
     */
    public static boolean isPrimitive(Class cls){
    	boolean flag=false;
    	if(cls.isPrimitive()||Reflections.isWrapClass(cls)||otherPrimitive(cls)){
    		flag=true;
    	}
    	return flag;
    }
    public static boolean otherPrimitive(Class cls){
    	String name=cls.getName();
    	if("java.lang.String".equals(name)||"java.math.BigDecimal".equals(name)||"java.util.Date".equals(name)){
    		return true;
    	}
    	return false;
    }
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + Arrays.hashCode(value);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MySearchFilter other = (MySearchFilter) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (operator != other.operator)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}
}
