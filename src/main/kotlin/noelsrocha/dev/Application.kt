package noelsrocha.dev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import noelsrocha.dev.plugins.*
import noelsrocha.dev.tasks.routes.configureTaskRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDependencyInjection()
    configureRouting()
    configureTaskRouting()
}
