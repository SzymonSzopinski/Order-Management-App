import { getData, deleteOrder } from "./api.js";

document.addEventListener("DOMContentLoaded", function () {
  loadOrders();
});

function loadOrders() {
  getData("http://localhost:8080/orders")
    .then((data) => {
      console.log("Received data:", data);
      renderOrders(data);
      addDeleteEventHandlers();
    })
    .catch((error) => {
      console.error("Error loading orders:", error);
    });
}
function renderOrders(data) {
  let rows = "";
  data.forEach((order) => {
    const customer = order.customer ? order.customer : {};
    rows += `<tr id="order-${order.id}">
            <td>${order.id}</td>
            <td>
                <a style="color: #d2ab67" href="/html/order-view.html?id=${
                  order.id
                }">
                    ${customer.firstName + customer.lastName + "#" + order.id}
                </a>
            </td>
            <td>${order.branch}</td>
            <td>${order.dateCreated}</td>
            <td>${order.status}</td>
            <td><a class="buttonDelete" data-id="${order.id}">-</a></td>
        </tr>`;
  });
  document.getElementById("tableRows").innerHTML = rows;
}

function addDeleteEventHandlers() {
  document.querySelectorAll(".buttonDelete").forEach((button) => {
    button.onclick = function () {
      if (window.confirm("Are you sure you want to delete this order?")) {
        deleteOrder(`http://localhost:8080/orders/delete/${this.dataset.id}`)
          .then(() => {
            document.querySelector(`#order-${this.dataset.id}`).remove();
          })
          .catch((error) => {
            console.error("Error deleting order:", error);
          });
      }
    };
  });
}
