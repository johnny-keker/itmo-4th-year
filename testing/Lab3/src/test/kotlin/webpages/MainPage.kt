package webpages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class MainPage(private val driver: WebDriver) {
    @FindBy(xpath = "//ul/li[@class='justify']")
    lateinit var infoBlock: WebElement

    init {
        driver.get("http://kinozal.tv/")
        PageFactory.initElements(driver, this)
    }
}