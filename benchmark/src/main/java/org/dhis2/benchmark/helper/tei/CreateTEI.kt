package org.dhis2.benchmark.helper.tei

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.helper.clickRes
import org.dhis2.benchmark.helper.clickTag
import org.dhis2.benchmark.helper.clickText
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.createTEI() {
  clickRes("createButton")
  selectOrgUnit()
  clickRes("acceptBtn")
  fillPatientEnrollmentDetails()
  clickRes("save")
  clickRes("action_button")
}

private fun MacrobenchmarkScope.selectOrgUnit() {
  //TODO: Need to allow constructor based org

  clickTag("ORG_UNIT_DIALOG_SEARCH")

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

  clickTag("ORG_UNIT_DIALOG_ITEM_CHECK_Madhabpur SC")
  clickTag("ORG_UNIT_DIALOG_DONE")
}

private fun MacrobenchmarkScope.fillPatientEnrollmentDetails() {
  clickRes("sectionButton")
  fillDOB()
  clickText("Male")
  scrollToEnd()
  selectAllYesRadioButtons()
}

private fun MacrobenchmarkScope.fillDOB() {
  //TODO: Need to allow constructor based dob

  clickRes("input_year")
  clickRes("input_year")

  device.pressKeyCode(KeyEvent.KEYCODE_3)
  device.pressKeyCode(KeyEvent.KEYCODE_6)

  clickText("Accept")
}

private fun scrollToEnd(maxSwipes: Int = 3) {
  val scrollableView = UiScrollable(UiSelector().scrollable(true))
  scrollableView.scrollToEnd(maxSwipes)
}

private fun MacrobenchmarkScope.selectAllYesRadioButtons() {
  val radioGroupsWithRes = device.findObjects(By.res(packageName, "yes"))
  radioGroupsWithRes?.forEach { radioGroup ->
    radioGroup.click()
  }

  val radioGroupsWithText = device.findObjects(By.text("Yes"))
  radioGroupsWithText?.forEach { radioGroup ->
    radioGroup.click()
  }
}
