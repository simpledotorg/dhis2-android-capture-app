package org.dhis2.benchmark

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.dhis2.benchmark.helper.attemptLogin
import org.dhis2.benchmark.helper.clickText
import org.dhis2.benchmark.helper.tei.createTEI
import org.dhis2.benchmark.helper.tei.searchTEI
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PatientRegistrationBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @OptIn(ExperimentalMetricApi::class)
  @Test
  fun patientRegistration() {
    var firstStart = true
    benchmarkRule.setRule(
      metrics = listOf(TraceSectionMetric("Patient Registration")),
      setupBlock = {
        if (firstStart) {
          attemptLogin()
          firstStart = false
        }
      },
      measureBlock = {
        startActivityAndWait()
        clickText(HTN_PROGRAM)
        searchTEI()
        createTEI()
        device.waitForIdle()
      }
    )
  }
}