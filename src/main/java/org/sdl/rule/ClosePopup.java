package org.sdl.rule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class ClosePopup {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		Thread.sleep(3000);
		List<WebElement> closeButtons = null;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Wait for up to 3 seconds
		try {
		 closeButtons = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[class*='close']")));
		}catch(TimeoutException e) {
			
		}
		if (closeButtons==null) {

			return;
			
		}else {

		// Click on each close button
		for (WebElement closeButton : closeButtons) {
			try {
				closeButton.click();
				

			} catch (Exception e) {

			}
		}
	
		}
		Thread.sleep(2000);
	}
}