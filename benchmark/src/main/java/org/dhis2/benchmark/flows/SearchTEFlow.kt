package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import org.dhis2.benchmark.utils.waitForObject

fun MacrobenchmarkScope.searchTEI() {
  device.waitForObject(By.text("Search")).click()
  searchPatientByFirstAndLastName()
}

private fun MacrobenchmarkScope.searchPatientByFirstAndLastName() {
  val elements = device.findObjects(By.res(packageName, "input_editText"))

  elements[1].click()
  device.pressKeyCode(KeyEvent.KEYCODE_R)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_J)
  device.pressKeyCode(KeyEvent.KEYCODE_E)
  device.pressKeyCode(KeyEvent.KEYCODE_S)
  device.pressKeyCode(KeyEvent.KEYCODE_H)

  elements[2].click()
  device.pressKeyCode(KeyEvent.KEYCODE_K)
  device.pressKeyCode(KeyEvent.KEYCODE_U)
  device.pressKeyCode(KeyEvent.KEYCODE_M)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_R)

  device.pressBack()

  device.waitForObject(By.res(packageName, "searchButton")).click()
}
