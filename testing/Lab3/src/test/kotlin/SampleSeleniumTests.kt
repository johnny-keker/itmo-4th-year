import webpages.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait

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
    fun `positive authorization test`(driver: WebDriver) {
        val mainPage = MainPage(driver)

        mainPage.login(Credentials.login, Credentials.password)
        assertEquals(Credentials.login, mainPage.username.text)

        driver.quit()
    }

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `negative authorization test`(driver: WebDriver) {
        val mainPage = MainPage(driver)

        mainPage.login(Credentials.login, Credentials.wrongPassword)
        assertEquals("Неверно указан пароль для имени « ${Credentials.login} »",
            mainPage.invalidPasswordError.text)

        driver.quit()
    }

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `check rating test`(driver: WebDriver) {
        val mainPage = MainPage(driver)

        mainPage.login(Credentials.login, Credentials.password)
        mainPage.showRatingButton.click()

        WebDriverWait(driver, 2).until(visibilityOf(mainPage.ratingInfo))
        assertEquals("Рейтинг: ---\nЗалил: 0 КБ\nСкачал: 0 КБ", mainPage.ratingInfo.text)

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

        driver.quit()
    }
}