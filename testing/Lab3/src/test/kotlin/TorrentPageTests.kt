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
    @CustomCsvProvider("sort.csv")
    fun `sort tests`(driver: WebDriver, sortBy: SortBy, sortType: SortType,
                             _f: FilterBy, _s: Status, _sMin: Int, _sMax: Int) {
        val torrentPage = TorrentPage(driver)

        torrentPage.sortBySelect.selectByIndex(TorrentUtils.getSelectIndexBySortBy(sortBy))
        torrentPage.sortModeSelect.selectByIndex(TorrentUtils.getSelectIndexBySortType(sortType))
        torrentPage.searchButton.click()

        val torrents = torrentPage.getResults()
        assertFalse(torrents.isEmpty(), "result list cannot be empty")

        when (sortType) {
            SortType.DESC -> assertTrue(
                IntStream.range(0, torrents.size - 1).allMatch {
                    torrents[it].getParameterBySortBy(sortBy) >= torrents[it + 1].getParameterBySortBy(sortBy)
                }
            )
            else -> assertTrue(
                IntStream.range(0, torrents.size - 1).allMatch {
                    torrents[it].getParameterBySortBy(sortBy) <= torrents[it + 1].getParameterBySortBy(sortBy)
                }
            )
        }

        driver.quit()
    }

    @ParameterizedTest
    @ArgumentsSource(DriverProvider::class)
    @CustomCsvProvider("filter.csv")
    fun `filter tests`(driver: WebDriver, _sB: SortBy, _sT: SortType,
                     filterBy: FilterBy, status: Status, sizeMin: Long, sizeMax: Long) {
        val torrentPage = TorrentPage(driver)

        torrentPage.filterBySelect.selectByIndex(TorrentUtils.getSelectIndexByFilterBy(filterBy))
        torrentPage.searchButton.click()

        val torrents = torrentPage.getResults()
        assertFalse(torrents.isEmpty(), "result list cannot be empty")

        when (filterBy) {
            FilterBy.SIZE_LESS_13, FilterBy.SIZE_13_22, FilterBy.SIZE_22_40,
                FilterBy.SIZE_40_95, FilterBy.SIZE_MORE_95 -> assertTrue(
                    torrents.all { it.sizeKb >= sizeMin && (it.sizeKb <= sizeMax || sizeMax == 0.toLong()) },
                    "expected sizes to be between $sizeMin and $sizeMax, but there is " +
                            "${torrents.find { it.sizeKb < sizeMin || it.sizeKb > sizeMax }?.sizeKb}Kb torrent."
                )
            else -> assertTrue(true)
        }

        driver.quit()
    }
}