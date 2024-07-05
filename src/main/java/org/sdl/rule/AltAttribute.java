package org.sdl.rule;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.task.SeleniumHandler;
import org.sdl.util.AppUtil;

public class AltAttribute {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException {
		
		List<WebElement> images = driver.findElements(By.tagName("img"));
		boolean allImagesHaveAlt = true;
		
		for (WebElement image : images) {
			String altText = image.getAttribute("alt");
			if (altText == null || altText.isEmpty()) {
				allImagesHaveAlt = false;
				
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


	}
	
}