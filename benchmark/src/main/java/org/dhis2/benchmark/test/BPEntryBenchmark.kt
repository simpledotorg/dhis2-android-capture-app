package org.dhis2.benchmark.test

import android.view.KeyEvent
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.flows.login
import org.dhis2.benchmark.flows.createTEI
import org.dhis2.benchmark.flows.searchTEI
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.wait
import org.dhis2.benchmark.utils.waitForObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.seconds

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
          login()

          device.waitForObject(By.text("Home"))
          device.wait(Until.gone(By.res("DOWNLOADING_PROGRAM")), 60.seconds)
          device.waitForObject(By.text(HTN_PROGRAM)).click()

          searchTEI()
          createTEI()

          device.waitForObject(By.res(packageName, "back")).click()
          device.waitForObject(By.res(packageName, "back_button")).click()

          firstStart = false
        }
      },
      measureBlock = {
        device.waitForObject(By.text(HTN_PROGRAM)).click()

        searchTEI()

        device.waitForObject(By.text("rajesh")).click()

        addHypertensionRecord()
        markFormAsCompleted()

        // Skip creating another new entry
        device.waitForObject(By.res(packageName, "negative")).click()
        device.waitForIdle()
      }
    )
  }

  private fun MacrobenchmarkScope.markFormAsCompleted() {
    device.waitForObject(By.text("Saved!"))
    device.waitForObject(By.res("MAIN_BUTTON_TAG")).click()
  }

  private fun MacrobenchmarkScope.addHypertensionRecord() {
    device.waitForObject(By.res(packageName, "addStageButton")).click()
    device.waitForObject(By.text("Add new")).click()
    device.waitForObject(By.res(packageName, "action_button")).click()

    device.waitForObject(By.res(packageName, "sectionName"))

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

    device.waitForObject(By.res(packageName, "inputEditText")).click()
    device.waitForObject(By.text("Amlodipine(5mg)")).click()

    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollToEnd(1)

    device.waitForObject(By.res(packageName, "actionButton")).click()
  }
}
