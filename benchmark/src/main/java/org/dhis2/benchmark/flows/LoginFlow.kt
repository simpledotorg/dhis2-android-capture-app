package org.dhis2.benchmark.flows

import androidx.benchmark.macro.MacrobenchmarkScope
import org.dhis2.benchmark.utils.clickByRes
import org.dhis2.benchmark.utils.clickByText
import org.dhis2.benchmark.utils.setTextByRes
import org.dhis2.benchmark.utils.waitForRes
import org.dhis2.benchmark.utils.waitForText

private const val SERVER_URL = "https://dhis2-sandbox2.simple.org"
private const val USERNAME = "admin"
private const val PASSWORD = "district"

fun MacrobenchmarkScope.login() {
  waitForRes("credentialLayout")
  setTextByRes("server_url_edit", SERVER_URL)
  setTextByRes("user_name_edit", USERNAME)
  setTextByRes("user_pass_edit", PASSWORD)
  clickByRes("login")
  optForAnalytics()
}

private fun MacrobenchmarkScope.optForAnalytics() {
  waitForText("Do you want to help us improve this app?", 60)
  clickByText("Yes")
}
