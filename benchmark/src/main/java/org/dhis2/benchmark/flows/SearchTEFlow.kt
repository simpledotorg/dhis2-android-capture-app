package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import org.dhis2.benchmark.utils.clickByRes
import org.dhis2.benchmark.utils.clickByText

fun MacrobenchmarkScope.searchTEIAndNavigateToFirstResult() {
  clickByText("Search")
  searchPatientByFirstAndLastName()
}

fun MacrobenchmarkScope.searchTEI() {
  clickByText("Search")
  device.waitForIdle()
  searchPatientByFirstAndLastName()
}

private fun MacrobenchmarkScope.searchPatientByFirstAndLastName() {
  //TODO: elements[0].text = "RAJESH" is not working

  val elements = device.findObjects(By.res(packageName, "input_editText"))
  elements[0].click()
  device.pressKeyCode(KeyEvent.KEYCODE_R)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_J)
  device.pressKeyCode(KeyEvent.KEYCODE_E)
  device.pressKeyCode(KeyEvent.KEYCODE_S)
  device.pressKeyCode(KeyEvent.KEYCODE_H)

  elements[1].click()
  device.pressKeyCode(KeyEvent.KEYCODE_K)
  device.pressKeyCode(KeyEvent.KEYCODE_U)
  device.pressKeyCode(KeyEvent.KEYCODE_M)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_R)

  device.pressBack()

  clickByRes("searchButton")
}