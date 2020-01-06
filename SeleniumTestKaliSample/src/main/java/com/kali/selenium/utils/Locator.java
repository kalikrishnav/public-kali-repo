package com.kali.selenium.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * @author Kalikrishna Veeranki
 *
 */
public class Locator {
    private Properties locationProps;
    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Default constructor.
     * 
     * @param objectRepository
     * @param driver
     * @param wait
     */
    public Locator(String objectRepository, WebDriver driver, WebDriverWait wait) {
        this.locationProps = loadProperties(objectRepository);
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Read properties from object repository
     * 
     * @param file
     *            the name of the properties file
     * @return the properties object representing it
     */
    public static Properties loadProperties(String file) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read properties from a directory
     * 
     * @param directory
     *            the name of the directory containing properties files
     * @return the properties object representing it
     */
    public static Properties loadPropertiesInDir(String propetiesDir) {
        File folder = new File(propetiesDir);
        try {
            Properties props = new Properties();
            File[] files = folder.listFiles();
            for (File file : files) {
                props.load(new FileInputStream(file));
            }
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Waits until element is available, then returns its WebElement
     * representation. If multiple WebElements match the elementName, the first
     * WebElement found is returned.
     * 
     * @param elementName
     *            the name of the element to find
     * @return the WebElement object representing it
     */
    public WebElement get(final String elementName) {
        Utils.waitUntilNoStaleElement(driver, wait, getByObject(elementName));
        WebElement element = Utils.findElement(driver, getByObject(elementName));
        return Utils.waitUntilElementReady(driver, element);
    }

    public WebElement get(final String elementName, String text) {
        Utils.waitForElementLocatedToHaveText(driver, getByObject(elementName), text);
        return get(elementName);
    }

    /**
     * Creates a By object from the selector value for an object repository element.
     * 
     * @param elementName
     *            the name of the element to find
     * @return the By object of the element
     */
    public By getByObject(final String elementName) {
        By byObject =  null;
        String selector = locationProps.getProperty(elementName);

        if (selector == null || selector.isEmpty())
            throw new IllegalArgumentException(elementName + " does not exist or empty in the object repository");

        if (elementName.toLowerCase().endsWith(".csspath")) {
            byObject = By.cssSelector(selector);
        } else if (elementName.toLowerCase().endsWith(".id")) {
            byObject = By.id(selector);
        } else {
            byObject = By.xpath(selector);
        }

        return byObject;
    }

}
