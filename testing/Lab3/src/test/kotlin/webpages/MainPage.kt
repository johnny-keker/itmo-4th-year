package webpages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait

class MainPage(private val driver: WebDriver) {
    private val queryResultNamesXPath =
        "//table[@class='t_peer w100p']/tbody/tr[@class='first bg' or @class='bg']/td[@class='nam']"

    @FindBy(xpath = "//ul/li[@class='justify']")
    lateinit var infoBlock: WebElement

    @FindBy(xpath = "//input[@name='username']")
    lateinit var loginInput: WebElement

    @FindBy(xpath = "//input[@name='password']")
    lateinit var passwordInput: WebElement

    @FindBy(xpath = "//input[@value='Вход']")
    lateinit var loginButton: WebElement

    @FindBy(xpath = "//li[@class='tp2 center b']/a[@class='u0']")
    lateinit var username: WebElement

    @FindBy(xpath = "")
    lateinit var queryResult: WebElement

    @FindBy(xpath = "//input[@name='s']")
    lateinit var searchInput: WebElement

    @FindBy(xpath = "//input[@class='s_submit']")
    lateinit var searchButton: WebElement

    @FindBy(xpath = "(//div[@class='bx1']/div[@class='red'])[1]")
    lateinit var invalidPasswordError: WebElement

    @FindBy(xpath = "//a[@class='sbab' and @href='']")
    lateinit var showRatingButton: WebElement

    @FindBy(xpath = "//td[@class='f10 right green']")
    lateinit var ratingInfo: WebElement

    init {
        driver.get("http://kinozal.tv/")
        PageFactory.initElements(driver, this)
    }

    fun login(login: String, password: String) {
        loginInput.sendKeys(login)
        passwordInput.sendKeys(password)
        loginButton.click()

        WebDriverWait(driver, 5).until(or(
            visibilityOf(username),
            visibilityOf(invalidPasswordError)
        ))
    }

    fun validateSearchResult(query: String, expectedResultFilePath: String): Boolean {
        val expected = MainPage::class.java.getResource("/$expectedResultFilePath").readText().split("\r\n")

        searchInput.sendKeys(query)
        searchButton.click()

        val actualResult = driver.findElements(By.xpath(queryResultNamesXPath))
        return actualResult.map { it.text }.containsAll(expected)
    }
}