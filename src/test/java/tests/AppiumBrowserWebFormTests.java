package tests;

import configs.TestPropertiesConfig;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PointerUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static constants.Constants.BASE_URL;
import static constants.Constants.SERVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AppiumBrowserWebFormTests {
    //Запуск: appium --allow-insecure chromedriver_autodownload
    private AndroidDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    public static final String VALUE_NAME = "value";
    private static final String ITEM = "Web form";
    private static final String BIG_TEXT = "Lorem ipsum dolor sit amet consectetur adipiscing elit habitant metus, " +
            "tincidunt maecenas posuere sollicitudin augue duis bibendum mauris eu, et dignissim magna ad nascetur suspendisse quis nunc. " +
            "Fames est ligula molestie aliquam pretium bibendum nullam, sociosqu maecenas mus etiam consequat ornare leo, sem mattis " +
            "varius luctus litora senectus. Parturient quis tristique erat natoque tortor nascetur, primis augue vivamus habitasse " +
            "senectus porta leo, aenean potenti ante a nam.";

    @BeforeEach
    void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options
                .setPlatformName("Android")
                .setPlatformVersion("16")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .noReset()
                .withBrowserName("Chrome");

        driver = new AndroidDriver(new URL(SERVER), options);
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void webFormTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();
        WebElement title =  wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//h1[@class = 'display-6']")));

        assertThat(title.getText()).isEqualTo(ITEM);

        Thread.sleep(3000);
    }

    @Test
    @DisplayName("Check user field")
    void textInputTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement textInputField = driver.findElement(By.id("my-text-id"));
        textInputField.sendKeys(config.getLogin());

        String actualLogin = textInputField.getDomProperty(VALUE_NAME);

        assertThat(actualLogin).isNotEmpty();
    }

    @Test
    @DisplayName("Check clear user field")
    void textInputClearTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement textInputField = driver.findElement(By.id("my-text-id"));
        textInputField.sendKeys(config.getLogin());
        textInputField.clear();

        String actualLogin = textInputField.getDomProperty(VALUE_NAME);

        assertThat(actualLogin).isEmpty();
    }

    @Test
    @DisplayName("Check password field")
    void passwordInputTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement passwordInputField = driver.findElement(By.name("my-password"));
        passwordInputField.sendKeys(config.getPassword());

        String actualPassword = passwordInputField.getDomProperty(VALUE_NAME);

        assertThat(actualPassword).isNotEmpty();
    }

    @Test
    @DisplayName("Check clear password field")
    void passwordClearTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement passwordInputField = driver.findElement(By.name("my-password"));
        passwordInputField.sendKeys(config.getPassword());
        passwordInputField.clear();

        String actualPassword = passwordInputField.getDomProperty(VALUE_NAME);

        assertThat(actualPassword).isEmpty();
    }

    @Test
    @DisplayName("Check textarea field")
    void textAreaFieldTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement textAreaInputField = driver.findElement(By.name("my-textarea"));
        textAreaInputField.sendKeys(BIG_TEXT);

        assertEquals(BIG_TEXT, textAreaInputField.getDomProperty(VALUE_NAME));
    }

    @Test
    @DisplayName("Check clear textarea field")
    void textAreaFieldClearTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement textAreaInputField = driver.findElement(By.name("my-textarea"));
        textAreaInputField.sendKeys(BIG_TEXT);
        textAreaInputField.clear();

        String bigText= textAreaInputField.getDomProperty(VALUE_NAME);

        assertThat(bigText).isEmpty();
    }

    @Test
    @DisplayName("Check Disabled input field")
    void disabledInputFieldTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement disabledInputField = driver.findElement(By.name("my-disabled"));

        assertFalse(disabledInputField.isEnabled());
        assertThrows(ElementNotInteractableException.class, () -> disabledInputField.sendKeys("test string"));
        assertEquals("Disabled input", disabledInputField.findElement(By.xpath("..")).getText());
        assertEquals("Disabled input", disabledInputField.getDomAttribute("placeholder"));
    }

    @Test
    @DisplayName("Check Readonly input field")
    void readonlyInputFieldTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement readonlyInputField = driver.findElement(By.name("my-readonly"));

        assertTrue(readonlyInputField.isEnabled());
        assertEquals("Readonly input", readonlyInputField.findElement(By.xpath("..")).getText());
        assertEquals("Readonly input", readonlyInputField.getDomAttribute(VALUE_NAME));
        readonlyInputField.sendKeys("test string");
        assertNotEquals("test string", readonlyInputField.findElement(By.xpath("..")).getText());
    }

    @Test
    @DisplayName("Check Dropdown (select) menu by visible text")
    void dropdownSelectTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));

        Select select = new Select(dropdownSelectMenu);
        assertEquals("Open this select menu", select.getFirstSelectedOption().getText());
        Thread.sleep(2000);
        select.selectByValue("1");
        assertEquals("One", select.getFirstSelectedOption().getText());
        assertTrue(select.getFirstSelectedOption().isSelected());
        Thread.sleep(2000);
        select.selectByValue("2");
        assertEquals("Two", select.getFirstSelectedOption().getText());
        assertTrue(select.getFirstSelectedOption().isSelected());
        Thread.sleep(2000);
        select.selectByVisibleText("Three");
        assertEquals("Three", select.getFirstSelectedOption().getText());
        assertTrue(select.getFirstSelectedOption().isSelected());
        Thread.sleep(2000);

        List<WebElement> selectedOptions = select.getAllSelectedOptions();
        for (WebElement selectedOption : selectedOptions) {
            System.out.println("Selected option: " + selectedOption.getText());
        }
        Thread.sleep(2000);

        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            System.out.printf("Available Option: %s isSelected = %s%n", option.getText(), option.isSelected());
        }
        Thread.sleep(2000);

        if (select.isMultiple()) {
            select.deselectByIndex(1);
            select.selectByValue("1");
            select.deselectByVisibleText("One");
            select.deselectAll();
        } else {
            System.out.println("You may only deselect all options of a multi-select");
        }
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Check Dropdown (datalist) menu")
    void dropdownDataListTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        WebElement dropdownDataList = driver.findElement(By.name("my-datalist"));

        dropdownDataList.sendKeys("San Francisco");
        assertEquals("San Francisco", dropdownDataList.getDomProperty(VALUE_NAME));
        dropdownDataList.clear();

        dropdownDataList.sendKeys("New York");
        assertEquals("New York", dropdownDataList.getDomProperty(VALUE_NAME));
        dropdownDataList.clear();

        dropdownDataList.sendKeys("Seattle");
        assertEquals("Seattle", dropdownDataList.getDomProperty(VALUE_NAME));
        dropdownDataList.clear();

        dropdownDataList.sendKeys("Los Angeles");
        assertEquals("Los Angeles", dropdownDataList.getDomProperty(VALUE_NAME));
        dropdownDataList.clear();

        dropdownDataList.sendKeys("Chicago");
        assertEquals("Chicago", dropdownDataList.getDomProperty(VALUE_NAME));
        dropdownDataList.clear();

        assertEquals("Type to search...", dropdownDataList.getDomProperty("placeholder"));
    }

    @Test
    @DisplayName("Check File input field")
    void fileInputTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement fileInputButton = driver.findElement(By.name("my-file"));

        String selectFile = System.getProperty("user.dir") + "/src/test/resources/STE In Banner.jpg";
        fileInputButton.sendKeys(selectFile);

        WebElement submit = driver.findElement(By.xpath("//button[@type = 'submit']"));
        submit.click();
        Thread.sleep(5000);
        assertThat(driver.getCurrentUrl()).contains("STE+In+Banner");

        WebElement formSubmittedText = driver.findElement(By.xpath("//h1[@class = 'display-6']"));
        assertEquals("Form submitted", formSubmittedText.getText());

        WebElement receivedText = driver.findElement(By.xpath("//p[@class = 'lead']"));
        assertEquals("Received!", receivedText.getText());
    }

    @Test
    @DisplayName("Check checked Checkbox")
    void checkedCheckboxTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement checkedCheckbox = driver.findElement(By.id("my-check-2"));

        assertTrue(checkedCheckbox.isSelected());
        checkedCheckbox.click();
        Thread.sleep(3000);
        assertFalse(checkedCheckbox.isSelected());
    }

    @Test
    @DisplayName("Check default Checkbox")
    void defaultCheckboxTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement defaultCheckbox = driver.findElement(By.id("my-check-1"));

        assertFalse(defaultCheckbox.isSelected());

        defaultCheckbox.click();
        assertTrue(defaultCheckbox.isSelected());
    }

    @Test
    @DisplayName("Check radio buttons")
    void radioButtonsTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement checkedRadioButton = driver.findElement(By.id("my-radio-1"));
        WebElement defaultRadioButton = driver.findElement(By.id("my-radio-2"));

        assertTrue(checkedRadioButton.isSelected());
        assertFalse(defaultRadioButton.isSelected());
        defaultRadioButton.click();
        assertFalse(checkedRadioButton.isSelected());
        assertTrue(defaultRadioButton.isSelected());
    }

    @Test
    @DisplayName("Check Color picker")
    void colorPickerTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement colorPicker = driver.findElement(By.name("my-colors"));

        JavascriptExecutor js = driver;

        String initColor = colorPicker.getDomProperty(VALUE_NAME);
        System.out.println("The initial color is " + initColor);

        Color green = new Color(0, 255, 0, 1);

        String script = String.format("arguments[0].setAttribute('value', '%s');", green.asHex());
        Thread.sleep(3000);
        js.executeScript(script, colorPicker);
        String finalColor = colorPicker.getDomProperty(VALUE_NAME);
        System.out.println("The initial color is " + finalColor);
        assertThat(finalColor).isNotEqualTo(initColor);
        assertThat(Color.fromString(finalColor)).isEqualTo(green);
    }

    @Test
    @DisplayName("Check Date picker")
    void datePickerTest() {
        String actualDate = "06/18/2025";
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement dateBox = driver.findElement(By.name("my-date"));

        dateBox.click();
        dateBox.sendKeys(actualDate);

        assertEquals(actualDate, dateBox.getDomProperty(VALUE_NAME));
    }

    @Test
    @DisplayName("Check Example range")
    void actionAPIMouseExampleRangeTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement rangeElement = driver.findElement(By.name("my-range"));

//        for (int min = 0; min <= 10 ; min++) {
//            rangeElement.sendKeys(Keys.ARROW_RIGHT);
//        }
//        Thread.sleep(2000);
//
//        assertEquals("10", rangeElement.getDomProperty(VALUE_NAME));
//
//        for (int min = 10; min >= 0 ; min--) {
//            rangeElement.sendKeys(Keys.ARROW_LEFT);
//        }
//        Thread.sleep(2000);
//
//        assertEquals("0", rangeElement.getDomProperty(VALUE_NAME));

        int width = rangeElement.getSize().getWidth();
        System.out.println("width = " + width);
        int x = rangeElement.getLocation().getX();
        System.out.println("x = " + x);
        int y = rangeElement.getLocation().getY();
        System.out.println("y = " + y);
        int i;
        for (i = 5; i <= 10; i++) {
            driver.executeScript("arguments[0].setAttribute('value', arguments[1]);", rangeElement, String.valueOf(i));
        }
        assertEquals(String.valueOf(i - 1), rangeElement.getDomProperty(VALUE_NAME));
    }

    @Test
    @DisplayName("Check submit button and form submitted")
    void submitButtonTest() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.linkText(ITEM))).click();

        PointerUtils.swipe(driver);
        PointerUtils.swipe(driver);

        WebElement submitButton = driver.findElement(By.xpath("//button[@type = 'submit']"));
        submitButton.click();
        Thread.sleep(3000);

        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html?my-text=&my-password=&my-textarea=&my-readonly=Readonly+input&my-select=Open+this+select+menu&my-datalist=&my-file=&my-check=on&my-radio=on&my-colors=%23563d7c&my-date=&my-range=5&my-hidden=", driver.getCurrentUrl());

        WebElement formSubmittedText = driver.findElement(By.xpath("//h1[@class = 'display-6']"));

        assertEquals("Form submitted", formSubmittedText.getText());
    }
}
