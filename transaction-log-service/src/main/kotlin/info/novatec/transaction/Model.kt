package info.novatec.transaction

import java.util.UUID


data class Transaction(
    val transactionId: UUID?,
    val orderId: UUID,
    val userId: UUID,
    val items: List<String>
)
