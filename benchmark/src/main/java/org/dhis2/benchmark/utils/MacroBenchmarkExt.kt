package org.dhis2.benchmark.utils

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import java.util.concurrent.TimeUnit

private const val TARGET_PACKAGE = "com.dhis2"
private const val DEFAULT_ITERATIONS = 11

fun MacrobenchmarkRule.measureRepeated(
    metrics: List<Metric>,
    iterations: Int = DEFAULT_ITERATIONS,
    setupBlock: MacrobenchmarkScope.() -> Unit = {},
    measureBlock: MacrobenchmarkScope.() -> Unit = {}
) {
    measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = metrics,
        compilationMode = CompilationMode.DEFAULT,
        startupMode = StartupMode.COLD,
        iterations = iterations,
        setupBlock = setupBlock,
        measureBlock = measureBlock
    )
}

fun MacrobenchmarkScope.clickByText(text: String) {
    if (!device.wait(
            Until.hasObject(By.text(text)),
            TimeUnit.SECONDS.toMillis(60)
        )
    ) {
        TestCase.fail("Could not find: $text")
    } else {
        device.findObject(By.text(text)).click()
    }
}

fun MacrobenchmarkScope.clickByRes(res: String) {
    if (!device.wait(
            Until.hasObject(By.res(packageName, res)),
            TimeUnit.SECONDS.toMillis(60)
        )
    ) {
        TestCase.fail("Could not find: $res")
    } else {
        device.findObject(By.res(packageName, res)).click()
    }
}

fun MacrobenchmarkScope.clickByTag(tag: String) {
    if (!device.wait(
            Until.hasObject(By.res(tag)),
            TimeUnit.SECONDS.toMillis(60)
        )
    ) {
        TestCase.fail("Could not find: $tag")
    } else {
        device.findObject(By.res(tag)).click()
    }
}

fun MacrobenchmarkScope.setTextByRes(res: String, text: String) {
    if (!device.wait(
            Until.hasObject(By.res(packageName, res)),
            TimeUnit.SECONDS.toMillis(60)
        )
    ) {
        TestCase.fail("Could not find: $res")
    } else {
        device.findObject(By.res(packageName, res)).text = text
    }
}

fun MacrobenchmarkScope.waitForText(
    text: String,
    timeoutSeconds: Long = 10,
) {
    if (!device.wait(
            Until.hasObject(By.text(text)),
            TimeUnit.SECONDS.toMillis(timeoutSeconds)
        )
    ) {
        TestCase.fail("Could not find: $text")
    }
}

fun MacrobenchmarkScope.waitForRes(
    resource: String,
    timeoutSeconds: Long = 10,
) {
    if (!device.wait(
            Until.hasObject(By.res(packageName, resource)),
            TimeUnit.SECONDS.toMillis(timeoutSeconds)
        )
    ) {
        TestCase.fail("Could not find: $resource")
    }
}
