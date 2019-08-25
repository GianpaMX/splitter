package io.github.gianpamx.splitter.settleup

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.github.gianpamx.splitter.app.TestApp
import io.github.gianpamx.splitter.frameworks.room.AppDatabase
import io.github.gianpamx.splitter.frameworks.room.DatabaseDao
import io.github.gianpamx.splitter.frameworks.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PaymentDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PersonDBModel
import io.github.gianpamx.splitter.frameworks.room.model.ReceiverDBModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SettleUpActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(SettleUpActivity::class.java, false, false)

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dao: DatabaseDao

    @Before
    fun setUp() {
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.testAppComponent.inject(this)

        dao.insert(ExpenseDBModel(id = 1L, title = "Any Title"))
        dao.insert(PersonDBModel(id = 1, name = "Any Name"))
        dao.insert(PersonDBModel(id = 2, name = "Any Other Name"))
        dao.insert(PaymentDBModel(expenseId = 1L, personId = 1, cents = 10))
        dao.insert(ReceiverDBModel(expenseId = 1L, personId = 2))
    }

    @After
    fun tearDown() {
        database.clearAllTables()
    }

    @Test
    fun empty() {
        activityTestRule.launchActivity(Intent())
    }
}
