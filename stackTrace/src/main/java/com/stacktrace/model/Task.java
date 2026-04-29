package com.stacktrace.model;

import com.stacktrace.exception.ValidationException;

import java.time.LocalDate;

public class Task extends Event{
    private static int nextId = 1;

    private int id;
    private Priority priority;
    private Effort effort;
    private int timelineId;

    public Task(String title, String description, LocalDate startDate, LocalDate deadline, Status status, Priority priority, Effort effort, int timelineId) throws ValidationException {
        super(title, description, startDate, deadline, status);
        if (priority == null) {
            throw new ValidationException("Priority cannot be null");
        }
        this.priority = priority;
        if (effort == null) {
            throw new ValidationException("Effort cannot be null");
        }
        this.effort = effort;
        this.timelineId = timelineId;
        this.id = nextId++;
    }

    public Task(String title, LocalDate deadline, int timelineId) throws ValidationException {
        this(title, "", LocalDate.now(), deadline, Status.NOT_STARTED, Priority.LOW, Effort.LOW, timelineId);
    }

    //getters
    public Priority getPriority() {
        return priority;
    }
    public Effort getEffort() {
        return effort;
    }
    public int getTimelineId() {
        return timelineId;
    }
    public int getId() {
        return id;
    }

    //setters
    public void setPriority(Priority newPriority) throws ValidationException {
        if (newPriority == null) {
            throw new ValidationException("Priority cannot be null");
        }
        priority = newPriority;
    }
    public void setEffort(Effort newEffort) throws ValidationException{
        if(newEffort == null) {
            throw new ValidationException("Effort cannot be null");
        }
        effort = newEffort;
    }
}
