package webpages

import utils.*

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select
import kotlin.math.pow

class TorrentPage(private val driver: WebDriver) {
    private val queryResultXPath = "(//table[@class='t_peer w100p']/tbody/tr[@class='first bg' or @class='bg'])"

    @FindBy(xpath = "//select[@name='t']")
    lateinit var sortBySelectElement: WebElement

    @FindBy(xpath = "//select[@name='f']")
    lateinit var sortModeSelectElement: WebElement

    var sortBySelect: Select
    var sortModeSelect: Select

    @FindBy(xpath = "//input[@class='buttonS w98p']")
    lateinit var searchButton: WebElement

    fun getResults(): Array<TorrentInfo> {
        val qResults = driver.findElements(By.xpath(queryResultXPath))
        return qResults.map {
            TorrentInfo(
                name = it.findElement(By.xpath(".//td[@class='nam']")).text,
                sizeKb = convertSizeToKb(it.findElement(By.xpath("(.//td[@class='s'])[2]")).text),
                // todo: implement
                comments = 0,
                peers = 0,
                seeds = 0,
                status = Status.NONE,
                date = 0
            )
        }.toTypedArray()
    }

    private fun convertSizeToKb(size: String): Int {
        val ext = size.takeLast(2)
        val value = size.filter { it.isDigit() || it == '.' }.toDouble()

        return when (ext) {
            "МБ" -> (value * 1000).toInt()
            "ГБ" -> (value * 10.0.pow(9.0)).toInt()
            "ТБ" -> (value * 10.0.pow(12.0)).toInt()
            else -> value.toInt()
        }
    }

    init {
        driver.get("http://kinozal.tv/browse.php")
        PageFactory.initElements(driver, this)

        sortBySelect = Select(sortBySelectElement)
        sortModeSelect = Select(sortModeSelectElement)
    }
}