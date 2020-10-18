import webpages.*
import utils.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.CsvFileSource
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait

class TorrentPageTests {
    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    @CustomCsvProvider("test")
    fun sample(driver: WebDriver, path: String) {
        assertEquals("test", path)
    }

}