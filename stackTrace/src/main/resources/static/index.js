const timelineContainer = document.getElementById("timelineContainer");
// each timeline on the home page
if(timelineContainer) {
    fetch('/timelines')
        .then(response => response.json())
        .then(timelines => {
            for (const timeline of timelines){
                const timelineDiv = document.createElement('div');
                timelineDiv.className = 'timeline';
                timelineDiv.innerHTML = `
                <h2><a href="timeline.html?id=${timeline.id}">${timeline.title}</a></h2>
                <p>Deadline: ${timeline.deadline}</p>
                <p>Progress: </p>
                <p>Number of Overdue Tasks: </p>
                <p>Next Task: </p>
                <button>Edit Timeline</button>
                <button>Delete Timeline</button>
                `
                timelineContainer.appendChild(timelineDiv);
            }
        })
}


