package com.lambdatest.Tests;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.Parameterized;

@Ignore
@RunWith(Parallelized.class)
public class ParallelJenkinsTest {

	public String platform;
	public String browserName;
	public String browserVersion;
	public String resolution;
	public static WebDriver driver;
	public static String status = "failed";

	@Parameterized.Parameters
	public static Iterator<Object[]> getEnvironments() throws Exception {
		/*LinkedList<String[]> env = new LinkedList<String[]>();
		env.add(new String[] { "WIN10", "chrome", "64.0" });
		env.add(new String[] { "WIN10", "firefox", "60.0" });
		env.add(new String[] { "WIN7", "internet explorer", "10.0" });*/
		
		String jsonText = System.getenv("LT_BROWSERS");

		ArrayList<Object> lt_browser = new ArrayList<Object>();
		ArrayList<Object> lt_operating_system = new ArrayList<Object>();
		ArrayList<Object> lt_browserVersion = new ArrayList<Object>();
		ArrayList<Object> lt_resolution = new ArrayList<Object>();

		JSONArray allData = new JSONArray(jsonText);
		for (int j = 0; j < allData.length(); j++) {
			JSONObject browsersObject = allData.getJSONObject(j);

			if (!browsersObject.getString("browserName").isEmpty()) {
				lt_browser.add(browsersObject.getString("browserName"));
			}

			if (!browsersObject.getString("operatingSystem").isEmpty()) {
				lt_operating_system.add(browsersObject.getString("operatingSystem"));
			}

			if (!browsersObject.getString("browserVersion").isEmpty()) {
				lt_browserVersion.add(browsersObject.getString("browserVersion"));
			}

			if (!browsersObject.getString("resolution").isEmpty()) {
				lt_resolution.add(browsersObject.getString("resolution"));
			}
		}
		Object[][] arrMulti = new Object[lt_browser.size()][1];

		for (int l = 0; l < lt_browser.size(); l++) {

			arrMulti[l][0] = lt_browser.get(l) + "," + lt_operating_system.get(l) + "," + lt_browserVersion.get(l) + ","
					+ lt_resolution.get(l);

		}

		List<Object[]> capabilitiesData = new ArrayList<Object[]>();
		for (int i = 0; i < arrMulti.length; i++) {
			for (int j = 0; j < 1; j++) {

				capabilitiesData.add(new Object[] { arrMulti[i][j] });

			}
		}
		return capabilitiesData.iterator();
	}

	public ParallelJenkinsTest(String param) {

		String[] envDeatails = param.split(",");
		this.platform = envDeatails[1];
		this.browserVersion = envDeatails[2];
		this.browserName = envDeatails[0];
		this.resolution= envDeatails[3];
		
		//this.platform = platform;
		//this.browserName = browserName;
		//this.browserVersion = browserVersion;
	}

	
	public void setUp() throws Exception {
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(CapabilityType.BROWSER_NAME, this.browserName);
		capability.setCapability(CapabilityType.VERSION, this.browserVersion);
		capability.setCapability(CapabilityType.PLATFORM, this.platform);
		capability.setCapability("screen_resolution", this.resolution);
		capability.setCapability("build", "Junit Jenkins Parallel Tests");
		capability.setCapability("network", true);
		capability.setCapability("video", true);
		capability.setCapability("console", true);
		capability.setCapability("visual", true);

		String username = Configuration.readConfig("LambdaTest_UserName");
		String accesskey = Configuration.readConfig("LambdaTest_AppKey");

		String gridURL = "https://" + username + ":" + accesskey + "@beta-hub.lambdatest.com/wd/hub";

		driver = new RemoteWebDriver(new URL(gridURL), capability);
	}

	public class JunitParallelTest extends ParallelJenkinsTest {

		public JunitParallelTest(String param) {
			super(param);
		}

		@Test
		public void testJunitParallel() throws InvalidElementStateException {
			// Launch the app
			driver.get("https://4dvanceboy.github.io/lambdatest/lambdasampleapp.html");

			// Click on First Item
			driver.findElement(By.name("li1")).click();

			// Click on Second Item
			driver.findElement(By.name("li2")).click();

			// Add new item is list
			driver.findElement(By.id("sampletodotext")).clear();
			driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
			driver.findElement(By.id("addbutton")).click();

			// Verify Added item
			String item = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
			Assert.assertTrue(item.contains("Yey, Let's add it to list"));
			status = "passed";
		}

	}

	@After
	public void afterTest() {
		((JavascriptExecutor) driver).executeScript("lambda-status=" + status + "");
		driver.quit();
	}

}