package com.stacktrace.model;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ManagerException;
import com.stacktrace.exception.ValidationException;
import com.stacktrace.service.TaskDao;
import com.stacktrace.service.TimelineDao;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.*;

@Service
public class Manager {

    private HashMap<Timeline, PriorityQueue<Task>> timelines = new HashMap<>();
    private TimelineDao timelineDao = new TimelineDao();
    private TaskDao taskDao = new TaskDao();
    private Comparator<Task> taskComparator = (a, b) -> {
        int deadlineCompare = a.getDeadline().compareTo(b.getDeadline());
        if (deadlineCompare != 0) { //if different deadline, no need to compare priority, just use one with first deadline
            return deadlineCompare;
        }
        return b.getPriority().compareTo(a.getPriority()); //compare b to a so high is first and low is last
    };

    public Manager() {

    }

    public void createTimeline(String title, String description, LocalDate startDate, LocalDate deadline, Status status) throws ValidationException, DatabaseException {
        Timeline newTimeline = new Timeline(title, description, startDate, deadline, status);
        timelineDao.create(newTimeline);
        timelines.put(newTimeline, new PriorityQueue<Task>(taskComparator)); //if database save fails it will not reach here
    }

    public void createTask(String title, String description, LocalDate startDate, LocalDate deadline, Status status, Priority priority, Effort effort, Integer timelineId) throws ValidationException, DatabaseException, ManagerException {
        Task newTask = new Task(title, description,startDate, deadline, status, priority, effort, timelineId);
        assignTaskToTimeLine(newTask);

    }

    //find the parent of the task
    public Timeline getTasksTimeline(Integer tasksTimelineId) throws ManagerException{
        Timeline parentTimeline = null;
        //find parent timeline in timelines hashmap
        for (Timeline t : timelines.keySet()) {
            if (t.getId() == tasksTimelineId) {
                parentTimeline = t;
            }
        }
        if (parentTimeline == null) {
            throw new ManagerException("Timeline not found");
        }
        return parentTimeline;
    }

    public void assignTaskToTimeLine(Task newTask) throws ManagerException, DatabaseException{
        Timeline parentTimeline = getTasksTimeline(newTask.getTimelineId());
        taskDao.create(newTask);
        //get the priority queue from timelines if the database succeeds
        timelines.get(parentTimeline).add(newTask);
    }

    public void updateTimeline(Timeline timelineToUpdate, String newTitle, String newDescription, LocalDate newStartDate, LocalDate newDeadline, Status newStatus) throws ValidationException, DatabaseException, ManagerException{
        if (timelineToUpdate == null) {
            throw new ManagerException("Timeline not found");
        }
        if (newTitle != null) {
            timelineToUpdate.setTitle(newTitle);
        }
        if (newDescription != null) {
            timelineToUpdate.setDescription(newDescription);
        }
        if(newStartDate != null) {
            timelineToUpdate.setStartDate(newStartDate);
        }
        if(newDeadline != null) {
            timelineToUpdate.setDeadline(newDeadline);
        }
        if (newStatus != null) {
            timelineToUpdate.setStatus(newStatus);
        }
        timelineDao.updateOne(timelineToUpdate);
    }

    public void updateTask(Task taskToUpdate, String newTitle, String newDescription, LocalDate newStartDate, LocalDate newDeadline, Status newStatus, Priority newPriority, Effort newEffort) throws ValidationException, DatabaseException, ManagerException{
        if (taskToUpdate == null) {
            throw new ManagerException("Task not found");
        }
        if (newTitle != null) {
            taskToUpdate.setTitle(newTitle);
        }
        if (newDescription != null) {
            taskToUpdate.setDescription(newDescription);
        }
        if(newStartDate != null) {
            taskToUpdate.setStartDate(newStartDate);
        }
        if(newDeadline != null) {
            taskToUpdate.setDeadline(newDeadline);
        }
        if (newStatus != null) {
            taskToUpdate.setStatus(newStatus);
        }
        if (newPriority != null) {
            taskToUpdate.setPriority(newPriority);
        }
        if (newEffort != null) {
            taskToUpdate.setEffort(newEffort);
        }
        taskDao.updateOne(taskToUpdate);
    }

    public void removeTaskFromTimeline(Task taskToRemove) throws ManagerException{
        Timeline parentTimeline = getTasksTimeline(taskToRemove.getTimelineId());
        timelines.get(parentTimeline).remove(taskToRemove);
    }

    public void deleteTask(Task taskToDelete) throws DatabaseException, ManagerException {
        if (taskToDelete == null) {
            throw new ManagerException("Cannot delete null task.");
        }
        removeTaskFromTimeline(taskToDelete);
        taskDao.deleteOne(taskToDelete.getId());

    }

    //get all of the tasks of a timeline
    public PriorityQueue<Task> getTimelinesTasks(Timeline timeline) {
        return timelines.get(timeline);
    }

    public void deleteAllTasks(Timeline timeline) throws ManagerException, DatabaseException {
        if (timeline == null) {
            throw new ManagerException("Cannot delete null timeline.");
        }
        PriorityQueue<Task> tasksToDelete = new PriorityQueue<>(getTimelinesTasks(timeline)); //to avoid ConcurrentModificationException, hard to use iterator because it is a function call
        for (Task task : tasksToDelete) {
            deleteTask(task);
        }
    }

    public void deleteTimeline(Timeline timelineToDelete) throws ManagerException, DatabaseException {
        deleteAllTasks(timelineToDelete);
        timelines.remove(timelineToDelete);
        timelineDao.deleteOne(timelineToDelete.getId());
    }

    public void deleteAllTimelines() throws ManagerException, DatabaseException {
        for (Timeline timeline : new HashSet<>(timelines.keySet())) { //avoid concurrent modification
            deleteTimeline(timeline);
        }
    }

    public PriorityQueue<Task> getTimelinesOverdueTasks(Timeline timeline){
        PriorityQueue<Task> tasksOverDue = new PriorityQueue<>(taskComparator);
        for (Task task : timelines.get(timeline)) {
            if (task.isOverDue()){
                tasksOverDue.add(task);
            }
        }
        return tasksOverDue;
    }

    public PriorityQueue<Task> getAllOverdueTasks() {
        PriorityQueue<Task> allOverDueTasks = new PriorityQueue<>(taskComparator);
        for (PriorityQueue<Task> tasks : timelines.values()) {
            for (Task task : tasks) {
                if (task.isOverDue()){
                    allOverDueTasks.add(task);
                }
            }
        }
        return allOverDueTasks;
    }

    public Integer getTimelineProgress(Timeline timeline){
        PriorityQueue<Task> tasks = timelines.get(timeline);
        Integer totalTasks = tasks.size();
        if (totalTasks == 0 ) return 0;
        Integer completedTasks = 0;
        for (Task task : tasks) {
            if (task.getStatus() == Status.COMPLETED){
                completedTasks += 1;
            }
        }
        return (completedTasks * 100) / totalTasks;
    }

    public Task getNextTask(Timeline timeline){
        return timelines.get(timeline).peek();
    }

    //get all timelines
    public Set<Timeline> getAllTimelines() {
        return timelines.keySet();
    }

    //get timeline by id
    public Timeline getTimelineById(Integer id) {
        for (Timeline timeline : timelines.keySet()) {
            if (timeline.getId() == id) {
                return timeline;
            }
        }
        return null;
    }

    //get task from timeline by id
    public Task getTaskById(Integer timelineId, Integer taskId) {
        Timeline timeline = getTimelineById(timelineId);
        PriorityQueue<Task> tasks = getTimelinesTasks(timeline);
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }




}
