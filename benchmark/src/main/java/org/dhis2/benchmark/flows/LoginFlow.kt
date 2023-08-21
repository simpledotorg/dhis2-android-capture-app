package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import java.util.concurrent.TimeUnit

internal fun MacrobenchmarkScope.loginIntoApp() {
    device.findObject(By.res(packageName, "server_url_edit")).text = "https://play.dhis2.org/40"
    device.findObject(By.res(packageName, "user_name_edit")).text = "android"
    device.findObject(By.res(packageName, "user_pass_edit")).text = "Android123"
    device.findObject(By.res(packageName, "login")).click()

    // Opt-in for analytics
    device.wait(
        Until.hasObject(By.text("Do you want to help us improve this app?")),
        TimeUnit.SECONDS.toMillis(10)
    )

    device.findObject(By.text("Yes")).click()
}
