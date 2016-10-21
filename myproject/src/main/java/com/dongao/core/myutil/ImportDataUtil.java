/*
 * Copyright (c) 2000-2009 CDELEDU.COM. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * CDELEDU.COM. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with CDELEDU.COM.
 *
 */
package com.dongao.core.myutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.druid.util.StringUtils;

/**
 * 
 * @version 1.0 2012-4-1
 * @author yaoxin - yaoxin@cdeledu.com
 */
public class ImportDataUtil {

	InputStream fs = null;
	Workbook wb = null;
	Sheet sh = null;

	public ImportDataUtil(String completePath) {
		try {
			fs = new FileInputStream(completePath);
			if (isExcel2003(completePath)) {
				wb = new HSSFWorkbook(fs); // word2003
			} else {
				wb = new XSSFWorkbook(fs); // word2007
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 得到 workbook

		/**
		 * 取得sheet，如果你的workbook里有多个sheet 可以利用 wb.getSheets()方法来得到所有的。
		 * getSheets() 方法返回 Sheet[] 数组 然后利用数组来操作。
		 */
		sh = wb.getSheetAt(0);
	}

	public int getColumnCount() {
		return sh.getRow(0).getPhysicalNumberOfCells();
	}

	public boolean validateMap(String classFullName,
			Map<Integer, String> columnIndexPropertyNameMap) {
		boolean returnValue = true;
		Class objectClass = null;
		try {
			objectClass = Class.forName(classFullName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// //System.out.println("参数classFullName有错误");
			returnValue = false;
		}
		int columnCount = getColumnCount() - 1;
		Set<Integer> relationSet = columnIndexPropertyNameMap.keySet();
		Iterator<Integer> it = relationSet.iterator();

		List<String> mapValueList = new ArrayList<String>();// 用户放入map中的类属性
		List<String> propertyFieldList = new ArrayList<String>();// 实际类中的属性

		// 判断map和实体类中属性是否一致
		while (it.hasNext()) {
			Integer keyValue = it.next();
			if (keyValue > columnCount) {
				returnValue = false;
				break;
			}
			String mapValue = columnIndexPropertyNameMap.get(keyValue);
			mapValueList.add(mapValue);
		}
		Field[] propertyFields = objectClass.getDeclaredFields();
		for (int i = 0; i < propertyFields.length; i++) {
			propertyFieldList.add(propertyFields[i].getName());
		}
		if (!propertyFieldList.containsAll(mapValueList)) {
			returnValue = false;
		}
		return true;
	}

	public List makeRelationShip(String classFullName,
			LinkedHashMap<Integer, String> columnIndexPropertyNameMap) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		boolean validateValue = validateMap(classFullName,
				columnIndexPropertyNameMap);
		List<Object> objectList = new ArrayList<Object>();
		if (validateValue) {
			try {
				// 得到总行数
				for (int i = 0; i < sh.getPhysicalNumberOfRows(); i++) {
					if (i == 0) {
						continue;// Excel表中第一行不做入库操作,因为excel表中第一行通常是文字标题，不是数据。
					}
					Class objectClass = Class.forName(classFullName);
					Object object = objectClass.newInstance();
					Iterator<Integer> columnIndexIt = columnIndexPropertyNameMap
							.keySet().iterator();
					while (columnIndexIt.hasNext()) {
						int columnIndext = columnIndexIt.next();
						Cell cell = sh.getRow(i).getCell(columnIndext);// 单元格
						String objectProperty = columnIndexPropertyNameMap
								.get(columnIndext);
						Field field = objectClass
								.getDeclaredField(objectProperty);
						field.setAccessible(true);// 设置允许访问
						object = getObjectByReflacted(object, field,
								getCellFormatValue(cell));
					}
					objectList.add(object);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	private Object getObjectByReflacted(Object object, Field field,
			String tempColumnContent) throws Exception {
		String fieldTypeName = field.getType().getName();
		if (fieldTypeName.equals("java.lang.Short")) {
			if (tempColumnContent != null
					&& !"".equals(tempColumnContent.trim())) {
				field.set(object, Short.valueOf(tempColumnContent));
			}
		}
		if (fieldTypeName.equals("java.lang.Integer")) {
			if (tempColumnContent != null
					&& !"".equals(tempColumnContent.trim())) {
				field.set(object, Integer.valueOf(tempColumnContent));
			}
		}
		if (fieldTypeName.equals("java.util.Date")) {// 可以继续扩展
			if (tempColumnContent != null && tempColumnContent != "") {
				String regx = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
				Pattern pattern = Pattern.compile(regx);
				boolean flag = pattern.matcher(tempColumnContent).matches();
				if (flag) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date newTempColumnContent = sdf.parse(tempColumnContent);
					field.set(object, newTempColumnContent);
				} else {
					throw new RuntimeException("日期格式错误");
				}

			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date newTempColumnContent = sdf.parse("1970-01-01");
				field.set(object, newTempColumnContent);
			}

		}
		if (fieldTypeName.equals("int")) {
			field.set(object, tempColumnContent);
		}
		if (fieldTypeName.equals("java.lang.String")) {
			tempColumnContent = tempColumnContent.trim();
			tempColumnContent = tempColumnContent.replaceAll("　", "");
			tempColumnContent = tempColumnContent.replaceAll(" ", "");
			field.set(object, tempColumnContent);
		}
		if (fieldTypeName.equals("java.lang.Float")) {
			field.set(object, Float.valueOf(tempColumnContent));
		}
		if (fieldTypeName.equals("java.lang.Double")) {
			field.set(object, Double.valueOf(tempColumnContent));
		}
		if (fieldTypeName.equals("java.math.BigDecimal")) {
			field.set(object, new BigDecimal(tempColumnContent));
		}
		return object;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellvalue = cell.getStringCellValue();
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return com.dongao.core.common.utils.StringUtils.trimQaSpaces(cellvalue);
	}

	/**
	 * 是否是2003的excel，返回true是2003
	 * 
	 * @param filePath
	 * @return
	 */

	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 是否是2007的excel，返回true是2007
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
	
	public static void main(String[] args){
		//ImportDataUtil i = new ImportDataUtil("E:\\fileupload\\opencourse\\7E53BA99425C4B4BAFBE8424140830C9.XLSX");
		System.out.println(com.dongao.core.common.utils.StringUtils.trimQaSpaces("aa bbb"));
		System.out.println(com.dongao.core.common.utils.StringUtils.trimQaSpaces("aa bbb"));
		System.out.println(com.dongao.core.common.utils.StringUtils.trimQaSpaces("aa bbb cc"));
	}
	
}
