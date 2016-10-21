package com.dongao.core.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 读取excel数据，用作数据导入
 * 
 * @date 2013-10-24 上午09:29:21
 * @author dongao version 1
 */
public class ExcelRead implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 文件类
	 */
	private File xlsFile;
	/**
	 * 读取XLS内容的集合类
	 */
	private List<String> contentList;
	/**
	 * 列数
	 */
	private int cols = 0;
	/**
	 * 行数
	 */
	private int rows = 0;

	/**
	 * 构造器，初始化文件类，并读取XLS文件
	 * @param xlsFile
	 */
	public ExcelRead(File xlsFile) {
		this.xlsFile = xlsFile;
		this.readXLS();
	}

	/**
	 * 构造器，是通过文件路径、文件全名来初始化文件类，并读取XLS文件
	 * @param xlsFileName
	 */
	public ExcelRead(String xlsFileName) {
		this.xlsFile = new File(xlsFileName);
		this.readXLS();
	}

	/**
	 * 读取XLS文件
	 */
	private void readXLS() {
		DecimalFormat df = new DecimalFormat("#");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		contentList = new ArrayList<String>();
		try {
			HSSFWorkbook xlsWorkbook = new HSSFWorkbook(new FileInputStream(xlsFile));
			if (null != xlsWorkbook.getSheetAt(0)) {
				HSSFSheet aSheet = xlsWorkbook.getSheetAt(0);// 获得第一个sheet
				rows = aSheet.getLastRowNum() + 1;
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
					HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
					cols = Integer.parseInt(Short.toString(aRow.getLastCellNum()));
					for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
						if (null != aRow.getCell(cellNumOfRow)) {
							HSSFCell aCell = aRow.getCell(cellNumOfRow);
							int cellType = aCell.getCellType();
							String strCell = null;
							switch (cellType) {
							case HSSFCell.CELL_TYPE_NUMERIC:// Numeric
								if (HSSFDateUtil.isCellDateFormatted(aCell))
									strCell = dateFormat.format(aCell.getDateCellValue());
								else
									strCell = df.format(aCell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_STRING:// String
								strCell = aCell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_BLANK:// String
								break;
							case HSSFCell.CELL_TYPE_ERROR:// String
								break;
							default:
								System.out.println("格式不对不读");// 其它格式的数据
							}
							if (strCell != null)
								contentList.add(strCell);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取行内容
	 * @param row
	 * @return String[]
	 */
	public String[] getRowContent(int row) {
		if (cols == 0)
			return null;
		String[] rowContent = new String[cols];
		for (int j = 0; j < cols; j++) {
			rowContent[j] = contentList.get(row * cols + j);
		}
		return rowContent;
	}

	/**
	 * 读取标题内容
	 * @return
	 */
	public String[] getTitleContent() {
		String[] titleContent = new String[cols];
		for (int i = 0; i < cols; i++) {
			titleContent[i] = contentList.get(0 + i);
		}
		return titleContent;
	}

	/**
	 * 获取列数
	 * @return int
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * 获取行数
	 * @return int
	 */
	public int getRows() {
		return rows;
	}

}
