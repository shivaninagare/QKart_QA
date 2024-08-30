package QKART_TESTNG;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class ListenersTestNG implements ITestListener{
    //take screenshot using listeners

        //when test case starts
    @Override
    public void onTestStart(ITestResult result) {
    QKART_Tests.takeScreenshot(QKART_Tests.driver, "Start", result.getName());
    }
    //when test case ends
    //pass
    @Override
    public void onTestSuccess(ITestResult result) {
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "success", result.getName());

    }

    //fail
    @Override
    public void onTestFailure(ITestResult result) {
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "Failure", result.getName());

    }
    

}
