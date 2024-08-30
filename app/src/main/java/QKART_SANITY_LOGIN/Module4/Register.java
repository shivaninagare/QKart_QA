package QKART_SANITY_LOGIN.Module4;

import java.sql.Timestamp;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Register {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/register";
    public String lastGeneratedUsername = "";

    public Register(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToRegisterPage() {
        if (!driver.getCurrentUrl().equals(this.url)) {
            driver.get(this.url);
        }
    }

    public Boolean registerUser(String Username, String Password, Boolean makeUsernameDynamic)
            throws InterruptedException {
        // Find the Username Text Box
        WebElement username_txt_box = this.driver.findElement(By.id("username"));

        // Get time stamp for generating a unique username
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String test_data_username;
        if (makeUsernameDynamic)
            // Concatenate the timestamp to string to form unique timestamp
            test_data_username = Username + "_" + String.valueOf(timestamp.getTime());
        else
            test_data_username = Username;

        // Type the generated username in the username field
        username_txt_box.sendKeys(test_data_username);

        // Find the password Text Box
        WebElement password_txt_box = this.driver.findElement(By.id("password"));
        String test_data_password = Password;

        // Enter the Password value
        password_txt_box.sendKeys(test_data_password);

        WebElement confirm_password_txt_box = this.driver.findElement(By.id("confirmPassword"));

        // Enter the Confirm Password Value
        confirm_password_txt_box.sendKeys(test_data_password);

        // Find the register now button
        WebElement register_now_button = this.driver.findElement(By.className("button"));

        // Click the register now button
        register_now_button.click();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login")));
        } catch (TimeoutException e) {
            return false;
        }

        this.lastGeneratedUsername = test_data_username;

        return this.driver.getCurrentUrl().endsWith("/login");
    }
}
