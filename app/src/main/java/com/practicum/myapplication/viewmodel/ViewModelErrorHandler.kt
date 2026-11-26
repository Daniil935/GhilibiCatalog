package com.practicum.myapplication.viewmodel

import kotlinx.coroutines.CoroutineExceptionHandler

fun createErrorHandler(
    onError: (String) -> Unit,
    defaultMessage: String
): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        onError(throwable.message ?: defaultMessage)
    }
}