package io.github.gianpamx.splitter.expense


import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.github.gianpamx.splitter.app.TestApp
import io.github.gianpamx.splitter.core.Expense
import io.github.gianpamx.splitter.core.PersistenceGateway
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

private const val ANY_EXPENSE_ID = 1L

@RunWith(AndroidJUnit4::class)
class ExpenseActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(ExpenseActivity::class.java, false, false)

    @Inject
    lateinit var persistenceGateway: PersistenceGateway

    @Before
    fun setUp() {
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.testAppComponent.inject(this)
    }

    @Test
    fun empty() {
        val intent = Intent()
        intent.putExtra("EXPENSE", ANY_EXPENSE_ID)
        whenever(persistenceGateway.findExpense(any())).thenReturn(Expense())

        activityTestRule.launchActivity(intent)
    }
}
