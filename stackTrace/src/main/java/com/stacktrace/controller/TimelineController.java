package com.stacktrace.controller;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ManagerException;
import com.stacktrace.exception.ValidationException;
import com.stacktrace.model.Manager;
import com.stacktrace.model.Task;
import com.stacktrace.model.Timeline;
import com.stacktrace.service.AIHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.PriorityQueue;
import java.util.Set;

@RestController
@RequestMapping("/timelines")
public class TimelineController {
    @Autowired
    private Manager manager;

    @Autowired
    private AIHelper aiHelper;

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

    // GET /timelines/{id}/tasks/{taskId}
    // get one task from list of tasks
    @GetMapping("/{id}/tasks/{taskId}")
    public Task getTask(@PathVariable Integer id, @PathVariable Integer taskId) {
        return manager.getTaskById(id, taskId);
    }

    // PUT /timelines/{timelineId}/tasks/{taskId}
    // update task
    @PutMapping("/{timelineId}/tasks/{taskId}")
    public void updateTask(@PathVariable Integer timelineId, @PathVariable Integer taskId, @RequestBody Task task) throws ValidationException, DatabaseException, ManagerException {
        Task taskToUpdate = manager.getTaskById(timelineId, taskId);
        manager.updateTask(taskToUpdate, task.getTitle(), task.getDescription(), task.getStartDate(), task.getDeadline(), task.getStatus(), task.getPriority(), task.getEffort());
    }

    // PUT /timelines/{id}
    // update timeline
    @PutMapping("/{id}")
    public void updateTimeline(@PathVariable Integer id, @RequestBody Timeline timeline) throws ValidationException, DatabaseException, ManagerException {
        Timeline timelineToUpdate = manager.getTimelineById(id);
        manager.updateTimeline(timelineToUpdate, timeline.getTitle(), timeline.getDescription(), timeline.getStartDate(), timeline.getDeadline(), timeline.getStatus());
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

    // can't access methods in js, have to have endpoints for them
    // GET /timelines/{id}/progress
    @GetMapping("/{id}/progress")
    public int getProgress(@PathVariable Integer id) {
        Timeline timeline = manager.getTimelineById(id);
        return manager.getTimelineProgress(timeline);
    }

    // GET /timelines/{id}/overdue
    @GetMapping("/{id}/overdue")
    public PriorityQueue<Task> getOverdueTasks(@PathVariable Integer id) {
        Timeline timeline = manager.getTimelineById(id);
        return manager.getTimelinesOverdueTasks(timeline);
    }

    // GET /timelines/{id}/next
    @GetMapping("/{id}/next")
    public ResponseEntity<Task> getNextTask(@PathVariable Integer id) {
        Timeline timeline = manager.getTimelineById(id);
        Task next = manager.getNextTask(timeline);
        if (next == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(next);
    }

    // DELETE /timelines
    @DeleteMapping
    public void deleteAllTasks() throws DatabaseException, ManagerException{
        manager.deleteAllTimelines();
    }

    //get overall next task from AI
    @GetMapping("/suggest")
    public Task suggestNextTask() {
        return aiHelper.suggestNextTask();
    }
}