import webpages.*
import utils.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.CsvFileSource
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.stream.IntStream

class TorrentPageTests {
    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    //@CustomCsvProvider("test")
    fun sample(driver: WebDriver/*, path: String*/) {
        val torrentPage = TorrentPage(driver)

        torrentPage.sortBySelect.selectByIndex(3)   // size
        torrentPage.sortModeSelect.selectByIndex(0) // desc
        torrentPage.searchButton.click()

        val torrents = torrentPage.getResults()
        assertTrue(IntStream.range(0, torrents.size - 1).noneMatch { torrents[it].sizeMb < torrents[it + 1].sizeMb })

        driver.quit()
    }

}