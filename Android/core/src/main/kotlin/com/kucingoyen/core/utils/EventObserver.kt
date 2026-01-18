package com.kucingoyen.core.utils


import androidx.compose.runtime.Composable
import androidx.lifecycle.Observer

class EventObserver<T>(
    private val onEventUnhandledContent: (T) -> Unit
) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }
}

@Composable
fun <T> Event<T>?.HandleEvent(content: @Composable (T) -> Unit) {
    this?.getContentIfNotHandled()?.let { content(it) }
}

fun Event<String>?.getValueContentViewModel(): String? {
    return this?.getContentIfNotHandled()
}