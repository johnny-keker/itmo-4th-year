import webpages.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class SampleSeleniumTests {
    private val queryResultsXPath = "(//table[@class='t_peer w100p']/tbody/tr[@class='first bg' or @class='bg']/td[@class='nam'])"
    private val sampleQueryResults = arrayOf(
        "Black Sabbath - Paranoid / Rock / 2009 / MP3",
        "Classic Albums - Black Sabbath - Paranoid / 2010 / СТ / BDRip (720p)",
        "Black Sabbath - Paranoid (Deluxe Edition, 2CD) (1970) / Rock / 2016 / MP3",
        "Black Sabbath - Paranoid (Deluxe Edition) (1970) / Rock / 2016 / FLAC / Lossless",
        "Black Sabbath - Paranoid In The 70's / 2007 / БП / DVD-5"
    )

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `sample test`(driver: WebDriver) {
        val mainPage = MainPage(driver)
        assertTrue(mainPage.infoBlock.text.contains(
            "На сайте представлено невероятное количество классических и современных кинолент мирового и отечественного кинематографа")
        )
        driver.quit()
    }

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `authorization test`(driver: WebDriver) {
        val mainPage = MainPage(driver)
        assertTrue(mainPage.loginInput.isDisplayed)
        assertTrue(mainPage.passwordInput.isDisplayed)
        assertTrue(mainPage.loginButton.isDisplayed)

        mainPage.loginInput.sendKeys(Credentials.login)
        mainPage.passwordInput.sendKeys(Credentials.password)
        mainPage.loginButton.click()

        assertTrue(mainPage.username.isDisplayed)
        assertEquals(Credentials.login, mainPage.username.text)

        driver.quit()
    }

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `sample query test`(driver: WebDriver) {
        val mainPage = MainPage(driver)
        assertTrue(mainPage.searchInput.isDisplayed)
        assertTrue(mainPage.searchButton.isDisplayed)

        mainPage.searchInput.sendKeys("black sabbath paranoid")
        mainPage.searchButton.click()

        for (i in 0..4) {
            val queryRes = driver.findElement(By.xpath("$queryResultsXPath[${i+1}]")).text
            assertEquals(sampleQueryResults[i], queryRes)
        }
    }
}