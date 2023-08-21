package org.dhis2.benchmark.utils

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import org.dhis2.benchmark.DEFAULT_ITERATIONS
import org.dhis2.benchmark.TARGET_PACKAGE

fun MacrobenchmarkRule.measureRepeated(
  metrics: List<Metric>,
  setupBlock: MacrobenchmarkScope.() -> Unit = {},
  measureBlock: MacrobenchmarkScope.() -> Unit = {}
) {
  measureRepeated(
    packageName = TARGET_PACKAGE,
    metrics = metrics,
    compilationMode = CompilationMode.DEFAULT,
    startupMode = StartupMode.COLD,
    iterations = DEFAULT_ITERATIONS,
    setupBlock = setupBlock,
  ) {
    measureBlock()
  }
}