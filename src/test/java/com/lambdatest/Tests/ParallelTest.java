package com.lambdatest.Tests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;


public class ParallelTest extends TestBase {

	public ParallelTest(String osName, String browserName, String browserVersion) {
		super(osName, browserName, browserVersion);
	}

	@Test
	public void testParallelTest() throws InvalidElementStateException {
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

	}

}