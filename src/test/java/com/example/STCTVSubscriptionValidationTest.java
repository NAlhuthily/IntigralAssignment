package com.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;

public class STCTVSubscriptionValidationTest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setUp() {
        //Set up chrome web driver
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        //Set up ExtentSparkReporter 
        ExtentSparkReporter spark = new ExtentSparkReporter("reports\\STCTVSubscriptionValidationTestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        driver.manage().window().maximize();
    }
    //Package Types
    private final List<String> packageTypes = Arrays.asList("LITE", "CLASSIC", "PREMIUM");
    //Prices per country
    private final List<Double> saPrices = Arrays.asList(15.0, 25.0, 60.0); //SA
    private final List<Double> kwPrices = Arrays.asList(1.2, 2.5, 4.8); //KW
    private final List<Double> bhPrices = Arrays.asList(2.0, 3.0, 6.0); //BH

    @DataProvider(name = "countryCurrencyData")
    public Object[][] createCountryCurrencyData() {
        return new Object[][] {
                { "SA", "SAR", saPrices },
                { "KW", "KWD", kwPrices },
                { "BH", "BHD", bhPrices }
        };
    }

    @Test(dataProvider = "countryCurrencyData")
    public void validateSubscriptionPackages(String country, String expectedCurrency, List<Double> expectedPrices) {
        
        driver.get("https://subscribe.stctv.com/sa-en");

        // Create a new test entry in the ExtentReports
        test = extent.createTest("Subscription Validation for " + country);

        WebElement countrySelector = driver.findElement(By.cssSelector("#country-btn"));
        countrySelector.click();

        WebElement countryOption = driver.findElement(By.xpath("//*[@id='" + country.toLowerCase() + "']"));
        countryOption.click();

        for (int i = 0; i < packageTypes.size(); i++) {
            
            String packageType = packageTypes.get(i);
            double expectedPrice = expectedPrices.get(i);

            //Get package type text
            WebElement packageElement = driver.findElement(By.xpath("//*[@id='name-" + packageType.toLowerCase() + "']"));
            String packageTypeText = packageElement.getText();   
            //Get package price
            String priceText = packageElement.findElement(By.xpath("//*[@id='currency-" + packageType.toLowerCase() + "']/b")).getText();
            //Get package currency
            WebElement currencyElement = driver.findElement(By.xpath("//*[@id='currency-" + packageType.toLowerCase() + "']/i"));
            String currency = currencyElement.getText().substring(0, currencyElement.getText().indexOf("/")).trim();

            test.info("Package: " + packageTypeText + " | Price: " + priceText + " | Expected Price: " + expectedPrice + " | Currency: " + currency + " | Expected Currency: " + expectedCurrency);

            Assert.assertEquals(packageType, packageTypeText, "Package Type does not match");
            Assert.assertEquals(currency, expectedCurrency, "Currency does not match for " + country);
            Assert.assertEquals(Double.parseDouble(priceText), expectedPrice, "Price does not match for package: " + packageTypeText);

            test.pass("Validation passed for package: " + packageType + " For country: " + country);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        extent.flush(); // Ensure all information is written to the report
    }
}
