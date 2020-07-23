package com.zdmoney.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导入Excel, 支持xls和xlsx格式
 */
public class PoiReadExcel {
	
	public static DecimalFormat nf = new DecimalFormat("0");// 格式化 number String
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
	public static DecimalFormat df = new DecimalFormat("#0.########");// 格式化数字
	
	/**
	 * 读取Excel
	 * @param file
	 * @return List<Map<String, Object>>
	 * @throws IOException
	 */
	public static List<Map<String, Object>> readExcel(File file) throws Exception {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<Map<String, Object>> read2003Excel(File file)throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hwb.getSheetAt(0);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int counter = 0;
		for (int i = 0; counter <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}else{
				counter++;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell)) {  
		                //如果是date类型则 ，获取该cell的date值  
		                Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());  
		                value = sdf.format(date);  
		            }else {//数字  
		            	String num = nf.format(cell.getNumericCellValue());  
		                if(num.indexOf(".") == -1){
		                	value = num;
		                }else{
		                	value = df.format(num);
		                }
		            }
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				map.put("c"+(j+1),value);
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 * */
	private static List<Map<String, Object>> read2007Excel(File file)throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row = null;
        Object objStr = null;
        XSSFCell cell = null;
        int counter = 0;
        for (int i = 0; counter <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }else{
                counter++;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                objStr = getCellValue2007(cell);
                if(objStr == "")break;
                map.put("c"+(j+1),objStr);
            }
            list.add(map);
        }
        return list;
	}
	
	public static Object getCellValue2007(XSSFCell cell){
		Object value = null;
		if(cell != null){
			switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell)) {  
		                //如果是date类型则 ，获取该cell的date值  
		                Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());  
		                value = sdf.format(date);  
		            }else {//数字  
		                Double num = cell.getNumericCellValue();  
		                if(num.toString().indexOf(".") == -1){
		                	value = nf.format(num);
		                }else{
		                	value = df.format(num);
		                }
		            }
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
			}
		}
		return value;
	}
}