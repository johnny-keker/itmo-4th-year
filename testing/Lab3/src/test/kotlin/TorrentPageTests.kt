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
                             _f: FilterBy, _s: Status, _sMin: Long, _sMax: Long) {
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
            FilterBy.STATUS_GOLD -> assertTrue(torrents.all { it.status == Status.GOLD })
            FilterBy.STATUS_SILVER -> assertTrue(torrents.all { it.status == Status.SILVER })
            FilterBy.DATE_TODAY -> assertTrue(torrents.all { it.changedToday })
            FilterBy.DATE_YESTERDAY -> assertTrue(torrents.all { it.changedYesterday })
            FilterBy.DATE_LAST_3_DAYS -> assertTrue(torrents.all { it.changerLastThreeDays })
            FilterBy.DATE_LAST_WEEK -> assertTrue(torrents.all { it.changedLastWeek })
            FilterBy.DATE_LAST_MONTH -> assertTrue(torrents.all { it.changedLastMonth })
            else -> fail("unknown filter - $filterBy")
        }

        driver.quit()
    }
}