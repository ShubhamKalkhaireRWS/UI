package org.sdl.rule;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class MinimumTargetSize {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException {
		AppUtil.log(this.getClass(), "Minimum Target Size rule invoked");

		By[] clickableElementsLocators = { By.tagName("a"),
				By.tagName("button"),
				By.tagName("input[type='button']"),
				By.tagName("input[type='submit']"),
				By.tagName("img"), By.tagName("select"),
				By.tagName("textarea"),
				By.cssSelector("[role='button']"),
				By.cssSelector("[role='link']"),
				By.cssSelector("[onclick]"),
				By.cssSelector("button[onclick]"),
				By.cssSelector("a[onclick]") };
		boolean allElementsHaveRequiredSize = true;

		for (By locator : clickableElementsLocators) {
			List<WebElement> elements = driver.findElements(locator);

			for (WebElement element : elements) {
				Dimension size = element.getSize();
				String elementText = element.getText();
				if (elementText.isBlank()) {
					elementText = element.getAccessibleName();
				}
				  // Skip elements inside an li tag
                if (hasLiAncestor(element)) {
                    continue;
                }
				if (!elementText.isBlank() && !"Skip to content".contains(elementText)
						&& !"Skip to main content".contains(elementText)) {

					if (size.getWidth() < 24 || size.getHeight() < 24) {
						AppUtil.log(this.getClass(), "\"" + elementText + "\" has width " + size.getWidth()
						+ " and Height " + size.getHeight());

						allElementsHaveRequiredSize = false;
					}
				}
			}
		}
		if (allElementsHaveRequiredSize) {
			AppUtil.log(this.getClass(), "All visible elements meet the 24x24 pixel size requirement.");
		}

	}
	   private static boolean hasLiAncestor(WebElement element) {
	        try {
	            element.findElement(By.xpath("ancestor-or-self::li"));
	            return true;
	        } catch (NoSuchElementException e) {
	            return false;
	        }
	    }	
}
