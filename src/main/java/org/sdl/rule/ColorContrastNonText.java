package org.sdl.rule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class ColorContrastNonText {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		AppUtil.log(this.getClass(), "Color Contrast Non-Text rule invoked");
		Thread.sleep(2000);
		List<WebElement> textElements = driver.findElements(By.xpath("//*"));
		boolean allElementsHaveRequiredContrast = true;
		for (WebElement element : textElements) {

			if (element.isDisplayed() && element.getText().trim().isEmpty()
					&& !element.getTagName().equalsIgnoreCase("span")) {

				// Get text color and background color
				String textColor = element.getCssValue("color");

				if ("rgba(255, 255, 255, 1)".equals(textColor)) {
					textColor = element.getCssValue("background-color");
				}

				String backgroundColor = "rgba(255, 255, 255, 1)";
				// Convert colors to RGB
				int[] textColorRGB = convertColorStringToRGB(textColor);
				int[] backgroundColorRGB = convertColorStringToRGB(backgroundColor);

				String textColorHex = convertRGBToHex(textColorRGB);
				String backgroundColorHex = convertRGBToHex(backgroundColorRGB);

				// Calculate contrast ratio

				String elementText = element.getAccessibleName();
				if (elementText.isEmpty()) {

					elementText = element.getAttribute("class");
				}

				double contrastRatio = calculateContrastRatio(textColorRGB, backgroundColorRGB);
				if (contrastRatio < 3) {
					allElementsHaveRequiredContrast = false;
//					System.out.println("Contrast Ratio: " + contrastRatio + " : 1  = Element Text : " + elementText);
					AppUtil.log(this.getClass(),
							"Contrast Ratio: " + contrastRatio + " : 1  = Element Text : " + elementText);
					AppUtil.log(this.getClass(),
							"Text Color : " + textColorHex + " and Background color : " + backgroundColorHex);

				}

			}

		}
		if (allElementsHaveRequiredContrast) {
			AppUtil.log(this.getClass(),"All Non-Text Elements have required contrast");
		}
	}

	public static int[] convertColorStringToRGB(String colorString) {
		if (colorString.startsWith("rgba")) {
			colorString = colorString.substring(5, colorString.length() - 1);
		} else if (colorString.startsWith("rgb")) {
			colorString = colorString.substring(4, colorString.length() - 1);
		}
		String[] parts = colorString.split(",");
		return new int[] { Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()),
				Integer.parseInt(parts[2].trim()) };
	}

	public static String convertRGBToHex(int[] rgb) {
		return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
	}

	public static double calculateLuminance(int[] rgb) {
		double[] srgb = new double[3];
		for (int i = 0; i < 3; i++) {
			double value = rgb[i] / 255.0;
			srgb[i] = (value <= 0.03928) ? value / 12.92 : Math.pow((value + 0.055) / 1.055, 2.4);
		}
		return 0.2126 * srgb[0] + 0.7152 * srgb[1] + 0.0722 * srgb[2];
	}

	public static double calculateContrastRatio(int[] rgb1, int[] rgb2) {
		double luminance1 = calculateLuminance(rgb1) + 0.05;
		double luminance2 = calculateLuminance(rgb2) + 0.05;
		return (luminance1 > luminance2) ? luminance1 / luminance2 : luminance2 / luminance1;
	}
}