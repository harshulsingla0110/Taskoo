package com.harshul.taskoo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "TASK_TABLE")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long taskId;

    private String task;

    private Priority priority;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "is_done")
    private Boolean isDone;

    private String desc;

    public Task() {
    }

    public Task(String task, Priority priority, Date dueDate, Date createdAt, Boolean isDone, String desc) {
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.isDone = isDone;
        this.desc = desc;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", createdAt=" + createdAt +
                ", isDone=" + isDone +
                '}';
    }
}