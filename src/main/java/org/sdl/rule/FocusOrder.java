package org.sdl.rule;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.HasFullPageScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

public class FocusOrder {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		AppUtil.log(this.getClass(), "Focus Order rule invoked");

		// List to store the focus order of elements
		List<WebElement> focusOrder = new ArrayList<>();

		// Create Actions object to perform keyboard actions
		Actions actions = new Actions(driver);
		List<WebElement> elementsWithTabIndex = driver.findElements(By.xpath("//*[not(self::span)]"));

		for (WebElement element : elementsWithTabIndex) {

			actions.sendKeys("\uE004").perform();
			// System.out.println("****************** "+ element.getText());
			WebElement activeElement = driver.switchTo().activeElement();
			if (!"site-nav__dropdown-link site-nav__dropdown-link--second-level"
					.equalsIgnoreCase(activeElement.getAttribute("class"))) {
				if (!focusOrder.contains(activeElement)
						|| "red heart layout-default-page pageID-16DFB6832EBD408DBC78E29D7BF316E2 has-sticky"
								.equalsIgnoreCase(activeElement.getAttribute("class"))) {
					if ("red heart layout-default-page pageID-16DFB6832EBD408DBC78E29D7BF316E2 has-sticky"
							.equalsIgnoreCase(activeElement.getAttribute("class"))) {
						actions.sendKeys("\uE004").perform();
						activeElement = driver.switchTo().activeElement();

					}

					focusOrder.add(activeElement);
				} else {
					break;
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		Thread.sleep(1000);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		int skipIndex = -1;
		for (int i = 0; i < focusOrder.size() - 1; i++) {
			WebElement element = focusOrder.get(i);
			String accessibleName = element.getAccessibleName();
			if (accessibleName.equalsIgnoreCase("Skip to content")
					|| accessibleName.equalsIgnoreCase("Skip to main content")) {
				skipIndex = i;
				break;
			}
		}

		if (skipIndex != -1) {
			// Remove all elements before skipIndex
			focusOrder.subList(0, skipIndex).clear();
		}

		// Loop to create markers and draw lines
		WebElement prevElement = null;
		boolean markerPlaced=false;
		
		System.out.println("*******"+focusOrder.get(focusOrder.size()-1).getAttribute("class"));
		int j = focusOrder.size();
		if(focusOrder.get(focusOrder.size()-1).getTagName().equalsIgnoreCase("body")) {
			j=j-1;
		}
		for (int i = 0; i < j ; i++) {
			Thread.sleep(100);
			WebElement element = focusOrder.get(i);

			int order = i + 1;

			String script = "var elem = arguments[0];" + "var prevElem = arguments[1];"
					+ "var order = arguments[2] || '1';" + "var isInViewport = function(elem) {"
					+ "    var bounding = elem.getBoundingClientRect();" + "    return ("
					+ "        bounding.top >= 0 &&" + "        bounding.left >= 0 &&"
					+ "        bounding.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&"
					+ "        bounding.right <= (window.innerWidth || document.documentElement.clientWidth)" + "    );"
					+ "};" + "var updateMarkerPosition = function(marker, targetElem) {"
					+ "    var rect = targetElem.getBoundingClientRect();"
					+ "    marker.style.top = (rect.top + window.scrollY + rect.height / 2 - 10) + 'px';"
					+ "    marker.style.left = (rect.left + window.scrollX + rect.width / 2 - 10) + 'px';" + "};"
					+ "var createMarker = function(order, targetElem) {"
					+ "    var div = document.createElement('div');" + "    div.id = 'markers';"
					+ "    div.style.position = 'absolute';" + "    div.style.backgroundColor = '#0075ff';"
					+ "    div.style.color = 'white';" + "    div.style.border = '1px solid black';"
					+ "    div.style.borderRadius = '50%';" + "    div.style.width = '25px';"
					+ "    div.style.height = '25px';" + "    div.style.display = 'flex';"
					+ "    div.style.alignItems = 'center';" + "    div.style.justifyContent = 'center';"
					+ "    div.style.zIndex = '200';" + "    div.style.fontSize = '12px';" + "    div.textContent = '"
					+ order + "';" + "    document.body.appendChild(div);"
					+ "    updateMarkerPosition(div, targetElem);" + "    return div;" + "};"
					+ "var marker = createMarker(order, elem);" + "var lineCanvas = document.createElement('canvas');"
					+ "lineCanvas.id = 'focusOrderCanvas';" + "lineCanvas.style.position = 'absolute';"
					+ "lineCanvas.style.top = '0';" + "lineCanvas.style.left = '0';"
					+ "lineCanvas.width = document.body.clientWidth;"
					+ "lineCanvas.height = document.body.clientHeight;" + "lineCanvas.style.zIndex = '100';"
					+ "document.body.appendChild(lineCanvas);" + "var ctx = lineCanvas.getContext('2d');"
					+ "var updatePositions = function() {" + "    updateMarkerPosition(marker, elem);"
					+ "    if (prevElem !== null) {"
					+ "        ctx.clearRect(0, 0, lineCanvas.width, lineCanvas.height);"
					+ "        var prevRect = prevElem.getBoundingClientRect();"
					+ "        var prevMarkerCenterX = prevRect.left + window.scrollX + prevRect.width / 2;"
					+ "        var prevMarkerCenterY = prevRect.top + window.scrollY + prevRect.height / 2;"
					+ "        var elemRect = elem.getBoundingClientRect();"
					+ "        var markerCenterX = elemRect.left + window.scrollX + elemRect.width / 2;"
					+ "        var markerCenterY = elemRect.top + window.scrollY + elemRect.height / 2;"
					+ "        ctx.beginPath();" + "        ctx.moveTo(prevMarkerCenterX, prevMarkerCenterY);"
					+ "        ctx.lineTo(markerCenterX, markerCenterY);" + "        ctx.strokeStyle = '#0075ff';"
					+ "        ctx.lineWidth = 3;" + "        ctx.stroke();" + "        ctx.closePath();" + "    }"
					+ "};" + "window.addEventListener('scroll', updatePositions);"
					+ "window.addEventListener('resize', function() {"
					+ "    lineCanvas.width = document.body.clientWidth;"
					+ "    lineCanvas.height = document.body.clientHeight;" + "    updatePositions();" + "});"+"return true";

			// Adding event listener to ensure markers update their positions dynamically
//			script += "window.addEventListener('scroll', function() {" + "    updatePositions();" + "});"+"return true";
			
			
			 markerPlaced =(boolean) js.executeScript(script, element, prevElement);

			prevElement = element;
		}


		if(markerPlaced) {
			
		if (getBrowserName(driver).equalsIgnoreCase("chrome")) {
//			Thread.sleep(1000);
			chromeSS(driver);
			
		} else if (getBrowserName(driver).equalsIgnoreCase("firefox")) {
			firefoxSS(driver);
		} else {
			System.out.println("Unable to get screenshot");
		}
		}
		try {

			Thread.sleep(1000); // Adjust the sleep time as needed

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void chromeSS(WebDriver driver) throws InterruptedException {

		Shutterbug.shootPage(driver, Capture.FULL, true).save("FocusOrder");
		Thread.sleep(2000);
		String currentDirectory = System.getProperty("user.dir");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var markersDivs = document.querySelectorAll('div[id=markers]');"
				+ "markersDivs.forEach(function(div) {" + "    div.remove();" + "});");

		js.executeScript("var markersDivs = document.querySelectorAll('canvas[id=focusOrderCanvas]');"
				+ "markersDivs.forEach(function(div) {" + "    div.remove();" + "});");

		AppUtil.log(this.getClass(), "Focus Order Result saved at : " + currentDirectory + "\\FocusOrder\\");
	}

	public void firefoxSS(WebDriver driver) throws IOException {

		HasFullPageScreenshot fps = (HasFullPageScreenshot) driver;

		File src = fps.getFullPageScreenshotAs(OutputType.FILE);

		FileUtils.copyFile(src, new File("./FocusOrder/FocusOrder.png"));

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("var markersDivs = document.querySelectorAll('div[id=markers]');"
				+ "markersDivs.forEach(function(div) {" + "    div.remove();" + "});");

		js.executeScript("var markersDivs = document.querySelectorAll('lineCanvas[id=focusOrderCanvas]');"
				+ "markersDivs.forEach(function(div) {" + "    div.remove();" + "});");
	}

	public String getBrowserName(WebDriver driver) {
		String browserName = "";

		// Check if driver is instance of ChromeDriver
		if (driver instanceof ChromeDriver) {
			browserName = "Chrome";

		}
		// Check if driver is instance of FirefoxDriver
		else if (driver instanceof FirefoxDriver) {
			browserName = "Firefox";

		}
		// Add more checks for other browsers if needed

		return browserName;

	}
}