package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.utils.waitForObject
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.createTEI() {
  device.waitForObject(By.res(packageName, "createButton")).click()
  selectOrgUnit()
  device.waitForObject(By.res(packageName, "acceptBtn")).click()
  fillPatientEnrollmentDetails()
  device.waitForObject(By.res(packageName, "save")).click()
  device.waitForObject(By.res(packageName, "action_button")).click()
}

private fun MacrobenchmarkScope.selectOrgUnit() {
  device.waitForObject(By.res("ORG_UNIT_DIALOG_SEARCH")).click()

  device.pressKeyCode(KeyEvent.KEYCODE_M)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_D)
  device.pressKeyCode(KeyEvent.KEYCODE_H)
  device.pressKeyCode(KeyEvent.KEYCODE_A)
  device.pressKeyCode(KeyEvent.KEYCODE_B)
  device.pressKeyCode(KeyEvent.KEYCODE_P)
  device.pressKeyCode(KeyEvent.KEYCODE_U)
  device.pressKeyCode(KeyEvent.KEYCODE_R)
  device.pressBack()

  device.waitForObject(By.res("ORG_UNIT_DIALOG_ITEM_CHECK_Madhabpur SC")).click()
  device.waitForObject(By.res("ORG_UNIT_DIALOG_DONE")).click()
}

private fun MacrobenchmarkScope.fillPatientEnrollmentDetails() {
  device.waitForObject(By.res(packageName, "sectionButton")).click()
  fillDOB()
  device.waitForObject(By.text("Male")).click()
  scrollToEnd()
  selectAllYesRadioButtons()
  selectPatientStatus()
}

fun MacrobenchmarkScope.selectPatientStatus() {
  device.waitForObject(By.res(packageName, "inputEditText")).click()
  device.waitForObject(By.text("Active")).click()
}

private fun MacrobenchmarkScope.fillDOB() {
  device.waitForObject(By.res(packageName, "input_year")).click()
  device.waitForObject(By.res(packageName, "input_year")).click()

  device.pressKeyCode(KeyEvent.KEYCODE_3)
  device.pressKeyCode(KeyEvent.KEYCODE_6)

  device.waitForObject(By.text("Accept")).click()
}

private fun scrollToEnd(maxSwipes: Int = 3) {
  val scrollableView = UiScrollable(UiSelector().scrollable(true))
  scrollableView.scrollToEnd(maxSwipes)
}

private fun MacrobenchmarkScope.selectAllYesRadioButtons() {
  val radioGroupsWithRes = device.findObjects(By.res(packageName, "yes"))
  radioGroupsWithRes?.forEach { radioGroup ->
    radioGroup.click()
    radioGroup.wait(Until.checked(true), TimeUnit.SECONDS.toMillis(10))
  }

  val radioGroupsWithText = device.findObjects(By.text("Yes"))
  radioGroupsWithText?.forEach { radioGroup ->
    radioGroup.click()
    radioGroup.wait(Until.checked(true), TimeUnit.SECONDS.toMillis(10))
  }
}
