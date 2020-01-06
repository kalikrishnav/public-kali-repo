/**
 * This is sample Class for text. More info to be updated.
 */
package com.kali.selenium.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kali.selenium.config.TestSetup;

/**
 * A collection of utility methods for Selenium tests.
 *
 * @author Kalikrishna Veeranki 
 */
public class Utils {

    private Utils() {
        // prevent external instances
    }

    /**
     * Gets Default wait object.
     * 
     * @param driver
     * @return wait
     */
    public static WebDriverWait defaultWait(WebDriver driver) {
        return TestSetup.getDriverWait(driver, TestSetup.DEFAULT_TIMEOUT);
    }

    /**
     * Gets WebElements for By locator.
     * 
     * @param driver
     * @param locator
     * @return matching WebELement
     */
    public static WebElement findElement(WebDriver driver, By locator) {
        defaultWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElement(locator);
    }

    /**
     * Gets list of all matching WebElements for By locator.
     * 
     * @param driver
     * @param locator
     * @return matching WebELements List
     */
    public static List<WebElement> findElements(WebDriver driver, By locator) {
        defaultWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator);
    }

    /**
     * @param driver
     * @param element
     * @return element
     */
    public static WebElement waitUntilElementReady(WebDriver driver, WebElement element) {
        WebDriverWait wait = defaultWait(driver);
        scrollIntoView(driver, element);

        if (!element.isEnabled()) {
            // if it's disabled, only wait until element is visible
            wait.until(ExpectedConditions
                    .refreshed(ExpectedConditions.visibilityOf(element)));
        } else {
            // otherwise, wait until element is clickable
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.elementToBeClickable(element)));
        }

        return element;
    }

    /**
     * Helper method to scroll an element into view.
     *
     * @param driver  an existing test driver
     * @param element the element to scroll into view
     */
    public static void scrollIntoView(WebDriver driver, WebElement element) {
        try {
            defaultWait(driver)
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
        } catch (ElementNotVisibleException enve) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';", element);
        }
        new Actions(driver).moveToElement(element).build().perform();
        //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * @param driver
     * @param locator
     * @param text
     */
    public static void waitForElementLocatedToHaveText(WebDriver driver, By locator, String text) {
        defaultWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Note: does not wait for data returned by ajax actions.
     *
     * @param driver
     * @param timeout (in seconds)
     * @throws TimeoutException
     */
    public static void waitForPageToLoad(final WebDriver driver)  {
        defaultWait(driver).until((WebDriver input) -> "complete"
                  .equals(((JavascriptExecutor) input).executeScript("return document.readyState")));
    }

    /**
     * Selects an option from a classic dropdown element (e.g., 'select').
     *
     * @param driver
     * @param dropdown
     * @param option
     */
    public static void clickDropdownOption(WebDriver driver, WebElement dropdown, String option) {
        waitUntilElementReady(driver, dropdown);
        dropdown.findElement(By.xpath(".//*[contains(text(),'" + option + "')]")).click();
    }

    /**
     * Helper method to clear a text input field and send a sequence of keys.
     */
    public static void clearFieldAndSendKeys(WebDriver driver, WebElement field, CharSequence keys) {
        field.sendKeys(Keys.CONTROL + "a");
        field.sendKeys(Keys.DELETE);
        field.clear();
        waitForExpectedCharCount(driver, field, 0);
        field.sendKeys(keys);
        //waitForExpectedCharCount(driver, field, keys.length())
    }

    /**
     * @param driver
     * @param textField
     * @param expectedCharCount
     */
    public static void waitForExpectedCharCount(WebDriver driver, WebElement textField, int expectedCharCount) {
        defaultWait(driver).until((WebDriver d) -> {
                        String text = textField.getAttribute("value");
                        if(text == null)
                            text = "";
                        return text.length() == expectedCharCount;
                    });
    }

    /**
     * Helper method to click on a WebElement after checking if the element is ready.
     */
    public static void waitAndClick(WebDriver driver, WebElement element) {
        waitUntilElementReady(driver, element);
        forceClick(driver, element);
    }

    /**
     * Helper method to click on a WebElement that is not scrolled in to view.
     */
    public static void forceClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Wait for the selector until there is no StaleElementReferenceException
     *
     * @param driver - WebDriver object
     * @param wait - WebDriverWait object
     * @param by - By selector
     * @return
     */
    public static void waitUntilNoStaleElement(WebDriver driver, WebDriverWait wait, By by) {
        Utils.waitForPageToLoad(driver);

        WebElement element = null;
        int count = 0;
        boolean foundException = true;
        while (count < 4 && foundException){
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(by));
                element = driver.findElement(by);
                foundException = false;
             } catch (StaleElementReferenceException e){
                 Utils.waitForPageToLoad(driver);
                 count = count+1;
             }
        }

        Utils.waitForPageToLoad(driver);
        Utils.scrollIntoView(driver, element);
    }

    /**
     * Execute a given command on the windows command prompt and display the
     * command output on the console.
     *
     * @param  String - Windows command
     * @return List<String> - Console output
     */
    public static List<String> runWinCmd(String command) {
        List<String> consoleOutput =  new ArrayList<>();
        if (command==null)
            command = "echo for /f \"skip=1 tokens=1,2,3\" %%s in ('query user %USERNAME%') do ( echo %%s %%t %%u  > mytest.bat &&echo if /I \"%%u\"==\"console\" (echo InConsoleMode) else ( tscon.exe %%t /dest:console )) >> mytest.bat &&mytest.bat";
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                consoleOutput.add(line);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return consoleOutput;
    }
 
}

