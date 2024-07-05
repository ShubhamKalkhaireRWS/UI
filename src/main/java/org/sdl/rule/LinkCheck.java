package org.sdl.rule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

public class LinkCheck {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException {
		AppUtil.log(this.getClass(), "Link Check rule invoked");

		List<String[]> errorLinks = new ArrayList<>(); // List to store links with exceptions

		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println(links.size());

		Map<String, String> linkMap = new HashMap<>();

		for (WebElement link : links) {

			String url1 = link.getAttribute("href");

			if (url1 != null && !url1.isEmpty() && !url1.contains("mailto:")) {
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(url1).openConnection();
					connection.setRequestMethod("HEAD");
					connection.connect();
					int responseCode = connection.getResponseCode();

					if (responseCode > 400) {
						AppUtil.log(this.getClass(), "Broken link: " + url1 + " with response code: " + responseCode);
						System.out.println("Broken link: " + url1 + " with response code: " + responseCode);
					}

				} catch (IOException e) {
					System.out.println("Exception while checking link: " + url1 + "Error :" + e);
					errorLinks.add(new String[] { url1, e.toString() }); // Add link and error message to the list
				}
			}
		}

		for (String[] errorLink : errorLinks) {

			AppUtil.log(this.getClass(), "Error occured :" + errorLink[1] + " for link " + errorLink[0]);

		}
		for (WebElement link : links) {
			String text = link.getText().trim();
			String url1 = link.getAttribute("href");

			if (!(text.isEmpty())) {
				if (linkMap.containsKey(text)) {

					System.out.println(
							"Difference in Link with same text " + text + "   " + linkMap.get(text) + "     " + url1);
					AppUtil.log(this.getClass(),
							"Difference in Link with same text " + text + "   " + linkMap.get(text) + "     " + url1);

				} else {

					linkMap.put(text, url1);
				}
			}
		}

		List<Map.Entry<String, String>> entries = new ArrayList<>(linkMap.entrySet());
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				Map.Entry<String, String> entry1 = entries.get(i);
				Map.Entry<String, String> entry2 = entries.get(j);
				if (entry1.getValue().equals(entry2.getValue())) {

					AppUtil.log(this.getClass(), "Different texts found with the same URL: " + entry1.getKey() + " and "
							+ entry2.getKey() + " " + entry1.getValue());

					System.out.println("Different texts found with the same URL: " + entry1.getKey() + " and "
							+ entry2.getKey() + " " + entry1.getValue());
				}
			}
		}

	}
}