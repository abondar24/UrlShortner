package org.abondar.experimental.config

import io.ktor.server.application.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun Application.configureKoin() {
    startKoin {
        modules(
            module { environment.config }
        )
    }

}