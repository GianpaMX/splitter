package io.github.gianpamx.splitter.app

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.gianpamx.splitter.expense.ExpenseActivity
import io.github.gianpamx.splitter.expense.ExpenseViewModel
import io.github.gianpamx.splitter.groupexpenses.GroupExpensesActivity
import io.github.gianpamx.splitter.groupexpenses.GroupExpensesViewModel
import io.github.gianpamx.splitter.settleup.SettleUpActivity
import io.github.gianpamx.splitter.settleup.SettleUpViewModel


@Module(includes = [ViewModelFactoryModule::class])
abstract class Binder {
    @ContributesAndroidInjector
    abstract fun bindExpenseActivity(): ExpenseActivity

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel::class)
    abstract fun bindExpenseViewModel(viewModel: ExpenseViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindGroupExpensesActivity(): GroupExpensesActivity

    @Binds
    @IntoMap
    @ViewModelKey(GroupExpensesViewModel::class)
    abstract fun bindGroupExpensesViewModel(viewModel: GroupExpensesViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindSettleUpActivity(): SettleUpActivity

    @Binds
    @IntoMap
    @ViewModelKey(SettleUpViewModel::class)
    abstract fun bindSettleUpViewModel(viewModel: SettleUpViewModel): ViewModel
}
