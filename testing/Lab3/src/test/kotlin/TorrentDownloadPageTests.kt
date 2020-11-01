import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import utils.Credentials
import utils.TorrentUtils
import webpages.TorrentDownloadPage
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest


class TorrentDownloadPageTests {

    @Test
    fun `download torrent test`() {
        val foxDriver = FirefoxDriver()
        var torrentDownloadPage = TorrentDownloadPage(foxDriver)
        val link = torrentDownloadPage.downloadButton.getAttribute("href")
        foxDriver.quit()

        val chromeDriver = ChromeDriver()
        torrentDownloadPage = TorrentDownloadPage(chromeDriver)
        val chromeLink = torrentDownloadPage.downloadButton.getAttribute("href")
        chromeDriver.quit()

        assertEquals(chromeLink, link)

        val `in`: InputStream = URL(link).openStream()
        Files.copy(`in`, Paths.get("${Credentials.downloadDirectory}\\actual.torrent"), StandardCopyOption.REPLACE_EXISTING)

        val file = File("${Credentials.downloadDirectory}\\actual.torrent")
        val md5Digest = MessageDigest.getInstance("MD5")
        val checksum = TorrentUtils.getFileChecksum(md5Digest, file)

        assertEquals("ef77c2e4c603f1555f27c5500", checksum)

        file.delete()
    }
}