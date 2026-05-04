const submitButton = document.getElementById('submitTimelineButton')
if (submitButton) {
    submitButton.addEventListener('click', (event) => {
    event.preventDefault();
        fetch('/timelines', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                startDate: document.getElementById('startDate').value,
                deadline: document.getElementById('deadline').value,
                description: document.getElementById('description').value,
                status: document.getElementById('status').value
            })
        }).then(response => {
              if (!response.ok) {
                  return response.text().then(error => alert(error));
              } else {
                  window.location.href = 'index.html'
              }
          });
    });
}