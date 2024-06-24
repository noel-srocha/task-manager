package noelsrocha.dev.tasks.routes

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task
import noelsrocha.dev.tasks.repositories.TaskRepository
import org.koin.ktor.ext.inject

fun Application.configureTaskRouting() {
    val repository by inject<TaskRepository>()

    routing {
        route("/tasks") {
            get {
                val tasks = repository.getTasks()
                call.respond(tasks)
            }

            get("/byName/{taskName}") {
                val taskName = call.parameters["taskName"]
                if (taskName.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.getTaskByName(taskName)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(task)
            }

            get("/byPriority/{priority}") {
                val priorityAsText = call.parameters["priority"]
                if (priorityAsText.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val priority = Priority.valueOf(priorityAsText)
                    val tasks = repository.getTasksByPriority(priority)

                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(tasks)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
            }

            post {
                try {
                    val task = call.receive<Task>()
                    repository.saveTask(task)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                } catch (ex: Exception) {
                    call.respond(HttpStatusCode.InternalServerError)
                    return@post
                }
            }

            delete("/{taskName}") {
                val name = call.parameters["taskName"]
                if (name.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.deleteTask(name)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}