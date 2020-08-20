package com.hapi.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * excel 工具
 * 
 * @author Admin
 *
 */
public class ExcelUtils {

	@SuppressWarnings("resource")
	public static <T> void httpDownload(HttpServletResponse response, String fileName,  List<String> fields, List<T> data) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		
		if( data == null ) {
			data = new ArrayList<T>();
		}
		for(int i=0;i<data.size();i++) {
			Object rowData = data.get(i);
			if( rowData == null ) {
				continue;
			}
			
			Map<String, String>  map = BeanUtils.toFormatMap(rowData);
			HSSFRow row = sheet.createRow(i);
			for( int j=0;j<fields.size();j++ ) {
				HSSFCell cell = row.createCell(j);
				String value = map.get( fields.get( j ) );
				cell.setCellValue( value==null?"":value );
			}
		}

		response.setHeader("Content-Disposition","attachment;filename=" + new String( fileName.getBytes(), "iso-8859-1") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;fileName=");
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
	}
	
	
	@SuppressWarnings("resource")
	public static <T> void httpDownload(HttpServletResponse response, String fileName, List<String> titles , List<String> fields, List<T> data) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		
		if( data == null ) {
			data = new ArrayList<T>();
		}
		
		HSSFRow titleRow = sheet.createRow(0);
		for( int j=0;j<titles.size();j++ ) {
			HSSFCell cell = titleRow.createCell(j);
			cell.setCellValue( titles.get(j) );
		}
		
		for(int i=0;i<data.size();i++) {
			Object rowData = data.get(i);
			if( rowData == null ) {
				continue;
			}
			
			Map<String, String>  map = BeanUtils.toFormatMap(rowData);
			HSSFRow row = sheet.createRow(i+1);
			for( int j=0;j<fields.size();j++ ) {
				HSSFCell cell = row.createCell(j);
				String value = map.get( fields.get( j ) );
				cell.setCellValue( value==null?"":value );
			}
		}
		
		response.setHeader("Content-Disposition","attachment;filename=" + new String( fileName.getBytes(), "iso-8859-1") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;fileName=");
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
	}

	
	
	

}
