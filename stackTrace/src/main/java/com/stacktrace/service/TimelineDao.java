package com.stacktrace.service;

import com.stacktrace.exception.DatabaseException;
import com.stacktrace.exception.ValidationException;
import com.stacktrace.model.*;

import java.sql.*;
import java.util.ArrayList;

public class TimelineDao implements EventDao<Timeline> {

    @Override
    public void create(Timeline timelineToCreate) throws DatabaseException {
        String sqlString = "INSERT INTO timelines (title, description, start_date, deadline, status, created_at, completed_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setString(1, timelineToCreate.getTitle());
            ps.setString(2, timelineToCreate.getDescription());
            ps.setDate(3, Date.valueOf(timelineToCreate.getStartDate()));
            ps.setDate(4, Date.valueOf(timelineToCreate.getDeadline()));
            ps.setString(5, timelineToCreate.getStatus().name());
            ps.setDate(6, Date.valueOf(timelineToCreate.getCreatedAt()));
            ps.setDate(7, timelineToCreate.getCompletedAt() != null ? Date.valueOf(timelineToCreate.getCompletedAt()) : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Timeline readOne(Integer idToRead) throws DatabaseException{
        String sqlString = "SELECT * FROM timelines WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setInt(1, idToRead);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timeline newTimeline = new Timeline(rs.getString("title"), rs.getString("description"), rs.getDate("start_date").toLocalDate(), rs.getDate("deadline").toLocalDate(), Status.valueOf(rs.getString("status")));
                return newTimeline;
            }
        } catch (SQLException | ValidationException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public ArrayList<Timeline> readAll() throws DatabaseException{
        ArrayList<Timeline> allTimelines = new ArrayList<>();
        String sqlString = "SELECT * FROM timelines";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Timeline newTimeline = new Timeline(rs.getString("title"), rs.getString("description"), rs.getDate("start_date").toLocalDate(), rs.getDate("deadline").toLocalDate(), Status.valueOf(rs.getString("status")));
                newTimeline.setCompletedAt(rs.getDate("completed_at").toLocalDate());
                newTimeline.setCreatedAt(rs.getDate("created_at").toLocalDate());
                allTimelines.add(newTimeline);
            }

        } catch (SQLException | ValidationException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        return allTimelines;
    }

    @Override
    public void updateOne(Timeline timelineToUpdate) throws DatabaseException {
        String sqlString = "UPDATE timelines SET title = ?, description = ?, start_date = ?, deadline = ?, status = ?, completed_at = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.setString(1, timelineToUpdate.getTitle());
            ps.setString(2, timelineToUpdate.getDescription());
            ps.setDate(3, Date.valueOf(timelineToUpdate.getStartDate()));
            ps.setDate(4, Date.valueOf(timelineToUpdate.getDeadline()));
            ps.setString(5, timelineToUpdate.getStatus().name());
            ps.setDate(6, timelineToUpdate.getCompletedAt() != null ? Date.valueOf(timelineToUpdate.getCompletedAt()) : null);
            ps.setInt(7, timelineToUpdate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

    }

    @Override
    public void deleteOne(Integer idToDelete) throws DatabaseException {
        String sqlString = "DELETE FROM timelines WHERE id = ?";
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
        String sqlString = "DELETE FROM timelines";
        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sqlString)) {
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
