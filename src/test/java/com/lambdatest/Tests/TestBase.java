package com.lambdatest.Tests;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.util.LinkedList;

import org.junit.runners.Parameterized;

@Ignore
@RunWith(Parallelized.class)
public class TestBase {

	public String platform;
	public String browserName;
	public String browserVersion;
	public static WebDriver driver;

	@Parameterized.Parameters
	public static LinkedList<String[]> getEnvironments() throws Exception {
		LinkedList<String[]> env = new LinkedList<String[]>();
		env.add(new String[] { "WIN10", "chrome", "64.0" });
		env.add(new String[] { "WIN10", "firefox", "60.0" });
		env.add(new String[] { "WIN7", "internet explorer", "10.0" });
		env.add(new String[] { "WIN7", "chrome", "67.0" });
		env.add(new String[] { "WIN8", "firefox", "58.0" });

		// add more browsers here

		return env;
	}

	public TestBase(String platform, String browserName, String browserVersion) {

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
		capability.setCapability("build", System.getenv("LT_BUILD"));
		capability.setCapability("name", "Single Junit Test");
		capability.setCapability("network", true);
		capability.setCapability("video", true);
		capability.setCapability("console", true);
		capability.setCapability("visual", true);

		String username = Configuration.readConfig("LambdaTest_UserName");
		String accesskey = Configuration.readConfig("LambdaTest_AppKey");
		
		String gridURL = "https://" + username + ":" + accesskey + "@beta-hub.lambdatest.com/wd/hub";

		driver = new RemoteWebDriver(new URL(gridURL), capability);
	}

	@After
	public void tearDown() throws Exception {
		if (driver != null)
			driver.quit();
	}

	public WebDriver getWebDriver() {
		return driver;
	}
}
