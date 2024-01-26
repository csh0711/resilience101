package info.novatec.transaction

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextBoolean

@RestController
class TransactionLogController {

    private val log = logger {}

    @PostMapping("/transactions")
    fun createTransactionLog(@RequestBody transaction: Transaction): Result {
        if (doesFail()) {
            log.error { "Failed to create transaction log" }
            error("Failed to create transaction log")
        }
        val transactionId = randomUUID()
        val enrichedTransaction = transaction.copy(transactionId = transactionId)
        log.info { "Received transaction [$enrichedTransaction]" }
        return Result(transactionId)
    }

    data class Result(val transactionId: UUID)

    fun doesFail() = nextBoolean()
}

