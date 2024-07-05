package org.sdl.task;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sdl.bean.RulesOutputHolder;
import org.sdl.rule.AltAttribute;
import org.sdl.rule.AriaLable;
import org.sdl.rule.FieldIdentification;
import org.sdl.rule.FocusOrder;
import org.sdl.rule.IconCheck;
import org.sdl.rule.ImageOfText;
import org.sdl.rule.LandMark;
import org.sdl.rule.LinkCheck;
import org.sdl.rule.MinimumTargetSize;
import org.sdl.rule.ClosePopup;
import org.sdl.rule.ColorContrastNonText;
import org.sdl.rule.ColorContrastText;
import org.sdl.ui.MainFrame;
import org.sdl.util.AppUtil;

import com.cybozu.labs.langdetect.LangDetectException;

public class SeleniumHandler {
	private WebDriver driver;
	String multiUrlInput;
	MainFrame mainFrame;
	List<RulesOutputHolder> outputList = new ArrayList<>();
	public static String WORKING_URL = "";

	public SeleniumHandler(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void setUrl(String url) {
		this.multiUrlInput = url;
	}

	public void executeInBackground() {
		try {

			AppUtil.log(this.getClass(), "---------------------------");

//			loadChromeDriver();

			AppUtil.log(this.getClass(), "---------------------------");
			AppUtil.log(this.getClass(), "Multi URLs received : " + multiUrlInput);

			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.schedule(() -> {
				try {

					executeRule();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SwingUtilities.invokeLater(() -> {
					mainFrame.enableExecuteButton();
				});

				executor.shutdown();
			}, 0, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
			AppUtil.log(this.getClass(), "Exception in processs : " + e.getMessage());
		}
	}

	private void loadChromeDriver() {
		// TODO Auto-generated method stub

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("chromedriver.exe");
		File f = new File("Driver");
		if (!f.exists()) {
			f.mkdirs();
		}
		File chromeDriver = new File("Driver" + File.separator + "chromedriver.exe");
		if (!chromeDriver.exists()) {
			try {
				AppUtil.log(this.getClass(), "creating chromedriver");
				chromeDriver.createNewFile();
				org.apache.commons.io.FileUtils.copyURLToFile(resource, chromeDriver);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
	}

	public void executeRule() throws IOException, LangDetectException, InterruptedException, InvalidFormatException {
		// https://robotjs.io/,
		// https://www.mcafee.com/consumer/en-us/landing-page/atp/mls-family/livesafe.html?affid=885&cid=246814&pir=1,https://www.google.com

		if (StringUtils.isEmpty(multiUrlInput)) {
			AppUtil.log(this.getClass(), "URL is not correct, pls validate " + multiUrlInput);
		} else {
			String[] urlArray = multiUrlInput.split(",");

			for (String url : urlArray) {

				AppUtil.log(this.getClass(), "----------");
				WORKING_URL = url;
				AppUtil.log(this.getClass(), "Accessibility Rules execution starting on URL : " + WORKING_URL);
				invokeRules(WORKING_URL.trim());

				AppUtil.log(this.getClass(), "Accessibility Rules execution finished");
			}

			AppUtil.log(this.getClass(), "----------");

			// Log output holder data
//			AppUtil.log(this.getClass(), "OutputHolder");
//			for (RulesOutputHolder o : outputList) {
//				AppUtil.log(this.getClass(), o.toString());
//
//			}

		}
		AppUtil.log(this.getClass(), "---------------------------");
	}

	private void invokeRules(String WORKING_URL)
			throws IOException, LangDetectException, InterruptedException, InvalidFormatException {
		AppUtil.log(this.getClass(), WORKING_URL);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("document.body.style.transform = '90%';");
		try {
			// driver.get("https://cpr.heart.org/en/");

			driver.get(WORKING_URL);
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log or process page content as needed
		
		ClosePopup closePopup = new ClosePopup();
		closePopup.execute(driver, outputList);

//		AriaLable ariaLable = new AriaLable();
//		ariaLable.execute(driver, outputList);
//
//		AltAttribute altAttribute = new AltAttribute();
//		altAttribute.execute(driver, outputList);
//
////		LinkCheck linkCheck = new LinkCheck();
////		linkCheck.execute(driver, outputList);
//
//		LandMark landMark = new LandMark();
//		landMark.execute(driver, outputList);
//
//		IconCheck iconCheck = new IconCheck();
//		iconCheck.execute(driver, outputList);
//		
//		FieldIdentification fieldIdentification = new FieldIdentification();
//		fieldIdentification.execute(driver, outputList);
//
//		
//		ColorContrastText colorContrastText = new ColorContrastText();
//		colorContrastText.execute(driver, outputList);
//		
//		ColorContrastNonText colorContrastNonText =new ColorContrastNonText();
//		colorContrastNonText.execute(driver, outputList);
//		
		FocusOrder focusOrder=new FocusOrder();
		focusOrder.execute(driver, outputList);
//		
//		MinimumTargetSize minimumTargetSize = new MinimumTargetSize();
//		minimumTargetSize.execute(driver, outputList);
		
//		ImageOfText imageOfText = new ImageOfText();
//		imageOfText.execute(driver, outputList);
		
	

//
//		HeadingsLables headingsLables = new HeadingsLables();
//		headingsLables.execute(driver, outputList);

//		testLanguage TestL = new testLanguage();
//		TestL.execute(driver, outputList, WORKING_URL);

//		CheckKeyboardFunctionality checkKeyboard = new CheckKeyboardFunctionality();
//		try {
//			checkKeyboard.execute(driver, outputList);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//		}

		driver.quit();
	}

}
