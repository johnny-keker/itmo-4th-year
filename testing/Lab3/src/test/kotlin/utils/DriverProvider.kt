package utils

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.util.stream.Stream

class DriverProvider: ArgumentsProvider {
    private val fireDriver = FirefoxDriver()
    private val chromeDriver = ChromeDriver()

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        Stream.of(
            Arguments.of(fireDriver),
            Arguments.of(chromeDriver)
        )
}