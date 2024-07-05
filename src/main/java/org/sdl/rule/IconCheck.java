package org.sdl.rule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class IconCheck {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException {
		AppUtil.log(this.getClass(), "Icon Check rule invoked");

		List<WebElement> socialMediaLinks = driver
				.findElements(By.cssSelector(".l-footer__social .l-icon-nav a:has(img)"));
		boolean allIconWithWorkingLink = true;
		List<String[]> errorLinks = new ArrayList<>();
		for (WebElement link : socialMediaLinks) {
			String url = link.getAttribute("href");
			if (url != null && !url.isEmpty() && !url.contains("mailto:")) {

				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.setRequestMethod("HEAD");
					connection.connect();
					int responseCode = connection.getResponseCode();
					if (responseCode > 400) {
						allIconWithWorkingLink = false;
						AppUtil.log(this.getClass(), "Broken link: " + url + " with response code: " + responseCode);
						System.out.println("Broken link: " + url + " with response code: " + responseCode);
					}
				} catch (IOException e) {
					
					System.out.println("Exception while checking link: " + url + "Error :" + e);
					errorLinks.add(new String[] { url, e.toString() }); // Add link and error message to the list
				}
			}
		}
		
		if(allIconWithWorkingLink) {
			AppUtil.log(this.getClass(), "All Icons have Working Link");
		}
		for (String[] errorLink : errorLinks) {

			AppUtil.log(this.getClass(), "Error occured :" + errorLink[1] + " for link " + errorLink[0]);

		}

	}
}