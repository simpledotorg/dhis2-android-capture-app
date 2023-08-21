package org.dhis2.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule

fun MacrobenchmarkRule.setRule(
  metrics: List<Metric> = listOf(StartupTimingMetric()),
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