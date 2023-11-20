package org.samples.todos.service

import org.samples.todos.model.Priority
import org.samples.todos.model.Task
import org.samples.todos.model.TaskGroup
import org.samples.todos.repository.TaskRepository
import java.util.*


class TaskManager(private val taskRepository: TaskRepository) {

    private val taskGroups: MutableList<TaskGroup> = taskRepository.load()

    fun createTask(task: Task, groupName: String): Boolean {
        // Code v Kotlinu bez pouziti firstOrNull
//        for (taskGroup in taskGroups) {
//            if (taskGroup.name == groupName) {
//                val tasks = taskGroup.tasks
//                tasks.add(task)
//                return true
//            }
//        }

//        println("Did not find given name of group: creating new group")
//        val tasks = mutableListOf(task)
//        val newTaskGroup = TaskGroup(groupName, tasks)
//        taskGroups.add(newTaskGroup)

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
                    return true;
                }
            }
        }
        return false;
    }

    fun deleteTask(id: UUID): Boolean {
        //TODO: zjednodusit
        var deleted = false
        for (taskGroup in taskGroups) {
            val updatedTasks: MutableList<Task> = mutableListOf()
            for (task in taskGroup.tasks) {
                if (task.id != id){
                    updatedTasks.add(task)
                } else {
                    deleted = true
                }
            }
            taskGroup.tasks = updatedTasks
        }
        return deleted
    }

    fun setDone(id: UUID): Boolean {
        for (taskGroup: TaskGroup in taskGroups) {
            for (task: Task in taskGroup.tasks) {
                if (task.id != id) {
                    task.done = true;
                    return true;
                }
            }
        }

        return false
    }

    fun getAll(): MutableList<MutableList<TaskGroup>> {
        return mutableListOf(taskGroups)
    }

    fun getBy(groupName: String): MutableList<Task>{
        //TODO: Zjednodusit
        val tasks: MutableList<Task> = mutableListOf()

        for(taskGroup: TaskGroup in taskGroups){
            if(taskGroup.name == groupName){
                tasks.addAll(taskGroup.tasks)
            }
        }
        return tasks
    }

    fun getBy(groupName: String, priority: Priority): MutableList<Task>{
        //TODO: Zjednodusit
        val tasks: MutableList<Task> = mutableListOf()

        for(taskGroup: TaskGroup in taskGroups){
            if(taskGroup.name == groupName){
                for (task:Task in taskGroup.tasks){
                    if(task.priority == priority){
                        tasks.add(task)
                    }
                }
            }
        }
        return tasks
    }

    fun getBy(groupName: String, done: Boolean): MutableList<Task>{
        //TODO: Zjednodusit
        val tasks: MutableList<Task> = mutableListOf()

        for(taskGroup: TaskGroup in taskGroups){
            if(taskGroup.name == groupName){
                for (task:Task in taskGroup.tasks){
                    if(task.done == done){
                        tasks.add(task)
                    }
                }
            }
        }
        return tasks
    }

    fun getBy(groupName: String, olderThan: Date): MutableList<Task>{
        //TODO: Zjednodusit
        val tasks: MutableList<Task> = mutableListOf()

        for(taskGroup: TaskGroup in taskGroups){
            if(taskGroup.name == groupName){
                for (task:Task in taskGroup.tasks){
                    if(task.createDate.before(olderThan)){
                        tasks.add(task)
                    }
                }
            }
        }
        return tasks
    }

    fun saveTasksToFile() {
        taskRepository.save(taskGroups)
    }
}
