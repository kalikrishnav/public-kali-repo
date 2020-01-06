package com.kali.selenium.pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.kali.selenium.config.TestSetup;
import com.kali.selenium.utils.Locator;
import com.kali.selenium.utils.Utils;

/**
 * 
 * @author Kalikrishna Veeranki
 *
 */
public class MsnTodaysWeatherPage extends BasePage {

    private Locator locator;

    /**
     * All XPaths/selectors for web elements on this page should be defined in
     * the object repository.
     * 
     * @param driver
     *            An existing WebDriver from test runner class.
     */
    public MsnTodaysWeatherPage(WebDriver driver) {
        this(driver, TestSetup.DEFAULT_TIMEOUT);
    }
    public MsnTodaysWeatherPage(WebDriver driver, int secondsToWait) {
        super(driver, secondsToWait);
        locator = new Locator("src/main/resources/objectRepository/MsnPage.OR", driver, wait);
        Utils.waitUntilNoStaleElement(driver, wait, locator.getByObject("MsnPage.Link.Signin.Xpath"));
    }

    public Locator getLocator() {
        return locator;
    }

    /**
     * 
     */
    public void clickFacebookLikeInFooter() {
        WebElement element = locator.get("MsnPage.Footer.FacebookLike.Xpath");
        String curentWindowHandle = driver.getWindowHandle();
        element.click();
        // Check new window and then close
        closeNewWidows(curentWindowHandle);
    }

    public void closeNewWidows(String curentWindowHandle) {
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String handle: allWindowHandles) {
            if (!handle.equals(curentWindowHandle)) {
                driver.switchTo().window(handle);
                driver.close();
            }
            driver.switchTo().window(curentWindowHandle);
        }
        
    }

    /**
     * 
     */
    public void clickTwtterFollowInFooter() {
        WebElement element = locator.get("MsnPage.Footer.TwitterFollow.Xpath");
        String curentWindowHandle = driver.getWindowHandle();
        element.click();
        // Check new window and then close
        closeNewWidows(curentWindowHandle);
        
    }

    /**
     * @return
     */
    public int getCurrentTemperature() {
        return Integer.parseInt(locator.get("MsnPage.CurrentLocation.Temperature.Xpath").getText());
    }

    /**
     * @param string
     */
    public MsnTodaysWeatherPage selectALocation(String newLocatoin) {
        locator.get("MsnPage.Field.Location.Xpath").click();
        locator.get("MsnPage.Field.Location.Field.Search.Xpath").sendKeys(newLocatoin);

        WebElement newSuggestedLocation = locator.get("MsnPage.Field.Location.Field.Search.AutoSuggestedResults.Xpath");
        Assert.assertTrue(newSuggestedLocation.getText().contains(newLocatoin));

        new Actions(driver)
           .click(driver.findElement(By.xpath(".//li[contains(text(),'"+newLocatoin+"')]")))
           //.click(locator.get("MsnPage.Field.Location.Button.Search.Xpath"))
           .build().perform();

        return new MsnTodaysWeatherPage(driver);
    }


}
