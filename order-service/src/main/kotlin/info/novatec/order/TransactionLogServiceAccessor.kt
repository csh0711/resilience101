package info.novatec.order

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.*

private val READ_TIMEOUT = Duration.ofSeconds(3L)

@Service
class TransactionLogServiceAccessor(
    webClientBuilder: WebClient.Builder
) {
    private val log = logger {}

    private val webClient = webClientBuilder
        .clientConnector(ReactorClientHttpConnector(HttpClient.create().responseTimeout(READ_TIMEOUT)))
        .baseUrl("http://localhost:8082")
        .build()

    @CircuitBreaker(name = "sendTransaction")
    fun sendTransaction(order: Order): UUID? {
        log.info { "Sending transaction for [$order]." }
        val result = webClient
            .post()
            .uri("/transactions")
            .bodyValue(order.toTransactionLog())
            .retrieve()
            .bodyToMono(Result::class.java)
            .doOnSuccess { log.info { "Received result [$it]." } }
            .onErrorMap { ex ->
                log.error(ex) { "Error while sending transaction [$order]." }
                ex
            }
            .block()
        return result?.transactionId
    }

    private fun Order.toTransactionLog() = TransactionLog(
        orderId = orderId!!,
        userId = userId,
        items = items.map { it.toString() }
    )

    private data class TransactionLog(
        val orderId: UUID,
        val userId: UUID,
        val items: List<String>
    )

    data class Result(val transactionId: UUID)
}
