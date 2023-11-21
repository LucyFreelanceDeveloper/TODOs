package org.samples.todos.service

import org.samples.todos.model.Priority
import org.samples.todos.model.Task
import org.samples.todos.model.TaskGroup
import org.samples.todos.repository.TaskRepository
import java.util.*

class TaskManager(private val taskRepository: TaskRepository) {

    val taskGroups: MutableList<TaskGroup> = taskRepository.load()

    fun createTask(task: Task, groupName: String): Boolean {
        taskGroups.firstOrNull { it.name == groupName }?.tasks?.add(task)

        if (taskGroups.none { it.name == groupName }) {
            println("Did not find given name of group: creating new group")
            val newTaskGroup = TaskGroup(groupName, mutableListOf(task))
            taskGroups.add(newTaskGroup)
        }
        return true
    }

    fun updateTask(task: Task): Boolean {
        for (taskGroup: TaskGroup in taskGroups) {
            for (existingTask: Task in taskGroup.tasks) {
                if (existingTask.id == task.id) {
                    existingTask.title = task.title
                    existingTask.description = task.description
                    existingTask.priority = task.priority
                    existingTask.done = task.done
                    existingTask.createDate = task.createDate
                    return true
                }
            }
        }
        return false
    }

    fun deleteTask(id: UUID): Boolean = taskGroups.any {
        it.tasks.removeIf { it.id == id }
    }

    fun setDone(id: UUID): Boolean {
        val task = taskGroups
            .flatMap { it.tasks }
            .firstOrNull { it.id == id }

        if (task != null) {
            task.done = true
            return true
        } else {
            return false
        }
    }

    fun getAll(): List<TaskGroup> = taskGroups.toList()

    fun getBy(groupName: String): List<Task> = taskGroups.firstOrNull { it.name == groupName }?.tasks ?: emptyList()

    fun getBy(groupName: String, priority: Priority): List<Task> {
        return taskGroups
            .filter { it.name == groupName }
            .flatMap { it.tasks }
            .filter { it.priority == priority }
    }

    fun getBy(groupName: String, done: Boolean): List<Task> {
        return taskGroups
            .filter { it.name == groupName }
            .flatMap { it.tasks }
            .filter { it.done == done }
    }

    fun getBy(groupName: String, olderThan: Date): List<Task> {
        return taskGroups
            .filter { it.name == groupName }
            .flatMap { it.tasks }
            .filter { it.createDate.before(olderThan) }
    }

    fun saveTasksToFile() {
        taskRepository.save(taskGroups)
    }
}
