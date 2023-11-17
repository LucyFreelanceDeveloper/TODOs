package org.samples.todos.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.samples.todos.repository.TaskRepository

class TaskManagerTest {

    @Test
    fun createTask() {
        val taskRepository = TaskRepository("resources/tasks.json", jacksonObjectMapper())
        val taskManager = TaskManager(taskRepository)
        val task = """
            "tasks": [
            {
                "id": "c5e128d1-24bf-4a4b-974d-72cbba71f9d3",
                "title": "Task 1",
                "description": "Description for Task 1",
                "priority": "LOW",
                "done": true,
                "createDate": "2023-08-01T08:00:00Z"
            }
        """
        val expected = """{
            "name": "Work",
            "tasks": [
            {
                "id": "c5e128d1-24bf-4a4b-974d-72cbba71f9d3",
                "title": "Task 1",
                "description": "Description for Task 1",
                "priority": "LOW",
                "done": true,
                "createDate": "2023-08-01T08:00:00Z"
            },
            {
                "id": "f327f935-89c9-43b2-9f18-87ac967035a6",
                "title": "Task 2",
                "description": "Description for Task 2",
                "priority": "HIGH",
                "done": false,
                "createDate": "2023-08-02T14:15:00Z"
            }
            ]
        }"""

//        assertEquals(expected, taskManager.createTask(task, "Work"))
    }

    @Test
    fun updateTask() {
    }

    @Test
    fun deleteTask() {
    }

    @Test
    fun setDone() {
    }

    @Test
    fun getAll() {
    }

    @Test
    fun getBy() {
    }

    @Test
    fun testGetBy() {
    }

    @Test
    fun testGetBy1() {
    }

    @Test
    fun testGetBy2() {
    }

    @Test
    fun saveTasksToFile() {
    }
}