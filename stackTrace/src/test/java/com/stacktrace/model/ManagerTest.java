package com.stacktrace.model;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ManagerException;
import com.stacktrace.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.Set;

public class ManagerTest {
    private Manager manager;
    private Timeline testTimeline;

    @BeforeEach
    void setUp() throws ValidationException, DatabaseException, ManagerException {
        manager = new Manager();
        manager.createTimeline("Test Timeline", null, LocalDate.now(), LocalDate.now().plusDays(7), null);
        testTimeline = manager.getAllTimelines().iterator().next();
    }

    //clean database
    @AfterEach
    void tearDown() throws ManagerException, DatabaseException {
        manager.deleteAllTimelines();
    }

    //progress
    @Test
    void zeroProgressTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, testTimeline.getId());
        assertEquals(0, manager.getTimelineProgress(testTimeline));
    }

    @Test
    void halfProgressTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.COMPLETED, null, null, testTimeline.getId());
        assertEquals(50, manager.getTimelineProgress(testTimeline));
    }

    @Test
    void fullProgressTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.COMPLETED, null, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.COMPLETED, null, null, testTimeline.getId());
        assertEquals(100, manager.getTimelineProgress(testTimeline));
    }

    //next task by deadline
    @Test
    void nextTaskByDeadlineTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(3), Status.NOT_STARTED, null, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, testTimeline.getId());
        assertEquals("Test Task 1", manager.getNextTask(testTimeline).getTitle() );
    }

    //next task by priority
    @Test
    void nextTaskByPriorityTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, Priority.LOW, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, Priority.HIGH, null, testTimeline.getId());
        assertEquals("Test Task 2", manager.getNextTask(testTimeline).getTitle() );
    }

    //next task by priority and deadline
    @Test
    void nextTaskByPriorityDeadlineTest() throws ValidationException, ManagerException, DatabaseException{
        manager.createTask("Test Task 1", "", LocalDate.now(), LocalDate.now().plusDays(3), Status.NOT_STARTED, Priority.LOW, null, testTimeline.getId());
        manager.createTask("Test Task 2", "", LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, Priority.HIGH, null, testTimeline.getId());
        assertEquals("Test Task 1", manager.getNextTask(testTimeline).getTitle() );
    }

    //task creation (adding to timeline)
    @Test
    void taskCreatedAddedToTimelineTest () throws ValidationException, ManagerException, DatabaseException {
        manager.createTask("Test Task", "", LocalDate.now(), LocalDate.now().plusDays(3), null, null, null, testTimeline.getId());
        PriorityQueue<Task> tasks = manager.getTimelinesTasks(testTimeline);
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Test Task")));
    }


    //delete task
    @Test
    void deleteTaskTest () throws ValidationException, ManagerException, DatabaseException {
        manager.createTask("Test Task", "", LocalDate.now(), LocalDate.now().plusDays(3), null, null, null, testTimeline.getId());
        PriorityQueue<Task> tasks = manager.getTimelinesTasks(testTimeline);
        Task taskToDelete = tasks.stream().filter(t -> t.getTitle().equals("Test Task")).findFirst().get();
        manager.deleteTask(taskToDelete);
        assertFalse(tasks.stream().anyMatch(t -> t.getTitle().equals("Test Task")));
    }


    //delete timeline
    @Test
    void deleteTimelineTest () throws ManagerException, DatabaseException{
        manager.deleteTimeline(testTimeline);
        Set<Timeline> allTimelines = manager.getAllTimelines();
        assertFalse(allTimelines.contains(testTimeline));

    }

    //timeline not found on task creation
    @Test
    void timelineNotFound () {
        assertThrows(ManagerException.class, () -> {
            manager.createTask("Test", "", LocalDate.now(), LocalDate.now().plusDays(7), null, null, null, 1000000000);
        });

    }




}
