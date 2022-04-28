package io.github.gianpamx.splitbetter.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.gianpamx.splitbetter.app.AppAction
import io.github.gianpamx.splitbetter.app.MutableAppActionFlow
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.entity.Receiver
import io.github.gianpamx.splitbetter.domain.entity.money
import io.github.gianpamx.splitbetter.domain.usecase.GetExpense
import io.github.gianpamx.splitbetter.domain.usecase.GetPeople
import io.github.gianpamx.splitbetter.domain.usecase.NewPerson
import io.github.gianpamx.splitbetter.domain.usecase.SaveExpense
import io.github.gianpamx.splitbetter.domain.usecase.SavePerson
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class EditExpenseViewModel @Inject constructor(
    getPeople: GetPeople,
    private val newPerson: NewPerson,
    private val savePerson: SavePerson,
    private val getExpense: GetExpense,
    private val saveExpense: SaveExpense,
    private val appActionFlow: MutableAppActionFlow,
) : ViewModel() {
    private val editStateFlow = MutableStateFlow(EditState())
    private var editState = editStateFlow.value
        set(value) {
            field = value
            editStateFlow.value = value
        }
    val state: StateFlow<EditState> = editStateFlow

    init {
        getPeople()
            .onEach { people ->
                val payers = editState.expense?.payers.orEmpty()
                val receivers = editState.expense?.receivers.orEmpty()
                editState = editState.copy(
                    payers = payers.mergeWithPeople(people),
                    receivers = receivers.mergeWithPeople(people),
                )
            }
            .launchIn(viewModelScope)
    }

    fun newExpense() {
        viewModelScope.launch {
            val expense = Expense()

            val people = editState.payers.map { it.person }
            editState = editState.copy(
                expense = expense,
                originalExpense = expense,
                payers = expense.payers.mergeWithPeople(people),
                receivers = expense.receivers.mergeWithPeople(people),
            )
        }
    }

    fun loadExpense(expenseId: String) {
        viewModelScope.launch {
            val expense = getExpense(expenseId) ?: return@launch

            val people = editState.payers.map { it.person }
            editState = editState.copy(
                expense = expense,
                originalExpense = expense,
                payers = expense.payers.mergeWithPeople(people),
                receivers = expense.receivers.mergeWithPeople(people),
            )
        }
    }

    fun saveExpenseAndExit(expense: Expense) {
        viewModelScope.launch {
            if (expense != editState.originalExpense) {
                saveExpense(expense)
            }

            exit()
        }
    }

    private suspend fun exit() {
        editState = editState.copy(
            expense = null,
            originalExpense = null,
        )
        appActionFlow.emit(AppAction.CloseExpenseEditor)
    }

    fun selectPayer(payer: Payer) {
        editState = editState.copy(selectedPayer = payer)
    }

    fun addNewPayer() {
        editState = editState.copy(selectedPayer = Payer(newPerson(), 0.money))
    }

    fun savePayer(person: Person, payedAmount: Money) {
        viewModelScope.launch {
            val payers = editState.expense
                ?.payers
                ?.update(
                    person = person,
                    payedAmount = payedAmount
                )
                .orEmpty()
            val expense = editState.expense?.copy(
                payers = payers
            )

            editState = editState.copy(
                expense = expense,
                payers = editState.payers
                    .update(
                        person = person,
                        payedAmount = payedAmount
                    ),
                selectedPayer = null,
            )

            savePerson(person)
        }
    }

    fun removeSelectedPayer() {
        editState = editState.copy(
            selectedPayer = null
        )
    }

    fun updateReceiver(receiver: Receiver) {
        viewModelScope.launch {
            val receivers = editState.expense
                ?.receivers
                ?.update(receiver)
                .orEmpty()

            val expense = editState.expense?.copy(
                receivers = receivers
            )

            editState = editState.copy(
                expense = expense,
                selectedReceiver = null,
            )

            savePerson(receiver.person)
        }
    }
}


@JvmName("mergeWithPeoplePayer")
private fun List<Payer>.mergeWithPeople(people: List<Person>) = people.map { person ->
    Payer(
        person = person,
        amount = find { it.person.id == person.id }
            ?.amount
            ?: Money(0)
    )
}

@JvmName("mergeWithPeopleReceiver")
private fun List<Receiver>.mergeWithPeople(people: List<Person>) = people.map { person ->
    Receiver(
        person = person,
        isChecked = indexOfFirst { it.person.id == person.id } != -1
    )
}

private fun List<Payer>.update(person: Person, payedAmount: Money): List<Payer> =
    if (this.find { it.person.id == person.id } != null) {
        this.map { payer ->
            if (payer.person.id == person.id) {
                payer.copy(amount = payedAmount)
            } else {
                payer
            }
        }
    } else {
        this + Payer(person, payedAmount)
    }

private fun List<Receiver>.update(receiver: Receiver): List<Receiver> =
    if (receiver.isChecked)
        checkReceiver(receiver)
    else
        uncheckRecever(receiver)

private fun List<Receiver>.checkReceiver(receiver: Receiver) =
    if (this.find { it.person.id == receiver.person.id } != null) {
        this.map { it ->
            if (it.person.id == receiver.person.id) {
                receiver.copy()
            } else {
                it
            }
        }
    } else {
        this + receiver.copy()
    }

private fun List<Receiver>.uncheckRecever(receiver: Receiver) = filter { it.person.id != receiver.person.id }
