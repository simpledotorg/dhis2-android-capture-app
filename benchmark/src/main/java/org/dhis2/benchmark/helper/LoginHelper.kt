package org.dhis2.benchmark.helper

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.PASSWORD
import org.dhis2.benchmark.SERVER_URL
import org.dhis2.benchmark.USERNAME
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.attemptLogin(
  serverUrl: String = SERVER_URL,
  userName: String = USERNAME,
  password: String = PASSWORD
) {
  uninstallExistingPackage()
  startLoginScreen()

  fillLoginDetails(serverUrl, userName, password)
  device.findObject(By.res(packageName, "login")).click()
  optForAnalytics()
}

fun MacrobenchmarkScope.fillLoginDetails(
  serverUrl: String,
  userName: String,
  password: String
) {
  device.findObject(By.res(packageName, "server_url_edit")).text = serverUrl
  device.findObject(By.res(packageName, "user_name_edit")).text = userName
  device.findObject(By.res(packageName, "user_pass_edit")).text = password
}

private fun MacrobenchmarkScope.optForAnalytics() {
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