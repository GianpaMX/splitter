package io.github.gianpamx.splitter.expense


import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.schibsted.spain.barista.assertion.BaristaCheckedAssertions.assertChecked
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDialogInteractions.clickDialogPositiveButton
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import io.github.gianpamx.splitter.R
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
class ExpenseActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(ExpenseActivity::class.java, false, false)

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dao: DatabaseDao

    private lateinit var intent: Intent

    @Before
    fun setUp() {
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.testAppComponent.inject(this)

        intent = Intent()
        intent.putExtra("EXPENSE", 1L)

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
    fun checkReceiver() {
        activityTestRule.launchActivity(intent)

        clickOn(R.string.expense_activity_tab_receivers_title)
        clickListItem(R.id.recyclerView, 0)

        assertChecked(R.id.receiverCheckBox)
    }

    @Test
    fun addReceiver() {
        val newReceiver = "New Receiver"
        activityTestRule.launchActivity(intent)

        clickOn(R.string.expense_activity_tab_receivers_title)
        clickOn(R.id.floatingActionButton)
        writeTo(R.id.nameEditText, newReceiver)
        clickDialogPositiveButton()

        assertRecyclerViewItemCount(R.id.recyclerView, 3)
        assertContains(newReceiver)
    }

    @Test
    fun addPayer() {
        val newPayer = "New Payer"
        activityTestRule.launchActivity(intent)

        clickOn(R.id.floatingActionButton)
        writeTo(R.id.nameEditText, newPayer)
        writeTo(R.id.amountEditText, "0.2")
        clickDialogPositiveButton()

        assertRecyclerViewItemCount(R.id.recyclerView, 3)
        assertContains(newPayer)
    }
}
