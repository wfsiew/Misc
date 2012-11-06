package app;

import jxl.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import app.bean.ProductSetBean;

public class ProductSetApp {
	private static final int DEFAULT_COLOR = 192;
	private static final int MAX_COLUMN = 20;
	
	private static ProductSetApp instance;
	
	public static ProductSetApp getInstance() throws Exception {
		try {
			if (instance == null)
				instance = new ProductSetApp();
			
			else
				throw new Exception("This instance already exist. Please remove the current instance first!");
		}
		
		catch (Exception e) {
			throw new Exception("getInstance() - Error : " + e.getMessage());
		}
		
		return instance;
	}
	
	private ArrayList<ProductSetBean> getProductSet() throws Exception {
		File file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Cell cell = null;
		jxl.format.CellFormat cellFormat = null;
		String filename = "c:\\Limited offer all sets.xls";
		ProductSetBean productSetBean = null;
		ProductSetBean tmp = null;
		ArrayList<ProductSetBean> productSetBeanList = new ArrayList<ProductSetBean>();
		boolean shouldAdd = false;
		String content = null;
		
		try {
			file = new File(filename);
			
			workbook = Workbook.getWorkbook(file);
			sheet = workbook.getSheet(0);
			
			for (int j = 3; j < sheet.getRows(); j++) {
				productSetBean = new ProductSetBean();
				shouldAdd = false;
				
				if (!productSetBeanList.isEmpty())
					tmp = productSetBeanList.get(productSetBeanList.size() - 1);
				
				else
					tmp = productSetBean;
				
				for (int i = 0; i < MAX_COLUMN; i++) {
					cell = sheet.getCell(i, j);
					
					if (cell == null)
						continue;
					
					cellFormat = cell.getCellFormat();
					
					if (cellFormat != null && cellFormat.getBackgroundColour().getValue() != DEFAULT_COLOR)
						shouldAdd = true;
					
					switch (i) {
					case 0:
						content = cell.getType() == CellType.EMPTY ? tmp.getCode() : cell.getContents();
						productSetBean.setCode(content);
						break;
						
					case 1:
						content = cell.getType() == CellType.EMPTY ? tmp.getType() : cell.getContents();
						productSetBean.setType(content);
						break;
						
					case 2:
						content = cell.getType() == CellType.EMPTY ? tmp.getPriceType() : cell.getContents();
						productSetBean.setPriceType(content);
						break;
						
					case 3:
						content = cell.getType() == CellType.EMPTY ? tmp.getItemQuantity() : cell.getContents();
						productSetBean.setItemQuantity(content);
						break;
					
					case 4:
						content = cell.getType() == CellType.EMPTY ? tmp.getChildCode() : cell.getContents();
						productSetBean.setChildCode(content);
						break;
						
					case 5:
						content = cell.getType() == CellType.EMPTY ? tmp.getProductGrouping() : cell.getContents();
						productSetBean.setProductGrouping(content);
						break;
						
					case 6:
						content = cell.getType() == CellType.EMPTY ? tmp.getChildQuantity() : cell.getContents();
						productSetBean.setChildQuantity(content);
						break;
						
					case 7:
						content = cell.getType() == CellType.EMPTY ? tmp.getChildRetailPrice() : cell.getContents();
						productSetBean.setChildRetailPrice(content);
						break;
						
					case 8:
						content = cell.getType() == CellType.EMPTY ? tmp.getChildPricePerUnit() : cell.getContents();
						productSetBean.setChildPricePerUnit(content);
						break;
						
					case 9:
						content = cell.getType() == CellType.EMPTY ? tmp.getChildEVPerUnit() : cell.getContents();
						productSetBean.setChildEVPerUnit(content);
						break;
						
					case 10:
						content = cell.getType() == CellType.EMPTY ? tmp.getTotalChildPrice() : cell.getContents();
						productSetBean.setTotalChildPrice(content);
						break;
						
					case 11:
						content = cell.getType() == CellType.EMPTY ? tmp.getTotalChildEV() : cell.getContents();
						productSetBean.setTotalChildEV(content);
						break;
					}
				}
				
				if (shouldAdd)
					productSetBeanList.add(productSetBean);
			}
		}
		
		catch (Exception e) {
			throw new Exception("getProductSet() - Error : " + e.getMessage());
		}
		
		finally {
			if (workbook != null)
				workbook.close();
		}
		
		return productSetBeanList;
	}
	
	private void generateFile(ArrayList<ProductSetBean> productSetBeanList) throws Exception {
		File productSetFile = null;
		File productGroupFile = null;
		FileWriter productSetFileWriter = null;
		FileWriter productGroupFileWriter = null;
		BufferedWriter buf1 = null;
		BufferedWriter buf2 = null;
		Calendar c = Calendar.getInstance();
		StringBuffer sb = null;
		String q1 = null;
		String q2 = null;
		
		String filename_productset = "c:\\product_set_price_" + fmtDate(c.getTime()) + ".txt";
		String filename_productgroup = "c:\\product_set_group_" + fmtDate(c.getTime()) + ".txt";
		
		try {
			productSetFile = new File(filename_productset);
			productGroupFile = new File(filename_productgroup);
			
			if (!productSetFile.exists())
				productSetFile.createNewFile();
			
			if (!productGroupFile.exists())
				productGroupFile.createNewFile();
			
			productSetFileWriter = new FileWriter(productSetFile.getAbsoluteFile());
			productGroupFileWriter = new FileWriter(productGroupFile.getAbsoluteFile());
			
			buf1 = new BufferedWriter(productSetFileWriter);
			buf2 = new BufferedWriter(productGroupFileWriter);
			
			sb = new StringBuffer("insert into product_set_price ")
			.append("(PARENT_PRICE_REF_NO, CHILD_PROD_REF_NO, GROUP_REF_NO, QTY, RETAIL_PRICE, PRICE_PER_UNIT, EV_PER_UNIT, TOTAL_PRICE, TOTAL_EV) ")
			.append("select price_ref_no, (select prod_ref_no from product where prod_id = '%s' and locale = 'en_US'),%s,%s,%s,%s,%s,%s,%s ")
			.append("from product_price where prod_ref_no = (select prod_ref_no from product where prod_id = '%s' and locale = 'en_US') and type in ")
			.append("('%s') and status = 'A' and category_ref_no in ")
			.append("(select child_category_ref_no from category_rel where parent_category_ref_no = 300012) ORDER BY EFFECTIVE_DATE DESC FETCH FIRST ROWS ONLY;");
			
			q1 = sb.toString();
			
			sb.setLength(0);
			
			sb.append("insert into product_set_group (PARENT_PRICE_REF_NO, PARENT_PROD_REF_NO, GROUP_REF_NO, TYPE, SET_ITEM_QTY) ")
			.append("select PRICE_REF_NO, PROD_REF_NO,%s,'%s',%s ")
			.append("from product_price where prod_ref_no = (select prod_ref_no from product where prod_id = '%s' ")
			.append("and locale = 'en_US') and type in ('%s') ")
			.append("and status = 'A' and category_ref_no in (select child_category_ref_no from category_rel where parent_category_ref_no = 300012) ORDER BY EFFECTIVE_DATE DESC FETCH FIRST ROWS ONLY;");
			
			q2 = sb.toString();
			
			buf1.write("connect to my_store user ecos using abcdef;\n\n");
			buf2.write("connect to my_store user ecos using abcdef;\n\n");
			
			for (ProductSetBean productSetBean : productSetBeanList) {
				buf1.write(String.format(q1, productSetBean.getChildCode(), productSetBean.getProductGrouping(), productSetBean.getChildQuantity(),
						productSetBean.getChildRetailPrice(), productSetBean.getChildPricePerUnit(), productSetBean.getChildEVPerUnit(), 
						productSetBean.getTotalChildPrice(), productSetBean.getTotalChildEV(), productSetBean.getCode(), 
						productSetBean.getPriceType()));
				buf1.write("\n");
				
				buf2.write(String.format(q2, productSetBean.getProductGrouping(), productSetBean.getType(), productSetBean.getItemQuantity(), 
						productSetBean.getCode(), productSetBean.getPriceType()));
				buf2.write("\n");
			}
			
			buf1.write("\nterminate;");
			buf2.write("\nterminate;");
		}
		
		catch (Exception e) {
			throw new Exception("generateFile() - Error : " + e.getMessage());
		}
		
		finally {
			if (buf1 != null)
				buf1.close();
			
			if (buf2 != null)
				buf2.close();
			
			if (productSetFileWriter != null)
				productSetFileWriter.close();
			
			if (productGroupFileWriter != null)
				productGroupFileWriter.close();
		}
	}
	
	private String fmtDate(Date dt) {
		SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy");
		return fmt.format(dt);
	}
	
	public static void main(String[] args) {
		ProductSetApp instance = null;
		ArrayList<ProductSetBean> productSetBeanList = null;
		
		try {
			instance = ProductSetApp.getInstance();
			
			productSetBeanList = instance.getProductSet();
			System.out.println("Size = " + productSetBeanList.size());
			instance.generateFile(productSetBeanList);
		}
		
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}