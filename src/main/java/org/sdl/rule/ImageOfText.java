package org.sdl.rule;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.util.AppUtil;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageOfText {

	public void execute(WebDriver driver, List<RulesOutputHolder> outputList) throws IOException, InterruptedException {
		AppUtil.log(this.getClass(), "Image Of Text rule invoked");
		Thread.sleep(2000);
		// Find all images on the webpage
		List<WebElement> images = driver.findElements(By.tagName("img"));

		// Initialize Tesseract instance
		ITesseract tesseract = new Tesseract();
		tesseract.setDatapath("./Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng"); // Ensure the language is set
		 boolean imagesWithEmbeddedText = false;
		for (WebElement imageElement : images) {
			String src = imageElement.getAttribute("src");
			if(src==null) {
				   String srcset = imageElement.getAttribute("srcset");

			        // Split the srcset attribute by commas to get individual links
			        String[] links = srcset.split(", ");

			        // Extract the first link (removing the 'w' at the end)
			       src = links[0].split(" ")[0];
			}
			System.out.println(src);
			if(imageElement.isDisplayed()) {
			try {
				// Check if the image URL is accessible
				URL url = new URL(src);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int responseCode = connection.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					System.err.println("Failed to access image URL: " + src + " with response code: " + responseCode);
					continue;
				}

				// Download the image using Apache Commons IO
				byte[] imageBytes = IOUtils.toByteArray(url);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

				// Check if image is successfully downloaded
				if (image == null) {
					System.err.println("Failed to download image from URL: " + src);
					continue;
				}

				// Create a temporary file to store the image
				File tempFile = File.createTempFile("image", ".png");
				ImageIO.write(image, "png", tempFile);

				
				// Perform OCR on the image
				String result = tesseract.doOCR(tempFile);

				// Check if the OCR result contains any text
				if (result.trim().length() > 0) {
					imagesWithEmbeddedText=true;
					AppUtil.log(this.getClass(), "Image with src: " + src + " contains embedded text.");
					System.out.println("Image with src: " + src + " contains embedded text.");
				} else {
//                    System.out.println("Image with src: " + src + " does not contain embedded text.");
				}

				// Delete the temporary file
				tempFile.delete();
				
				if(!imagesWithEmbeddedText) {
					AppUtil.log(this.getClass(), "There are no images with Embdedded Text");
				}

			} catch (IOException e) {
				System.err.println("IOExceptions while processing image from URL: " + src);
			
			} catch (TesseractException e) {
				System.err.println("TesseractException while processing image from URL: " + src);
				
			}
			}
		}
	}
}
