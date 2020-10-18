package webpages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait

data class TorrentInfo (
    var name: String,
    var sizeMb: Double,
    var seeds: Int,
    var peers: Int,
    var status: Status,
    var comments: Int
)

enum class SortBy {
    SIZE, SEED, PEER, COMM, NONE
}

enum class SortType {
    ASC, DESC, NONE
}

enum class FilterBy {
    SIZE_LESS_13,
    SIZE_13_22,
    SIZE_22_40,
    SIZE_40_95,
    SIZE_MORE_95,
    STATUS_GOLD,
    STATUS_SILVER,
    DATE_TODAY,
    DATE_YESTERDAY,
    DATE_LAST_3_DAYS,
    DATE_LAST_WEEK,
    DATE_LAST_MONTH
}

enum class Status {
    GOLD, SILVER, NONE
}

class TorrentPage(private val driver: WebDriver) {

}