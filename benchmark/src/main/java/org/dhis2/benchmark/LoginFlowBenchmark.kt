package org.dhis2.benchmark

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

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
        // TODO: Use trace section metric instead of startup?
        metrics = listOf(StartupTimingMetric()),
        iterations = 11,
        startupMode = StartupMode.COLD,
        setupBlock = {
            instrumentation.uiAutomation
                .executeShellCommand("pm clear $packageName")
                .close()
        }
    ) {
        pressHome()
        startActivityAndWait()

        // Credentials Screen
        device.wait(
            Until.hasObject(By.res(packageName, "credentialLayout")),
            TimeUnit.SECONDS.toMillis(10)
        )

        device.findObject(
            By.res(packageName, "server_url_edit")
        ).text = "https://play.dhis2.org/40"

        device.findObject(
            By.res(packageName, "user_name_edit")
        ).text = "android"

        device.findObject(
            By.res(packageName, "user_pass_edit")
        ).text = "Android123"

        device.findObject(
            By.res(packageName, "login")
        ).click()

        // Opt-in for analytics
        device.wait(
            Until.hasObject(By.text("Do you want to help us improve this app?")),
            TimeUnit.SECONDS.toMillis(10)
        )

        device.findObject(By.text("Yes")).click()

        // Home
        device.wait(
            Until.hasObject(By.text("Home")),
            TimeUnit.SECONDS.toMillis(60)
        )
    }
}
