package blcrawler.model.selenium;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import blcrawler.model.ConsoleOutput;
import blcrawler.model.GUIModel;

public class SeleniumModel
{
    
    private ProfilesIni profile;
    private FirefoxProfile myprofile;
    private WebDriver driver;
    FirefoxOptions firefoxOptions;
    
    public SeleniumModel()
    {
        System.setProperty("webdriver.gecko.driver",
                "C:/Users/Owner/Desktop/Multicircuit Tors/Geckodriver/"
                        + "geckodriver-v0.23.0-win64/geckodriver.exe");
        profile = new ProfilesIni();
        myprofile = profile.getProfile("9152");
        firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(myprofile);
        driver = new FirefoxDriver(firefoxOptions);
        
        GUIModel.getGuiView().setIndeterminiteOff();
    }
    
    public void relaunchTor() throws InterruptedException, IOException
    {
        GUIModel.getConsoleController().killFirefox();
        Thread.sleep(1000);
        Runtime.getRuntime().exec("C:/Users/Owner/Desktop/Tor Browser/Browser/firefox.exe");
        Thread.sleep(5000);
        driver = new FirefoxDriver(firefoxOptions);
    }
    
    public void gotoURL(String url)
    {
        driver.get(url);
        new ConsoleOutput("Selenium: ", "Page title is: " + driver.getTitle());
    }
    
    public String getHTML()
    {
        return driver.getPageSource();
    }
    
}