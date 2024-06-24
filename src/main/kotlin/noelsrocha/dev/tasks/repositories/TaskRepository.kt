package noelsrocha.dev.tasks.repositories

import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task

interface TaskRepository {
   suspend fun getTasks(): List<Task>
   suspend fun getTasksByPriority(priority: Priority): List<Task>
   suspend fun getTaskByName(name: String): Task?
   suspend fun saveTask(task: Task)
   suspend fun deleteTask(name: String): Boolean
}