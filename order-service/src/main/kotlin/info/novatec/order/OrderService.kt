package info.novatec.order

import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
class OrderService(
    private val transactionLogServiceAccessor: TransactionLogServiceAccessor
) {

    fun createOrder(order: Order): UUID? {
        // Save in database etc.
        return transactionLogServiceAccessor.sendTransaction(order.enrichWithOrderId())
    }

    private fun Order.enrichWithOrderId() = this.copy(orderId = randomUUID())

}
