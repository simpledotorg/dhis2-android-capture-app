package org.dhis2.benchmark.test

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.flows.attemptLogin
import org.dhis2.benchmark.flows.optForAnalytics
import org.dhis2.benchmark.flows.createTEI
import org.dhis2.benchmark.flows.searchTEI
import org.dhis2.benchmark.utils.clearData
import org.dhis2.benchmark.utils.clickByRes
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForRes
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
          waitForRes("credentialLayout", 30)
          attemptLogin()
          optForAnalytics()
          waitForText("Home", 60)
          firstStart = false
        }
      },
      measureBlock = {
        clickByText(HTN_PROGRAM)
        device.waitForIdle()
        searchTEI()
        device.waitForIdle()
        createTEI()
        waitForRes("tei_pager", 30)
      }
    )
  }

  @OptIn(ExperimentalMetricApi::class)
  @Test
  fun patientSearch() {
    var firstStart = true
    benchmarkRule.measureRepeated(
      metrics = listOf(TraceSectionMetric("PATIENT_SEARCH_FLOW_")),
      setupBlock = {
        pressHome()
        startActivityAndWait()

        if (firstStart) {
          waitForRes("credentialLayout", 30)
          attemptLogin()
          optForAnalytics()
          waitForText("Home", 60)
          clickByText(HTN_PROGRAM)
          searchTEI()
          createTEI()
          clickByRes("back")
          clickByRes("back_button")
          firstStart = false
        }
      },
      measureBlock = {
        clickByText(HTN_PROGRAM)
        device.waitForIdle()
        searchTEI()
        device.waitForIdle()
        clickByText("rajesh")
        device.waitForIdle()
        waitForRes("tei_pager", 30)
      }
    )
  }
}
