package org.samples.todos.repository

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.samples.todos.model.Priority
import org.samples.todos.model.Task
import org.samples.todos.model.TaskGroup
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TaskRepositoryTest {

    private val TASKS_FILE_NAME = "src/test/resources/tasks.json"
    private val TASKS_FILE_NAME_FOR_SAVE = "src/test/resources/tasksTestSave.json"
    private val mapper = jacksonObjectMapper()

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    init {
        sdf.timeZone = TimeZone.getTimeZone("Europe/Prague")
    }
    @Test
    fun load() {
        val expectedResult = createExpectedResult1()
        val actualResult: List<TaskGroup>

        val taskRepository = TaskRepository(TASKS_FILE_NAME, mapper)
        actualResult = taskRepository.load()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun save() {
        val expectedResult = createExpectedResult2()
        val actualResult: String

        val taskRepository = TaskRepository(TASKS_FILE_NAME_FOR_SAVE, mapper)
        taskRepository.save(createSampleTasks())

        Files.newInputStream(Paths.get(TASKS_FILE_NAME_FOR_SAVE)).use { inputStream ->
            actualResult = String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
        }

        assertEquals(expectedResult?.replace("\\s".toRegex(), ""), actualResult.replace("\\s".toRegex(), ""))
        File(TASKS_FILE_NAME_FOR_SAVE).delete()
    }

    @Throws(ParseException::class)
    private fun createSampleTasks(): MutableList<TaskGroup> {
        val task1 = Task(
            UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d3"),
            "Task 1",
            "Description for Task 1",
            Priority.MEDIUM,
            true,
            sdf.parse("2023-11-20T08:00:00Z")
        )

        val task2 = Task(
            UUID.fromString("f327f935-89c9-43b2-9f18-87ac967035a6"),
            "Task 2",
            "Description for Task 2",
            Priority.LOW,
            false,
            sdf.parse("2023-11-20T16:15:00Z")
        )

        val personalGroup = TaskGroup("Personal", mutableListOf(task1, task2))
        val workGroup = TaskGroup("Work", mutableListOf(task1, task2))

        return mutableListOf(personalGroup, workGroup)
    }

    @Throws(ParseException::class)
    private fun createExpectedResult1(): List<TaskGroup?>? {
        return createSampleTasks()
    }

    @Throws(ParseException::class, JsonProcessingException::class)
    private fun createExpectedResult2(): String? {
        val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
        return ow.writeValueAsString(createSampleTasks())
    }
}