package org.samples.todos.model;

import java.util.List;
import java.util.Objects;

public class TaskGroup {

    private String name;
    private List<Task> tasks;

    public TaskGroup() {
    }

    public TaskGroup(final String name, final List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TaskGroup taskGroup = (TaskGroup) o;
        return Objects.equals(name, taskGroup.name) && Objects.equals(tasks, taskGroup.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tasks);
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
