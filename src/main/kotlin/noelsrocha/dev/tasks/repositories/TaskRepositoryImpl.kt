package noelsrocha.dev.tasks.repositories

import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task

class TaskRepositoryImpl : TaskRepository {
    override suspend fun getTasks(): List<Task> = tasks

    override suspend fun getTasksByPriority(priority: Priority): List<Task> = tasks.filter { it.priority == priority }

    override suspend fun getTaskByName(name: String): Task? = tasks.firstOrNull { it.name.equals(name, ignoreCase = true) }

    override suspend fun saveTask(task: Task) {
        if (getTaskByName(task.name) != null) {
            throw IllegalStateException("Cannot duplicate task names!")
        }
        tasks.add(task)
    }

    override suspend fun deleteTask(name: String): Boolean = tasks.removeIf { it.name == name }

    companion object {
        val tasks = mutableListOf(
            Task("Task 1", "Description 1", Priority.LOW),
            Task("Task 2", "Description 2", Priority.MEDIUM),
            Task("Task 3", "Description 3", Priority.HIGH),
            Task("Task 4", "Description 4", Priority.VITAL),
        )
    }
}