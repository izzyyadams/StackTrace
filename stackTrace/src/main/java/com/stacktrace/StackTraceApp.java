package com.stacktrace;
import com.stacktrace.service.DatabaseSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackTraceApp {
    public static void main(String[] args) {
        DatabaseSetup.initialize();
        SpringApplication.run(StackTraceApp.class, args);
    }
}