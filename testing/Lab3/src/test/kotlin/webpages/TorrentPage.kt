package webpages

import utils.*

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.pow
import kotlin.time.days

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
                comments = it.findElement(By.xpath("(.//td[@class='s'])[1]")).text.toInt(),
                peers = it.findElement(By.xpath("(.//td[@class='sl_p'])")).text.toInt(),
                seeds = it.findElement(By.xpath("(.//td[@class='sl_s'])")).text.toInt(),
                // todo: implement
                status = Status.NONE,
                date = convertStringTimeToMinutes(it.findElement(By.xpath("(.//td[@class='s'])[3]")).text)
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

    private fun convertStringTimeToMinutes(datetime: String): Int {
        val year: Int
        val month: Int
        val day: Int

        when {
            datetime.contains("сегодня в") -> {
                val today = Date()
                year = SimpleDateFormat("YYYY").format(today).toInt()
                month = SimpleDateFormat("MM").format(today).toInt()
                day = SimpleDateFormat("dd").format(today).toInt()
            }
            datetime.contains("вчера в") -> {
                val yesterday = Instant.now().minus(1, ChronoUnit.DAYS)
                year = SimpleDateFormat("YYYY").format(yesterday).toInt()
                month = SimpleDateFormat("MM").format(yesterday).toInt()
                day = SimpleDateFormat("dd").format(yesterday).toInt()
            }
            else -> {
                val date = SimpleDateFormat("dd.MM.YYYY в HH:mm")
                year = SimpleDateFormat("YYYY").format(date).toInt()
                month = SimpleDateFormat("MM").format(date).toInt()
                day = SimpleDateFormat("dd").format(date).toInt()
            }
        }

        val time = datetime.takeLast(5).split(':')
        val h = time[0].toInt()
        val m = time[1].toInt()
        return (year - 1970) * 525600 + month * 43800 + day * 1440 + h * 60 + m
    }

    init {
        driver.get("http://kinozal.tv/browse.php")
        PageFactory.initElements(driver, this)

        sortBySelect = Select(sortBySelectElement)
        sortModeSelect = Select(sortModeSelectElement)
    }
}