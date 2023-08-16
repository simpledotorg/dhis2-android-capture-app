package org.dhis2.benchmark.helper

import android.content.Intent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import junit.framework.TestCase
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.startLoginScreen() {
  startActivityAndWait(Intent("$packageName.LOGIN_ACTIVITY"))
}

fun MacrobenchmarkScope.clickText(text: String) {
  if (!device.wait(
      Until.hasObject(By.text(text)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find $text")
  } else {
    device.findObject(By.text(text)).click()
  }
}

fun MacrobenchmarkScope.clickRes(res: String) {
  if (!device.wait(
      Until.hasObject(By.res(packageName, res)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find $res")
  } else {
    device.findObject(By.res(packageName, res)).click()
  }
}

fun MacrobenchmarkScope.clickTag(tag: String) {
  if (!device.wait(
      Until.hasObject(By.res(tag)),
      TimeUnit.SECONDS.toMillis(60)
    )
  ) {
    TestCase.fail("Could not find $tag")
  } else {
    device.findObject(By.res(tag)).click()
  }
}

