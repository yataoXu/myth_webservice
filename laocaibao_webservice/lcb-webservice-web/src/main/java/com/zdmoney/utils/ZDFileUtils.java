package com.zdmoney.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinqigu on 2015/11/13.
 */
@Slf4j
public final class ZDFileUtils {

    public static final String UTF_8 = "UTF-8";

    private ZDFileUtils() {

    }

    /**
     * 文件读取
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static List<String> readFileLine(String filePath, String fileName) {
        List<String> detailTxtList = new ArrayList<>();
        InputStreamReader read = null;
        BufferedReader reader = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath + fileName);
            if (file.exists() && file.isFile() && file.canRead()) {
                in = new FileInputStream(file);
                read = new InputStreamReader(in, UTF_8);
                reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    detailTxtList.add(line);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    log.error(ioe.getMessage(), ioe);
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException ioe) {
                    log.error(ioe.getMessage(), ioe);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    log.error(ioe.getMessage(), ioe);
                }
            }
        }
        return detailTxtList;
    }

//    Workbook book = null;
//    try {
//        book = new XSSFWorkbook(excelFile);
//    } catch (Exception ex) {
//        book = new HSSFWorkbook(new FileInputStream(excelFile));
//    }

    public static List<String[]> parseXlsxFile(InputStream inputStream) throws IOException {
        List<String[]> result = new LinkedList<>();
        XSSFSheet sheet0 = new XSSFWorkbook(inputStream).getSheetAt(0);
        int rowNum = sheet0.getLastRowNum();
        int cellNum = sheet0.getRow(0).getLastCellNum();
        for (int i = 1; i <= rowNum; i++) {
            String[] rowData = new String[cellNum];
            XSSFRow row = sheet0.getRow(i);
            if(null == row) {
                continue;
            }
            for (int j = 0; j < cellNum; j++) {
                rowData[j] = getXlsxCellValue(row.getCell(j));
            }
            if (StringUtils.isEmpty(StringUtils.join(rowData, ""))){
                continue;
            }
            result.add(rowData);
        }
        return result;
    }

    public static List<String[]> parseXlsFile(InputStream inputStream) throws IOException {
        List<String[]> result = new LinkedList<>();
        HSSFSheet sheet0 = new HSSFWorkbook(new POIFSFileSystem(inputStream)).getSheetAt(0);
        int rowNum = sheet0.getLastRowNum();
        int cellNum = sheet0.getRow(0).getLastCellNum();
        for (int i = 1; i <= rowNum; i++) {
            String[] rowData = new String[cellNum];
            HSSFRow row = sheet0.getRow(i);
            if(null == row) {
                continue;
            }
            for (int j = 0; j < cellNum; j++) {
                rowData[j] = getCellValue(row.getCell(j));
            }
            if (StringUtils.isEmpty(StringUtils.join(rowData, ""))){
                continue;
            }
            result.add(rowData);
        }
        return result;
    }

    private static String getXlsxCellValue(XSSFCell cell) {
        String value = "";
        if (cell == null) {
            return value;
        }
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
            default:
                value = "";
                break;
        }
        return value;
    }

    private static String getCellValue(HSSFCell cell) {
        String value = "";
        if (cell == null) {
            return value;
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
            default:
                value = "";
                break;
        }
        return value;
    }


}
