package info.novatec.order

import java.util.UUID


data class Order(
    val orderId: UUID?,
    val userId: UUID,
    val items: List<OrderItem>
)

data class OrderItem(
    val itemId: UUID,
    val name: String,
    val quantity: Int
)
