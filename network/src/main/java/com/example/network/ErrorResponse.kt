package com.example.network

import java.lang.Exception

class ErrorResponse(
    override val message: String? = null,
    var name: String? = null,
    var code: Int = 0
): Exception() {
    override fun toString(): String {
        val error = ""
        return if (message != null && message.isNotEmpty())
           error + message
        else if (name != null)
            error + name
        else
            error.trim()
    }
}