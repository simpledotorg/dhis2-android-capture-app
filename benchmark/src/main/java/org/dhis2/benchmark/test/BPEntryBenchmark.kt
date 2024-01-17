package org.dhis2.benchmark.test

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.flows.addEvent
import org.dhis2.benchmark.flows.allowNotificationsPermission
import org.dhis2.benchmark.flows.createTEI
import org.dhis2.benchmark.flows.login
import org.dhis2.benchmark.flows.markFormAsCompleted
import org.dhis2.benchmark.flows.searchTEI
import org.dhis2.benchmark.flows.waitForSyncToFinish
import org.dhis2.benchmark.utils.measureRepeated
import org.dhis2.benchmark.utils.waitForObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
                    allowNotificationsPermission()
                    waitForSyncToFinish()
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

                device.waitForObject(By.text("rajesh kumar, MALE, 1964-08-11")).click()

                addEvent()
                markFormAsCompleted()

                // Skip creating another new entry
                device.waitForObject(By.res(packageName, "negative")).click()

                device.waitForObject(By.res(packageName, "tei_pager"))
                device.waitForIdle()
            }
        )
    }
}
