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

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        if (!context!!.element.get().isAnnotationPresent(CustomCsvProvider::class.java))
         return Stream.of(
                Arguments.of(fireDriver),
                Arguments.of(chromeDriver)
            )

        val csvPath = context!!.element.get().getAnnotation(CustomCsvProvider::class.java).path
        return Stream.of(
            Arguments.of(fireDriver, csvPath),
            Arguments.of(chromeDriver, csvPath)
        )
    }
}