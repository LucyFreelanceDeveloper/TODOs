package org.samples.todos.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.samples.todos.model.Priority
import org.samples.todos.model.Task
import org.samples.todos.model.TaskGroup
import org.samples.todos.repository.TaskRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.assertFalse

class TaskManagerTest {

    private lateinit var taskManager: TaskManager

    private val taskRepository: TaskRepository
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    constructor() {
        sdf.timeZone = TimeZone.getTimeZone("Europe/Prague")
        taskRepository = Mockito.mock(TaskRepository::class.java)
        Mockito.`when`(taskRepository.load()).thenReturn(createSampleTasks())
    }

    @BeforeTest
    fun init() {
        taskManager = TaskManager(taskRepository)
    }
    @Test
    fun createTask() {
        val expectedResult: MutableList<TaskGroup> = createExpectedResult1()
        val actualResult: List<TaskGroup>

        val result = taskManager!!.createTask(createSampleTask(), "Personal")
        actualResult = taskManager!!.getAll()

        assertTrue(result)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateTask() {
        val task: Task = taskManager.getAll()
            .first { it.name == "Personal" }
            .tasks[0]

        task.title = "Test update task"

        val result: Boolean = taskManager.updateTask(task)

        val taskAfterUpdate = taskManager.getAll().first { it.name == "Personal" }.tasks[0]

        assertTrue(result)
        assertEquals("Test update task", taskAfterUpdate.title)
    }

    @Test
    fun deleteTask() {
        val deleteTaskId = UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d3")

        val result = taskManager.deleteTask(deleteTaskId)

        val uuids = taskManager.getAll().flatMap { it.tasks }.map { it.id }

        assertTrue(result)
        assertFalse(deleteTaskId in uuids)
    }

    @Test
    fun setDone() {
        val taskId = UUID.fromString("f327f935-89c9-43b2-9f18-87ac967035a6");

        taskManager.setDone(taskId)

        val task = taskManager.getAll().flatMap { it.tasks }.first { it.id == taskId }

        assertEquals(taskId, task.id)
        assertTrue(task.done)
    }

    @Test
    fun getAll() {
        assertEquals(createSampleTasks(), taskManager.getAll())
    }

    @Test
    fun getByGroupName() {
        val expectedResult = createSampleTasks()[0].tasks
        val actualResult = taskManager.getBy("Personal")
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getByGroupNameAndPriority() {
        val expectedResult = createSampleTasks()[0].tasks.filter { it.priority == Priority.LOW }
        val actualResult = taskManager.getBy("Personal", Priority.LOW)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getByGroupNameAndDone() {
        val expectedResult = createSampleTasks()[0].tasks.filter { it.done }
        val actualResult = taskManager.getBy("Personal", true)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getByGroupNameAndOlderThan() {
        val olderThanDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2023-08-03T09:00:00Z")

        val expectedResult = createSampleTasks()[0].tasks.filter { it.createDate.before(olderThanDate) }
        val actualResult = taskManager.getBy("Personal", olderThanDate)

        assertEquals(expectedResult, actualResult)
    }

    @Throws(ParseException::class)
    private fun createSampleTasks(): MutableList<TaskGroup> {
        val task1 = Task(
            UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d3"),
            "Task 1",
            "Description for Task 1",
            Priority.LOW,
            true,
            sdf.parse("2023-08-01T10:00:00Z")
        )

        val task2 = Task(
            UUID.fromString("f327f935-89c9-43b2-9f18-87ac967035a6"),
            "Task 2",
            "Description for Task 2",
            Priority.HIGH,
            false,
            sdf.parse("2023-08-02T16:15:00Z")
        )

        val task3 = Task(
            UUID.fromString("312437b1-416d-4e38-8c89-4b811ad67a6e"),
            "Task 3",
            "Description for Task 3",
            Priority.LOW,
            true,
            sdf.parse("2023-08-01T10:00:00Z")
        )

        val task4 = Task(
            UUID.fromString("bb90c86a-ad33-4700-bf08-cda65130025b"),
            "Task 4",
            "Description for Task 4",
            Priority.HIGH,
            false,
            sdf.parse("2023-08-02T16:15:00Z")
        )

        val personalGroup = TaskGroup("Personal", mutableListOf(task1, task2))
        val workGroup = TaskGroup("Work", mutableListOf(task3, task4))

        return mutableListOf(personalGroup, workGroup)
    }

    @Throws(ParseException::class)
    private fun createExpectedResult1(): MutableList<TaskGroup> {
        val taskGroups: MutableList<TaskGroup> = createSampleTasks()
        val personalTaskGroup = taskGroups.first { it.name == "Personal" }

        val tasks: MutableList<Task> = personalTaskGroup.tasks.toMutableList()
        tasks.add(createSampleTask())
        personalTaskGroup.tasks = tasks
        return taskGroups
    }

    @Throws(ParseException::class)
    private fun createSampleTask() = Task(
        UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d7"),
        "Sample Task",
        "Description for Sample Task",
        Priority.LOW,
        true,
        sdf.parse("2023-08-01T10:00:00Z")
    )
}