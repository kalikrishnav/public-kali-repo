/**
 * This is sample Class for text. More info to be updated.
 */
package com.kali.selenium.pages;

import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kali.selenium.config.TestSetup;
import com.kali.selenium.utils.Utils;

/**
 * Common behaviors applicable for all pages of the application.
 * 
 * @author Kalikrishna Veeranki
 *
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this(driver, TestSetup.DEFAULT_TIMEOUT);
    }
    public BasePage(WebDriver driver, int secondsToWait) {
        this.wait = TestSetup.getDriverWait(driver, secondsToWait);
        this.driver = driver;
    }

    /**
     * Create a new page object.
     * 
     * @param landingPage
     * @param secondsToWait
     * @return new page object
     */
    protected <T extends BasePage> T newPage(Class<T> landingPage, int secondsToWait) {
        try {
            return landingPage.getDeclaredConstructor(WebDriver.class, int.class)
                                .newInstance(driver, secondsToWait);
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Navigate to the new page.
     *
     * @param landingPage
     * @return new page object
     */
    public <T extends BasePage> T expectPage(Class<T> landingPage) {
        Utils.waitForPageToLoad(driver);
        return newPage(landingPage, TestSetup.DEFAULT_TIMEOUT);
    }

    /**
     * Navigate back to the previous page.
     *
     * @param landingPage
     * @return previous page object
     */
    public <T extends BasePage> T navigateBack(Class<T> landingPage) {
        driver.navigate().back();
        if(landingPage==null)
            return null;

        return expectPage(landingPage);
    }

    /**
     * Retrieve the URL for the current page.
     *
     * @return the URL for the page
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Retrieve the header text for the current page.
     *
     * @return  header text title for the page
     */
    public String getPageTitle() {
        return Utils.findElement(driver, By.tagName("title")).getText();
    }

}
