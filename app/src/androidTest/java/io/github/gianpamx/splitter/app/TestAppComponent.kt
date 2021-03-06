package io.github.gianpamx.splitter.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.gianpamx.splitter.data.MockModule
import io.github.gianpamx.splitter.expense.ExpenseActivityTest
import io.github.gianpamx.splitter.groupexpenses.GroupExpensesActivityTest
import io.github.gianpamx.splitter.settleup.SettleUpActivityTest
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    MockModule::class
])
interface TestAppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent

    }

    fun inject(app: TestApp)

    fun inject(test: ExpenseActivityTest)
    fun inject(test: GroupExpensesActivityTest)
    fun inject(test: SettleUpActivityTest)
}
