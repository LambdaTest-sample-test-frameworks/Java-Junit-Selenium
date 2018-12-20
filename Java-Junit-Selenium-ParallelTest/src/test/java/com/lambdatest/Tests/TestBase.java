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
public class TestBase  {

	public String platform;
	public String browserName;
	public String browserVersion;
	public  static WebDriver driver;
	
	public static String username = System.getenv("LT_USERNAME");
	public static String accesskey = System.getenv("LT_APPKEY");
        public static String gridURL = System.getenv("LT_GRID_URL");

	@Parameterized.Parameters
	public static LinkedList<String[]> getEnvironments() throws Exception {
		LinkedList<String[]> env = new LinkedList<String[]>();
		env.add(new String[]{"WIN10", "chrome", "64.0"});
		env.add(new String[]{"WIN10","firefox","60.0"});
		env.add(new String[]{"WIN7","internet explorer","10.0"});

		//add more browsers here

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
		capability.setCapability("build", "Junit Parallel Tests");
		capability.setCapability("network", true);
		capability.setCapability("video", true);
		capability.setCapability("console", true);
		capability.setCapability("visual", true);
        
	if (username==null)
		// Set you LAMBDATEST Username here if not Provided from Jenkins
        	username = "nikhily";
        
        if (accesskey==null) 
        	// Set you LAMBDATEST AppKey here if not Provided from Jenkins
        	accesskey = "3Z6PQHL1sUEW2nuvvPZxg7zom6y8ZqAvfPJBX5Ne2rWN3uwfGi";
        
        if (gridURL==null)
        	gridURL = "https://" + username + ":" + accesskey + "@stage-hub.lambdatest.com/wd/hub";
        
        
		driver = new RemoteWebDriver(
			new URL(gridURL),capability);
	}

    @After
    public void tearDown() throws Exception {
        if (driver!=null)
    	driver.quit();
    }
    
    public WebDriver getWebDriver() {
        return driver;
    }
}
