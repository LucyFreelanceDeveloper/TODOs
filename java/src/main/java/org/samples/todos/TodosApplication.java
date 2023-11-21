package org.samples.todos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.samples.todos.model.Priority;
import org.samples.todos.model.Task;
import org.samples.todos.model.TaskGroup;
import org.samples.todos.repository.TaskRepository;
import org.samples.todos.service.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TodosApplication {

    private static final String TASK_FILE_PATH = TodosApplication.class.getClassLoader().getResource("tasks.json")
            .getPath();
    private final TaskManager taskManager;

    private final Scanner scanner;

    public TodosApplication() {
        ObjectMapper objectMapper = new ObjectMapper();
        TaskRepository taskRepository = new TaskRepository(TASK_FILE_PATH, objectMapper);
        taskManager = new TaskManager(taskRepository);
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("------ TODOS MENU ------");
            System.out.println("1. List of all tasks");
            System.out.println("2. Create task");
            System.out.println("3. Update task");
            System.out.println("4. Delete task");
            System.out.println("5. Set done to task");
            System.out.println("6. Get tasks by group name");
            System.out.println("7. Get tasks by group name and priority");
            System.out.println("8. Get tasks by group name and state of done");
            System.out.println("9. Get tasks by group name and older than date");
            System.out.println("10. Exit");

            System.out.print("Select an option:");
            String choice = scanner.nextLine();

            if (StringUtils.isNumeric(choice)) {
                switch (Integer.parseInt(choice)) {
                    case 1 -> displayAllTasks();
                    case 2 -> createTask();
                    case 3 -> updateTask();
                    case 4 -> deleteTask();
                    case 5 -> setDone();
                    case 6 -> getTasksByGroupName();
                    case 7 -> getTasksByGroupNameAndPriority();
                    case 8 -> getTasksByGroupNameAndDone();
                    case 9 -> getTasksByGroupNameAndDate();
                    case 10 -> exit();
                    default -> System.out.println("Invalid choice. Select again.");
                }
            } else {
                System.err.println("Validation error: choice must be number like (1-10).");
            }
        }
    }

    private void displayAllTasks() {
        List<TaskGroup> taskGroups = taskManager.getAll();

        for (var taskGroup : taskGroups) {
            System.out.println("-----");
            System.out.printf("Task group: %s%n", taskGroup.getName());
            System.out.println("-----");
            for (var task : taskGroup.getTasks()) {
                System.out.println(task);
            }
        }
    }

    private void createTask() {
        // Read input
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Group:");
        String group = scanner.nextLine();
        System.out.print("Priority (LOW, MEDIUM, HIGH): ");
        String priority = scanner.nextLine();

        // Validation
        if (title.isEmpty()) {
            System.err.println("Validation error: Title must not be empty.");
            return;
        }
        if (description.isEmpty()) {
            System.err.println("Validation error: Description must not be empty.");
            return;
        }
        if (group.isEmpty()) {
            System.err.println("Validation error: Group must not be empty.");
            return;
        }
        if (priority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.");
            return;
        }
        if (!List.of("LOW", "MEDIUM", "HIGH").contains(priority.toUpperCase())) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.");
            return;
        }

        Task task = new Task(UUID.randomUUID(),
                title,
                description,
                Priority.valueOf(priority.toUpperCase()),
                false,
                new Date());
        System.out.println(task);
        boolean result = taskManager.createTask(task, group);
        if (result) {
            System.out.println("Successful create task");
        } else {
            System.out.println("Failed create task");
        }
    }

    private void updateTask() {
        // Read input
        System.out.print("ID task for update: ");
        String taskId = scanner.nextLine();
        System.out.print("New title: ");
        String newTitle = scanner.nextLine();
        System.out.print("New description: ");
        String newDescription = scanner.nextLine();
        System.out.print("New priority (LOW, MEDIUM, HIGH): ");
        String newPriority = scanner.nextLine();

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.");
            return;
        }
        if (newTitle.isEmpty()) {
            System.err.println("Validation error: Title must not be empty.");
            return;
        }
        if (newDescription.isEmpty()) {
            System.err.println("Validation error: Description must not be empty.");
            return;
        }
        if (newPriority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.");
            return;
        }
        if (!List.of("LOW", "MEDIUM", "HIGH").contains(newPriority.toUpperCase())) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.");
            return;
        }

        try {
            UUID.fromString(taskId);
        } catch (Exception e) {
            System.err.println(
                    "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)");
            return;
        }

        Task task = new Task(UUID.fromString(taskId),
                newTitle,
                newDescription,
                Priority.valueOf(newPriority.toUpperCase()),
                false,
                new Date());
        boolean result = taskManager.updateTask(task);
        if (result) {
            System.out.println("Successful update task");
        } else {
            System.out.println("Failed update task");
        }
    }

    private void deleteTask() {
        // Read input
        System.out.print("Write ID of task for delete: ");
        String taskId = scanner.nextLine();

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.");
            return;
        }
        try {
            UUID.fromString(taskId);
        } catch (Exception e) {
            System.err.println(
                    "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)");
            return;
        }

        boolean result = taskManager.deleteTask(UUID.fromString(taskId));
        if (result) {
            System.out.println("Successful delete task");
        } else {
            System.out.println("Failed delete task");
        }
    }

    private void setDone() {
        // Read input
        System.out.print("Write ID of task for setting task to done: ");
        String taskId = scanner.nextLine();

        // Validation
        if (taskId.isEmpty()) {
            System.err.println("Validation error: ID must not be empty.");
            return;
        }
        try {
            UUID.fromString(taskId);
        } catch (Exception e) {
            System.err.println(
                    "Validation error: Not parsable UUID write correct id of task you want to modify like (c5e128d1-24bf-4a4b-974d-72cbba71f9d3)");
            return;
        }

        boolean result = taskManager.setDone(UUID.fromString(taskId));
        if (result) {
            System.out.println("Successful set done to task");
        } else {
            System.out.println("Failed set done to task");
        }
    }

    private void getTasksByGroupName() {
        // Read input
        System.out.print("Write group name of task you want to filter by: ");
        String groupName = scanner.nextLine();

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.");
            return;
        }

        List<Task> tasksByGroupName = taskManager.getBy(groupName);
        if (tasksByGroupName != null) {
            System.out.println(tasksByGroupName);
        } else {
            System.out.println("Failed to load tasks by group name.");
        }
    }

    private void getTasksByGroupNameAndPriority() {
        // Read input
        System.out.print("Write group name of task you want to filter by: ");
        String groupName = scanner.nextLine();
        System.out.print("Write priority of task you want to filter by: ");
        String priority = scanner.nextLine();

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.");
            return;
        }
        if (priority.isEmpty()) {
            System.err.println("Validation error: Priority must not be empty.");
            return;
        }
        if (!List.of("LOW", "MEDIUM", "HIGH").contains(priority.toUpperCase())) {
            System.err.println("Validation error: Priority must contains value of LOW, MEDIUM or HIGH.");
            return;
        }

        List<Task> tasksByGroupNameAndPriority = taskManager.getBy(groupName, Priority.valueOf(priority.toUpperCase()));
        if (tasksByGroupNameAndPriority != null) {
            System.out.println(tasksByGroupNameAndPriority);
        } else {
            System.out.println("Failed to load tasks by group name and priority.");
        }
    }

    private void getTasksByGroupNameAndDone() {
        // Read input
        System.out.print("Write group name of task you want to filter by: ");
        String groupName = scanner.nextLine();
        System.out.print("Write state of done of task you want to filter by: ");
        String done = scanner.nextLine();

        // Validation
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.");
            return;
        }
        if (!List.of("false", "true").contains(done)) {
            System.err.println("Validation error: done must contains value of true or false.");
            return;
        }

        List<Task> tasksByGroupNameAndDone = taskManager.getBy(groupName, Boolean.parseBoolean(done));
        if (tasksByGroupNameAndDone != null) {
            System.out.println(tasksByGroupNameAndDone);
        } else {
            System.out.println("Failed to load tasks by group name and state of done.");
        }
    }

    private void getTasksByGroupNameAndDate() {
        // Read input
        System.out.print("Write group name of task you want to filter by: ");
        String groupName = scanner.nextLine();
        System.out.print("Write date of task you want to filter by (pattern: yyyy-MM-dd): ");
        String date = scanner.nextLine();

        // Validation
        Date parsedDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            parsedDate = dateFormat.parse(date);
        } catch (Exception e) {
            System.err.printf("Validation error: Date is not parsable: %s%n", date);
            return;
        }
        if (groupName.isEmpty()) {
            System.err.println("Validation error: Group name must not be empty.");
            return;
        }

        List<Task> tasksByGroupNameAndDate = taskManager.getBy(groupName, parsedDate);
        if (tasksByGroupNameAndDate != null) {
            System.out.println(tasksByGroupNameAndDate);
        } else {
            System.out.println("Failed to load tasks by group name and older than given date.");
        }
    }

    private void exit() {
        taskManager.saveTasksToFile();
        System.out.println("Exit app. Thank you!");
        System.exit(0);
    }

    public static void main(final String[] args) {
        System.out.println("Start TodosApplication");
        TodosApplication app = new TodosApplication();
        app.run();
        System.out.println("Finished TodosApplication");
    }
}
