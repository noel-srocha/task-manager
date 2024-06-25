package noelsrocha.dev.tasks

import io.kotest.core.spec.style.DescribeSpec
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import noelsrocha.dev.plugins.configureDependencyInjection
import noelsrocha.dev.plugins.configureRouting
import noelsrocha.dev.plugins.configureSerialization
import noelsrocha.dev.tasks.models.Priority
import noelsrocha.dev.tasks.models.Task
import noelsrocha.dev.tasks.routes.configureTaskRouting
import kotlin.test.*

class TaskTest : DescribeSpec({
    describe("Task Context") {
        it("can be found by priority") {
            testApplication {
                application {
                    configureSerialization()
                    configureDependencyInjection()
                    configureRouting()
                    configureTaskRouting()
                }

                val client = createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }

                val response = client.get("/tasks/byPriority/MEDIUM")
                val results = response.body<List<Task>>()

                assertEquals(HttpStatusCode.OK, response.status)

                val expectedTaskNames = listOf("Task 2")
                val actualTaskNames = results.map(Task::name)
                assertContentEquals(expectedTaskNames, actualTaskNames)
            }
        }

        it("can be created") {
            testApplication {
                application {
                    configureSerialization()
                    configureDependencyInjection()
                    configureRouting()
                    configureTaskRouting()
                }

                val client = createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }

                val task = Task(name = "Create a Ktor REST API", description = "Programming Tutorial", priority = Priority.HIGH)
                val response = client.post("/tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)

                    setBody(task)
                }
                assertEquals(HttpStatusCode.Created, response.status)

                val tasks = client.get("/tasks")
                val taskNames = tasks.body<List<Task>>().map(Task::name)

                assertContains(taskNames, "Create a Ktor REST API")
            }
        }

        it("invalidates request with wrong priority") {
            testApplication {
                application {
                    configureSerialization()
                    configureDependencyInjection()
                    configureRouting()
                    configureTaskRouting()
                }

                val response = client.get("/tasks/byPriority/INVALID")
                assertEquals(HttpStatusCode.BadRequest, response.status)
            }
        }
    }
})