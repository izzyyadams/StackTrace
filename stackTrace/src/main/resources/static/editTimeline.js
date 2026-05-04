const parentDiv = document.getElementById("body");
const titleDiv = document.getElementById("timelineTitle");
const formDiv = document.getElementById("editTimelineForm");


const params = new URLSearchParams(window.location.search);
const id = params.get('id');

if(parentDiv) {
    fetch(`/timelines/${id}`)
        .then(response => response.json())
        .then(timeline => {
            const timelineFormDiv = document.createElement('div');
            timelineFormDiv.className = 'card';
            timelineFormDiv.innerHTML = `
                <label for="title">Title</label>
                <input type="text" id="title" name="title" value="${timeline.title}" required>

                <label for="startDate">Start Date</label>
                <input type="date" id="startDate" name="startDate" value="${timeline.startDate}">

                <label for="deadline">Deadline</label>
                <input type="date" id="deadline" name="deadline" value="${timeline.deadline}" required>

                <label for="description">Description</label>
                <input type="text" id="description" name="description" value="${timeline.description}">

                <label for="status">Status</label>
                <select id="status" name="status">
                    <option value="NOT_STARTED">Not Started</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                </select>

                <button type="submit" id="submitTimelineButton">Submit</button>

            `;
            formDiv.appendChild(timelineFormDiv);
            document.getElementById('status').value = timeline.status;
            const submitButton = document.getElementById('submitTimelineButton');
            submitButton.addEventListener('click', (event) => {
                event.preventDefault();
                    fetch(`/timelines/${id}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            title: document.getElementById('title').value,
                            startDate: document.getElementById('startDate').value,
                            deadline: document.getElementById('deadline').value,
                            status: document.getElementById('status').value,
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



