package org.samples.todos.service;

import org.samples.todos.model.Priority;
import org.samples.todos.model.Task;
import org.samples.todos.model.TaskGroup;
import org.samples.todos.repository.TaskRepository;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {

    private final TaskRepository taskRepository;
    private final List<TaskGroup> taskGroups;

    public TaskManager(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskGroups = taskRepository.load();
    }

    public boolean createTask(final Task task, final String groupName) {
        // Classic java code before stream and lambda
//        for (TaskGroup taskGroup : taskGroups) {
//            if (taskGroup.getName().equals(groupName)) {
//                List<Task> tasks = taskGroup.getTasks();
//                if (tasks != null) {
//                    tasks.add(task);
//                    return true;
//                }
//            }
//        }
//
//        System.out.println("Did not find given name of group: creating new group");
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(task);
//        TaskGroup newTaskGroup = new TaskGroup(groupName, tasks);
//        taskGroups.add(newTaskGroup);

//        return true;

        Optional<TaskGroup> existingGroup = taskGroups.stream()
                .filter(taskGroup -> taskGroup.getName().equals(groupName))
                .findFirst();

        if (existingGroup.isPresent()) {
            existingGroup.get().getTasks().add(task);
        } else {
            System.out.println("Did not find given name of group: creating new group");
            List<Task> tasks = new ArrayList<>();
            tasks.add(task);
            taskGroups.add(new TaskGroup(groupName, tasks));
        }
        return true;
    }

    public boolean updateTask(final Task task) {
        // Classic java code before stream and lambda
//        for (TaskGroup taskGroup : taskGroups) {
//            for (Task existingTask : taskGroup.getTasks()) {
//                if (existingTask.getId().equals(task.getId())) {
//                    existingTask.setTitle(task.getTitle());
//                    existingTask.setDescription((task.getDescription()));
//                    existingTask.setPriority(task.getPriority());
//                    existingTask.setDone(task.isDone());
//                    existingTask.setCreateDate(task.getCreateDate());
//                    return true;
//                }
//            }
//        }
//        return false;

        return taskGroups.stream()
                .flatMap(group -> group.getTasks().stream())
                .filter(existingTask -> existingTask.getId().equals(task.getId()))
                .findFirst()
                .map(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription((task.getDescription()));
                    existingTask.setPriority(task.getPriority());
                    existingTask.setDone(task.isDone());
                    existingTask.setCreateDate(task.getCreateDate());
                    return true;
                }).orElse(false);
    }

    public boolean deleteTask(final UUID id) {
        // Classic java code before stream and lambda
//        boolean deleted = false;
//
//        for (TaskGroup taskGroup : taskGroups) {
//            List<Task> updatedTasks = new ArrayList<>();
//
//            for (Task task : taskGroup.getTasks()) {
//                if (!task.getId().equals(id)) {
//                    updatedTasks.add(task);
//                } else {
//                    deleted = true;
//                }
//            }
//            taskGroup.setTasks(updatedTasks);
//        }
//
//        return deleted;

        boolean deleted = false;
        for(TaskGroup taskGroup: taskGroups) {
            boolean result = taskGroup.getTasks().removeIf(task -> task.getId().equals(id));
            if (result) {
                deleted = true;
            }
        }
        return deleted;
    }

    public boolean setDone(final UUID id) {
        // Classic java code before stream and lambda
//        for (TaskGroup taskGroup : taskGroups) {
//            for (Task task : taskGroup.getTasks()) {
//                if (task.getId().equals(id)) {
//                    task.setDone(true);
//                    return true;
//                }
//            }
//        }
//
//        return false;

        return taskGroups.stream()
                .flatMap(group -> group.getTasks().stream())
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> {
                    task.setDone(true);
                    return true;
                }).orElse(false);
    }

    public List<TaskGroup> getAll() {
        return new ArrayList<>(taskGroups);
    }

    public List<Task> getBy(final String groupName) {
        // Classic java code before stream and lambda
//        List<Task> tasks = new ArrayList<>();
//
//        for (TaskGroup taskGroup : taskGroups) {
//            if (taskGroup.getName().equals(groupName)) {
//                tasks = taskGroup.getTasks();
//            }
//        }
//        return tasks;

        return taskGroups.stream()
                .filter(group -> group.getName().equals(groupName))
                .map(TaskGroup::getTasks)
                .findFirst().orElse(Collections.emptyList());
    }

    public List<Task> getBy(final String groupName, final Priority priority) {
        // Classic java code before stream and lambda
//        List<Task> tasks = new ArrayList<>();
//
//        for (TaskGroup taskGroup : taskGroups) {
//            if (taskGroup.getName().equals(groupName)) {
//                for (Task task : taskGroup.getTasks()) {
//                    if (task.getPriority() == priority) {
//                        tasks.add(task);
//                    }
//                }
//            }
//        }
//        return tasks;

        return taskGroups.stream()
                .filter(group -> group.getName().equals(groupName))
                .flatMap(group -> group.getTasks().stream())
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getBy(final String groupName, final boolean done) {
        // Classic java code before stream and lambda
//        List<Task> tasks = new ArrayList<>();
//
//        for (TaskGroup taskGroup : taskGroups) {
//            if (taskGroup.getName().equals(groupName)) {
//                for (Task task : taskGroup.getTasks()) {
//                    if (task.isDone() == done) {
//                        tasks.add(task);
//                    }
//                }
//            }
//        }
//        return tasks;

        return taskGroups.stream()
                .filter(group -> group.getName().equals(groupName))
                .flatMap(group -> group.getTasks().stream())
                .filter(task -> task.isDone() == done)
                .collect(Collectors.toList());
    }

    public List<Task> getBy(final String groupName, final Date olderThan) {
        // Classic java code before stream and lambda
//        List<Task> tasks = new ArrayList<>();
//
//        for (TaskGroup taskGroup : taskGroups) {
//            if (taskGroup.getName().equals(groupName)) {
//                for (Task task : taskGroup.getTasks()) {
//                    if (task.getCreateDate().before(olderThan)) {
//                        tasks.add(task);
//                    }
//                }
//            }
//        }
//        return tasks;

        return taskGroups.stream()
                .filter(group -> group.getName().equals(groupName))
                .flatMap(group -> group.getTasks().stream())
                .filter(task -> task.getCreateDate().before(olderThan))
                .collect(Collectors.toList());
    }

    public void saveTasksToFile() {
        taskRepository.save(taskGroups);
    }
}
