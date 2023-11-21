package org.samples.todos.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private Priority priority;
    private boolean done;
    private Date createDate;

    public Task() {
    }

    public Task(final UUID id, final String title, final String description,
            final Priority priority, final boolean done, final Date createDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.done = false;
        this.createDate = createDate;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", done=" + done +
                ", createDate=" + createDate +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return done == task.done && Objects.equals(id, task.id) && Objects.equals(title, task.title)
                && Objects.equals(description, task.description) && priority == task.priority
                && Objects.equals(createDate, task.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, priority, done, createDate);
    }
}
