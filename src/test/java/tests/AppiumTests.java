package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PointerUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.function.Function;

import static constants.Constants.APP;
import static constants.Constants.SERVER;
import static org.assertj.core.api.Assertions.assertThat;

public class AppiumTests {
    //Запуск: appium --allow-cors
    private AndroidDriver driver;

    @BeforeEach
    void setup() throws MalformedURLException {
        DesiredCapabilities ds = new DesiredCapabilities();
        ds.setCapability("platformName", "Android");
        ds.setCapability("platformVersion", "16");
        ds.setCapability("deviceName", "emulator-5554");
        ds.setCapability("app", APP);
        ds.setCapability("automationName", "UiAutomator2");

        driver = new AndroidDriver(new URL(SERVER), ds);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void simpleTest() throws InterruptedException {
        Thread.sleep(30000);
        WebElement login = driver.findElement(AppiumBy.accessibilityId("Login Screen"));

        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    void simpleTestWithImplicitWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement login = driver.findElement(AppiumBy.accessibilityId("Login Screen"));

        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    void simpleTestWithExplicitWait() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));

        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    void simpleTestWithFluentWait() {
        FluentWait<AndroidDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement login = wait.until((Function<WebDriver, WebElement>) driver -> driver.findElement(AppiumBy.accessibilityId("Login Screen")));

        assertThat(login.isDisplayed()).isTrue();
    }

    @Test
    void simpleActionsTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(login.getText()).isEqualTo("");
        softly.assertThat(login.isDisplayed()).isTrue();
        softly.assertThat(login.isSelected()).isFalse();
        softly.assertThat(login.isEnabled()).isTrue();
        softly.assertAll();
    }

    @Test
    void loginTest() throws InterruptedException {
        String user = "alice";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login Screen")));
        login.click();
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("username")));
        username.sendKeys(user);
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys("mypassword");
        driver.findElement(AppiumBy.accessibilityId("loginBtn")).click();
        Thread.sleep(5000);

        String source = driver.getPageSource();
        System.out.println("SourceCode:" + source);
        WebElement loginMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));

        assertThat(loginMessage.getText()).contains(user);
    }

    @Test
    void swipeTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement listDemoItem = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("List Demo")));
        listDemoItem.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("AWS")));

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement lastElement = driver.findElement(AppiumBy.accessibilityId("Stratus"));

        assertThat(lastElement.isDisplayed()).isTrue();
    }
}
