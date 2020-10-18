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

    data class DateFeatures (
        var date: Long,
        var changedToday: Boolean,
        var changedYesterday: Boolean,
        var changedLastThreeDays: Boolean,
        var changedLastWeek: Boolean,
        var changedLastMonth: Boolean
    )

    fun getResults(): Array<TorrentInfo> {
        val qResults = driver.findElements(By.xpath(queryResultXPath))
        return qResults.map {
            val dateFeatures = convertStringTimeToDateFeatures(it.findElement(By.xpath("(.//td[@class='s'])[3]")).text)
            TorrentInfo(
                name = it.findElement(By.xpath(".//td[@class='nam']")).text,
                sizeKb = convertSizeToKb(it.findElement(By.xpath("(.//td[@class='s'])[2]")).text),
                comments = it.findElement(By.xpath("(.//td[@class='s'])[1]")).text.toLong(),
                peers = it.findElement(By.xpath("(.//td[@class='sl_p'])")).text.toLong(),
                seeds = it.findElement(By.xpath("(.//td[@class='sl_s'])")).text.toLong(),
                status = getStatus(it.findElement(By.xpath(".//td[@class='nam']/a"))),
                date = dateFeatures.date,
                changedToday = dateFeatures.changedToday,
                changedYesterday = dateFeatures.changedYesterday,
                changerLastThreeDays = dateFeatures.changedLastThreeDays,
                changedLastWeek = dateFeatures.changedLastWeek,
                changedLastMonth = dateFeatures.changedLastMonth
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

    private fun convertStringTimeToDateFeatures(datetime: String): DateFeatures {
        val year: Long
        val month: Long
        val day: Long
        var h: Long = 0
        var m: Long = 0

        var changedToday = false
        var changedYesterday = false
        var changedLastThreeDays = false
        var changedLastWeek = false
        var changedLastMonth = false
        var now = false

        val today = Date()
        val yearT = SimpleDateFormat("YYYY").format(today).toLong()
        val monthT = SimpleDateFormat("MM").format(today).toLong()
        val dayT = SimpleDateFormat("dd").format(today).toLong()
        val hourT = SimpleDateFormat("HH").format(today).toLong()
        val minT = SimpleDateFormat("mm").format(today).toLong()
        val absTimeT = getAbsMinutes(yearT, monthT, dayT, hourT, minT)

        when {
            datetime.contains("сейчас") -> {
                year = yearT
                month = monthT
                day = dayT
                h = hourT
                m = minT
                changedToday = true
                now = true
            }
            datetime.contains("сегодня в") -> {
                year = yearT
                month = monthT
                day = dayT
                changedToday = true
            }
            datetime.contains("вчера в") -> {
                val yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS))
                year = SimpleDateFormat("YYYY").format(yesterday).toLong()
                month = SimpleDateFormat("MM").format(yesterday).toLong()
                day = SimpleDateFormat("dd").format(yesterday).toLong()
                changedYesterday = true
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
        if (!now) {
            h = time[0].toLong()
            m = time[1].toLong()
        }
        val absTime = getAbsMinutes(year, month, day, h, m)
        changedLastThreeDays = absTimeT - absTime <= 4320
        changedLastWeek = absTimeT - absTime <= 10080
        changedLastMonth = absTimeT - absTime <= 44640
        return DateFeatures(absTime, changedToday, changedYesterday, changedLastThreeDays, changedLastWeek, changedLastMonth)
    }

    private fun getAbsMinutes(y: Long, m: Long, d: Long, h: Long, mn: Long) =
        (y - 1970) * 525600 + m * 43800 + d * 1440 + h * 60 + m

    private fun getStatus(nameLink: WebElement): Status {
        val classes = nameLink.getAttribute("class").split(" ")
        return when {
            classes.contains("r1") -> Status.GOLD
            classes.contains("r2") -> Status.SILVER
            else -> Status.NONE
        }
    }
}