# StackTrace

## Overview
StackTrace is a web app designed for the software development industry. It helps users track
tasks, timelines, and project progress, while AI assists in deciding task priority amongst all ongoing projects.
This addresses the problem of overcomplicated sprint trackers and reduces the time spent planning software projects. The intended users are
software developers, primarily for personal or small-scale projects.

## Features
- Timeline management
- Task management with priority queue
- AI-powered task suggestion
- Progress tracking
- Overdue task detection

## Tech Stack
- Java 25
- Spring Boot
- H2 Database (file-based)
- HTML/CSS/JavaScript frontend
- OpenAI API

## How to Run
1. Clone the repository
2. Add your OpenAI API key to `src/main/resources/application.properties`
3. Run `StackTraceApp.java`
4. Open `localhost:8080` in your browser

## Architecture
- Model layer (Event, Task, Timeline) → Classes for most basic data structures: task and timeline
- Service layer (Manager, DAOs, AIHelper) → Handles relationship between basic structures, database, and API
- Controller layer (TimelineController) → Connect frontend to services
- Frontend (HTML/CSS/JS) → Styling and UI

## Testing
- run `mvn test` in the terminal from the project root