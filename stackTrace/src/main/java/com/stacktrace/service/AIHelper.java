package com.stacktrace.service;

import com.stacktrace.model.Manager;
import com.stacktrace.model.Task;
import com.stacktrace.model.Timeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIHelper {

    @Value("${openai.api.key}")
    private String apiKey;
    private final Manager manager;

    public AIHelper(Manager manager) {
        this.manager = manager;
    }
    public Task suggestNextTask() {
        try {
            String prompt = "I will provide a list of timelines and tasks. " +
                    "I need you to choose which task of all of the tasks should be chosen next. " +
                    "You should consider deadline of both the timeline and the task. " +
                    "You should consider status. " +
                    "You should consider the task's priority and effort. " +
                    "You should choose the task that makes most sense to do next based on these factors." +
                    "You should only consider tasks that are not completed." +
                    "Respond with ONLY the task title, nothing else.";
            StringBuilder data = new StringBuilder("Here are the timelines and tasks:\n");
                for (Timeline timeline : manager.getAllTimelines()) {
                    data.append("Timeline: ").append(timeline.getTitle())
                            .append(", Deadline: ").append(timeline.getDeadline())
                            .append("\n");
                    for (Task task : manager.getTimelinesTasks(timeline)) {
                        data.append("  Task: ").append(task.getTitle())
                                .append(", Deadline: ").append(task.getDeadline())
                                .append(", Status: ").append(task.getStatus())
                                .append(", Priority: ").append(task.getPriority())
                                .append(", Effort: ").append(task.getEffort())
                                .append("\n");
                    }
                }

                String safeData = data.toString()
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String requestBody = "{\n" +
                    "\"model\": \"gpt-4.1-mini\",\n" +
                    "\"messages\": [\n" +
                    "  {\"role\": \"system\", \"content\": \"" + prompt + "\"},\n" +
                    "  {\"role\": \"user\", \"content\": \"" + safeData + "\"}\n" +
                    "]\n" +
                    "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();

            String content = responseBody.split("\"content\":\\s*\"")[1].split("\"")[0];
            for (Timeline timeline : manager.getAllTimelines()) {
                for (Task task : manager.getTimelinesTasks(timeline)) {
                    if (task.getTitle().equalsIgnoreCase(content.trim())) {
                        return task;
                    }
                }
            }
            return manager.getOverallNextTask();

        } catch (Exception e) {
            return manager.getOverallNextTask();
        }
    }


}
