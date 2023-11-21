package org.samples.todos.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import junit.framework.TestCase;
import org.samples.todos.model.Priority;
import org.samples.todos.model.Task;
import org.samples.todos.model.TaskGroup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class TaskRepositoryTest extends TestCase {

    private static final String TASKS_FILE_NAME = "src/test/resources/tasks.json";
    private static final String TASKS_FILE_NAME_FOR_SAVE = "src/test/resources/tasksTestSave.json";
    private final ObjectMapper mapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public TaskRepositoryTest(){
        this.mapper = new ObjectMapper();
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Prague"));
    }

    public void testLoad() throws ParseException {
        final List<TaskGroup> expectedResult = createExpectedResult1();
        final List<TaskGroup> actualResult;

        TaskRepository taskRepository = new TaskRepository(TASKS_FILE_NAME, mapper);
        actualResult = taskRepository.load();

        assertEquals(expectedResult, actualResult);
    }

    public void testSave() throws ParseException, IOException {
        final String expectedResult = createExpectedResult2();
        final String actualResult;

        TaskRepository taskRepository = new TaskRepository(TASKS_FILE_NAME_FOR_SAVE, mapper);
        taskRepository.save(createSampleTasks());

        try (InputStream inputStream = Files.newInputStream(Paths.get(TASKS_FILE_NAME_FOR_SAVE))) {
            actualResult = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        assertEquals(expectedResult.replaceAll("\\s", ""), actualResult.replaceAll("\\s", ""));
        new File(TASKS_FILE_NAME_FOR_SAVE).delete();
    }

    private List<TaskGroup> createExpectedResult1() throws ParseException {
        return createSampleTasks();
    }

    private String createExpectedResult2() throws ParseException, JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(createSampleTasks());
    }

    private List<TaskGroup> createSampleTasks() throws ParseException{
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

        personalGroup.setTasks(List.of(task1, task2));

        // Druhá TaskGroup
        TaskGroup workGroup = new TaskGroup();
        workGroup.setName("Work");

        workGroup.setTasks(List.of(task1, task2));

        taskGroups.add(personalGroup);
        taskGroups.add(workGroup);

        return taskGroups;
    }
}