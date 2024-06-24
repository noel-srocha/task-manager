package noelsrocha.dev.plugins

import io.ktor.server.application.*
import noelsrocha.dev.tasks.repositories.TaskRepository
import noelsrocha.dev.tasks.repositories.TaskRepositoryImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    val taskModule = module {
        single<TaskRepository> { TaskRepositoryImpl() }
    }

    install(Koin) {
        slf4jLogger()
        modules(taskModule)
    }
}