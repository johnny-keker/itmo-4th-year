package utils

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.util.stream.Stream

class DriverProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        if (!context!!.element.get().isAnnotationPresent(CustomCsvProvider::class.java))
         return Stream.of(
                Arguments.of(FirefoxDriver()),
                Arguments.of(ChromeDriver())
            )

        val csvPath = context.element.get().getAnnotation(CustomCsvProvider::class.java).path
        val data = DriverProvider::class.java.getResource("/$csvPath").readText().split("\r\n").drop(1)

        return Stream.concat(
            data.stream().filter { !it.startsWith("//") }.map {
                val x = it.split(",")
                Arguments.of(
                    FirefoxDriver(),
                    enumValueOf<SortBy>(x[0]),
                    enumValueOf<SortType>(x[1]),
                    enumValueOf<FilterBy>(x[2]),
                    enumValueOf<Status>(x[3]),
                    x[4].toLong(),
                    x[5].toLong()
                )
            },
            data.stream().filter { !it.startsWith("//") }.map {
                val x = it.split(",")
                Arguments.of(
                    ChromeDriver(),
                    enumValueOf<SortBy>(x[0]),
                    enumValueOf<SortType>(x[1]),
                    enumValueOf<FilterBy>(x[2]),
                    enumValueOf<Status>(x[3]),
                    x[4].toLong(),
                    x[5].toLong()
                )
            }
        )
    }
}