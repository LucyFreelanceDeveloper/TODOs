package org.samples.todos.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.samples.todos.model.TaskGroup
import java.io.File
import java.io.IOException


class TaskRepository (private val taskFileName: String, private val mapper: ObjectMapper){

    fun load(): MutableList<TaskGroup> {
        var taskGroups: MutableList<TaskGroup> = ArrayList()

        try {
            taskGroups = mapper.readValue(taskFileName);
            return taskGroups
        } catch (e: IOException) {
            System.err.printf(String.format("Failed to load tasks from file: %s%n", taskFileName));
            e.printStackTrace();
        }

        return taskGroups;
    }

    fun save(taskGroups: MutableList<TaskGroup>) {
        try {
            mapper.writeValue(File(taskFileName), taskGroups)
        } catch (e: IOException) {
            System.err.printf(String.format("Failed to save tasks to file: %s", taskFileName))
            e.printStackTrace()
        }
    }
}

