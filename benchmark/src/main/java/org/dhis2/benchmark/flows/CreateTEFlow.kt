package org.dhis2.benchmark.flows

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.dhis2.benchmark.utils.waitForObject

fun MacrobenchmarkScope.createTEI() {
    device.waitForObject(By.res(packageName, "createButton")).click()
//    selectOrgUnit()

    //Select today's date
    device.waitForObject(By.res(packageName, "acceptBtn")).click()

    addPatientDetails()

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
    device.waitForObject(By.text("House address *")).click()
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

//    val scrollableView = UiScrollable(UiSelector().scrollable(true))
//    scrollableView.scrollTextIntoView("State *")
    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollForward(120)

    //Select District
    val inputDropDowns = device.findObjects(By.res("INPUT_DROPDOWN"))

    inputDropDowns[0].click()
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
    inputDropDowns[1].click()
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
    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollTextIntoView("DATE OF BIRTH")

    device.waitForObject(By.text("DATE OF BIRTH")).click()

    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_0)
    device.pressKeyCode(KeyEvent.KEYCODE_8)
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_9)
    device.pressKeyCode(KeyEvent.KEYCODE_6)
    device.pressKeyCode(KeyEvent.KEYCODE_4)
    device.pressBack()
}

private fun MacrobenchmarkScope.addDiagnosis() {
    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollIntoView(UiSelector().text("Does patient have hypertension? *"))

    val radioGroupsWithText = device.findObjects(By.text("Yes"))
    radioGroupsWithText.forEach { radioButton ->
        radioButton.click()
    }
}

private fun MacrobenchmarkScope.addConsentAndStatus() {
    device.findObject(By.scrollable(true))
        .scroll(Direction.DOWN, 100f)

    val scrollableView = UiScrollable(UiSelector().scrollable(true))
    scrollableView.scrollForward(80)

    device.waitForObject(By.text("By tapping the checkbox you confirm that any patient data you enter has been obtained under appropriate informed consent. This means that the patient or legal guardian knows that you are entering their personal data into the app, understands what data is being collected, and knows that they may be contacted via SMS, WhatsApp or other methods using the phone number provided in the app.   Anyone authorized by you, or the healthcare provider for whom you work, will have access to the patient data and information you enter here. By using the app you confirm that the patient understands who will have access to the data obtained through this app and consents to that access."))
        .click()
}

fun MacrobenchmarkScope.addHypertensionRecord() {
    device.waitForObject(By.text("Hypertension record"))
    val elements = device.findObjects(By.res("INPUT_POSITIVE_INTEGER"))
    elements[0].click()
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.pressKeyCode(KeyEvent.KEYCODE_4)
    device.pressKeyCode(KeyEvent.KEYCODE_8)
    device.pressBack()

    elements[1].click()
    device.pressKeyCode(KeyEvent.KEYCODE_9)
    device.pressKeyCode(KeyEvent.KEYCODE_7)
    device.pressBack()

    val dropDownElements = device.findObjects(By.res("INPUT_DROPDOWN"))
    dropDownElements[0].click()

    device.waitForObject(By.text("Amlodipine 5mg OD")).click()
}

fun MacrobenchmarkScope.markFormAsCompleted() {
    device.waitForObject(By.text("Saved!"))
    device.waitForObject(By.res("MAIN_BUTTON_TAG")).click()
}
