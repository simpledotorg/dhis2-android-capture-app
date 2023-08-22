package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.PASSWORD
import org.dhis2.benchmark.SERVER_URL
import org.dhis2.benchmark.USERNAME
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.waitForRes
import org.dhis2.benchmark.utils.waitForText
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.attemptLogin(
  serverUrl: String = SERVER_URL,
  userName: String = USERNAME,
  password: String = PASSWORD
) {
  waitForRes("server_url_edit")
  device.findObject(By.res(packageName, "server_url_edit")).text = serverUrl
  device.findObject(By.res(packageName, "user_name_edit")).text = userName
  device.findObject(By.res(packageName, "user_pass_edit")).text = password

  device.findObject(By.res(packageName, "login")).click()
}

fun MacrobenchmarkScope.optForAnalytics() {
  waitForText("Do you want to help us improve this app?", 60)
  clickByText("Yes")
}
