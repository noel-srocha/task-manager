package noelsrocha.dev.tasks.data.mappers

import kotlinx.coroutines.Dispatchers
import noelsrocha.dev.tasks.data.daos.TaskDAO
import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: TaskDAO) = Task(dao.name, dao.description, Priority.valueOf(dao.priority))