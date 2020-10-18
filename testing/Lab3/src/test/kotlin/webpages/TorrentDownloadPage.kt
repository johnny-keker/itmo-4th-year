package webpages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import utils.Credentials

class TorrentDownloadPage(private val driver: WebDriver) {

    @FindBy(xpath = "//td[@class='nw']/a")
    lateinit var downloadButton: WebElement

    @FindBy(xpath = "//input[@name='username']")
    lateinit var loginInput: WebElement

    @FindBy(xpath = "//input[@name='password']")
    lateinit var passwordInput: WebElement

    @FindBy(xpath = "//input[@value='Вход']")
    lateinit var loginButton: WebElement

    @FindBy(xpath = "//li[@class='tp2 center b']/a[@class='u0']")
    lateinit var username: WebElement

    init {
        driver.get("http://kinozal.tv/details.php?id=293488")
        PageFactory.initElements(driver, this)

        loginInput.sendKeys(Credentials.login)
        passwordInput.sendKeys(Credentials.password)
        loginButton.click()

        WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(username))
    }
}