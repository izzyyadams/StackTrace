package com.stacktrace.service;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ValidationException;
import com.stacktrace.model.Effort;
import com.stacktrace.model.Priority;
import com.stacktrace.model.Status;
import com.stacktrace.model.Task;

import java.sql.*;
import java.util.ArrayList;

public class TaskDao implements EventDao<Task> {
    @Override
    public void create(Task taskToCreate) throws DatabaseException {
        String sqlString = "INSERT INTO tasks (timeline_id, title, description, start_date, deadline, status, priority, effort, created_at, completed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setInt(1, taskToCreate.getTimelineId());
            ps.setString(2, taskToCreate.getTitle());
            ps.setString(3, taskToCreate.getDescription());
            ps.setDate(4, Date.valueOf(taskToCreate.getStartDate()));
            ps.setDate(5, Date.valueOf(taskToCreate.getDeadline()));
            ps.setString(6, taskToCreate.getStatus().name());
            ps.setString(7, taskToCreate.getPriority().name());
            ps.setString(8, taskToCreate.getEffort().name());
            ps.setDate(9, Date.valueOf(taskToCreate.getCreatedAt()));
            ps.setDate(10, taskToCreate.getCompletedAt() != null ? Date.valueOf(taskToCreate.getCompletedAt()) : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Task readOne(Integer idToRead) throws DatabaseException{
        String sqlString = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setInt(1, idToRead);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Task newTask = new Task(rs.getString("title"), rs.getString("description"), rs.getDate("start_date").toLocalDate(), rs.getDate("deadline").toLocalDate(), Status.valueOf(rs.getString("status")), Priority.valueOf(rs.getString("priority")), Effort.valueOf(rs.getString("effort")), rs.getInt("timeline_id"));
                return newTask;
            }
        } catch (SQLException | ValidationException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public ArrayList<Task> readAll() throws DatabaseException{
        ArrayList<Task> allTasks = new ArrayList<>();
        String sqlString = "SELECT * FROM tasks ";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Task newTask = new Task(rs.getString("title"), rs.getString("description"), rs.getDate("start_date").toLocalDate(), rs.getDate("deadline").toLocalDate(), Status.valueOf(rs.getString("status")), Priority.valueOf(rs.getString("priority")), Effort.valueOf(rs.getString("effort")), rs.getInt("timeline_id"));
                newTask.setCompletedAt(rs.getDate("completed_at").toLocalDate());
                newTask.setCreatedAt(rs.getDate("created_at").toLocalDate());
                allTasks.add(newTask);
            }

        } catch (SQLException | ValidationException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        return allTasks;
    }

    @Override
    public void updateOne(Task taskToUpdate) throws DatabaseException {
        String sqlString = "UPDATE tasks SET timeline_id = ?, title = ?, description = ?, start_date = ?, deadline = ?, status = ?, priority = ?, effort = ?, completed_at = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setInt(1, taskToUpdate.getTimelineId());
            ps.setString(2, taskToUpdate.getTitle());
            ps.setString(3, taskToUpdate.getDescription());
            ps.setDate(4, Date.valueOf(taskToUpdate.getStartDate()));
            ps.setDate(5, Date.valueOf(taskToUpdate.getDeadline()));
            ps.setString(6, taskToUpdate.getStatus().name());
            ps.setString(7, taskToUpdate.getPriority().name());
            ps.setString(8, taskToUpdate.getEffort().name());
            ps.setDate(9, taskToUpdate.getCompletedAt() != null ? Date.valueOf(taskToUpdate.getCompletedAt()) : null);
            ps.setInt(10, taskToUpdate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteOne(Integer idToDelete) throws DatabaseException {
        String sqlString = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setInt(1, idToDelete);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

    }

    @Override
    public void deleteAll() throws DatabaseException {
        String sqlString = "DELETE FROM tasks";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
