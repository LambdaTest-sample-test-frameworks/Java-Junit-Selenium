package com.lambdatest.Tests;

import org.junit.Test;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;

import com.lambdatest.Pages.*;


public class AddTodoListTest extends TestBase {

    public AddTodoListTest(String os,
                          String browser, String version) {
            super(os, browser, version);
    }


    @Test
    public void testAddTodoListTest() throws InvalidElementStateException {

    	 WebDriver driver = this.getWebDriver();
    	 //Visiting Application  page
        ToDo page = ToDo.visitPage(driver);

        //Click on First Item
        page.clickOnFirstItem();
        
        //Click on Second Item
        page.clickOnSecondItem();
        
        //Add new item is list
        page.addNewItem("Yey, Let's add it to list");
        
        //Verify Added item
        page.verifyAddeItem();
    }
}