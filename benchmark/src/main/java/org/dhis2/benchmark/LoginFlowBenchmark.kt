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
            instrumentation.uiAutomation
                .executeShellCommand("pm clear $packageName")
                .close()
        }
    ) {
        startActivityAndWait(Intent("$packageName.LOGIN_ACTIVITY"))

        device.findObject(By.res(packageName, "server_url_edit")).text = "https://play.dhis2.org/40"
        device.findObject(By.res(packageName, "user_name_edit")).text = "android"
        device.findObject(By.res(packageName, "user_pass_edit")).text = "Android123"
        device.findObject(By.res(packageName, "login")).click()

        device.waitForIdle()
    }
}
