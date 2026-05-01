package com.stacktrace.controller;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ManagerException;
import com.stacktrace.exception.ValidationException;
import com.stacktrace.model.Manager;
import com.stacktrace.model.Task;
import com.stacktrace.model.Timeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.PriorityQueue;
import java.util.Set;

@RestController
@RequestMapping("/timelines")
public class TimelineController {
    @Autowired
    private Manager manager;

    //get all timelines
    // GET /timelines
    @GetMapping
    public Set<Timeline> getAllTimelines() {
        return manager.getAllTimelines();
    }

    //get one timeline
    // GET /timelines/{id}
    @GetMapping("/{id}")
    public Timeline getTimeline(@PathVariable Integer id) {
        return manager.getTimelineById(id);
    }

    //get tasks of a timeline
    // GET /timelines/{id}/tasks
    @GetMapping("/{id}/tasks")
    public PriorityQueue<Task> getTasks(@PathVariable  Integer id) {
        Timeline timeline = manager.getTimelineById(id);
        return manager.getTimelinesTasks(timeline);
    }

    //create timeline
    // POST /timelines
    @PostMapping
    public void createTimeline(@RequestBody Timeline timeline) throws ValidationException, DatabaseException {
        manager.createTimeline(
                timeline.getTitle(),
                timeline.getDescription(),
                timeline.getStartDate(),
                timeline.getDeadline(),
                timeline.getStatus()
        );
    }

    //create task
    //POST /timelines/{id}/tasks
    @PostMapping("/{id}/tasks")
    public void createTask(@RequestBody Task task) throws ValidationException, DatabaseException, ManagerException {
        manager.createTask(
                task.getTitle(),
                task.getDescription(),
                task.getStartDate(),
                task.getDeadline(),
                task.getStatus(),
                task.getPriority(),
                task.getEffort(),
                task.getTimelineId()
        );
    }

    //delete timeline
    // DELETE /timesline/{id}
    @DeleteMapping("/{id}")
    public void deleteTimeline(@PathVariable Integer id) throws ManagerException, DatabaseException {
        Timeline timelineToDelete = manager.getTimelineById(id);
        manager.deleteTimeline(timelineToDelete);
    }

    //delete task
    // DELETE /timesline/{id}/tasks/{id}
    @DeleteMapping("/{timelineId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Integer timelineId, @PathVariable Integer taskId) throws ManagerException, DatabaseException {
        Timeline timeline = manager.getTimelineById(timelineId);
        Task taskToDelete = manager.getTaskById(timelineId, taskId);
        manager.deleteTask(taskToDelete);
    }
}