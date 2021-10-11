package com.cleanup.todoc.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // Repositories
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // Data
    @Nullable
    private LiveData<Project> currentProject;
    private LiveData<List<Project>> currentProjects;

    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }

    public void init(long projectId) {
//        currentProjects = projectDataSource.getProjects();
        if (this.currentProject != null) { return; }
        currentProject = projectDataSource.getProject(projectId);
    }

    // ---------------------
    // For Project
    // ---------------------

    public LiveData<Project> getProject(long id) { return this.currentProject; }
    public LiveData<List<Project>> getProjects() { return projectDataSource.getProjects(); }

    // ---------------------
    // For Task
    // ---------------------

    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void createTask(Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> {
            taskDataSource.deleteTask(taskId);
        });
    }

    public void updateTask(Task task) {
        executor.execute(() -> {
            taskDataSource.updateTask(task);
        });
    }

    public Task getAnyTask() { return taskDataSource.getAnyTask(); }
}
