package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.dao.dao
import com.example.models.CreateTodo
import com.example.models.Todo
import com.example.plugins.ExposedUser
import io.ktor.http.*
import io.ktor.server.request.*

fun Route.todoRouting() {
    route("/todos") {
        get {
            call.respond(dao.allTodos())
        }
        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = dao.todo(id)
            if (todo != null) {
                call.respond(HttpStatusCode.OK, todo)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            val requestTodo = call.receive<CreateTodo>()
            val todo = dao.addNewTodo(requestTodo.title, requestTodo.description)
            call.respond(HttpStatusCode.Created)
        }
        put("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = call.receive<CreateTodo>()
            dao.editTodo(id, todo.title, todo.description)
            call.respond(HttpStatusCode.OK)
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            dao.deleteTodo(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}