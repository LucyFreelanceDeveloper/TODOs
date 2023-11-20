import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.samples.todos.model.Priority
import org.samples.todos.model.Task
import org.samples.todos.repository.TaskRepository
import org.samples.todos.service.TaskManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


class TodosApp {
    val TASK_FILE_PATH = "src/main/resources/tasks.json"

    private val scanner: Scanner = Scanner(System.`in`)

    private val objectMapper = jacksonObjectMapper()
    private val taskRepository = TaskRepository(TASK_FILE_PATH, objectMapper)
    private val taskManager = TaskManager(taskRepository)
    fun run() {
        while (true) {
            println("------ TODOS MENU ------")
            println("1. List of all tasks")
            println("2. Create task")
            println("3. Update task")
            println("4. Delete task")
            println("5. Set done to task")
            println("6. Get tasks by group name")
            println("7. Get tasks by group name and priority")
            println("8. Get tasks by group name and state of done")
            println("9. Get tasks by group name and older than date")
            println("10. Exit")

            print("Select an option:")
            val choice = scanner.nextLine()

            if (choice.isNumeric()) {
                when (choice.toInt()) {
                    1 -> displayAllTasks()
                    2 -> createTask()
                    3 -> updateTask()
                    4 -> deleteTask()
                    5 -> setDone()
                    6 -> getTasksByGroupName()
                    7 -> getTasksByGroupNameAndPriority()
                    8 -> getTasksByGroupNameAndDone()
                    9 -> getTasksByGroupNameAndDate()
                    10 -> exit()
                    else -> println("Invalid choice. Select again.")
                }
            } else {
                System.err.println("Validation error: choice must be number like (1-10).")
            }
        }
    }

    private fun displayAllTasks() {
        val taskGroups = taskManager.getAll()

        for (taskGroup in taskGroups.flatten()) {
            println("----------------------------")
            println("----------------------------")
            println("Task group:${taskGroup.name}")
            println("----------------------------")

            taskGroup.tasks.forEach { task ->
                println("id=${task.id}, title=${task.title}, description=${task.description}, priority=${task.priority}, done=${task.done}, createDate=${task.createDate})")
            }
            println("")
            println("")
        }
    }


    private fun createTask() {
        // Read input
        print("Title: ")
        val title = scanner.nextLine()
        print("Description: ")
        val description = scanner.nextLine()
        print("Group:")
        val group = scanner.nextLine()
        print("Priority (LOW, MEDIUM, HIGH): ")
        val priority = scanner.nextLine()

        // Validation
        if (title.isEmpty()) {
            System.err.println("Validation error: Title must not be empty.")
            return
        }
        if (description.isEmpty()) {
            System.err.println("Validation error: Description must not be empty.")
            return
        }
        if (group.isEmpty()) {
            System.err.println("Validation error: Group must not be empty.")
            return
        }
        if (priority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.")
            return
        }
        if (!listOf("LOW", "MEDIUM", "HIGH").contains(priority.uppercase(Locale.getDefault()))) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.")
            return
        }
        val task = Task(
            UUID.randomUUID(),
            title,
            description,
            Priority.valueOf(priority.uppercase(Locale.getDefault())),
            false,
            Date()
        )
        println(task)
        val result = taskManager.createTask(task, group)
        if (result) {
            println("Successful create task")
        } else {
            println("Failed create task")
        }
    }

    private fun updateTask() {
        // Read input
        print("ID task for update: ")
        val taskId = scanner.nextLine()
        print("New title: ")
        val newTitle = scanner.nextLine()
        print("New description: ")
        val newDescription = scanner.nextLine()
        print("New priority (LOW, MEDIUM, HIGH): ")
        val newPriority = scanner.nextLine()

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.")
            return
        }
        if (newTitle.isEmpty()) {
            System.err.println("Validation error: Title must not be empty.")
            return
        }
        if (newDescription.isEmpty()) {
            System.err.println("Validation error: Description must not be empty.")
            return
        }
        if (newPriority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.")
            return
        }
        if (!listOf("LOW", "MEDIUM", "HIGH").contains(newPriority.uppercase(Locale.getDefault()))) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.")
            return
        }
        try {
            UUID.fromString(taskId)
        } catch (e: Exception) {
            System.err.println(
                "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)"
            )
            return
        }
        val task = Task(
            UUID.fromString(taskId),
            newTitle,
            newDescription,
            Priority.valueOf(newPriority.uppercase(Locale.getDefault())),
            false,
            Date()
        )
        val result = taskManager.updateTask(task)
        if (result) {
            println("Successful update task")
        } else {
            println("Failed update task")
        }
    }

    private fun deleteTask() {
        // Read input
        print("Write ID of task for delete: ")
        val taskId = scanner.nextLine()

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.")
            return
        }
        try {
            UUID.fromString(taskId)
        } catch (e: Exception) {
            System.err.println(
                "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)"
            )
            return
        }
        val result = taskManager.deleteTask(UUID.fromString(taskId))
        if (result) {
            println("Successful delete task")
        } else {
            println("Failed delete task")
        }
    }

    private fun setDone() {
        // Read input
        print("Write ID of task for setting task to done: ")
        val taskId = scanner.nextLine()

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.")
            return
        }
        try {
            UUID.fromString(taskId)
        } catch (e: Exception) {
            System.err.println(
                "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)"
            )
            return
        }
        val result = taskManager.setDone(UUID.fromString(taskId))
        if (result) {
            println("Successful set done to task")
        } else {
            println("Failed set done to task")
        }
    }

    private fun getTasksByGroupName() {
        // Read input
        print("Write group name of task you want to filter by: ")
        val groupName = scanner.nextLine()

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.")
            return
        }
        val tasksByGroupName: List<Task> = taskManager.getBy(groupName)

        tasksByGroupName.forEach { task ->
            println(task)
        }
    }

    private fun getTasksByGroupNameAndPriority() {
        // Read input
        print("Write group name of task you want to filter by: ")
        val groupName = scanner.nextLine()
        print("Write priority of task you want to filter by: ")
        val priority = scanner.nextLine()

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.")
            return
        }
        if (priority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.")
            return
        }
        if (!listOf("LOW", "MEDIUM", "HIGH").contains(priority.uppercase(Locale.getDefault()))) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.")
            return
        }
        val tasksByGroupNameAndPriority: List<Task> =
            taskManager.getBy(groupName, Priority.valueOf(priority.uppercase(Locale.getDefault())))

        tasksByGroupNameAndPriority.forEach { task ->
            println(task)
        }
    }

    private fun getTasksByGroupNameAndDone() {
        // Read input
        print("Write group name of task you want to filter by: ")
        val groupName = scanner.nextLine()
        print("Write state of done of task you want to filter by: ")
        val done = scanner.nextLine()

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.")
            return
        }
        if (!listOf("false", "true").contains(done)) {
            System.err.println("Validation error: done must contains value of true or false.")
            return
        }
        val tasksByGroupNameAndDone: List<Task> = taskManager.getBy(groupName, done.toBoolean())

        tasksByGroupNameAndDone.forEach { task ->
            println(task)
        }
    }

    private fun getTasksByGroupNameAndDate() {
        // Read input
        print("Write group name of task you want to filter by: ")
        val groupName = scanner.nextLine()
        print("Write date of task you want to filter by (pattern: yyyy-MM-dd): ")
        val date = scanner.nextLine()

        // Validation
        val parsedDate: Date
        parsedDate = try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.parse(date)
        } catch (e: Exception) {
            System.err.printf("Validation error: Date is not parsable: %s%n", date)
            return
        }
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.")
            return
        }
        val tasksByGroupNameAndDate: List<Task> = taskManager.getBy(groupName, parsedDate)

        tasksByGroupNameAndDate.forEach { task ->
            println(task)
        }
    }

    private fun exit() {
        taskManager.saveTasksToFile()
        println("Exit app. Thank you!")
        exitProcess(0)
    }


    private fun String.isNumeric(): Boolean {
        return this.matches("-?\\d+(\\.\\d+)?".toRegex())
    }
}