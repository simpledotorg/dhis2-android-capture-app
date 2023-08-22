package org.dhis2.benchmark.utils

import android.app.Instrumentation
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import java.util.concurrent.TimeUnit

fun Instrumentation.clearData(packageName: String) {
  uiAutomation
    .executeShellCommand("pm clear $packageName")
    .close()
}

fun MacrobenchmarkScope.clickByText(text: String) {
  if (!device.wait(
      Until.hasObject(By.text(text)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find: $text")
  } else {
    device.findObject(By.text(text)).click()
  }
}

fun MacrobenchmarkScope.clickByRes(res: String) {
  if (!device.wait(
      Until.hasObject(By.res(packageName, res)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find: $res")
  } else {
    device.findObject(By.res(packageName, res)).click()
  }
}

fun MacrobenchmarkScope.clickByTag(tag: String) {
  if (!device.wait(
      Until.hasObject(By.res(tag)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find: $tag")
  } else {
    device.findObject(By.res(tag)).click()
  }
}

fun MacrobenchmarkScope.waitForText(
  text: String,
  timeoutSeconds: Long = 10,
) {
  if (!device.wait(
      Until.hasObject(By.text(text)),
      TimeUnit.SECONDS.toMillis(timeoutSeconds)
    )
  ) {
    TestCase.fail("Could not find: $text")
  }
}

fun MacrobenchmarkScope.waitForRes(
  resource: String,
  timeoutSeconds: Long = 10,
) {
  if (!device.wait(
      Until.hasObject(By.res(packageName, resource)),
      TimeUnit.SECONDS.toMillis(timeoutSeconds)
    )
  ) {
    TestCase.fail("Could not find: $resource")
  }
}
