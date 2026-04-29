package com.stacktrace.model;

import com.stacktrace.exception.ValidationException;

import java.time.LocalDate;

public abstract class Event {


    protected String title;
    protected String description;
    protected LocalDate deadline;
    protected Status status;
    protected LocalDate startDate;
    private LocalDate  createdAt;
    private LocalDate completedAt;

    // constructor
    public Event (String title, String description, LocalDate startDate, LocalDate deadline, Status status) throws ValidationException {
        if (title == null || title.isEmpty()){
            throw new ValidationException("Title cannot be empty");
        }
        this.title = title;
        this.description = description;
        if (startDate == null) {
            throw new ValidationException("Start date cannot be null");
        }
        this.startDate = startDate;
        if (deadline == null) {
            throw new ValidationException("Deadline cannot be null");
        }
        if (deadline.isBefore(startDate)) {
            throw new ValidationException("Deadline must be after start date.");
        }
        this.deadline = deadline;
        if (status == null) {
            throw new ValidationException("Status cannot be null");
        }
        this.status = status;
        this.createdAt = LocalDate.now();
    }

    // default description, start date, and status
    public Event (String title, LocalDate deadline) throws ValidationException {
        this(title, "", LocalDate.now(), deadline, Status.NOT_STARTED);
    }

    // getters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getDeadline() {
        return deadline;
    }
    public Status getStatus() {
        return status;
    }
    public LocalDate getCompletedAt() {
        return completedAt;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    // setters
    public void setStatus (Status newStatus) throws ValidationException{
        if (newStatus == null) {
            throw new ValidationException("Status cannot be empty.");
        }
        if (newStatus.equals(Status.COMPLETED)) {
            this.completedAt = LocalDate.now();
        }
        status = newStatus;
    }
    public void setTitle (String updatedTitle) throws ValidationException{
        if (updatedTitle == null || updatedTitle.isEmpty()){
            throw new ValidationException("Title cannot be empty");
        }
        title = updatedTitle;
    }
    public void setDescription (String newDescription) {
        description = newDescription;
    }
    public void setStartDate (LocalDate newDate) throws ValidationException{
        if (newDate == null) {
            throw new ValidationException("Start date cannot be null");
        }
        startDate = newDate;
    }
    public void setDeadline (LocalDate newDate) throws ValidationException{
        if (newDate == null) {
            throw new ValidationException("Deadline cannot be null");
        }
        if (newDate.isBefore(startDate)) {
            throw new ValidationException("Deadline must be after start date.");
        }
        deadline = newDate;
    }

    public boolean isActive () {
        return (!getStatus().equals(Status.COMPLETED));
    }

    public boolean isOverDue () {
        return (!getStatus().equals(Status.COMPLETED) && getDeadline().isBefore(LocalDate.now()));
    }


}
