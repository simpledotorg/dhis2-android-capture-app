package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.HTN_PROGRAM
import org.dhis2.benchmark.utils.waitForObject
import java.util.concurrent.TimeUnit

fun MacrobenchmarkScope.createTEI() {
    device.waitForObject(By.res(packageName, "createButton")).click()
    selectOrgUnit()
    device.waitForObject(By.res(packageName, "acceptBtn")).click()
    fillPatientEnrollmentDetails()
    device.waitForObject(By.res(packageName, "save")).click()

    device.waitForObject(By.text("Hypertension record"))
    val elements = device.findObjects(By.res(packageName, "input_editText"))
    elements[0].click()
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_3)
    device.pressKeyCode(KeyEvent.KEYCODE_6)
    device.pressBack()

    elements[1].click()
    device.pressKeyCode(KeyEvent.KEYCODE_8)
    device.pressKeyCode(KeyEvent.KEYCODE_4)
    device.pressBack()

    device.waitForObject(By.res(packageName, "inputEditText")).click()
    device.waitForObject(By.text("Amlodipine 5mg OD")).click()

    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollToEnd(1)

    device.waitForObject(By.res(packageName, "sectionButton")).click()
    device.waitForObject(By.res(packageName, "action_button")).click()

    device.waitForObject(By.text("Saved!"))
    device.waitForObject(By.res("MAIN_BUTTON_TAG")).click()

    // Skip creating another new entry
    device.waitForObject(By.res(packageName, "negative")).click()
    device.waitForIdle()
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

    val elements = device.findObjects(By.res(packageName, "input_editText"))
    elements[4].click()

    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_2)
    device.pressKeyCode(KeyEvent.KEYCODE_0)
    device.pressKeyCode(KeyEvent.KEYCODE_R)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_M)
    device.pressKeyCode(KeyEvent.KEYCODE_N)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_G)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_R)
    device.pressBack()

    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollIntoView(UiSelector().text("Next"))

    val dropDownElements = device.findObjects(By.res(packageName, "inputEditText"))
    dropDownElements[0].click()

    device.waitForObject(By.text("Search")).click()

    device.pressKeyCode(KeyEvent.KEYCODE_B)
    device.pressKeyCode(KeyEvent.KEYCODE_E)
    device.pressKeyCode(KeyEvent.KEYCODE_N)
    device.pressKeyCode(KeyEvent.KEYCODE_G)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_L)
    device.pressKeyCode(KeyEvent.KEYCODE_U)
    device.pressKeyCode(KeyEvent.KEYCODE_R)
    device.pressKeyCode(KeyEvent.KEYCODE_U)
    device.pressBack()

    device.waitForObject(By.text("Bengaluru Urban")).click()

    dropDownElements[1].click()

    device.waitForObject(By.text("Search")).click()

    device.pressKeyCode(KeyEvent.KEYCODE_K)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_R)
    device.pressKeyCode(KeyEvent.KEYCODE_N)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_T)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressKeyCode(KeyEvent.KEYCODE_K)
    device.pressKeyCode(KeyEvent.KEYCODE_A)
    device.pressBack()

    device.waitForObject(By.text("Karnataka")).click()

    device.waitForObject(By.res(packageName, "sectionButton")).click()

    //  scrollToEnd()
    selectAllYesRadioButtons()
//    selectPatientStatus()
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
    val radioGroupsWithText = device.findObjects(By.text("Yes"))

    radioGroupsWithText[0].click()
//    radioGroupsWithText[0].wait(Until.checked(true), TimeUnit.SECONDS.toMillis(10))

    radioGroupsWithText[2].click()
//    radioGroupsWithText[2].wait(Until.checked(true), TimeUnit.SECONDS.toMillis(10))

//    radioGroupsWithText?.forEach { radioGroup ->
//        radioGroup.click()
//        device.waitForIdle()
//        radioGroup.wait(Until.checked(true), TimeUnit.SECONDS.toMillis(10))
//    }

    device.waitForObject(By.res(packageName, "sectionButton")).click()
    device.waitForObject(By.res(packageName, "sectionButton")).click()

    val radioGroupsWithRes = device.findObjects(By.res(packageName, "radio"))
    radioGroupsWithRes[0].click()
}
