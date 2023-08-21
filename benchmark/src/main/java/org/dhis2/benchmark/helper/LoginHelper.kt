package org.dhis2.benchmark.helper

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.attemptLogin(
  serverUrl: String,
  userName: String,
  password: String
) {
  device.findObject(By.res(packageName, "server_url_edit")).text = serverUrl
  device.findObject(By.res(packageName, "user_name_edit")).text = userName
  device.findObject(By.res(packageName, "user_pass_edit")).text = password

  device.findObject(By.res(packageName, "login")).click()
}

fun MacrobenchmarkScope.optForAnalytics() {
  device.wait(
    Until.hasObject(By.text("Do you want to help us improve this app?")),
    TimeUnit.SECONDS.toMillis(10)
  )

  device.findObject(By.text("Yes")).click()

  device.wait(
    Until.hasObject(By.text("Home")),
    TimeUnit.SECONDS.toMillis(60)
  )
}