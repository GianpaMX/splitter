package io.github.gianpamx.splitter.groupexpenses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.app.TestApp
import io.github.gianpamx.splitter.expense.ExpenseActivity
import io.github.gianpamx.splitter.gateway.room.AppDatabase
import io.github.gianpamx.splitter.gateway.room.DatabaseDao
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel
import io.github.gianpamx.splitter.gateway.room.model.ReceiverDBModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class GroupExpensesActivityTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var activityTestRule = IntentsTestRule(GroupExpensesActivity::class.java, false, false)

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
    fun addExpense() {
        activityTestRule.launchActivity(Intent())

        clickOn(R.id.addExpenseFAB)

        intended(hasComponent(ExpenseActivity::class.java.name))
    }

    @Test
    fun editExpense() {
        activityTestRule.launchActivity(Intent())

        clickListItem(R.id.expenseRecyclerView, 0)

        intended(hasComponent(ExpenseActivity::class.java.name))
    }
}
