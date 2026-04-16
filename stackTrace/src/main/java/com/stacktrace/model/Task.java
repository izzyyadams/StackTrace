package com.stacktrace.model;

import java.time.LocalDate;

public class Task extends Event{
    private static int nextId = 1;

    protected int id;
    private Priority priority;
    private Effort effort;
    private int timelineId;

    public Task(String title, String description, LocalDate deadline, Status status, Priority priority, Effort effort, int timelineId) {
        super(title, description, deadline, status);
        this.priority = priority;
        this.effort = effort;
        this.timelineId = timelineId;
        this.id = nextId++;
    }

    public Task(String title, LocalDate deadline, int timelineId) {
        this(title, "", deadline, Status.NOT_STARTED, Priority.LOW, Effort.LOW, timelineId);
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
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public void setEffort(Effort effort) {
        this.effort = effort;
    }
    public void setTimelineId(int timelineId) {
        this.timelineId = timelineId;
    }
}
