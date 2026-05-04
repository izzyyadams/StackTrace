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

    //getters
    public Integer getId() {
        return id;
    }

    //used in my hashmap in manager to compare if two timelines are equal using id, hashCode used for buckets
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //check if self
        if (o == null || getClass() != o.getClass()) return false; //make sure class is same
        Timeline timeline = (Timeline) o; //cast o onto timeline since it is object when passed
        return id == timeline.id; //compare ids
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    //setter
    public void setId(int id) {
        this.id = id;
    }

}
