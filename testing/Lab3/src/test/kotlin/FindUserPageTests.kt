import webpages.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.WebDriver
import utils.Credentials
import utils.DriverProvider

class FindUserPageTests {

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    fun `find user test`(driver: WebDriver) {
        val findUserPage = FindUserPage(driver)

        findUserPage.usernameField.sendKeys(Credentials.login)
        findUserPage.findUserButton.click()

        assertEquals(Credentials.login, findUserPage.foundUserName.text)

        driver.quit()
    }
}