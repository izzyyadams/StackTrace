const parentDiv = document.getElementById("body");
const titleDiv = document.getElementById("taskTitle");
const formDiv = document.getElementById("editTaskForm");
console.log('parentDiv:', parentDiv);


const params = new URLSearchParams(window.location.search);
const id = params.get('id');
const taskId = params.get('taskId');

if(parentDiv) {
    fetch(`/timelines/${id}/tasks/${taskId}`)
        .then(response => response.json())
        .then(task => {
            const taskFormDiv = document.createElement('div');
            taskFormDiv.className = 'card';
            taskFormDiv.innerHTML = `
                <label for="title">Title</label>
                <input type="text" id="title" name="title" value="${task.title}" required>

                <label for="startDate">Start Date</label>
                <input type="date" id="startDate" name="startDate" value="${task.startDate}">

                <label for="deadline">Deadline</label>
                <input type="date" id="deadline" name="deadline" value="${task.deadline}" required>

                <label for="description">Description</label>
                <input type="text" id="description" name="description" value="${task.description}">

                <label for="status">Status</label>
                <select id="status" name="status">
                    <option value="NOT_STARTED">Not Started</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                </select>

                <label for="priority">Priority</label>
                <select id="priority" name="priority">
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                </select>

                <label for="effort">Effort</label>
                <select id="effort" name="effort">
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                </select>

                <button type="submit" id="submitTaskButton">Submit</button>

            `;
            formDiv.appendChild(taskFormDiv);
            document.getElementById('status').value = task.status;
            document.getElementById('priority').value = task.priority;
            document.getElementById('effort').value = task.effort;
            const submitButton = document.getElementById('submitTaskButton');
            submitButton.addEventListener('click', (event) => {
                event.preventDefault();
                    fetch(`/timelines/${id}/tasks/${taskId}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            title: document.getElementById('title').value,
                            startDate: document.getElementById('startDate').value,
                            deadline: document.getElementById('deadline').value,
                            status: document.getElementById('status').value,
                            priority: document.getElementById('priority').value,
                            effort: document.getElementById('effort').value,
                            timelineId: parseInt(id)
                        })
                    }).then(response => {
                          if (!response.ok) {
                              return response.text().then(error => alert(error));
                          } else {
                              window.location.href = `timeline.html?id=${id}`;
                          }
                      });
                });
        })

}



