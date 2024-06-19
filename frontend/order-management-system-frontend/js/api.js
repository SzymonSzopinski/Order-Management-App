function handleErrors(response) {
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return response.json();
}

export function postOrder(url, data) {
  return fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then(handleErrors)
    .catch((error) => {
      console.error("Error posting data:", error);
      throw error;
    });
}

export function getData(url) {
  return fetch(url)
    .then(handleErrors)
    .catch((error) => {
      console.error("Error fetching data:", error);
      throw error;
    });
}

export function deleteOrder(url) {
  return fetch(url, {
    method: "DELETE",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
    })
    .catch((error) => {
      console.error("Error deleting data:", error);
      throw error;
    });
}

export function getOrderById(url) {
  return fetch(url)
    .then(handleErrors)
    .catch((error) => {
      console.error("Error fetching data:", error);
      throw error;
    });
}

export function updateData(url, data) {
  return fetch(url, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then(handleErrors)
    .catch((error) => {
      console.error("Error updating data:", error);
      throw error;
    });
}
