package com.cleanup.todoc.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // Repositories
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // Data
    @Nullable
//    private LiveData<Project> currentProject;
    private LiveData<List<Project>> currentProjects;

    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }

    public void init(/*long projectId*/) {
        currentProjects = projectDataSource.getProjects();
//        if (this.currentProject != null) { return; }
 //       currentProject = projectDataSource.getProject(projectId);
        if (currentProjects != null) {
            currentProjects.observeForever(this::updateProjectsList);
        }
    }

    private void updateProjectsList(List<Project> projects) {
        this.allProjects = projects;
    }

    // ---------------------
    // For Project
    // ---------------------

//    public LiveData<Project> getProject(long id) { return projectDataSource.getProject(id); }
    public LiveData<List<Project>> getProjects() { return projectDataSource.getProjects(); }
    /**
     * List of all projects available in the application
     */
    private List<Project> allProjects = new ArrayList<>();//Project.getAllProjects();

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    @Nullable
    public Project getProjectById(long id) {
        for (Project project : allProjects) {
            if (project.getId() == id)
                return project;
        }
        return null;
    }

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
}
