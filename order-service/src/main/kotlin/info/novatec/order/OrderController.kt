package info.novatec.order

import io.github.resilience4j.retry.annotation.Retry
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/orders")
    fun placeOrder(@RequestBody order: Order): Result {
        val transactionId = orderService.createOrder(order)
        return Result(transactionId)
    }

    data class Result(val transactionId: UUID?)
}

