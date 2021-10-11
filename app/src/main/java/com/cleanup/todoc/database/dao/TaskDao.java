package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
//    @Query("SELECT * FROM Task WHERE projectId = :projectId")
    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    @Insert
    long insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    int deleteTask(long taskId);

    @Query("SELECT * FROM Task LIMIT 1")
    Task getAnyTask();
}
