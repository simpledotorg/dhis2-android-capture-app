package org.dhis2.benchmark.utils

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.SearchCondition
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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

fun UiDevice.waitForObject(selector: BySelector, timeout: Duration = 10.seconds): UiObject2 {
    if (wait(Until.hasObject(selector), timeout)) {
        return findObject(selector)
    }
    error("Object with selector [$selector] not found")
}

fun <R> UiDevice.wait(condition: SearchCondition<R>, timeout: Duration): R {
    return wait(condition, timeout.inWholeMilliseconds)
}
