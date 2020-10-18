package webpages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait
import utils.Credentials

class FindUserPage(private val driver: WebDriver) {

    @FindBy(xpath = "//input[@name='username']")
    lateinit var loginInput: WebElement

    @FindBy(xpath = "//input[@name='password']")
    lateinit var passwordInput: WebElement

    @FindBy(xpath = "//input[@class='buttonS']")
    lateinit var loginButton: WebElement

    @FindBy(xpath = "//li[@class='tp2 center b']/a[@class='u0']")
    lateinit var username: WebElement

    @FindBy(xpath = "//input[@class='w120' and @name='s1']")
    lateinit var usernameField: WebElement

    @FindBy(xpath = "//input[@class='w200'][1]")
    lateinit var findUserButton: WebElement

    @FindBy(xpath = "//div[@class='ptable']/ul/li/a[@class='u0']")
    lateinit var foundUserName: WebElement

    init {
        driver.get("http://kinozal.tv/users.php")
        PageFactory.initElements(driver, this)

        loginInput.sendKeys(Credentials.login)
        passwordInput.sendKeys(Credentials.password)
        loginButton.click()

        WebDriverWait(driver, 5).until(visibilityOf(username))
    }
}