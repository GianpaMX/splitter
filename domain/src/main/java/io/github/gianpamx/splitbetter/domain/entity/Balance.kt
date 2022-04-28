package io.github.gianpamx.splitbetter.domain.entity

data class Balance(
    val person: Person,
    val spent: Money,
    val paid: Money,
    val breakdownRows: List<Row>,
) {

    sealed class Row {
        data class Borrower(
            val person: Person,
            val amount: Money,
        ) : Row()

        data class Lender(
            val person: Person,
            val amount: Money,
        ) : Row()
    }
}
