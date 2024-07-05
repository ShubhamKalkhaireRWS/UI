package org.sdl.rule;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.HasFullPageScreenshot;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

public class LandMark {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		AppUtil.log(this.getClass(), "Landmark rule invoked");

		JavascriptExecutor js = (JavascriptExecutor) driver;

		List<WebElement> landmarks = driver.findElements(By.cssSelector(
				"[role=navigation], [role=banner], [role=main], [role=title], [role=contentinfo],header, nav, main, footer"));
		int j=0;
		List<WebElement> toolBar=driver.findElements(By.xpath("//div[@class='toolbar__content']"));
		if(toolBar.size()>0) {
			j=1;
		}
		
		
		for (int i = landmarks.size() - 1; i >= j; i--) {
			WebElement landmark = landmarks.get(i);

			int width = landmark.getSize().getWidth();
			int height = landmark.getSize().getHeight();
			int x = landmark.getLocation().getX();
			int y = landmark.getLocation().getY();

			String boxId = "box-" + System.identityHashCode(landmark);

			js.executeScript("arguments[0].setAttribute('data-box-id', arguments[1]);", landmark, boxId);

			// Draw a transparent box with only visible border using JavaScript
			js.executeScript("var div = document.createElement('div'); "
					+ "div.id = 'deletable" + boxId + "'; "
					+ "div.style.position = 'absolute'; "
					+ "div.style.border = '3px dotted #0075ff'; "
					+ "div.style.backgroundColor = 'transparent'; "
					+ "div.style.color = 'black'; "
					+ "div.style.opacity = '1'; " + 
					"div.style.top = '" + y + "px'; " + "div.style.left = '" + x + "px'; " + "div.style.width = '"
					+ width + "px'; " + "div.style.height = '" + height + "px'; " + "div.style.zIndex = '200';"
					+ "var textDiv = document.createElement('div'); "
					+ "textDiv.id = 'deletable'; "
					+ "textDiv.style.position = 'absolute'; "
					+ "textDiv.style.backgroundColor = '#0075ff'; " + "textDiv.style.color = 'white'; "
					+ "    textDiv.style.width = '100px';" + "textDiv.textContent = '"
					+ (landmark.getAttribute("role") != null ? landmark.getAttribute("role")
							: (landmark.getTagName().equalsIgnoreCase("nav") ? "navigation"
									: (landmark.getTagName().equalsIgnoreCase("header") ? "banner"
											: (landmark.getTagName().equalsIgnoreCase("footer") ? "contentinfo"
													: landmark.getTagName()))))
					+ "'; " + "div.appendChild(textDiv); " + "document.body.appendChild(div);");

		}

		js.executeScript("window.addEventListener('scroll', function() {"
				+ "var landmarks = document.querySelectorAll('[role=navigation], [role=banner], [role=main], [role=title], [role=contentinfo], header, nav, main, footer');"
				+ "landmarks.forEach(function(landmark) {" + "    var rect = landmark.getBoundingClientRect();"
				+ "    var box = document.getElementById(landmark.getAttribute('data-box-id'));" + "    if (box) {"
				+ "        box.style.top = (window.scrollY + rect.top) + 'px';"
				+ "        box.style.left = (window.scrollX + rect.left) + 'px';"
				+ "        box.style.width = rect.width + 'px';" + "        box.style.height = rect.height + 'px';"
				+ "    }" + "});" + "});");

		
		if (getBrowserName(driver).equalsIgnoreCase("chrome")) {
			Thread.sleep(2000);
			chromeSS(driver);
		} else if (getBrowserName(driver).equalsIgnoreCase("firefox")) {
			firefoxSS(driver);
		} else {
			System.out.println("Unable to get screenshot");
		}

	}

	public  void chromeSS(WebDriver driver) throws InterruptedException {
		Shutterbug.shootPage(driver, Capture.FULL, true).save("Landmarks");
		String currentDirectory = System.getProperty("user.dir");
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
			    "var deletableDivs = document.querySelectorAll('[id*=deletable]');" +
			    "deletableDivs.forEach(function(div) {" +
			    "    div.remove();" +
			    "});"
			);
		AppUtil.log(this.getClass(), "Landmark Result saved at : " + currentDirectory + "\\Landmarks\\");
	}

	public  void firefoxSS(WebDriver driver)
			throws IOException, InterruptedException {
		String currentDirectory = System.getProperty("user.dir");
		HasFullPageScreenshot fps = (HasFullPageScreenshot) driver;

		File src = fps.getFullPageScreenshotAs(OutputType.FILE);

		FileUtils.copyFile(src, new File("./Landmarks/Landmarks.png"));
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript(
			    "var deletableDivs = document.querySelectorAll('[id*=deletable]');" +
			    "deletableDivs.forEach(function(div) {" +
			    "    div.remove();" +
			    "});"
			);
		AppUtil.log(this.getClass(), "Landmark Result saved at : " + currentDirectory + "\\Landmarks\\");
	}

	public static String getBrowserName(WebDriver driver) {
		String browserName = "";

		if (driver instanceof ChromeDriver) {
			browserName = "Chrome";

		}

		else if (driver instanceof FirefoxDriver) {
			browserName = "Firefox";

		}
	

		return browserName;
	}
}