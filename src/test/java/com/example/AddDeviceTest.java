package com.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.HashMap;
import java.util.Map;

public class AddDeviceTest {
    private static ExtentReports extent;
    private static ExtentTest logger;

    @BeforeSuite
    public void beforeSuite() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("reports\\AddDeviceTestReport.html");
        extent.attachReporter(spark);
    }

    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }

    @BeforeMethod
    public void setupTest() {
        logger = extent.createTest("Add New Device Test");
    }

    @AfterMethod
    public void teardownTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
        } else {
            logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
            logger.log(Status.SKIP, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Skipped", ExtentColor.ORANGE));
        }
    }

    @Test
    public void testAddNewDevice() {
        String apiEndpoint = "https://api.restful-api.dev/objects";

        // Request body test data
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Apple Max Pro 1TB");

        Map<String, Object> data = new HashMap<>();
        data.put("year", 2023);
        data.put("price", 7999.99);
        data.put("CPU model", "Apple ARM A7");
        data.put("Hard disk size", "1 TB");

        requestBody.put("data", data);

        // Validate request fields existence
        Assert.assertTrue(requestBody.containsKey("name"), "Request does not contain 'name' field");
        Assert.assertTrue(requestBody.containsKey("data"), "Request does not contain 'data' field");

        Object dataObj = requestBody.get("data");
        Map<String, Object> requestData = (Map<String, Object>) dataObj;

        Assert.assertTrue(requestData.containsKey("year"), "Request data does not contain 'year' field");
        Assert.assertTrue(requestData.containsKey("price"), "Request data does not contain 'price' field");
        Assert.assertTrue(requestData.containsKey("CPU model"), "Request data does not contain 'CPU model' field");
        Assert.assertTrue(requestData.containsKey("Hard disk size"), "Request data does not contain 'Hard disk size' field");

        logger.log(Status.PASS, "Validate request fields existence is successfull");

        // Validate request fields not null
        Assert.assertNotNull(requestBody.get("name"), "'name' field in request is null");
        Assert.assertNotNull(requestBody.get("data"), "'data' field in request is null");

        Assert.assertNotNull(requestData.get("year"), "'year' field in request data is null");
        Assert.assertNotNull(requestData.get("price"), "'price' field in request data is null");
        Assert.assertNotNull(requestData.get("CPU model"), "'CPU model' field in request data is null");
        Assert.assertNotNull(requestData.get("Hard disk size"), "'Hard disk size' field in request data is null");

        logger.log(Status.PASS, "Validate request fields not null is successfull");

        // Validate request fields data type
        Assert.assertTrue(requestBody.get("name") instanceof String, "'name' field in request is not of type String");
        Assert.assertTrue(requestData.get("year") instanceof Integer, "'year' field in request data is not of type Integer");
        Assert.assertTrue(requestData.get("price") instanceof Double, "'price' field in request data is not of type Double");
        Assert.assertTrue(requestData.get("CPU model") instanceof String, "'CPU model' field in request data is not of type String");
        Assert.assertTrue(requestData.get("Hard disk size") instanceof String, "'Hard disk size' field in request data is not of type String");

        logger.log(Status.PASS, "Validate request fields data type is successfull");

        // Capture the response
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(apiEndpoint) ;

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        // Validate response status code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");
        logger.log(Status.PASS, "Status code equals 200");

        // Validate response fields existence
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("id"), "Response does not contain 'id' field");
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("name"), "Response does not contain 'name' field");
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("createdAt"), "Response does not contain 'createdAt' field");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("year"), "Response data does not contain 'year' field");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("price"), "Response data does not contain 'price' field");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("CPU model"), "Response data does not contain 'CPU model' field");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("Hard disk size"), "Response data does not contain 'Hard disk size' field");

        logger.log(Status.PASS, "Validate response fields existence is successfull");

        // Validate response fields not null
        Assert.assertNotNull(response.jsonPath().get("id"), "'id' field in response is null");
        Assert.assertNotNull(response.jsonPath().get("createdAt"), "'createdAt' field in response is null");

        logger.log(Status.PASS, "Validate response fields not null is successfull");

        // Validate response fields data type
        Assert.assertTrue(response.jsonPath().get("id") instanceof String, "'id' field in response is not of type String");
        Assert.assertTrue(response.jsonPath().get("name") instanceof String, "'name' field in response is not of type String");
        Assert.assertTrue(response.jsonPath().get("createdAt") instanceof String, "'createdAt' field in response is not of type String");
        Assert.assertTrue(response.jsonPath().get("data.year") instanceof Integer, "'year' field in response data is not of type Integer");
        Assert.assertTrue(response.jsonPath().get("data.price") instanceof Float, "'price' field in response data is not of type Double");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("CPU model"), "Response data does not contain 'CPU model' field");
        Assert.assertTrue(response.jsonPath().getMap("data").containsKey("Hard disk size"), "Response data does not contain 'Hard disk size' field");

        logger.log(Status.PASS, "Validate response fields data type is successfull");

        int year = response.jsonPath().get("data.year");
        float price = response.jsonPath().get("data.price");
        String cpuModel = response.jsonPath().get("data['CPU model']");
        String hardDiskSize = response.jsonPath().get("data['Hard disk size']");
        logger.log(Status.INFO, "Device added successfully with ID: " + response.jsonPath().get("id") +
                ", Name: " + response.jsonPath().get("name") +
                ", Created At: " + response.jsonPath().get("createdAt") +
                ", Year: " + year +
                ", Price: " + price +
                ", CPU Model: " + cpuModel +
                ", Hard Disk Size: " + hardDiskSize);
    }
}