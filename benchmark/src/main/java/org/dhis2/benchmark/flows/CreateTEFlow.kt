package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.dhis2.benchmark.utils.waitForObject

fun MacrobenchmarkScope.createTEI() {
    device.waitForObject(By.res(packageName, "createButton")).click()
    selectOrgUnit()

    //Select today's date
    device.waitForObject(By.res(packageName, "acceptBtn")).click()

    device.waitForObject(By.res(packageName, "sectionButton")).click()

    addPatientDetails()

    device.waitForObject(By.res(packageName, "sectionButton")).click()

    addDiagnosis()

    device.waitForObject(By.res(packageName, "sectionButton")).click()
    device.waitForObject(By.res(packageName, "sectionButton")).click()

    addConsentAndStatus()

    device.waitForObject(By.res(packageName, "save")).click()

    addHypertensionRecord()

    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollToEnd(1)

    device.waitForObject(By.res(packageName, "actionButton")).click()

    markFormAsCompleted()

    // Skip creating another new entry
    device.waitForObject(By.res(packageName, "negative")).click()
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

private fun MacrobenchmarkScope.addPatientDetails() {
    fillDOB()

    //Select Gender
    device.waitForObject(By.text("Male")).click()

    //Enter House address
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

    //Select District
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

    //Select State
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
}

private fun MacrobenchmarkScope.fillDOB() {
    device.waitForObject(By.res(packageName, "input_year")).click()
    device.waitForObject(By.res(packageName, "input_year")).click()

    device.pressKeyCode(KeyEvent.KEYCODE_3)
    device.pressKeyCode(KeyEvent.KEYCODE_6)

    device.waitForObject(By.text("Accept")).click()
}

private fun MacrobenchmarkScope.addDiagnosis() {
    val radioGroupsWithText = device.findObjects(By.text("Yes"))
    radioGroupsWithText.forEach { radioButton ->
        radioButton.click()
    }
}

private fun MacrobenchmarkScope.addConsentAndStatus() {
    device.waitForObject(By.text("Yes, patient gives consent")).click()
    device.waitForObject(By.text("Active record")).click()
}

fun MacrobenchmarkScope.addHypertensionRecord() {
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
}

fun MacrobenchmarkScope.markFormAsCompleted() {
    device.waitForObject(By.text("Saved!"))
    device.waitForObject(By.res("MAIN_BUTTON_TAG")).click()
}
