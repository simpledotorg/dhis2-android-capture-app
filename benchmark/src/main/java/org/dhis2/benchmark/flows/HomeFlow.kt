package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.utils.wait
import org.dhis2.benchmark.utils.waitForObject
import kotlin.time.Duration.Companion.minutes

fun MacrobenchmarkScope.allowNotificationsPermission() {
    device.waitForObject(By.text("Allow")).click()
}

fun MacrobenchmarkScope.waitForSyncToFinish() {
    device.waitForObject(By.text("Home"))
    device.wait(Until.gone(By.res("DOWNLOADING_PROGRAM")), 30.minutes)
}