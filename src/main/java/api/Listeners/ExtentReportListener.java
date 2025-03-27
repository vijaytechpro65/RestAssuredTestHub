package api.Listeners;
import com.aventstack.extentreports.ExtentReports;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class ExtentReportListener implements ITestListener {

    private static final String OUTPUT_FOLDER = "./build/";
    private static final String FILE_NAME = "TestExecutionReport.html";

    private static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static ExtentReports init() {
        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        extent = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        reporter.config().setReportName("API Test Results");
        extent.attachReporter(reporter);
        extent.setSystemInfo("System", "Windows10");
        extent.setSystemInfo("Author", "Your Name");
        extent.setSystemInfo("Team", "API Testing");

        return extent;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("Test Suite started!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite ended!");
        extent.flush();
        test.remove();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        test.set(extentTest);
        test.get().assignCategory(result.getTestContext().getSuite().getName());
        test.get().assignCategory(result.getTestClass().getName());
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " passed!");
        test.get().pass("Test passed");
        String responseBody = "Response Body: " + getApiResponse(); // Log API response
        String statusCode = "Status Code: " + getApiStatusCode(); // Log API status code
        test.get().info(responseBody);
        test.get().info(statusCode);
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " failed!");
        test.get().fail(result.getThrowable());
        String responseBody = "Response Body: " + getApiResponse(); // Log API response
        String statusCode = "Status Code: " + getApiStatusCode(); // Log API status code
        test.get().info(responseBody);
        test.get().info(statusCode);
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " skipped!");
        test.get().skip(result.getThrowable());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Failed but within success percentage: " + result.getMethod().getMethodName());
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private String getApiResponse() {
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");
        return response.getBody().asString();
    }

    private String getApiStatusCode() {
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");
        return String.valueOf(response.getStatusCode());
    }
}
