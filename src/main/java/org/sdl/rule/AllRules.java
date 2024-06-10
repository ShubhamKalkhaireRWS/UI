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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AllRules {
	XSSFWorkbook workbook = new XSSFWorkbook();
	// Create a new sheet in the workbook
	XSSFSheet sheet;
	int rowNum;

	
	public void ariaLabel(String filePath,WebDriver driver, List<RulesOutputHolder> outputList,int b,String url) throws FileNotFoundException, IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(filePath));
		sheet= workbook.createSheet("Accessibility"+b);
		List<WebElement> buttons = driver.findElements(By.tagName("button"));
		buttons.addAll(driver.findElements(By.tagName("a")));

		AppUtil.log(this.getClass(), "Aria Label rule invoked");

		System.out.println("Elements found : " + buttons.size());
		AppUtil.log(this.getClass(), "Elements found : " + buttons.size());

		System.out.println("Elements without aria-label:");
		AppUtil.log(this.getClass(), "Elements without aria-label:");
		
		// Initialize a row index counter
				XSSFRow headingRow1 = sheet.createRow(rowNum++);
				XSSFRow headingRow = sheet.createRow(rowNum++);
				// Create a cell for the heading
				XSSFCell headingCell = headingRow.createCell(1);
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

		for (WebElement button : buttons) {
			String tagName = button.getTagName();
			String ariaLabel = button.getAttribute("aria-label");
			if ((ariaLabel == null || ariaLabel.isEmpty()) && !StringUtils.isEmpty(button.getText())) {
				System.out.println("Button has no aria label : " + button.getText());
				AppUtil.log(this.getClass(), "Button has no aria label : " + button.getText());
				XSSFRow row = sheet.createRow(rowNum++);
				// Create a new cell in the row and set the button text as its value
				row.createCell(2).setCellValue(button.getText());


				RulesOutputHolder outputHolder = new RulesOutputHolder();
				outputHolder.setRuleName("AriaLable");
				outputHolder.setRuleDescription("Rule check if Button has Aria label");
				outputHolder.setRuleStatus("FAILED");
				outputHolder.setFieldType("Button");
				outputHolder.setFieldIdentifier(button.getText());
				outputHolder.setFieldDescription("Button text used as identifier");
				outputHolder.setURL(SeleniumHandler.WORKING_URL);
				outputList.add(outputHolder);
			}
		}
	 

	}
	
	
	public void altAttribute(String filePath, WebDriver driver, List<RulesOutputHolder> outputList,int b,String url) throws IOException {
//		sheet= workbook.createSheet("Accessibility"+b);
		rowNum++;
		List<WebElement> images = driver.findElements(By.tagName("img"));
		boolean allImagesHaveAlt = true;
		// Initialize a row index counter
				XSSFRow headingRow1 = sheet.createRow(rowNum++);
				XSSFRow headingRow = sheet.createRow(rowNum++);
				// Create a cell for the heading
				XSSFCell headingCell = headingRow.createCell(1);
				XSSFCell headingCell1 = headingRow1.createCell(1);
				// Set the heading text
				headingCell.setCellValue("Buttons Without Alt Attribute");
			
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
				row.createCell(2).setCellValue(image.getAttribute("src"));
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
		  try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
		        workbook.write(fileOut);
		    }

	}
	
	public void keyboard(WebDriver driver, List<RulesOutputHolder> outputList) throws InterruptedException, IOException {
		String currentHandle = null;
		AppUtil.log(this.getClass(), "CheckKeyboardFunctionality invoked");

		try {
			Actions actions = new Actions(driver);

			// Move mouse to the right edge of the webpage
//		        actions.moveToElement(driver.findElement(By.cssSelector("input[title='Submit Search']"))).build().perform();
		
			String baseURL = driver.getCurrentUrl();
			List<WebElement> hrefs = driver.findElements(By.tagName("a"));

			int min = 1;
			int max = hrefs.size(); // Change this value to your desired maximum limit

			// Create an instance of Random class
			Random random = new Random();

			// Generate a random number within the specified range
			int randomNumber = random.nextInt(max - min + 1) + min;
			// Initialize Actions class
			List<String> autoCompleteElementsProcessed = new ArrayList<>();
			List<String> tabbedButton = new ArrayList<>();
			// Simulate pressing the tab button multiple times
			for (int i = 0; i < max; i++) {
				actions.sendKeys("\t").perform();
//				Thread.sleep(1000);
				WebElement focusedElement = driver.switchTo().activeElement();

				if (!(focusedElement.getAttribute("type") == null)) {
					if (focusedElement.getAttribute("type").equalsIgnoreCase("radio")) {
						focusedElement.sendKeys(Keys.DOWN);

					}

					if (focusedElement.getTagName().equalsIgnoreCase("Select")) {
						Thread.sleep(1000);
						Select sel = new Select(driver.findElement(By.name(focusedElement.getAttribute("name"))));
						int options = sel.getOptions().size() - 1;
						int randomOption = random.nextInt(options - min + 1) + min;

						for (int j = 0; j < randomOption; j++) {
							focusedElement.sendKeys(Keys.DOWN);
						}
						System.out.println(sel.getFirstSelectedOption().getText());
						System.out.println(sel.getOptions().get(randomOption).getText());
						Assert.assertEquals(sel.getFirstSelectedOption().getText(),
								sel.getOptions().get(randomOption).getText());

					}

					if (focusedElement.getAttribute("type").equalsIgnoreCase("checkbox")) {
						focusedElement.sendKeys(Keys.SPACE);

						Assert.assertTrue(focusedElement.isSelected());
					}

					if (focusedElement.getAttribute("class").contains("autocomplete")
							&& !autoCompleteElementsProcessed.contains(focusedElement.getAttribute("name"))) {
						focusedElement.sendKeys("in");
						Thread.sleep(3000);
						List<WebElement> autoSuggestOptions = driver
								.findElements(By.cssSelector(".ui-menu.ui-widget.ui-widget-content li"));
						int autoSuggestOptionsCount = autoSuggestOptions.size();

						int randomAutoSuggestOption = random.nextInt(autoSuggestOptionsCount - min + 1) + min;
						String expected = autoSuggestOptions.get(randomAutoSuggestOption - 1).getText();
						for (int k = 0; k < randomAutoSuggestOption; k++) {
							focusedElement.sendKeys(Keys.DOWN);
							Thread.sleep(200);

						}
						Thread.sleep(1000);
						String actual = focusedElement.getAttribute("value");

						System.out.println("Actual : " + focusedElement.getAttribute("value"));
						autoCompleteElementsProcessed.add(focusedElement.getAttribute("name"));
						focusedElement.sendKeys(Keys.ENTER);

						Thread.sleep(2000);
						System.out.println("Expected : " + expected);
						Assert.assertEquals(actual, expected);

					}

				}

				String linkOfFocusedElement = focusedElement.getAttribute("href");
				if (!tabbedButton.contains(linkOfFocusedElement)) {
					if (focusedElement.getTagName().equals("a")
							&& !focusedElement.getText().equalsIgnoreCase("Skip to main content")) {
						System.out.println(focusedElement.getText());
						
						focusedElement.sendKeys(Keys.ENTER);
						WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
						wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
						String currentURL = null;
						Thread.sleep(2000);

						if (!(driver.getCurrentUrl().equals(baseURL))
								|| driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
						
							if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
								driver.switchTo().frame("__checkout2");
								Thread.sleep(3000);
								driver.findElement(By.xpath("//button[@data-tracking-element-name='closeButton']")).click();
								driver.switchTo().defaultContent();
							
							}
							JavascriptExecutor js = (JavascriptExecutor) driver;
					        boolean isDocumentReady = (boolean) js.executeScript("return document.readyState === 'complete';");

							if(isDocumentReady) {
							currentURL = driver.getCurrentUrl();}
						
							driver.navigate().back();

						} else {
							if (driver.getCurrentUrl().equals(baseURL)) {

								Thread.sleep(3000);
								if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
									Assert.assertTrue(true);
								} else {
									wait.until(ExpectedConditions.numberOfWindowsToBe(3));
									// Switch to the new tab
									Set<String> windowHandles = driver.getWindowHandles();
									for (String windowHandle : windowHandles) {
										if (!windowHandle.equals(currentHandle)) {
											String currentHandle1 = driver.getWindowHandle();
											if (!windowHandle.equals(currentHandle1)) {
												driver.switchTo().window(windowHandle);
												currentURL = driver.getCurrentUrl();
												driver.close();
												driver.switchTo().window(currentHandle1);
												break;
											}
										}
									}
								}
							}
						}
				
						if (driver.getCurrentUrl().contains("https://www.heart.org/?form=") || linkOfFocusedElement.contains("https://mygiving.heart.org/")) {
						
							Assert.assertTrue(true);
						} else {
						
							System.out.println(currentURL);
							System.out.println(linkOfFocusedElement);
							AppUtil.log(this.getClass(), "Current URL : " + currentURL);
							AppUtil.log(this.getClass(), "Link of Focused Element : " + linkOfFocusedElement);
							Assert.assertEquals(currentURL, linkOfFocusedElement);
							
						}
					}

				}
					tabbedButton.add(linkOfFocusedElement);
				}
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void mergeExcelFilesFromFolder(String folderPath, String outputPath) throws IOException, InvalidFormatException {
        XSSFWorkbook mergedWorkbook = new XSSFWorkbook();
        XSSFSheet targetSheet = mergedWorkbook.createSheet("Accessibility Result");

        // Get all XLSX files from the specified folder
        List<String> filePaths = getAllXLSXFilesInFolder(folderPath);

        for (String filePath : filePaths) {
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sourceSheet = workbook.getSheetAt(i);
                    copySheet(sourceSheet, targetSheet);
                    addColoredRow(targetSheet);
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            mergedWorkbook.write(outputStream);
        }
    }

    private static List<String> getAllXLSXFilesInFolder(String folderPath) throws IOException {
        List<String> filePaths = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath), "*.xlsx")) {
            for (Path path : directoryStream) {
                filePaths.add(path.toString());
            }
        }
        return filePaths;
    }

    private static void copySheet(Sheet sourceSheet, Sheet targetSheet) {
        int rowCount = targetSheet.getLastRowNum() + 1; // Get the current row count in the target sheet

        for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
           
            Row targetRow = targetSheet.createRow(rowCount++); // Create a new row in the target sheet
         
            if (sourceRow != null) {
                for (int j = sourceRow.getFirstCellNum(); j < sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    Cell targetCell = targetRow.createCell(j);

                    if (sourceCell != null) {
                        // Copy cell value and type directly
                       
                        switch (sourceCell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            targetCell.setCellValue(sourceCell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            targetCell.setCellValue(sourceCell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            targetCell.setCellValue(sourceCell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            targetCell.setCellFormula(sourceCell.getCellFormula());
                            break;
                        default:
                            // Handle other cell types as needed
                    }
                    }
                   
                }
            }
        }
        formatSheet(targetSheet);
    }
    private static void addColoredRow(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        Row coloredRow = sheet.createRow(lastRowNum + 4);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        style.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal()); // Convert FillPatternType to short
        
        Row lastRow = sheet.getRow(lastRowNum);
        int lastCellNum = lastRow != null ? lastRow.getLastCellNum() : 0;

        for (int i = 0; i < 100; i++) {
            Cell cell = coloredRow.createCell(i);
            cell.setCellStyle(style);
        }
    }
    
 

    
    private static void formatSheet(Sheet sheet) {
        // Set fixed column width
        int fixedColumnWidth = 30*256; // Adjust this value as needed (in units of 1/256th of a character width)
        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = 0;
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                lastCellNum = Math.max(lastCellNum, row.getLastCellNum());
            }
        }
        for (int i = 0; i < lastCellNum; i++) {
        	if(i==0) {
        		sheet.setColumnWidth(i, 5*256);
        	}else {
            sheet.setColumnWidth(i, fixedColumnWidth);
        }
        }	

        // Auto-adjust row height and enable text wrapping
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellStyle style = cell.getCellStyle();
                if (style == null) {
                    style = sheet.getWorkbook().createCellStyle();
                    cell.setCellStyle(style);
                }
                style.setWrapText(true); // Enable text wrapping
               
            }
        }
    }


    
    public void LinkCheck(WebDriver driver, String filePath,String url,int b) throws IOException {
    	rowNum++;
//    	PrintWriter writer = new PrintWriter(new FileWriter(filePath));
//		sheet= workbook.createSheet("Accessibility"+b);
        // Find all hyperlink elements
    	  List<String[]> errorLinks = new ArrayList<>(); // List to store links with exceptions

        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println(links.size());
 
        // Map to store link text and corresponding URL
        Map<String, String> linkMap = new HashMap<>();
        
        
    	// Initialize a row index counter
		XSSFRow headingRow1 = sheet.createRow(rowNum++);
		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(1);
		XSSFCell headingCell1 = headingRow1.createCell(1);
		// Set the heading text
		headingCell.setCellValue("Broken Links");
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
		
        // Loop through all hyperlinks
        for (WebElement link : links) {
            String text = link.getText().trim();
            String url1 = link.getAttribute("href");
        
            // Check if the link is working
            if (url1 != null && !url1.isEmpty() && !url1.contains("mailto:")) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url1).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
//                    System.out.println(url1);
//                    System.out.println(responseCode);
                    if(responseCode >400) {
                    	XSSFRow row = sheet.createRow(rowNum++);
                    	row.createCell(2).setCellValue("Response ");
                    	row.createCell(3).setCellValue(responseCode);
                    	row.createCell(4).setCellValue(url1);
                    	System.out.println("Broken link: " + url1 + " with response code: " + responseCode);
                    }
//                    Assert.assertTrue(responseCode < 400, "Broken link: " + url + " with response code: " + responseCode);
                } catch (IOException e) {
                    System.out.println("Exception while checking link: " + url1 +"Error :"+e);
                    errorLinks.add(new String[]{url1, e.toString()}); // Add link and error message to the list
                }
            } 
        }
        
        // After all links are checked, add the error links to the Excel sheet
        for (String[] errorLink : errorLinks) {
        	
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(2).setCellValue("Error occured :");
            row.createCell(3).setCellValue(errorLink[1]);
            row.createCell(4).setCellValue(errorLink[0]);
        }
        for (WebElement link : links) {
            String text = link.getText().trim();
            String url1 = link.getAttribute("href");
           
     
           
            if(!(text.isEmpty())) {
            // If the text is already in the map, check if the URLs match
            if (linkMap.containsKey(text)) {
           
            	XSSFRow row = sheet.createRow(rowNum++);
            	row.createCell(2).setCellValue("Same Text with Different Links");
            	row.createCell(3).setCellValue(text);
            	row.createCell(4).setCellValue(linkMap.get(text));
            	row.createCell(5).setCellValue(url1);
            	System.out.println("Difference in Link with same text "+text +"   "+linkMap.get(text)+"     "+url1);
//                Assert.assertEquals(linkMap.get(text), url, "Mismatch found for link text: " + text);
            } else {
                // Otherwise, add the text and URL to the map
                linkMap.put(text, url1);
            }
        }
        }
        
      
        
        
        // Ensure that different texts have different URLs
        List<Map.Entry<String, String>> entries = new ArrayList<>(linkMap.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                Map.Entry<String, String> entry1 = entries.get(i);
                Map.Entry<String, String> entry2 = entries.get(j);
                if (entry1.getValue().equals(entry2.getValue())) {
                    XSSFRow row = sheet.createRow(rowNum++);
                 
                    row.createCell(2).setCellValue("Different Text for Same Link");
                    row.createCell(3).setCellValue(entry1.getValue());
                    row.createCell(4).setCellValue(entry2.getValue());
                    row.createCell(5).setCellValue(entry1.getKey());
                    row.createCell(6).setCellValue(entry2.getKey());
                    System.out.println("Different texts found with the same URL: " + entry1.getKey() + " and " + entry2.getKey() + " " + entry1.getValue());
                }
            }
        }
//        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//	        workbook.write(fileOut);
//	    }
    }
    
    
    public void landMark(WebDriver driver,String url) {
        // Get all unique role attribute values
//        Set<String> uniqueRoles = getAllUniqueRoleAttributes(driver);

   
        // Check for HTML5 semantic elements
        checkSemanticElements(driver,url);
    
    }
    
    // Method to check if elements with specific tags are present
    public  void checkSemanticElements(WebDriver driver,String url) {
    	rowNum++;
    	// Initialize a row index counter
		XSSFRow headingRow1 = sheet.createRow(rowNum++);
		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(1);
		XSSFCell headingCell1 = headingRow1.createCell(1);
		// Set the heading text
		headingCell.setCellValue("Landmark");
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
		boolean allTagsPresent=true;
        String[] tags = { "header","nav", "main", "footer", "form", "input"};
        for (String tag : tags) {
            List<WebElement> elements = driver.findElements(By.tagName(tag));
            if (elements.size() > 0) {
                System.out.println("Elements with tag '" + tag + "' are present.");
            } else {
                System.out.println("No elements with tag '" + tag + "' found.");
                XSSFRow row = sheet.createRow(rowNum++);
				// Create a new cell in the row and set the button text as its value
                row.createCell(2).setCellValue("Missing HTML Semantics");
				row.createCell(3).setCellValue(tag);
				allTagsPresent=false;
            }
        }
        if(allTagsPresent) {
        	 XSSFRow row = sheet.createRow(rowNum++);
        	row.createCell(2).setCellValue("All HTML Semantics are present");
        }
    }
    
    
    public void excelSetup(String url,String ruleName) {
    	
    }
}