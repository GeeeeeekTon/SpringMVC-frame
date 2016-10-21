package com.dongao.core.mybase;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 排序
 * 
 * @author zhangzhen 
 * 
 * v1.0
 * 
 */
public class Order implements Serializable {
	private static final long serialVersionUID = -3078342809727773232L;

	/**
	 * 方向
	 */
	public enum Direction {
		/** 递增 */
		asc,

		/** 递减 */
		desc;
		/**
		 * 从String中获取Direction
		 * 
		 * @param value
		 *            值
		 * @return String对应的Direction
		 */
		public static Direction fromString(String value) {
			return Direction.valueOf(value.toLowerCase());
		}
	}

	/** 默认方向 */
	private static final Direction DEFAULT_DIRECTION = Direction.desc;

	/** 属性 */
	private String property;

	/** 方向 */
	private Direction direction = DEFAULT_DIRECTION;
	private Order(){}
	/**
	 * @param property
	 *            属性
	 * @param direction
	 *            方向
	 */
	private Order(String property, Direction direction) {
		this.property = property;
		this.direction = direction;
	}
	/**
	 * 返回递增排序
	 * 
	 * @param property
	 *            属性
	 * @return 递增排序
	 */
	public static Order asc(String property) {
		return new Order(property, Direction.asc);
	}

	/**
	 * 返回递减排序
	 * 
	 * @param property
	 *            属性
	 * @return 递减排序
	 */
	public static Order desc(String property) {
		return new Order(property, Direction.desc);
	}
	/**
	 * 获取属性
	 * 
	 * @return 属性
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * 获取方向
	 * 
	 * @return 方向
	 */
	public Direction getDirection() {
		return direction;
	}
	public static Map<String,Order> parse(Map<String, String[]> params){
		if (params == null || params.isEmpty()) return Collections.emptyMap();
		Map<String, Order> orders = new HashMap<String, Order>();
		for (Entry<String, String[]> entry : params.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (ArrayUtils.isEmpty(values)) {
				continue;
			}
			if(values[0]==null||"".equals(values[0])){
				continue;
			}
			Direction direction = Direction.fromString(values[0]);
			orders.put(key, new Order(key, direction));
		}
		return orders;	
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
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
		Order other = (Order) obj;
		if (direction != other.direction)
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println();
	}


}