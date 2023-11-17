package org.samples.todos.model

data class TaskGroup(
    var name:String,
    var tasks: MutableList<Task>


)
