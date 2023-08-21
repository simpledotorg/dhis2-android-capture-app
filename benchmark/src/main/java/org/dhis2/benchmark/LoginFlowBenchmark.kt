package org.dhis2.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.dhis2.benchmark.flows.loginIntoApp
import org.dhis2.benchmark.utils.clearData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private val instrumentation by lazy(LazyThreadSafetyMode.NONE) {
        InstrumentationRegistry.getInstrumentation()
    }

    @Test
    fun login() = benchmarkRule.measureRepeated(
        packageName = "com.dhis2",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        iterations = 11,
        compilationMode = CompilationMode.DEFAULT,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
            instrumentation.clearData(packageName)
        }
    ) {
        startActivityAndWait(Intent("$packageName.LOGIN_ACTIVITY"))
        loginIntoApp()

        device.wait(Until.hasObject(By.res(packageName, "sync_layout")), 5000)
    }
}
