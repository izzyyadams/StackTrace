const submitButton = document.getElementById('submitTaskButton')
const params = new URLSearchParams(window.location.search);
const id = params.get('id');

if (submitButton) {
    submitButton.addEventListener('click', (event) => {
    event.preventDefault();
        fetch(`/timelines/${id}/tasks`, {
            method: 'POST',
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
        }).then(() => window.location.href = `timeline.html?id=${id}`);
    });
}