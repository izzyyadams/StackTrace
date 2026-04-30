package com.stacktrace.model;
import com.stacktrace.exception.ValidationException;
import java.time.LocalDate;

public class Task extends Event{
    private static Integer nextId = 1;

    private Integer id;
    private Priority priority;
    private Effort effort;
    private Integer timelineId;

    public Task(String title, String description, LocalDate startDate, LocalDate deadline, Status status, Priority priority, Effort effort, Integer timelineId) throws ValidationException {
        super(title, description, startDate, deadline, status);
        if (priority == null) {
            throw new ValidationException("Priority cannot be null");
        }
        this.priority = priority;
        if (effort == null) {
            throw new ValidationException("Effort cannot be null");
        }
        this.effort = effort;
        if (timelineId == null) {
            throw new ValidationException("Timeline id cannot be null");
        }
        this.timelineId = timelineId;
        this.id = nextId++;
    }

    public Task(String title, LocalDate deadline, Integer timelineId) throws ValidationException {
        this(title, "", LocalDate.now(), deadline, Status.NOT_STARTED, Priority.LOW, Effort.LOW, timelineId);
    }

    //getters
    public Priority getPriority() {
        return priority;
    }
    public Effort getEffort() {
        return effort;
    }
    public Integer getTimelineId() {
        return timelineId;
    }
    public Integer getId() {
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
