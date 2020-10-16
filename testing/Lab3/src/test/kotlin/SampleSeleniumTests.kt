import webpages.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.WebDriver

class SampleSeleniumTests {
    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `sample test`(driver: WebDriver) {
        val mainPage = MainPage(driver)
        assertTrue(mainPage.infoBlock.text.contains(
            "На сайте представлено невероятное количество классических и современных кинолент мирового и отечественного кинематографа")
        )
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
    }
}