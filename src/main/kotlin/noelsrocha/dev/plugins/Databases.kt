package noelsrocha.dev.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        System.getenv("DB_URL"),
        user = System.getenv("DB_USERNAME"),
        password = System.getenv("DB_PASSWORD")
    )
}