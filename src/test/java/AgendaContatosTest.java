import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgendaContatosTest {
    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:5500/");
        driver.manage().window().maximize();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    @Order(1)
    void should_enter_website_and_create_new_user() {
        driver.findElement(By.cssSelector("#p-login > p > a")).click();
        WebElement email = driver.findElement(By.name("email"));
        WebElement senha = driver.findElement(By.name("senha"));
        WebElement nome = driver.findElement(By.name("nome"));
        WebElement btnCadastrar = driver.findElement(By.cssSelector("#p-signup > button"));
        email.sendKeys("samir@gmail.com");
        senha.sendKeys("123");
        nome.sendKeys("Samir Hamade Rocha");
        btnCadastrar.submit();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertThat(driver.getCurrentUrl()).isEqualTo("http://localhost:5500/#login");
        assertThat(driver.findElement(By.cssSelector("#p-login > button")).isDisplayed());
    }

    @Test
    @Order(2)
    void should_validate_if_email_is_already_registered() {
        driver.findElement(By.cssSelector("#p-login > p > a")).click();
        WebElement email = driver.findElement(By.name("email"));
        WebElement senha = driver.findElement(By.name("senha"));
        WebElement nome = driver.findElement(By.name("nome"));
        WebElement btnCadastrar = driver.findElement(By.cssSelector("#p-signup > button"));
        email.sendKeys("samir@gmail.com");
        senha.sendKeys("123456");
        nome.sendKeys("Fulano de Tal");
        btnCadastrar.submit();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertThat(driver.findElement(By.cssSelector("#p-signup > span")).getText()).contains("E-mail já cadastrado");
        assertThat(driver.getCurrentUrl()).endsWith("#signup");
    }

    @Test
    @Order(3)
    void should_be_able_to_login() {
        WebElement btnLogin = driver.findElement(By.cssSelector("#p-login > button"));
        WebElement email = driver.findElement(By.name("email"));
        WebElement senha = driver.findElement(By.name("senha"));
        email.sendKeys("samir@gmail.com");
        senha.sendKeys("123");
        btnLogin.submit();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertThat(driver.findElement(By.id("btn-sair")).isDisplayed());
        assertThat(driver.getCurrentUrl()).endsWith("#contacts");
    }

    @Test
    @Order(4)
    void should_be_able_to_add_contact_after_login() {
        driver.findElement(By.name("email")).sendKeys("samir@gmail.com");
        driver.findElement(By.name("senha")).sendKeys("123");
        driver.findElement(By.cssSelector("#p-login > button")).submit();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.findElement(By.id("add-contact")).click();
//        driver.findElement(By.linkText("Adicionar contato")).click();
        WebElement nome = driver.findElement(By.name("nome"));
        nome.sendKeys("William Henry Gates III");
        driver.findElement(By.name("apelido")).sendKeys("Bill");
        driver.findElement(By.name("email")).sendKeys("bill@gates.com");
        driver.findElement(By.name("notas")).sendKeys("Fundador da Microsoft");
        driver.findElement(By.name("logradouro")).sendKeys("Vale do Silício");
        driver.findElement(By.name("email")).sendKeys("bill@gates.com");
        WebElement selectElement = driver.findElement(By.name("tipo-telefone-1"));
        Select select = new Select(selectElement);
        select.selectByValue("celular");
        driver.findElement(By.name("numero-1")).sendKeys("9988776622");
        driver.findElement(By.cssSelector("#p-add-contact > button")).submit();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        boolean contact = driver.getPageSource().contains("William Henry Gates III");
        Assertions.assertTrue(contact);
    }
}
