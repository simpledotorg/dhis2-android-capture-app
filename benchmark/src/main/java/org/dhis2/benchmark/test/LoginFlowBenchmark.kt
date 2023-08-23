package org.dhis2.benchmark.test

import android.content.Intent
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.flows.login
import org.dhis2.benchmark.utils.clearData
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForRes
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
class LoginFlowBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  private val instrumentation by lazy(LazyThreadSafetyMode.NONE) {
    InstrumentationRegistry.getInstrumentation()
  }

  @Test
  fun login() = benchmarkRule.measureRepeated(
    metrics = listOf(TraceSectionMetric("LOGIN_FLOW_")),
    setupBlock = {
      pressHome()
      instrumentation.clearData(packageName)
    }
  ) {
    startActivityAndWait(Intent("$packageName.LOGIN_ACTIVITY"))
    login()
    waitForRes("sync_layout")
  }
}
