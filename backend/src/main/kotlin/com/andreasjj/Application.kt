package com.andreasjj

import io.micronaut.runtime.Micronaut.*
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
            title = "backend",
            version = "0.1"
    ),
    servers = [
        Server(
            url = "/api/",
            description = "Dev server"
        )
    ]
)
object Api {
}
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.andreasjj")
		.start()
}

