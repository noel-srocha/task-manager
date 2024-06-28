package noelsrocha.dev.tasks.repositories

import noelsrocha.dev.tasks.data.daos.TaskDAO
import noelsrocha.dev.tasks.data.mappers.daoToModel
import noelsrocha.dev.tasks.data.mappers.suspendTransaction
import noelsrocha.dev.tasks.data.tables.TaskTable
import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class TaskRepositoryImpl : TaskRepository {
    override suspend fun getTasks(): List<Task> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun getTasksByPriority(priority: Priority): List<Task> = suspendTransaction {
        TaskDAO.find { (TaskTable.priority eq priority.toString()) }.map(::daoToModel)
    }

    override suspend fun getTaskByName(name: String): Task? = suspendTransaction {
        TaskDAO.find { (TaskTable.name eq name) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun saveTask(task: Task) : Unit = suspendTransaction {
        TaskDAO.new {
            name = task.name
            description = task.description
            priority = task.priority.toString()
        }
    }

    override suspend fun deleteTask(name: String): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.name eq name
        }
        rowsDeleted == 1
    }
}