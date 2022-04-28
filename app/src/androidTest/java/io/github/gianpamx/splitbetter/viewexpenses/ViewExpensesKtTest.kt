package io.github.gianpamx.splitbetter.viewexpenses

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ViewExpensesKtTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var targetContext: Context

    @Before
    fun setUp() {
        targetContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        composeTestRule.setContent {
            ViewExpensesContent(emptyList())
        }
    }

    @Test
    fun dummy() {
        val runInTheBackgroundSwitch = composeTestRule
            .onNodeWithText("Hello world")

        runInTheBackgroundSwitch.assertIsDisplayed()
    }
}
