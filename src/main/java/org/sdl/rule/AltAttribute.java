package org.sdl.rule;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.task.SeleniumHandler;
import org.sdl.util.AppUtil;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AltAttribute {
	XSSFWorkbook workbook = new XSSFWorkbook();
	// Create a new sheet in the workbook
	XSSFSheet sheet;
	int rowNum;
	public void execute(String filePath, WebDriver driver, List<RulesOutputHolder> outputList,int b,String url) throws IOException {
		sheet= workbook.createSheet("Accessibility"+b);
		List<WebElement> images = driver.findElements(By.tagName("img"));
		boolean allImagesHaveAlt = true;
		// Initialize a row index counter
				XSSFRow headingRow1 = sheet.createRow(rowNum++);
				XSSFRow headingRow = sheet.createRow(rowNum++);
				// Create a cell for the heading
				XSSFCell headingCell = headingRow.createCell(0);
				XSSFCell headingCell1 = headingRow1.createCell(1);
				// Set the heading text
				headingCell.setCellValue("Buttons Without Aria Label");
			
				headingCell1.setCellValue(url);
				headingRow1.createCell(0).setCellValue("Link");

				// Create a font with bold style for the heading
				XSSFFont boldFont = workbook.createFont();
				boldFont.setBold(true);
				// Apply the bold font to the heading cell
				XSSFCellStyle boldCellStyle = workbook.createCellStyle();
				boldCellStyle.setFont(boldFont);
				headingCell.setCellStyle(boldCellStyle);

				// Set default font for other cells
				XSSFFont defaultFont = workbook.createFont();
				defaultFont.setBold(false);
				XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
				defaultCellStyle.setFont(defaultFont);
		for (WebElement image : images) {
			String altText = image.getAttribute("alt");
			if (altText == null || altText.isEmpty()) {
				allImagesHaveAlt = false;
				XSSFRow row = sheet.createRow(rowNum++);
				// Create a new cell in the row and set the button text as its value
				row.createCell(3).setCellValue(image.getAttribute("src"));
				System.out.println("Image missing alt attribute:" + image.getAttribute("src"));
				AppUtil.log(this.getClass(), "Image missing alt attribute:" + image.getAttribute("src"));
				
				RulesOutputHolder outputHolder =new RulesOutputHolder();
				outputHolder.setRuleName("AltAttribute");
				outputHolder.setRuleDescription("Rule check if image has alternative text");
				outputHolder.setRuleStatus("FAILED");
				outputHolder.setFieldType("Image");
				outputHolder.setFieldIdentifier(image.getAttribute("src"));
				outputHolder.setFieldDescription("Link opens image with missing alternative text");
				outputHolder.setURL(SeleniumHandler.WORKING_URL);
				outputList.add(outputHolder);
			}
		}
		if (allImagesHaveAlt) {
			System.out.println("All images have alt attributes.");

			AppUtil.log(this.getClass(), "All images have alt attributes.");
		} else {
			System.out.println("Some images are missing alt attributes.");
			AppUtil.log(this.getClass(), "Some images are missing alt attributes.");
		}
//		  try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//		        workbook.write(fileOut);
//		    }

	}
	
}