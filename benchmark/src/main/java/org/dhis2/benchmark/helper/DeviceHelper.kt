package org.dhis2.benchmark.helper

import androidx.test.platform.app.InstrumentationRegistry
import org.dhis2.benchmark.TARGET_PACKAGE

private val instrumentation by lazy(LazyThreadSafetyMode.NONE) {
  InstrumentationRegistry.getInstrumentation()
}

fun uninstallExistingPackage(
  packageName: String = TARGET_PACKAGE
) {
  instrumentation.uiAutomation
    .executeShellCommand("pm clear $packageName")
    .close()
}