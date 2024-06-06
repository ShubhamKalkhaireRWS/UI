package org.sdl.rule;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.task.SeleniumHandler;
import org.sdl.util.AppUtil;

public class AriaLable {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) {

		List<WebElement> buttons = driver.findElements(By.tagName("button"));
		buttons.addAll(driver.findElements(By.tagName("a")));

		AppUtil.log(this.getClass(), "Aria Label rule invoked");

		System.out.println("Elements found : " + buttons.size());
		AppUtil.log(this.getClass(), "Elements found : " + buttons.size());

		System.out.println("Elements without aria-label:");
		AppUtil.log(this.getClass(), "Elements without aria-label:");

		for (WebElement button : buttons) {
			String tagName = button.getTagName();
			String ariaLabel = button.getAttribute("aria-label");
			if ((ariaLabel == null || ariaLabel.isEmpty()) && !StringUtils.isEmpty(button.getText())) {
				System.out.println("Button has no aria label : " + button.getText());
				AppUtil.log(this.getClass(), "Button has no aria label : " + button.getText());

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
}
