package org.sdl.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class HeadingsLables {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) {

		List<String> headingList = new ArrayList<>();
		List<String> labelList = new ArrayList<>();

		WebElement closeButton = driver
				.findElement(By.xpath("(//button[@class='close c-modal--promotion__close btn btn-dark'])[1]"));
		closeButton.click();
		List<WebElement> headings = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //h4 | //h5 | //h6"));
//		System.out.println("Headings on the page:");
		if (headings.size() > 0)
			AppUtil.log(this.getClass(), "Headings on the page:");
		else
			AppUtil.log(this.getClass(), "No headings found");

		for (WebElement heading : headings) {
			if (!StringUtils.isEmpty(heading.getText())) {
				if (headingList.contains(heading.getText())) {
					System.out.println("Duplicate heading :" + heading.getText());
					AppUtil.log(this.getClass(), "Duplicate heading :");

					// Changes
					RulesOutputHolder outputHolder = new RulesOutputHolder();

				} else {
					headingList.add(heading.getText());
					AppUtil.log(this.getClass(), "Heading :" + heading.getText());

				}
			}
		}

		List<WebElement> labels = driver.findElements(By.tagName("label"));
		if (labels.size() > 0)
			AppUtil.log(this.getClass(), "Labels on the page:");
		else
			AppUtil.log(this.getClass(), "No labels found");

		for (WebElement label : labels) {
			if (!StringUtils.isEmpty(label.getText())) {
				if (labelList.contains(label.getText())) {
					System.out.println("Duplicate label :" + label.getText());
					AppUtil.log(this.getClass(), "Duplicate label :");

				} else {
					labelList.add(label.getText());
					AppUtil.log(this.getClass(), "Label :" + label.getText());

				}
			}
		}
		driver.quit();
	}
}
