package webpages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class MainPage(private val driver: WebDriver) {
    @FindBy(xpath = "//ul/li[@class='justify']")
    lateinit var infoBlock: WebElement

    @FindBy(xpath = "//input[@name='username']")
    lateinit var loginInput: WebElement

    @FindBy(xpath = "//input[@name='password']")
    lateinit var passwordInput: WebElement

    init {
        driver.get("http://kinozal.tv/")
        PageFactory.initElements(driver, this)
    }
}