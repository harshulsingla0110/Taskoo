package com.harshul.taskoo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.util.TaskRoomDatabase;

import java.util.List;

public class TodoRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTask;

    public TodoRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTask = taskDao.getAll();
    }

    public LiveData<List<Task>> getAll() {
        return allTask;
    }

    public void insertTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.insertTask(task));
    }

    public LiveData<Task> getId(long id) {
        return taskDao.getId(id);
    }

    public void updateTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.updateTask(task));
    }

    public void deleteTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.deleteTask(task));
    }
}
