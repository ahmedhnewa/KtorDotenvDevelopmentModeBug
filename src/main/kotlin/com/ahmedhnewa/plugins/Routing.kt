package com.ahmedhnewa.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.webjars.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.resources.*
import io.ktor.server.resources.Resources
import kotlinx.serialization.Serializable
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    
    install(Resources)
    install(DoubleReceive)
    install(AutoHeadResponse)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
        get<Articles> { article ->
            // Get all articles ...
            call.respond("List of articles sorted starting from ${article.sort}")
        }
        post("/double-receive") {
            val first = call.receiveText()
            val theSame = call.receiveText()
            call.respondText(first + " " + theSame)
        }
    }
}
@Serializable
@Resource("/articles")
class Articles(val sort: String? = "new")
