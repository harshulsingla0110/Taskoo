package com.harshul.taskoo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.harshul.taskoo.data.TodoRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    public static TodoRepository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTasks = repository.getAll();
    }

    public static void insertTask(Task task) {
        repository.insertTask(task);
    }

    public static void updateTask(Task task) {
        repository.updateTask(task);
    }

    public static void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getId(long id) {
        return repository.getId(id);
    }


}
