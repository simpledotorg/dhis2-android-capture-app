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
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForClass
import org.dhis2.benchmark.utils.waitForText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PatientFlowBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  private val instrumentation by lazy(LazyThreadSafetyMode.NONE) {
    InstrumentationRegistry.getInstrumentation()
  }

  @OptIn(ExperimentalMetricApi::class)
  @Test
  fun patientRegistration() {
    benchmarkRule.measureRepeated(
      metrics = listOf(TraceSectionMetric("PATIENT_REGISTRATION_FLOW_")),
      setupBlock = {
        instrumentation.clearData(packageName)
        pressHome()
        startActivityAndWait()

        device
          .wait(Until.hasObject(By.res(packageName, "credentialLayout")), TimeUnit.SECONDS.toMillis(30))

        attemptLogin()
        optForAnalytics()
        waitForText("Home", 60)
      },
      measureBlock = {
        clickByText(HTN_PROGRAM)
        searchTEI()
        createTEI()
        device.wait(Until.hasObject(By.res(packageName, "tei_pager")), TimeUnit.SECONDS.toMillis(30))
      }
    )
  }
}
