package info.novatec.transaction

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean

@RestController
class TransactionLogController(
    @Value("\${transaction-log-service.feature-toggles.might-fail:false}") val mightFail: Boolean
) {
    private val log = logger {}

    @PostMapping("/transactions")
    fun createTransactionLog(@RequestBody transaction: Transaction): Result {
        failPseudoRandomly()
        val transactionId = randomUUID()
        val enrichedTransaction = transaction.copy(transactionId = transactionId)
        log.info { "Received transaction [$enrichedTransaction]" }
        return Result(transactionId)
    }

    data class Result(val transactionId: UUID)

    fun failPseudoRandomly() {
        if (mightFail && nextBoolean()) {
            log.error { "Failed to create transaction log" }
            error("Failed to create transaction log")
        }
    }
}

