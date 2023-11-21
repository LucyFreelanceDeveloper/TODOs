package org.samples.todos.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.samples.todos.model.TaskGroup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final String taskFileName;
    private final ObjectMapper mapper;

    public TaskRepository(final String taskFileName, final ObjectMapper mapper) {
        this.taskFileName = taskFileName;
        this.mapper = mapper;
    }

    public List<TaskGroup> load() {
        List<TaskGroup> taskGroups = new ArrayList<>();

        try {
            taskGroups = mapper.readValue(Paths.get(taskFileName).toFile(), new TypeReference<>() {
            });
        } catch (IOException e) {
            System.err.printf(String.format("Failed to load tasks from file: %s%n", taskFileName));
            e.printStackTrace();
        }

        return taskGroups;
    }

    public void save(final List<TaskGroup> taskGroups) {
        try {
            mapper.writeValue(new File(taskFileName), taskGroups);
        } catch (IOException e) {
            System.err.printf(String.format("Failed to save tasks to file: %s", taskFileName));
            e.printStackTrace();
        }
    }
}
