package org.samples.todos.model

import java.util.Date
import java.util.UUID

data class Task(
    var id: UUID,
    var title: String,
    var description: String,
    var priority: Priority,
    var done: Boolean,
    var createDate: Date
)
