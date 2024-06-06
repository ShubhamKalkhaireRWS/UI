package org.sdl.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class CheckKeyboardFunctionality {
//	public checkKeyboardFunctionality(String filePath) throws InterruptedException, IOException {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws InterruptedException, IOException {
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
}
