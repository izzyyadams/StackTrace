const parentDiv = document.getElementById("body");
const titleDiv = document.getElementById("timelineTitle");
const datesDiv = document.getElementById("timelineDates");
const infoDiv = document.getElementById("timelineInformation");
const nextTaskDiv = document.getElementById("nextTask");
const overdueTasksDiv = document.getElementById("overdueTask");
const allTasksDiv = document.getElementById("allTasks");
const params = new URLSearchParams(window.location.search);
const id = params.get('id');

document.getElementById('addTaskLink').href = `newTask.html?id=${id}`;

if(parentDiv) {
    fetch(`/timelines/${id}`)
        .then(response => response.json())
        .then(timeline => {

            const title = document.createElement('h2');
            title.textContent = timeline.title;
            titleDiv.appendChild(title);

            const startDate = document.createElement('p');
            startDate.textContent = timeline.startDate;
            const deadline = document.createElement('p');
            deadline.textContent = "Deadline: " + timeline.deadline;
            datesDiv.appendChild(startDate);
            datesDiv.appendChild(deadline);

            const description = document.createElement('p');
            description.textContent = "Description: " + timeline.description || "None provided.";
            infoDiv.appendChild(description);

            //TODO: add progress to info
            //TODO: add next task
            //TODO: add overdue tasks

        })

        fetch(`/timelines/${id}/tasks`)
            .then(response => response.json())
            .then(tasks => {
                for (const task of tasks){
                    const taskDiv = document.createElement('div');
                    taskDiv.className = 'task';
                    taskDiv.innerHTML = `
                    <h2>${task.title}</h2>
                    <p>Deadline: ${task.deadline}</p>
                    <p>Status: ${task.status}</p>
                    <p>Priority: ${task.priority}</p>
                    <p>Effort: ${task.effort}</p>
                    <button>Edit Task</button>
                    <button>Delete Task</button>
                    `
                    allTasksDiv.appendChild(taskDiv);
                }

        })
}