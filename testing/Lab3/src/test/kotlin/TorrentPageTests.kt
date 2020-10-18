import org.junit.jupiter.api.AfterEach
import webpages.*
import utils.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.openqa.selenium.WebDriver
import java.util.stream.IntStream

class TorrentPageTests {

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    @CustomCsvProvider("sort_by_size.csv")
    fun `sort by size tests`(driver: WebDriver, sortBy: SortBy, sortType: SortType,
                             _f: FilterBy, _s: Status, _sMin: Double, _sMax: Double) {
        val torrentPage = TorrentPage(driver)

        torrentPage.sortBySelect.selectByIndex(TorrentUtils.getSelectIndexBySortBy(sortBy))
        torrentPage.sortModeSelect.selectByIndex(TorrentUtils.getSelectIndexBySortType(sortType))
        torrentPage.searchButton.click()

        val torrents = torrentPage.getResults()
        assertFalse(torrents.isEmpty(), "result list cannot be empty")

        when (sortType) {
            SortType.DESC -> assertTrue(
                IntStream.range(0, torrents.size - 1).noneMatch { torrents[it].sizeMb < torrents[it + 1].sizeMb })
            else -> assertTrue(
                IntStream.range(0, torrents.size - 1).noneMatch { torrents[it].sizeMb > torrents[it + 1].sizeMb })
        }

        driver.quit()
    }
}