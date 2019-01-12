package com.lambdatest.Tests;

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
import java.util.LinkedList;
import org.junit.runners.Parameterized;

@Ignore
@RunWith(Parallelized.class)
public class ParallelTest {

	public String platform;
	public String browserName;
	public String browserVersion;
	public static WebDriver driver;
	public static String status = "failed";

	@Parameterized.Parameters
	public static LinkedList<String[]> getEnvironments() throws Exception {
		LinkedList<String[]> env = new LinkedList<String[]>();
		env.add(new String[] { "WIN10", "chrome", "64.0" });
		env.add(new String[] { "WIN10", "firefox", "60.0" });
		env.add(new String[] { "WIN7", "internet explorer", "10.0" });

		// add more browsers here

		return env;
	}

	public ParallelTest(String platform, String browserName, String browserVersion) {

		this.platform = platform;
		this.browserName = browserName;
		this.browserVersion = browserVersion;
	}

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(CapabilityType.BROWSER_NAME, this.browserName);
		capability.setCapability(CapabilityType.VERSION, this.browserVersion);
		capability.setCapability(CapabilityType.PLATFORM, this.platform);
		capability.setCapability("build", "Junit Parallel Tests");
		capability.setCapability("network", true);
		capability.setCapability("video", true);
		capability.setCapability("console", true);
		capability.setCapability("visual", true);

		String username = Configuration.readConfig("LambdaTest_UserName");
		String accesskey = Configuration.readConfig("LambdaTest_AppKey");

		String gridURL = "https://" + username + ":" + accesskey + "@beta-hub.lambdatest.com/wd/hub";

		driver = new RemoteWebDriver(new URL(gridURL), capability);
	}

	public class JunitParallelTest extends ParallelTest {

		public JunitParallelTest(String osName, String browserName, String browserVersion) {
			super(osName, browserName, browserVersion);
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