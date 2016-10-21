package com.dongao.core.myutil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.dongao.core.mybase.Order;
import com.dongao.core.mybase.Order.Direction;
import com.dongao.core.myutil.compare.BeanComparator;
import com.dongao.core.myutil.compare.ComparableComparator;
import com.dongao.core.myutil.compare.ComparatorChain;
import com.dongao.core.myutil.compare.FixedOrderComparator;

/**
 * Collections工具集.
 * 
 * 在JDK的Collections和Guava的Collections2后, 命名为Collections3.
 * 
 * 函数主要由两部分组成，一是自反射提取元素的功能，二是源自Apache Commons Collection, 争取不用在项目里引入它。
 * 
 * @author zhangzhen
 */
public class Collections3 {
	
	/**
	 * 对List的对象按照多个属性（支持asc、desc）进行排序
	 * <pre>
	 *  Order[] orders={Order.desc("id"),Order.asc("name")};
     *  Collections3.sort(Arrays.asList(orders), list, CompareTipA.class);
	 * </pre>
	 * @param orders 要按照排序的属性对象
	 * @param sourceList 排序前的list
	 * @param cls list的泛型
	 */
	public static void sort(List<Order> orders,List sourceList,Class cls){
		 Assert.notNull(cls,"class not allow Null");
		 Assert.notNull(orders,"orders not allow Null");
		 Assert.notNull(sourceList,"sourceList not allow Null");
		 Comparator desc = ComparableComparator.getInstance();  
		 desc = ComparatorUtils.nullLowComparator(desc);  //允许null且null优先
		 Comparator asc=desc;//asc 正序
		 desc = ComparatorUtils.reversedComparator(desc); //desc 逆序    
		 ArrayList<BeanComparator> sortFields = new ArrayList<BeanComparator>();
		 for(Order o:orders){
			 Field declaredField=Reflections.getField(cls, o.getProperty());
			 if(declaredField!=null){
				 sortFields.add(new BeanComparator(o.getProperty(), Direction.asc==o.getDirection()?asc:desc));
			 }
		 }
		 if(sortFields.size()==0){ //no match
			 return ;
		 }
		 ComparatorChain multiSort = new ComparatorChain(sortFields);
		 Collections.sort(sourceList,multiSort); 
	}
	/**
	 * 对集合对象的某个属性按照参照排序集合
	 * @param items
	 * @param sourceList
	 * @param cls
	 * @param sortFieldName
	 */
	public static void fixedSort(Object[] items,List sourceList,Class cls,String sortFieldName){
		 Assert.notNull(items,"items not allow Null");
		 Assert.notNull(cls,"cls not allow Null");
		 Assert.notNull(sortFieldName,"sortFieldName not allow Null");
		 Assert.notNull(sourceList,"sourceList not allow Null");
	     Field declaredField=Reflections.getField(cls, sortFieldName);
	     if(declaredField==null) return ;
	     ComparatorChain compChain = new ComparatorChain();
	     FixedOrderComparator severityComparator = new FixedOrderComparator(items);
	     severityComparator.setUnknownObjectBehavior(FixedOrderComparator.UNKNOWN_AFTER);
		 compChain.addComparator(new BeanComparator(sortFieldName, severityComparator));
		 Collections.sort(sourceList, compChain);
	}
	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * 
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 */
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(Reflections.invokeGetter(obj, keyPropertyName),
						Reflections.invokeGetter(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}
	
	public static Map extractToMap(final Collection collection, final String keyPropertyName){
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(Reflections.invokeGetter(obj, keyPropertyName),
						obj);
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(Reflections.invokeGetter(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection collection) {
		return ((collection == null) || collection.isEmpty());
	}
	public static boolean isEmpty(Object[] collection) {
		return ((collection == null) || collection.length==0);
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isNotEmpty(Collection collection) {
		return ((collection != null) && !(collection.isEmpty()));
	}
	public static boolean isNotEmpty(Object[] collection) {
		return ((collection != null) && collection.length>0);
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素 ，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		// 当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		// 其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}

		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();

		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
	
	public static String[] getValue(Serializable ...sers){
		//System.out.println(sers.length+":"+sers);
		if(sers!=null&&sers.length>0){
			if(sers[0] instanceof Object[]){
				Object[] o=(Object[]) sers[0];
				String[] value=new String[o.length];
				int i=0;
				for(Object s:o){
					Assert.notNull(s,"sers not allow contains Null");
					value[i++]=s.toString();
				}
				return value;
				
			}else{
				String[] value=new String[sers.length];
				int i=0;
				for(Object s:sers){
					Assert.notNull(s,"sers not allow contains Null");
					value[i++]=s.toString();
				}
				return value;
			}
		}
		return new String[]{""};
	}
	
	public static void main(String[] args) {
		//System.out.println(Arrays.asList(getValue(1,2,5.0,6.8)));
//		String[] str={"1","2"};
//		System.out.println(Arrays.asList(getValue(str)));
//		System.out.println(Arrays.asList(getValue(str,str,str)));
//		 AtomicLong                        uriStatMapFullCount            = new AtomicLong();
//		 uriStatMapFullCount.set(0);
//		long andIncrement = uriStatMapFullCount.getAndIncrement();
//		System.out.println(andIncrement);
		
		Map<Integer,String> map = new LinkedHashMap<Integer,String>();
		map.put(1, "星期一");
		map.put(2, "星期二");
		map.put(3, "星期三");
		map.put(4, "星期四");
		map.put(5, "星期五");
		map.put(6, "星期六");
		map.put(7, "星期日");
		System.out.println(map.containsKey(9));
		for(Map.Entry<Integer, String> entry: map.entrySet()) {
			 System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
		}
		for(Integer dataKey : map.keySet())   {  
		    System.out.println(dataKey );             
		}
	}
}
