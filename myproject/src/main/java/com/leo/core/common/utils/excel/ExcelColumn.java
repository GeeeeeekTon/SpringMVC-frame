package com.leo.core.common.utils.excel;

import java.util.Map;

/**
 * 导出excel 列对象
 * 
 * @date：2014-3-7 下午02:35:03    
 * @author leo@leo.com
 * @version 1
 */
public class ExcelColumn implements java.io.Serializable{

	private static final long serialVersionUID = 5073949199351381541L;
	private String columnName; // 列名
	private ColumnType columnType; // 类型
	private int columnOrder; // 列排序
	private String propertyName; // 对应实体属性名称
	private int columnWidth; // 列宽
	private Map<Object, String> columnKeyValue; // 列值的键值对应
	
	public ExcelColumn() {
	}
	
	public ExcelColumn(String columnName, ColumnType columnType, int columnOrder,
			String propertyName, int columnWidth) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnOrder = columnOrder;
		this.propertyName = propertyName;
		this.columnWidth = columnWidth;
	}
	
	public ExcelColumn(String columnName, ColumnType columnType, int columnOrder,
			String propertyName, int columnWidth, Map<Object, String> columnKeyValue) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnOrder = columnOrder;
		this.propertyName = propertyName;
		this.columnWidth = columnWidth;
		this.columnKeyValue = columnKeyValue;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public ColumnType getColumnType() {
		return columnType;
	}
	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}
	public int getColumnOrder() {
		return columnOrder;
	}
	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public Map<Object, String> getColumnKeyValue() {
		return columnKeyValue;
	}

	public void setColumnKeyValue(Map<Object, String> columnKeyValue) {
		this.columnKeyValue = columnKeyValue;
	}
}
