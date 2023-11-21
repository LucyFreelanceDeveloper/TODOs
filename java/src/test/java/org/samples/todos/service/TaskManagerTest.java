package org.samples.todos.service;

import junit.framework.TestCase;
import org.mockito.Mockito;
import org.samples.todos.model.Priority;
import org.samples.todos.model.Task;
import org.samples.todos.model.TaskGroup;
import org.samples.todos.repository.TaskRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerTest extends TestCase {

    private TaskManager taskManager;

    private TaskRepository taskRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


    @Override
    protected void setUp() throws ParseException {
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Prague"));
        taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.load()).thenReturn(createSampleTasks());
        taskManager = new TaskManager(taskRepository);

    }

    public void testCreateTask() throws ParseException {
        List<TaskGroup> expectedResult = createExpectedResult1();
        List<TaskGroup> actualResult;

        boolean result = taskManager.createTask(createSampleTask(), "Personal");
        actualResult = taskManager.getAll();

        assertTrue(result);
        assertEquals(expectedResult, actualResult);
    }


    public void testUpdateTask() {
        Task task = taskManager.getAll().stream().filter(g -> g.getName().equals("Personal")).findFirst().get().getTasks().get(0);
        task.setTitle("Test update task");

        boolean result = taskManager.updateTask(task);

        Task taskAfterUpdate = taskManager.getAll().stream().filter(g -> g.getName().equals("Personal")).findFirst().get().getTasks().get(0);

        assertTrue(result);
        assertEquals("Test update task", taskAfterUpdate.getTitle());
    }

    public void testDeleteTask() {
        final UUID deleteTaskId = UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d3");

        boolean result = taskManager.deleteTask(deleteTaskId);

        List<UUID> uuids = taskManager.getAll()
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getId)
                .collect(Collectors.toList());

        assertTrue(result);
        assertFalse(uuids.contains(deleteTaskId));
    }

    public void testSetDone() {
        UUID taskId = UUID.fromString("f327f935-89c9-43b2-9f18-87ac967035a6");

        taskManager.setDone(taskId);
        List<TaskGroup> tasks = taskManager.getAll();

        Task task = tasks
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .filter(x -> x.getId().equals(taskId))
                .findFirst().get();

        assertEquals(taskId, task.getId());
        assertTrue(task.isDone());
    }

    public void testGetAll() throws ParseException {
        assertEquals(createSampleTasks(), taskManager.getAll());
    }

    public void testGetByGroupName() throws ParseException {
        List<Task> expectedResult = createSampleTasks().get(0).getTasks();
        List<Task> actualResult = taskManager.getBy("Personal");
        assertEquals(expectedResult, actualResult);
    }

    public void testGetByGroupNameAndPriority() throws ParseException {
        List<Task> actualResult = taskManager.getBy("Personal", Priority.LOW);

        List<Task> personalTasks = createSampleTasks().get(0).getTasks();
        List<Task> expectedResult = new ArrayList<>();

        for (Task task : personalTasks) {
            if (task.getPriority() == Priority.LOW) {
                expectedResult.add(task);
            }
        }
        assertEquals(actualResult, expectedResult);
    }

    public void testGetByGroupNameAndDone() throws ParseException {
        List<Task> actualResult = taskManager.getBy("Personal", true);

        List<Task> personalTasks = createSampleTasks().get(0).getTasks();
        List<Task> expectedResult = new ArrayList<>();

        for (Task task : personalTasks) {
            if (task.isDone()) {
                expectedResult.add(task);
            }
        }
        assertEquals(actualResult, expectedResult);
    }

    public void testGetByGroupNameAndOlderThan() throws ParseException {
        Date olderThanDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2023-08-03T09:00:00Z");

        List<Task> actualResult = taskManager.getBy("Personal", olderThanDate);

        List<Task> personalTasks = createSampleTasks().get(0).getTasks();
        List<Task> expectedResult = new ArrayList<>();

        for (Task task : personalTasks) {
            if (task.getCreateDate().before(olderThanDate)) {
                expectedResult.add(task);
            }
        }
        assertEquals(actualResult, expectedResult);
    }

    private List<TaskGroup> createSampleTasks() throws ParseException {
        List<TaskGroup> taskGroups = new ArrayList<>();

        // První TaskGroup
        TaskGroup personalGroup = new TaskGroup();
        personalGroup.setName("Personal");

        Task task1 = new Task();
        task1.setId(UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d3"));
        task1.setTitle("Task 1");
        task1.setDescription("Description for Task 1");
        task1.setPriority(Priority.LOW);
        task1.setDone(true);
        task1.setCreateDate(sdf.parse("2023-08-01T10:00:00Z"));

        Task task2 = new Task();
        task2.setId(UUID.fromString("f327f935-89c9-43b2-9f18-87ac967035a6"));
        task2.setTitle("Task 2");
        task2.setDescription("Description for Task 2");
        task2.setPriority(Priority.HIGH);
        task2.setDone(false);
        task2.setCreateDate(sdf.parse("2023-08-02T16:15:00Z"));

        personalGroup.setTasks(new ArrayList<>(List.of(task1, task2)));

        // Druhá TaskGroup
        TaskGroup workGroup = new TaskGroup();
        workGroup.setName("Work");

        workGroup.setTasks(new ArrayList<>(List.of(task1, task2)));

        taskGroups.add(personalGroup);
        taskGroups.add(workGroup);

        return taskGroups;
    }

    private Task createSampleTask() throws ParseException {
        Task task = new Task();
        task.setId(UUID.fromString("c5e128d1-24bf-4a4b-974d-72cbba71f9d7"));
        task.setTitle("Sample Task");
        task.setDescription("Description for Sample Task");
        task.setPriority(Priority.LOW);
        task.setDone(true);
        task.setCreateDate(sdf.parse("2023-08-01T10:00:00Z"));
        return task;
    }

    private List<TaskGroup> createExpectedResult1() throws ParseException {
        List<TaskGroup> taskGroups = createSampleTasks();

        TaskGroup personalTaskGroup = taskGroups.stream().filter(g -> g.getName().equals("Personal")).findFirst().get();
        List<Task> tasks = new ArrayList<>(personalTaskGroup.getTasks());
        tasks.add(createSampleTask());
        personalTaskGroup.setTasks(tasks);

        return taskGroups;
    }
}