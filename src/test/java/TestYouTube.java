import io.qameta.allure.Step;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestYouTube {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp(){
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--disable-notifications");
        driver = new ChromeDriver(option);
        wait = new WebDriverWait(driver,50);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Step("1.Перейдите на сайт https://www.youtube.com/")
    public void step1(){
        driver.get("https://www.youtube.com/");
    }

    @Step("2.Выполните авторизацию на сервисе")
    public void step2() throws InterruptedException {
        driver.findElement(By.xpath("//*[text()='Войти']")).click();
        driver.findElement(By.cssSelector("input[type = email]")).sendKeys("ваша почта гугл");
        driver.findElement(By.id("identifierNext")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("input[type = password]")).sendKeys("ваш пароль");
        driver.findElement(By.id("passwordNext")).click();
    }

    @Step("3.Нажмите на кнопку «Добавить видео»")
    public void step3(){
        driver.findElement(By.cssSelector("[aria-label = 'Создать видео или запись']")).click();
        driver.findElement(By.xpath("//*[text() = 'Добавить видео']")).click();

    }

    @Step("4.В открывшейся форме добавьте  медиа файл для загрузки")
    public void step4(){
        driver.findElement(By.cssSelector("[id = upload-prompt-box] input[type = file]")).sendKeys("C:\\Users\\Kambal\\Desktop\\выфафыаыфсчяпв.mp4");
    }

    @Step("5.Дождитесь пока медиа файл загрузится и обработается на сервисе")
    public void step5(){
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.className("upload-status-text")), "innerText", "Загрузка завершена!"));
    }

    @Step("6.Добавьте необходимое описание и tags (в дальнейшем по ним необходимо будет выполнить поиск)")
    public void step6(){
        driver.findElement(By.cssSelector("textarea[name = description]")).sendKeys("Проверяем описание теста");
        //driver.findElement(By.className("video-settings-add-tag")).sendKeys("Проверка");
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.className("save-error-message")), "innerText", "Предварительная версия сохранена."));
    }

    @Step("7.Опубликуйте медиа")
    public void step7(){
        driver.findElement(By.className("save-cancel-buttons")).click();
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.className("save-error-message")), "innerText", "Все изменения сохранены."));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[text() = 'Вернуться к редактированию']"))));
    }

    @Step("8.Перейдите на главную страницу сервиса")
    public void step8(){
        driver.get("https://www.youtube.com/");
    }

    @Step("9.Выполните поиск по параметрам загруженного ранее видео")
    public void step9(){
        driver.findElement(By.cssSelector("input[id = search]")).sendKeys("выфафыаыфсчяпв");
        driver.findElement(By.cssSelector("input[id = search]")).submit();
    }

    @Step("10.Проверьте, что в результатах поиска есть видео, загруженное вами")
    public void step10(){
        Assert.assertEquals("выфафыаыфсчяпв", driver.findElement(By.id("video-title")).getText());
        Assert.assertEquals("Проверяем описание теста", driver.findElement(By.id("description-text")).getText());
    }

    @Test
    public void YTtest() throws InterruptedException {
        step1();
        step2();
        step3();
        step4();
        step5();
        step6();
        step7();
        step8();
        step9();
        step10();
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
