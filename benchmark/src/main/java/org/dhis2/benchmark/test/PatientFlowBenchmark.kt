package org.dhis2.benchmark.test

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.flows.attemptLogin
import org.dhis2.benchmark.flows.optForAnalytics
import org.dhis2.benchmark.flows.createTEI
import org.dhis2.benchmark.flows.searchTEI
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForClass
import org.dhis2.benchmark.utils.waitForText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PatientFlowBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @OptIn(ExperimentalMetricApi::class)
  @Test
  fun patientRegistration() {
    var firstStart = true
    benchmarkRule.measureRepeated(
      metrics = listOf(TraceSectionMetric("PATIENT_REGISTRATION_FLOW_")),
      setupBlock = {
        pressHome()
        startActivityAndWait()
        if (firstStart) {
          waitForClass("$packageName.LoginActivity")
          attemptLogin()
          optForAnalytics()
          waitForText("Home", 60)
          firstStart = false
        }
      },
      measureBlock = {
        clickByText(HTN_PROGRAM)
        searchTEI()
        createTEI()
        waitForClass("$packageName.TeiDashboardMobileActivity")
      }
    )
  }
}