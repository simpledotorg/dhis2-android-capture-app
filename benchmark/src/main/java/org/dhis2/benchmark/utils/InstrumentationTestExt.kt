package org.dhis2.benchmark.utils

import android.app.Instrumentation

fun Instrumentation.clearData(packageName: String) {
    uiAutomation
        .executeShellCommand("pm clear $packageName")
        .close()
}
