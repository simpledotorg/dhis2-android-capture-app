package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.dhis2.benchmark.utils.waitForObject

fun MacrobenchmarkScope.addEvent() {
    device.waitForObject(By.res(packageName, "addStageButton")).click()
    device.waitForObject(By.text("Add new")).click()
    device.waitForObject(By.res(packageName, "action_button")).click()

    addHypertensionRecord()

    //Scroll is need, so save button appears
    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollToEnd(1)

    device.waitForObject(By.res(packageName, "actionButton")).click()
}