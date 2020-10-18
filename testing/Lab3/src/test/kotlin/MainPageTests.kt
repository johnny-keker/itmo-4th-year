import webpages.*
import utils.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait

class MainPageTests {

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

        assertTrue(mainPage.validateSearchResult("black sabbath paranoid", "sampleQueryResults.txt"))

        driver.quit()
    }
}