package com.stacktrace.model;

import java.time.LocalDate;

public abstract class Event {

    private static int nextId = 1;

    protected int id;
    protected String title;
    protected String description;
    protected LocalDate deadline;
    protected Status status;

    // constructor
    public Event (String title, String description, LocalDate deadline, Status status){
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.id = nextId++;
    }

    // default description and status
    public Event (String title, LocalDate deadline) {
        this(title, "", deadline, Status.NOT_STARTED);
    }

    // getters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public Status getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }

    // setters
    public void setStatus (Status newStatus) {
        status = newStatus;
    }
    public void setTitle (String updatedTitle) {
        title = updatedTitle;
    }
    public void setDescription (String newDescription) {
        description = newDescription;
    }
    public void setDeadline (LocalDate newDate) {
        deadline = newDate;
    }

    public boolean isActive () {
        return (!getStatus().equals(Status.COMPLETED));
    }

    public boolean isOverDue () {
        return (!getStatus().equals(Status.COMPLETED) && getDeadline().isBefore(LocalDate.now()));
    }


}
