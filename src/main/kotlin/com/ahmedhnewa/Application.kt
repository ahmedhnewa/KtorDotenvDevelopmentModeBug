package com.ahmedhnewa

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ahmedhnewa.plugins.*
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory

lateinit var dotenv: Dotenv

fun main() {
    dotenv = dotenv()
    // will not crash
    println(dotenv["SOME_VARIABLE"])
    embeddedServer(
        factory = Netty,
        environment = applicationEngineEnvironment {
            developmentMode = true
            connector { port = 8080 }
            watchPaths = listOf("classes", "resources")
            module(Application::module)
        },
    ).start(wait = true)

    // if the application was running using this code instead, and the development mode set to true in build.gradle.kts
    // then the issue will not happen
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    println(environment.developmentMode)
    // this line will crash the app
    println(dotenv["SOME_VARIABLE"])
    configureAdministration()
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
