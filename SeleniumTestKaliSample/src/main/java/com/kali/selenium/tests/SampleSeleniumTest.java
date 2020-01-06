package com.kali.selenium.tests;

import org.testng.annotations.Test;

import com.kali.selenium.config.TestSetup;
import com.kali.selenium.pages.MsnTodaysWeatherPage;

/**
 * 
 * 
 * @author Kalikrishna Veeranki
 *
 */
public class SampleSeleniumTest extends TestSetup {

    @Test
    public void test1() {
        // Open URL
        driver.get(configs.getProperty("msnURL1"));
        MsnTodaysWeatherPage msnTWPage = new MsnTodaysWeatherPage(driver);

        // Click on footer links
        msnTWPage.clickFacebookLikeInFooter();
        msnTWPage.clickTwtterFollowInFooter();

        // Get Delhi and Bengaluru temperature
        int delhiTemp = msnTWPage.getCurrentTemperature();
        msnTWPage = msnTWPage.selectALocation("Bengaluru");
        int bangaluruTemp = msnTWPage.getCurrentTemperature();

        // Compare Delhi and Bengaluru temperature
        if(delhiTemp>bangaluruTemp) {
            System.out.println("Delhi:"+delhiTemp);
        } else if(delhiTemp<bangaluruTemp) {
            System.out.println("Bangaluru:"+bangaluruTemp);
        } else {
            System.out.println("Delhi and Bangaluru have same temperature:"+delhiTemp);
        }
    }

}
