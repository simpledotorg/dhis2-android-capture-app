package org.dhis2.benchmark.test

import android.view.KeyEvent
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.flows.attemptLogin
import org.dhis2.benchmark.flows.createTEI
import org.dhis2.benchmark.flows.optForAnalytics
import org.dhis2.benchmark.flows.searchTEI
import org.dhis2.benchmark.utils.clickByRes
import org.dhis2.benchmark.utils.clickByTag
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForRes
import org.dhis2.benchmark.utils.waitForText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class BPEntryBenchmark {

  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @OptIn(ExperimentalMetricApi::class)
  @Test
  fun addNewBloodPressure() {
    var firstStart = true
    benchmarkRule.measureRepeated(
      metrics = listOf(TraceSectionMetric("BP_ENTRY_FLOW_")),
      setupBlock = {
        pressHome()
        startActivityAndWait()
        if (firstStart) {
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
        searchTEI()
        device.waitForIdle()
        clickByText("rajesh")
        device.waitForIdle()

        addHypertensionRecords()
        markFormAsComplete()

        device.wait(
          Until.hasObject(By.clazz("$packageName.TeiDashboardMobileActivity")),
          TimeUnit.SECONDS.toMillis(10)
        )

        generateNewEvent(generate = false)
      }
    )
  }

  private fun MacrobenchmarkScope.addHypertensionRecords() {
    clickByRes("addStageButton")
    device.waitForIdle()

    clickByText("Add new")
    clickByRes("action_button")

    waitForRes("sectionName")

    val elements = device.findObjects(By.res(packageName, "input_editText"))
    elements[0].click()
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_3)
    device.pressKeyCode(KeyEvent.KEYCODE_6)
    device.pressBack()

    elements[1].click()
    device.pressKeyCode(KeyEvent.KEYCODE_8)
    device.pressKeyCode(KeyEvent.KEYCODE_4)
    device.pressBack()

    elements[2].click()
    clickByText("Amlodipine(5mg)")

    device.waitForIdle()
    clickByRes("actionButton")
  }

  private fun MacrobenchmarkScope.markFormAsComplete() {
    clickByTag("MAIN_BUTTON_TAG")
  }

  private fun MacrobenchmarkScope.generateNewEvent(generate: Boolean) {
    if (generate) {
      clickByRes("possitive")
    } else {
      clickByRes("negative")
    }
    device.waitForIdle()
  }
}