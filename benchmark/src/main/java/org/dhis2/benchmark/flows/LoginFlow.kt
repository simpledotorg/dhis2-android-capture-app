package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import org.dhis2.benchmark.utils.waitForObject

private const val SERVER_URL = "https://dhis2-sandbox2.simple.org"
private const val USERNAME = "bolanurse"
private const val PASSWORD = "Test123!"

fun MacrobenchmarkScope.login() {
  device.waitForObject(By.res(packageName, "server_url_edit")).text = SERVER_URL
  device.waitForObject(By.res(packageName, "user_name_edit")).text = USERNAME
  device.waitForObject(By.res(packageName, "user_pass_edit")).text = PASSWORD
  device.waitForObject(By.res(packageName, "login")).click()

  // Opt for analytics
  device.waitForObject(By.text("Yes")).click()
}
