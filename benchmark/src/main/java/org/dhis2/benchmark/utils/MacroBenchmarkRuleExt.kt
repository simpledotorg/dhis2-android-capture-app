package org.dhis2.benchmark.utils

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule

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
