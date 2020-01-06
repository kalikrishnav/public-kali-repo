/**
 * This is sample Class for text. More info to be updated.
 */
package com.kali.selenium.config;

import java.util.Properties;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.kali.selenium.utils.Locator;
import com.kali.selenium.utils.Utils;

/**
 * 
 * @author Kalikrishna Veeranki
 *
 */
public class TestSetup {

	// Default WebDriver & WebDriverWait objects
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    // Test suite related configurations
    protected static Properties configs = Locator.loadProperties("src/main/resources/config.txt");

    // Wait times
    public static final int DEFAULT_TIMEOUT = 30; // in seconds
    public static final int FAST_TIMEOUT = DEFAULT_TIMEOUT/5; // in seconds
    public static final int POLLING_INTERVAL = DEFAULT_TIMEOUT*5; // in milliseconds

    //Default Constructor.
    protected TestSetup() {

    }

    /**
     * Initial Setup before starting the Test Suite
     */
    @BeforeSuite(alwaysRun = true)
    public static void initialize() {
        driver = getDriver();
        wait = getDriverWait(driver, DEFAULT_TIMEOUT);
    }

    /**
     * Create a browser instance
     * 
     * @param 
     * @return the webdriver object representing a browser instance
     */
    public static WebDriver getDriver() {
        if(driver != null)
           return driver;

        WebDriver ndriver;
        switch (configs.getProperty("browserType")) {
            case "firefox":
                //WIP
                FirefoxProfile ffProfile = new FirefoxProfile();
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.setProfile(ffProfile);
                ndriver = new FirefoxDriver(ffOptions);
                break;
            case "ie":
                //WIP
                ndriver = new InternetExplorerDriver();
                break;
            case "edge":
                //WIP
                ndriver = new EdgeDriver();
                break;
            default:
                // Default ChromeDriver
                ChromeOptions chOptions = new ChromeOptions();
                chOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
                chOptions.addArguments("--disable-notifications");
                //ndriver = new ChromeDriver(chOptions);
                ndriver = new ChromeDriver();
        }

        ndriver.manage().window().maximize();
        driver = ndriver;

        return ndriver;
    }

    /**
     * Create a wait object associated with the browser instance
     * 
     * @param 
     * @return the webdriver object representing a browser instance
     */
    public static WebDriverWait getDriverWait(WebDriver driver, int timeOut) {
        return (new WebDriverWait(driver, timeOut, POLLING_INTERVAL));
    }

    /**
     * Clean up before exiting.
     * To be called before and after this suite 
     * 
     * @param 
     * @return 
     */
    @AfterSuite(alwaysRun = true)
    public static void terminate() {
        // Close the browsers
        driver.quit();
        if (System.getProperty("os.name").toLowerCase().startsWith("win"))
            Utils.runWinCmd("taskkill /IM chromedriver.exe /IM IEDriverServer.exe /IM MicrosoftWebDriver.exe /F /IM geckodriver.exe");
       
    }

}
