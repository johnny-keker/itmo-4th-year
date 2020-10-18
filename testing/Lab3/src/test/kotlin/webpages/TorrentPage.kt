package webpages

import utils.*

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.pow

class TorrentPage(private val driver: WebDriver) {
    private val queryResultXPath = "(//table[@class='t_peer w100p']/tbody/tr[@class='first bg' or @class='bg'])"

    @FindBy(xpath = "//select[@name='t']")
    lateinit var sortBySelectElement: WebElement

    @FindBy(xpath = "//select[@name='f']")
    lateinit var sortModeSelectElement: WebElement

    var sortBySelect: Select
    var sortModeSelect: Select

    @FindBy(xpath = "//select[@name='w']")
    lateinit var filterBySelectElement: WebElement

    var filterBySelect: Select

    @FindBy(xpath = "//input[@class='buttonS w98p']")
    lateinit var searchButton: WebElement

    init {
        driver.get("http://kinozal.tv/browse.php")
        PageFactory.initElements(driver, this)

        sortBySelect = Select(sortBySelectElement)
        sortModeSelect = Select(sortModeSelectElement)
        filterBySelect = Select(filterBySelectElement)
    }

    fun getResults(): Array<TorrentInfo> {
        val qResults = driver.findElements(By.xpath(queryResultXPath))
        return qResults.map {
            TorrentInfo(
                name = it.findElement(By.xpath(".//td[@class='nam']")).text,
                sizeKb = convertSizeToKb(it.findElement(By.xpath("(.//td[@class='s'])[2]")).text),
                comments = it.findElement(By.xpath("(.//td[@class='s'])[1]")).text.toLong(),
                peers = it.findElement(By.xpath("(.//td[@class='sl_p'])")).text.toLong(),
                seeds = it.findElement(By.xpath("(.//td[@class='sl_s'])")).text.toLong(),
                status = getStatus(it.findElement(By.xpath(".//td[@class='nam']/a"))),
                date = convertStringTimeToMinutes(it.findElement(By.xpath("(.//td[@class='s'])[3]")).text)
            )
        }.toTypedArray()
    }

    private fun convertSizeToKb(size: String): Long {
        val ext = size.takeLast(2)
        val value = size.filter { it.isDigit() || it == '.' }.toDouble()

        return when (ext) {
            "МБ" -> (value * 1000).toLong()
            "ГБ" -> (value * 10.0.pow(9.0)).toLong()
            "ТБ" -> (value * 10.0.pow(12.0)).toLong()
            else -> value.toLong()
        }
    }

    private fun convertStringTimeToMinutes(datetime: String): Long {
        val year: Long
        val month: Long
        val day: Long

        when {
            datetime.contains("сегодня в") -> {
                val today = Date()
                year = SimpleDateFormat("YYYY").format(today).toLong()
                month = SimpleDateFormat("MM").format(today).toLong()
                day = SimpleDateFormat("dd").format(today).toLong()
            }
            datetime.contains("вчера в") -> {
                val yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS))
                year = SimpleDateFormat("YYYY").format(yesterday).toLong()
                month = SimpleDateFormat("MM").format(yesterday).toLong()
                day = SimpleDateFormat("dd").format(yesterday).toLong()
            }
            else -> {
                val regex = """(\d+)\.(\d+)\.(\d+) в \d+:\d+""".toRegex()
                val matchResult = regex.find(datetime)
                year = matchResult!!.destructured.component3().toLong()
                month = matchResult.destructured.component2().toLong()
                day = matchResult.destructured.component1().toLong()
            }
        }

        val time = datetime.takeLast(5).split(':')
        val h = time[0].toInt()
        val m = time[1].toInt()
        return (year - 1970) * 525600 + month * 43800 + day * 1440 + h * 60 + m
    }

    private fun getStatus(nameLink: WebElement): Status {
        val classes = nameLink.getAttribute("class").split(" ")
        return when {
            classes.contains("r1") -> Status.GOLD
            classes.contains("r2") -> Status.SILVER
            else -> Status.NONE
        }
    }
}