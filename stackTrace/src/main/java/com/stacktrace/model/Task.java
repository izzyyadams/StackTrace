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
            this.priority = Priority.LOW;
        } else {
            this.priority = priority;
        }
        if (effort == null) {
            this.effort = Effort.LOW;
        } else {
            this.effort = effort;
        }
        if (timelineId == null) {
            throw new ValidationException("Timeline id cannot be null");
        }
        this.timelineId = timelineId;
        this.id = nextId++;
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

    //used in my hashmap in manager to compare if two tasks are equal using id, hashCode used for buckets
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //check if self
        if (o == null || getClass() != o.getClass()) return false; //make sure class is same
        Task task = (Task) o; //cast o onto task since it is object when passed
        return id == task.id; //compare ids
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
