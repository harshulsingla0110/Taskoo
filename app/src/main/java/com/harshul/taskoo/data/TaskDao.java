package com.harshul.taskoo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.harshul.taskoo.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM task_table ORDER BY due_date")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM task_table WHERE task_table.task == :id ")
    LiveData<Task> getId(long id);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
