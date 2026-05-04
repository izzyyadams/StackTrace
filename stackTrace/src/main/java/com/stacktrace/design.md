# StackTrace Design Document

## Project Overview
StackTrace is a web app designed for the software development industry. It helps users track
tasks, timelines, and project progress, while AI assists in deciding task priority amongst all ongoing projects.
This addresses the problem of overcomplicated sprint trackers and reduces the time spent planning software projects. The intended users are
software developers, primarily for personal or small-scale projects.

## Requirements
- Functional requirements
  - Timeline management
  - Task management with priority queue
  - AI-powered task suggestion
  - Progress tracking
  - Overdue task detection
- Non-functional requirements
  - Input validation
  - JUnit testing
  - Exception handling

## Architecture
- Model layer (Event, Task, Timeline)
  - Classes for most basic data structures: task and timeline
  - See data model below
- Service layer (Manager, DAOs, AIHelper)
  - Handles relationship between basic structures, database, and API
  - Manager handles the relationship between timeline and tasks, it holds all of the timelines that UI accesses
  - EventDao is an interface that both TaskDao and TimelineDao to handle CRUD database interactions
  - APIHelper makes calls and handles responses with OpenAI API
- Controller layer (TimelineController)
  - Connect frontend to services
  - Handles API endpoints and uses manager methods to allow user to interact with data
- Frontend (HTML/CSS/JS)
  - Styling and UI

## Data Model
- Event (abstract) → for shared functionality between task and timeline
- Task → to track tasks in a timeline and information about the task
- Timeline → to track timeline and basic information about the timeline itself
- Enums (Status, Priority, Effort) 

## Database Schema
- Tasks Table
  - id: primary key
  - timeline_id: foreign key to timeline table
  - title, description, start_date, deadline, status, priority, effort, created_at, completed_at
- Timelines Table 
  - id: primary key
  - title, description, start_date, deadline, status, created_at, completed_at

## API Endpoints
- GET /timelines → gets all timeline
- GET /timelines/{id} → get timeline
- GET /timelines/{id}/tasks → get tasks from a timeline
- GET /timelines/{id}/tasks/{taskId} → get one task
- GET /timelines/{id}/progress → get timeline progress
- GET /timelines/{id}/overdue → get overdue tasks from a timeline
- GET /timelines/{id}/next → get next task to complete from a timeline
- GET /suggest → gets AI suggestion
- PUT /timelines/{timelineId}/tasks/{taskId} → update task
- PUT /timelines/{id} → update timeline
- POST /timelines → create timeline
- POST /timelines/{id}/tasks → create task
- DELETE /timeslines/{id} → delete timeline
- DELETE /timeslines/{id}/tasks/{id} → delete task
- DELETE /timelines → delete all timelines

## AI Integration

### Why AI is Used
Managing multiple timelines and tasks simultaneously makes it difficult for users to manually
determine which task to prioritize next. The AI integration addresses this by analyzing all
active timelines and tasks together, considering deadlines, priority, effort, and status to suggest the next task.

### How it Works
The AIHelper class sends a prompt to the OpenAI GPT-4.1-mini API containing a structured
summary of all timelines and their tasks. The AI responds with a task title, which is matched
back to a Task object in memory.

### Fallback Behavior
If the API call fails for any reason, the system falls back to getOverallNextTask() which
finds the timeline with the soonest deadline and returns its highest priority task. This
ensures the application remains functional without AI.

## External Libraries and Tools
- H2 Database — embedded relational database for persistence
- Spring Boot — backend framework for REST API and dependency injection
- JUnit 5 — unit testing framework
- Google Fonts — typography
- OpenAI API — AI task suggestion

## Design Decisions
Key decisions you made and why:
- HashMap<Timeline, PriorityQueue<Task>>
  - Timelines are stored as keys in a HashMap with a value of a PriorityQueue of their tasks. This allows O(1) timeline lookup and automatic task ordering by deadline and priority without manual sorting. 
  - The PriorityQueue uses a custom comparator that sorts by earliest deadline first, then priority if they have the same deadline.
- JDBC over ORM
  - More experience with SQL query writing
- File-based H2
  - No external server needed
- Event class and EventDao class
  - Event was designed as an abstract class to capture shared fields and validation logic between Task and Timeline which ensures consistent rules across both subclasses
  - EventDao was designed as a generic interface so that TaskDao and TimelineDao share a consistent set of CRUD operations

## Future Improvements
- Multi user support
- More use of AI to suggest time needed, assigning tasks, etc.
- Notifications
- Mobile version
- Importing/exporting other timelines