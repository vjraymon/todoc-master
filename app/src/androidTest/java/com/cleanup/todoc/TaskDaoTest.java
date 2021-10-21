package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.dao.SaveTodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {
    // For Data
    private SaveTodocDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                SaveTodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    // DATA SET for test
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID,"Projet Tartempion", 0xFFEADAD1);

    private static Task NEW_TASK_1 = new Task(0, PROJECT_ID, "cuisine", 0);
    private static Task NEW_TASK_2 = new Task(0, PROJECT_ID, "salon", 0);
    private static Task NEW_TASK_3 = new Task(0, PROJECT_ID, "chambre", 0);

    @Test
    public void insertAndGetProject() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        List<Project> projects = LiveDataTestUtil.getValue(this.database.projectDao().getProjects());
        Project project = projects.get(0);
        assert(project.getId()==PROJECT_ID);
        assert(project.getName().equals(PROJECT_DEMO.getName()));
        assert(project.getColor()==PROJECT_DEMO.getColor());
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assert(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_1);
        this.database.taskDao().insertTask(NEW_TASK_2);
        this.database.taskDao().insertTask(NEW_TASK_3);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assert(tasks.size()==3);
    }

    @Test
    public void insertAndUpdateTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        taskAdded.setName("salon");
        this.database.taskDao().updateTask(taskAdded);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assert(tasks.size()==1);
        assert(tasks.get(0).getName().equals("salon"));
    }

    @Test
    public void insertAndDeleteTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_3);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getTaskId());
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assert(tasks.isEmpty());
    }
}
