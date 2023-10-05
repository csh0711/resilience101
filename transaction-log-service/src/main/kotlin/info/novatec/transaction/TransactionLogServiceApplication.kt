package info.novatec.transaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionLogServiceApplication

fun main(args: Array<String>) {
	runApplication<TransactionLogServiceApplication>(*args)
}
