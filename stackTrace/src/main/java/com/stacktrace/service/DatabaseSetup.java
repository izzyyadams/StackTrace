package com.stacktrace.service;

import java.io.File;
import java.sql.*;

public class DatabaseSetup {
    private DatabaseSetup() {}

    public static void initialize() {
        new File("./data").mkdirs();
        String createTasksTableSql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "timeline_id INTEGER REFERENCES timelines(id), " +
                "title VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255), " +
                "start_date DATE NOT NULL, " +
                "deadline DATE NOT NULL, " +
                "status VARCHAR(50) NOT NULL, " +
                "priority VARCHAR(50) NOT NULL, " +
                "effort VARCHAR(50) NOT NULL, " +
                "created_at TIMESTAMP NOT NULL, " +
                "completed_at TIMESTAMP)";
        String createTimelinesTableSql = "CREATE TABLE IF NOT EXISTS timelines (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255), " +
                "start_date DATE NOT NULL, " +
                "deadline DATE NOT NULL, " +
                "status VARCHAR(50) NOT NULL, " +
                "created_at TIMESTAMP NOT NULL, " +
                "completed_at TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(DbConfig.JDBC_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTimelinesTableSql);
            stmt.execute(createTasksTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Initial database setup failed", e);
        }
    }
}
