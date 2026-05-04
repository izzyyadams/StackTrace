const timelineContainer = document.getElementById("timelineContainer");
const deleteAllButton = document.getElementById('deleteAllTimelinesButton')
// each timeline on the home page
if(timelineContainer) {
    fetch('/timelines')
        .then(response => response.json())
        .then(timelines => {
            console.log('timelines:', timelines);
            for (const timeline of timelines){
                fetch(`/timelines/${timeline.id}/progress`).then(r => r.json())
                    .then(progress => {

                        fetch(`/timelines/${timeline.id}/overdue`).then(r => r.json())
                            .then(overdue => {

                                fetch(`/timelines/${timeline.id}/next`)
                                    .then(r => r.status === 204 ? null : r.json())
                                    .then(next => {

                                        const timelineDiv = document.createElement('div');
                                        timelineDiv.className = 'card';
                                        timelineDiv.innerHTML = `
                                            <h2><a href="timeline.html?id=${timeline.id}">${timeline.title}</a></h2>
                                            <p>Deadline: ${timeline.deadline}</p>
                                            <p>Progress: ${progress}%</p>
                                            <p>Overdue: ${overdue.length}</p>
                                            <p>Next: ${next ? next.title : 'None'}</p>

                                        `;

                                        const editButton = document.createElement('button');
                                        editButton.textContent = 'Edit Timeline';
                                        editButton.addEventListener('click', () => {
                                            window.location.href = `editTimeline.html?id=${timeline.id}`;
                                        });
                                        timelineDiv.appendChild(editButton);
                                        const deleteBtn = document.createElement('button');
                                        deleteBtn.textContent = 'Delete Timeline';
                                        deleteBtn.addEventListener('click', () => {
                                            fetch(`/timelines/${timeline.id}`, {
                                                method: 'DELETE'
                                            }).then(response => {
                                                  if (!response.ok) {
                                                      return response.text().then(error => alert(error));
                                                  } else {
                                                      window.location.reload();
                                                  }
                                              });
                                        });
                                        timelineDiv.appendChild(deleteBtn);
                                        timelineContainer.appendChild(timelineDiv);
                                    })
                            })
                    })

            }
        })

    deleteAllButton.addEventListener('click', () => {
        fetch (`/timelines`, {
            method: 'DELETE'
        }).then(() => window.location.reload());
    });

}


