package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.utils.clickByRes
import org.dhis2.benchmark.utils.clickByTag
import org.dhis2.benchmark.utils.clickByText
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.createTEI() {
  clickByRes("createButton")
  selectOrgUnit()
  clickByRes("acceptBtn")
  fillPatientEnrollmentDetails()
  clickByRes("save")
  clickByRes("action_button")
}

private fun MacrobenchmarkScope.selectOrgUnit() {
  //TODO: Need to allow constructor based org

  clickByTag("ORG_UNIT_DIALOG_SEARCH")

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

  clickByTag("ORG_UNIT_DIALOG_ITEM_CHECK_Madhabpur SC")
  clickByTag("ORG_UNIT_DIALOG_DONE")
}

private fun MacrobenchmarkScope.fillPatientEnrollmentDetails() {
  clickByRes("sectionButton")
  fillDOB()
  clickByText("Male")
  scrollToEnd()
  selectAllYesRadioButtons()
}

private fun MacrobenchmarkScope.fillDOB() {
  //TODO: Need to allow constructor based dob

  clickByRes("input_year")
  clickByRes("input_year")

  device.pressKeyCode(KeyEvent.KEYCODE_3)
  device.pressKeyCode(KeyEvent.KEYCODE_6)

  clickByText("Accept")
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
