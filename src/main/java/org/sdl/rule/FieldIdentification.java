package org.sdl.rule;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class FieldIdentification {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		AppUtil.log(this.getClass(), "Field Identification rule invoked");

		try {
			List<WebElement> submitButtons = driver.findElements(By.xpath("//button[@type='submit']"));
			int i=0;
			int submitbtns=submitButtons.size();
			if (submitButtons.size() >= 0) {
				for (WebElement submitButton : submitButtons) {
					i++;
					if (submitButton.isDisplayed()) {
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
						submitButton.click();
						Thread.sleep(3000);
					} else if(i==submitbtns-1){
						throw new Exception("No element found for Xpath : '//button[@type='submit']'");
					}
				}
			} else {
				throw new Exception("No element found for Xpath : '//button[@type='submit']'");
			}
		} catch (Exception e) {
			try {
				List<WebElement> submitButtons = driver.findElements(By.xpath("//input[@type='submit']"));
				for (WebElement submitButton : submitButtons) {
					if (submitButton.isDisplayed()) {
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
						submitButton.click();
						Thread.sleep(3000);
					}
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
		boolean allInputsWithInstructions = true;

		for (WebElement inputField : inputFields) {
			System.out.println(inputField.getAttribute("name"));
			if (!"hidden".equals(inputField.getAttribute("type")) && inputField.isDisplayed()) {
				WebElement label = null;
				try {
					label = driver.findElement(By.xpath("//label[@for='" + inputField.getAttribute("id") + "']"));
				} catch (Exception e) {

				}

				String ariaLabel = inputField.getAttribute("aria-label");

				WebElement subtext = null;
				try {
					subtext = driver.findElement(
							By.xpath("//input[@id='" + inputField.getAttribute("id") + "']/following-sibling::span"));
				} catch (Exception e) {

				}

				if (label != null) {
					System.out.println("Label: " + label.getText());
				} else if (ariaLabel != null && !ariaLabel.isEmpty()) {
					System.out.println("Aria-label: " + ariaLabel);
				} else if (subtext != null) {
					System.out.println("Subtext: " + subtext.getText());
				} else {
					allInputsWithInstructions = false;
					AppUtil.log(this.getClass(), "No label, aria-label, or subtext found for this input field. : "
							+ "//input[@id='" + inputField.getAttribute("id") + "']");
				}

				String errorMessage = null;
				try {
					errorMessage = driver.findElement(By.xpath("//input[@id='" + inputField.getAttribute("id")
							+ "']/following-sibling::span[contains(@class, 'error')]")).getText();
				} catch (Exception e) {
					try {
						errorMessage = inputField.getAttribute("data-val-required");
						if (errorMessage == null || errorMessage.isEmpty()) {
							throw new Exception("Attribute 'data-val-required' is empty or null.");
						}
					} catch (Exception err) {
						errorMessage = inputField.getAttribute("data-val-phonenum");
					}
				}

				
				if (errorMessage != null) {
					System.out.println("Error message: " + errorMessage);

				} else {
					AppUtil.log(this.getClass(), "No error message found for this input field. : " + "//input[@id='"
							+ inputField.getAttribute("id") + "']");
				}
			
			}

		}
		List<WebElement> errors = driver.findElements(By.cssSelector(".errors"));
		if(errors.size() >0) {
			for (WebElement error : errors) {
			AppUtil.log(this.getClass(), "There is a Combined Error Message: " + error.getText());
			}
		}
		if (allInputsWithInstructions) {
			AppUtil.log(this.getClass(), "All Input Fields have Proper Instructions");
		}
		Thread.sleep(5000);

	}
}