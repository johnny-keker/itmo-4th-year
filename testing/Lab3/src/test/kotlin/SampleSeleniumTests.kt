import webpages.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver

class SampleSeleniumTests {
    @Test
    fun `sample fox test`() {
        val fireDriver = FirefoxDriver()
        val mainPage = MainPage(fireDriver)
        assertTrue(mainPage.infoBlock.text.contains(
            "На сайте представлено невероятное количество классических и современных кинолент мирового и отечественного кинематографа")
        )
    }

    @Test
    fun `sample chrome test`() {
        val chromeDriver = ChromeDriver()
        val mainPage = MainPage(chromeDriver)
        assertTrue(mainPage.infoBlock.text.contains(
            "На сайте представлено невероятное количество классических и современных кинолент мирового и отечественного кинематографа")
        )
    }
}