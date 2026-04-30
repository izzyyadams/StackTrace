package com.stacktrace.model;
import com.stacktrace.exception.ValidationException;
import java.time.LocalDate;

public class Timeline extends Event{
    private static Integer nextId = 1;

    private Integer id;

    public Timeline(String title, String description, LocalDate startDate, LocalDate deadline, Status status) throws ValidationException {
        super(title, description, startDate, deadline, status);
        this.id = nextId++;
    }

    public Timeline(String title, LocalDate deadline) throws ValidationException {
        this(title, "", LocalDate.now(), deadline, Status.NOT_STARTED);
    }

    //getters
    public Integer getId() {
        return id;
    }

}
