package com.stacktrace.model;

import com.stacktrace.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    //empty title for timeline
    @Test
    void testEmptyTitleThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new Timeline("", null, LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED);
        });
        assertThrows(ValidationException.class, () -> {
            new Task("", null, LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, 1);
        });
    }

    //empty deadline
    @Test
    void testEmptyDeadlineException() {
        assertThrows(ValidationException.class, () -> {
            new Timeline("Test", null, LocalDate.now(), null, Status.NOT_STARTED);
        });
        assertThrows(ValidationException.class, () -> {
            new Task("test", null, LocalDate.now(), null, Status.NOT_STARTED, null, null, 1);
        });
    }

    //deadline before start
    @Test
    void testDeadlineAfterStartException() {
        assertThrows(ValidationException.class, () -> {
            new Timeline("Test", null, LocalDate.now().plusDays(7), LocalDate.now(), Status.NOT_STARTED);
        });
        assertThrows(ValidationException.class, () -> {
            new Task("test", null, LocalDate.now().plusDays(7), LocalDate.now(), Status.NOT_STARTED, null, null, 1);
        });
    }

    //successful timeline and task creation, no exceptions thrown
    @Test
    void testCreationNoException() {
        assertDoesNotThrow(() -> {
            new Timeline("Test", null, LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED);
        });
        assertDoesNotThrow(() -> {
            new Task("Test", null, LocalDate.now(), LocalDate.now().plusDays(7), Status.NOT_STARTED, null, null, 1);
        });
    }

    //successful timeline and task creation, correct fields, including status being not started if not declared
    @Test
    void testCreationFields() throws ValidationException{
        Timeline testTimeline = new Timeline("Test", null, LocalDate.now(), LocalDate.now().plusDays(7), null);
        Task testTask = new Task("Test", null, LocalDate.now(), LocalDate.now().plusDays(7), null, null, null, 1);
        assertEquals("Test", testTimeline.getTitle());
        assertEquals(Status.NOT_STARTED, testTimeline.getStatus());
        assertEquals("Test", testTask.getTitle());
        assertEquals(Status.NOT_STARTED, testTask.getStatus());
        assertEquals(Priority.LOW, testTask.getPriority());
        assertEquals(Effort.LOW, testTask.getEffort());
    }

    // empty timeline id for task creation should fail
    @Test
    void testEmptyTimelineIdTask() {
        assertThrows(ValidationException.class, () -> {
            Task testTask = new Task("test", "", LocalDate.now(), LocalDate.now().plusDays(7), null, null, null, null);
        });
    }

}
