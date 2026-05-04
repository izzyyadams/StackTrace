const parentDiv = document.getElementById("body");
const titleDiv = document.getElementById("timelineTitle");
const datesDiv = document.getElementById("timelineDates");
const infoDiv = document.getElementById("timelineInformation");
const nextTaskDiv = document.getElementById("nextTask");
const overdueTasksDiv = document.getElementById("overdueTasks");
const allTasksDiv = document.getElementById("allTasks");
const params = new URLSearchParams(window.location.search);
const id = params.get('id');

document.getElementById('addTaskLink').href = `newTask.html?id=${id}`;

if(parentDiv) {
    fetch(`/timelines/${id}`)
        .then(response => response.json())
        .then(timeline => {
            fetch(`/timelines/${id}/progress`)
                .then(response => response.json())
                .then(progress => {
                    fetch(`/timelines/${id}/overdue`)
                        .then(response => response.json())
                        .then(overdueTasks => {
                            fetch(`/timelines/${id}/next`)
                                .then(response => response.status === 204 ? null : response.json())
                                .then(next => {


                                    const title = document.createElement('h1');
                                    title.className = 'title';
                                    title.textContent = timeline.title;
                                    titleDiv.appendChild(title);

                                    const startDate = document.createElement('p');
                                    startDate.textContent = "Start Date: "+ timeline.startDate;
                                    const deadline = document.createElement('p');
                                    deadline.textContent = "Deadline: " + timeline.deadline;
                                    datesDiv.appendChild(startDate);
                                    datesDiv.appendChild(deadline);

                                    const status = document.createElement('p');
                                    status.textContent = "Status: " + timeline.status;
                                    infoDiv.appendChild(status);

                                    const description = document.createElement('p');
                                    description.textContent = "Description: " + timeline.description || "None provided.";
                                    infoDiv.appendChild(description);

                                    const progressP = document.createElement('p');
                                    progressP.textContent = "Progress: " + progress + "%";
                                    infoDiv.appendChild(progressP);

                                    const nextTaskP = document.createElement('p');
                                    nextTaskP.textContent = "Next Task: " + (next ? next.title : 'None');
                                    nextTaskDiv.appendChild(nextTaskP);


                                    if(overdueTasks.length === 0) {
                                        const overDueTaskP = document.createElement('p');
                                        overDueTaskP.textContent = "None";
                                        overdueTasksDiv.appendChild(overDueTaskP);
                                    }
                                    for (const overdueTask of overdueTasks ) {
                                        const overDueTaskP = document.createElement('p');
                                        overDueTaskP.textContent = overdueTask.title;
                                        overdueTasksDiv.appendChild(overDueTaskP);
                                    }

                            })
                        })
                })
        })



        fetch(`/timelines/${id}/tasks`)
            .then(response => response.json())
            .then(tasks => {
                for (const task of tasks){
                    const taskDiv = document.createElement('div');
                    taskDiv.className = 'task card';
                    taskDiv.innerHTML = `
                    <h3>${task.title}</h3>
                    <p>Deadline: ${task.deadline}</p>
                    <div style="display: flex; gap: 16px;">
                        <p>Status: ${task.status}</p>
                        <p>Priority: ${task.priority}</p>
                        <p>Effort: ${task.effort}</p>
                    </div>

                    `
                    allTasksDiv.appendChild(taskDiv);
                    const editButton = document.createElement('button');
                    editButton.textContent = 'Edit Task';
                    editButton.addEventListener('click', () => {
                        window.location.href = `editTask.html?id=${id}&taskId=${task.id}`;
                    });
                    taskDiv.appendChild(editButton);
                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = 'Delete Task';
                    deleteButton.addEventListener('click', () => {
                        fetch(`/timelines/${id}/tasks/${task.id}`, {
                            method: 'DELETE'
                        }).then(response => {
                            if (!response.ok) {
                                return response.text().then(error => alert(error));
                            } else {
                                window.location.reload();
                            }
                        });
                    });
                    taskDiv.appendChild(deleteButton);
                }

        })

    document.getElementById('deleteTimelineButton').addEventListener('click', () => {
        fetch (`/timelines/${id}`, {
            method: 'DELETE'
        }).then(() => window.location.href = 'index.html');
    });

    document.getElementById('editTimelineButton').addEventListener('click', () => {
        window.location.href = `editTimeline.html?id=${id}`;
    });

}