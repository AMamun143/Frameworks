package base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import reporting.ExtentManager;
import reporting.ExtentTestManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommonAPI {

    public static ExtentReports extent;
    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        ExtentTestManager.endTest();
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(driver, result.getName());
        }
        driver.quit();
    }

    @AfterSuite
    public void generateReport() {
        extent.close();
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


    public static WebDriver driver = null;

    @Parameters({"os","os_version","browserName","browserVersion","url"})
    @BeforeMethod
    public void setUp(@Optional("Windows") String os,@Optional("10") String os_version, @Optional("firefox") String browserName, @Optional("34")
                              String browserVersion, @Optional("http://www.amazon.com") String url)throws IOException {

        if(browserName.equalsIgnoreCase("chrome")){
            if(os.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver");
            }else if(os.equalsIgnoreCase("Windows")){
                System.setProperty("webdriver.chrome.driver", "../Generic/driver/chromedriver.exe");
            }
            driver = new ChromeDriver();
        }else if(browserName.equalsIgnoreCase("firefox")){
            if(os.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver");
            }else if(os.equalsIgnoreCase("Windows")) {
                System.setProperty("webdriver.gecko.driver", "../Generic/driver/geckodriver.exe");
            }
            driver = new FirefoxDriver();

        } else if(browserName.equalsIgnoreCase("ie")) {
            System.setProperty("webdriver.ie.driver", "../Generic/driver/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(35, TimeUnit.SECONDS);
        driver.get(url);
        driver.manage().window().maximize();
    }


    @AfterMethod
    public static void cleanUp(){
        driver.quit();
    }

    //Click WebElements
    public void clickByCSS(String locator){
        driver.findElement(By.cssSelector(locator)).click();
    }
    public void clickByXpath(String locator){
        driver.findElement(By.xpath(locator)).click();
    }
    public void clickById(String locator){
        driver.findElement(By.id(locator)).click();
    }
    public void clickByClassName(String locator){
        driver.findElement(By.className(locator)).click();
    }


    //Type into input fields
    public void typeByCSS(String locator, String value){
        driver.findElement(By.cssSelector(locator)).sendKeys(value);
    }
    public void typeByXpath(String locator, String value){
        driver.findElement(By.xpath(locator)).sendKeys(value);
    }
    public void typeById(String locator, String value){
        driver.findElement(By.id(locator)).sendKeys(value);
    }
    public void typeByClassName(String locator, String value){
        driver.findElement(By.className(locator)).sendKeys(value);
    }

    //Clear Input Fields
    public void clearInputFieldByCSS(String locator){
        driver.findElement(By.cssSelector(locator)).clear();
    }
    public void clearInputFieldByXpath(String locator){
        driver.findElement(By.xpath(locator)).clear();
    }
    public void clearInputFieldByID(String locator){
        driver.findElement(By.id(locator)).clear();
    }
    public void clearInputFieldByClassName(String locator){
        driver.findElement(By.className(locator)).clear();
    }

    //Convert to String
    public String converToString(String st){
        String splitString ;
        splitString = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(st), ' ');
        return splitString;
    }

    //Click Enter on key board
    public void takeEnterKeys(String locator) {
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }

    //List of WebElements by ID
    public List<WebElement> getListOfWebElementsById(String locator) {
        List<WebElement> list = new ArrayList<>();
        list = driver.findElements(By.id(locator));
        return list;
    }

    //Text From WebElements
    public List<String> getTextFromWebElements(String locator){
        List<WebElement> element = new ArrayList<WebElement>();
        List<String> text = new ArrayList<String>();
        element = driver.findElements(By.cssSelector(locator));
        for(WebElement web:element){
            text.add(web.getText());
        }

        return text;
    }

    //List of WebElements by CSS
    public List<WebElement> getListOfWebElementsByCss(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.cssSelector(locator));
        return list;
    }
    //List of WebElements by xpath
    public List<WebElement> getListOfWebElementsByXpath(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.xpath(locator));
        return list;
    }
    //List of String
    public List<String> getListOfString(List<WebElement> list) {
        List<String> items = new ArrayList<String>();
        for (WebElement element : list) {
            items.add(element.getText());
        }
        return items;
    }

    //Current URL
    public String  getCurrentPageUrl(){
        String url = driver.getCurrentUrl();
        return url;
    }

    //Get title
    public String getTitle(){
        String title = driver.getTitle();
        return title;
    }

    //Navigate Back
    public void navigateBack(){
        driver.navigate().back();
    }

    //Navigate Forward
    public void navigateForward(){
        driver.navigate().forward();
    }

    //Get Text
    public String getTextByCss(String locator){
        String st = driver.findElement(By.cssSelector(locator)).getText();
        return st;
    }
    public String getTextByXpath(String locator){
        String st = driver.findElement(By.xpath(locator)).getText();
        return st;
    }
    public String getTextById(String locator){
        String st = driver.findElement(By.id(locator)).getText();
        return st;
    }
    public String getTextByName(String locator){
        String st = driver.findElement(By.name(locator)).getText();
        return st;
    }

    //Handling Static Dropdowns with Select
    public void SelectOptionByVisibleText(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }
    public void SelectOptionByIndex(WebElement element, int value) {
        Select select = new Select(element);
        select.selectByIndex(value);
    }
    public void SelectOptionByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    //Deselecting options
    public void DeSelectOptionByVisibleText(WebElement element, String value) {
        Select select = new Select(element);
        select.deselectByVisibleText(value);
    }
    public void DeSelectOptionByIndex(WebElement element, int value) {
        Select select = new Select(element);
        select.deselectByIndex(value);
    }
    public void DeSelectOptionByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.deselectByValue(value);
    }
    public void DeSelectAllOptions(WebElement element, String value) {
        Select select = new Select(element);
        select.deselectAll();
    }

    //Handling Dynamic Dropdowns
    public void SelectDropdowByXpath(String locator, String ValueLocator){
        driver.findElement(By.xpath(locator)).click();
        driver.findElement(By.xpath(ValueLocator)).click();
    }
    public void SelectDropdowByCssSelector(String locator, String ValueLocator){
        driver.findElement(By.cssSelector(locator)).click();
        driver.findElement(By.cssSelector(ValueLocator)).click();
    }
    public void SelectDropdowById(String locator, String ValueLocator){
        driver.findElement(By.id(locator)).click();
        driver.findElement(By.id(ValueLocator)).click();
    }
    public void SelectDropdowByClassName(String locator, String ValueLocator){
        driver.findElement(By.className(locator)).click();
        driver.findElement(By.className(ValueLocator)).click();
    }

    //Handling Checkboxes
    public void SelectCheckboxById(String locator) {
        driver.findElement(By.id(locator)).click();
    }
    public void CheckIfCheckboxSelectedById(String locator){
        driver.findElement(By.id(locator)).isSelected();
    }

    public void SelectCheckboxByXpath(String locator) {
        driver.findElement(By.xpath(locator)).click();
    }
    public void CheckIfCheckboxSelectedByXpath(String locator){
        driver.findElement(By.xpath(locator)).isSelected();
    }

    public void SelectCheckboxByCssSelector(String locator) {
        driver.findElement(By.cssSelector(locator)).click();
    }
    public void CheckIfCheckboxSelectedByCssSelector(String locator){
        driver.findElement(By.cssSelector(locator)).isSelected();
    }

    //Handling Radiobutton
    public void SelectRadiobuttonByXpath(String locator){
        driver.findElement(By.xpath(locator)).click();
    }
    public void SelectRadiobuttonByCssSelector(String locator){
        driver.findElement(By.cssSelector(locator)).click();
    }
    public void SelectRadiobuttonById(String locator){
        driver.findElement(By.id(locator)).click();
    }
    public void SelectRadiobuttonByClassName(String locator){
        driver.findElement(By.className(locator)).click();
    }

    //Get size of the Radiobutton
    public void SizeOfRadiobuttonByXpath(String locator){
        driver.findElements(By.xpath(locator)).size();
    }
    public void SizeOfRadiobuttonByCssSelector(String locator){
        driver.findElements(By.cssSelector(locator)).size();
    }
    public void SizeOfRadiobuttonById(String locator){
        driver.findElements(By.id(locator)).size();
    }
    public void SizeOfRadiobuttonByClassName(String locator){
        driver.findElements(By.className(locator)).size();
    }

    //Handling Radiobutton Dynamically
    public void SelectRadiobuttonDynamicallyByXpath(String locator, int indexValue){
        int count = driver.findElements(By.xpath(locator)).size();

        for (int i=0; i<count; i++){
            driver.findElements(By.xpath(locator)).get(indexValue).click();
        }
    }
    public void SelectRadiobuttonDynamicallyByCssSelector(String locator, int indexValue){
        int count = driver.findElements(By.cssSelector(locator)).size();

        for (int i=0; i<count; i++){
            driver.findElements(By.cssSelector(locator)).get(indexValue).click();
        }
    }
    public void SelectRadiobuttonDynamicallyById(String locator, int indexValue){
        int count = driver.findElements(By.id(locator)).size();

        for (int i=0; i<count; i++){
            driver.findElements(By.id(locator)).get(indexValue).click();
        }
    }
    public void SelectRadiobuttonDynamicallyByClassName(String locator, int indexValue){
        int count = driver.findElements(By.className(locator)).size();

        for (int i=0; i<count; i++){
            driver.findElements(By.className(locator)).get(indexValue).click();
        }
    }

    //Handling Radiobutton Dynamically with Text Values
    public void SelectRadiobuttonWithTextValueByXpath(String locator, String textValue){
        int count = driver.findElements(By.xpath(locator)).size();

        for (int i=0; i<count; i++){
            String text = driver.findElements(By.xpath(locator)).get(i).getAttribute("value");

            if (text.equals(textValue)){
                driver.findElements(By.xpath(locator)).get(i).click();
            }
        }
    }
    public void SelectRadiobuttonWithTextValueByCssSelector(String locator, String textValue){
        int count = driver.findElements(By.cssSelector(locator)).size();

        for (int i=0; i<count; i++){
            String text = driver.findElements(By.cssSelector(locator)).get(i).getAttribute("value");

            if (text.equals(textValue)){
                driver.findElements(By.cssSelector(locator)).get(i).click();
            }
        }
    }

    //Handling Java Alert
    public String getAlertText(){
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        return text;
    }
    public void okAlert(){
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
    public void cancelAlert(){
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }
    public void enterMessage(String message){
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(message);
    }

    //Handling multiple windows
    public void selectParentWindow(){
        Set<String>ids = driver.getWindowHandles();
        Iterator<String> it = ids.iterator();
        String parentID = it.next();
        String childID = it.next();
        driver.switchTo().window(parentID);
    }
    public void selectChildWindow(){
        Set<String>ids = driver.getWindowHandles();
        Iterator<String> it = ids.iterator();
        String parentID = it.next();
        String childID = it.next();
        driver.switchTo().window(childID);
    }

    //iFrame Handle
    public void selectFrameByCSS(String locator){
        driver.switchTo().frame(driver.findElement(By.cssSelector(locator)));
    }
    public void selectFrameByXpath(String locator){
        driver.switchTo().frame(driver.findElement(By.xpath(locator)));
    }
    public void selectFrameByID(String locator){
        driver.switchTo().frame(driver.findElement(By.id(locator)));
    }
    public void selectFrameByClassName(String locator){
        driver.switchTo().frame(driver.findElement(By.className(locator)));
    }
    public void iframeHandle(WebElement element){
        driver.switchTo().frame(element);
    }
    public void goBackToHomeWindow(){
        driver.switchTo().defaultContent();
    }

    //WebElements Visibility Validations (Present in the code but not visible)
    public void webElementVisibilityByXpath(String locator){
        driver.findElement(By.xpath(locator)).isDisplayed();
    }
    public void webElementVisibilityByCssSelector(String locator){
        driver.findElement(By.cssSelector(locator)).isDisplayed();
    }
    public void webElementVisibilityById(String locator){
        driver.findElement(By.id(locator)).isDisplayed();
    }
    public void webElementVisibilityByClassName(String locator){
        driver.findElement(By.className(locator)).isDisplayed();
    }

    //Validate the Object which is present in Web page or code base
    public void objectValidationByXpath(String locator){
        int count = driver.findElements(By.xpath(locator)).size();

        if (count==0){
            System.out.println("Varified");
        }
    }
    public void objectValidationByCssSelector(String locator){
        int count = driver.findElements(By.cssSelector(locator)).size();

        if (count==0){
            System.out.println("Varified");
        }
    }
    public void objectValidationById(String locator){
        int count = driver.findElements(By.id(locator)).size();

        if (count==0){
            System.out.println("Varified");
        }
    }
    public void objectValidationByClassName(String locator){
        int count = driver.findElements(By.className(locator)).size();

        if (count==0){
            System.out.println("Varified");
        }
    }

    //Validate if WebElements in the web page are enabled or disabled mode
    public void objectEnabilityByXpath(String locator){
        driver.findElement(By.xpath(locator)).isEnabled();
    }
    public void objectEnabilityByCssSelector(String locator){
        driver.findElement(By.cssSelector(locator)).isEnabled();
    }
    public void objectEnabilityById(String locator){
        driver.findElement(By.id(locator)).isEnabled();
    }
    public void objectEnabilityByClassName(String locator){
        driver.findElement(By.className(locator)).isEnabled();
    }

    //Synchronization
    public void waitTime(){
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    public void waitUntilClickAble(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public void waitUntilVisible(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public void waitUntilSelectable(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));
    }

    //Thread.sleep
    public void sleepFor(int sec)throws InterruptedException{
        Thread.sleep(sec * 1000);
    }

    //Mouse Hovering
    public void mouseHoverByCSS(String locator){
        try {
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
        }
    }
    public void mouseHoverByXpath(String locator){
        try {
            WebElement element = driver.findElement(By.xpath(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.xpath(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
        }
    }
    public void dragAndDrop(String sourceLocator, String targetLocator){
        Actions action = new Actions(driver);
        WebElement source = driver.findElement(By.id(sourceLocator));
        WebElement target = driver.findElement(By.id(targetLocator));

        action.dragAndDrop(source, target).build().perform();
    }

    public static void captureScreenshot(WebDriver driver, String screenshotName){

        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir")+ "/screenshots/"+screenshotName+" "+df.format(date)+".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot "+e.getMessage());;
        }

    }

    //Taking Screen shots
    public void takeScreenShot()throws IOException {
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File("screenShots.png"));
    }

    //Upload files
    public void upLoadFile(String locator,String path){
        driver.findElement(By.cssSelector(locator)).sendKeys(path);
        /* path example to upload a file/image
           path= "C:\\Users\\rrt\\Pictures\\ds1.png";
         */
    }

    //Assertions
    public void hardAssertINT(int actualINT, int expectedINT, String errorMessage){
        Assert.assertEquals(actualINT, expectedINT, errorMessage);
    }
    public void hardAssertString(String actualINT, String expectedINT, String errorMessage){
        Assert.assertEquals(actualINT, expectedINT, errorMessage);
    }

    //get Links
    public void getLinks(String locator){
        driver.findElement(By.linkText(locator)).findElement(By.tagName("a")).getText();
    }

    //Validate broken links
    public void validateBrokeLinks(){
        List<WebElement> links=driver.findElements(By.tagName("a"));
        System.out.println("Total links are "+links.size());

        for(int i=0;i<links.size();i++){
            WebElement ele= links.get(i);
            String url=ele.getAttribute("href");
            verifyLinkActive(url);
        }
    }
    public static void verifyLinkActive(String linkUrl){
        try{
            URL url = new URL(linkUrl);
            HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
            httpURLConnect.setConnectTimeout(3000);
            httpURLConnect.connect();

            if(httpURLConnect.getResponseCode()==200){
                System.out.println(linkUrl+" - "+httpURLConnect.getResponseMessage());
            }if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND){
                System.out.println(linkUrl+" - "+httpURLConnect.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
            }
        } catch (Exception e) {

        }
    }

    //Validate broken images
    private int invalidImageCount;
    public void validateInvalidImages(){
        try {
            invalidImageCount = 0;
            List<WebElement> imagesList = driver.findElements(By.tagName("img"));
            System.out.println("Total no. of images are " + imagesList.size());
            for (WebElement imgElement : imagesList) {
                if (imgElement != null) {
                    verifyimageActive(imgElement);
                }
            }
            System.out.println("Total no. of invalid images are "	+ invalidImageCount);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public void verifyimageActive(WebElement imgElement){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imgElement.getAttribute("src"));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200)
                invalidImageCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
