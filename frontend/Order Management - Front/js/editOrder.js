import { getData, getOrderById, putData } from "./api.js";
import { validateForm, formFields, resetFormFields } from "./validation.js";
import { createFormObject } from "./form.js";
import {
  loadProducts,
  addProductToList,
  updateTotalCostDisplay,
} from "./productOrderManager.js";

const getOrderIdFromUrl = () => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("id");
};

const id = getOrderIdFromUrl();
let productCount = 0;

const form = document.querySelector("#form");
const messageDiv = document.querySelector("#order-added-success");
const closeAlertButton = document.querySelector("#order-added-success .close");

getOrderById(`http://localhost:8080/orders/order/${id}`).then((order) => {
  console.log("Order loaded: ", order);
  document.querySelector("#dateCreated").value = order.dateCreated;
  document.querySelector("#status").value = order.status;
  document.querySelector("#branch").value = order.branch;
  document.querySelector("#deliveryMethod").value = order.deliveryMethod;
  document.querySelector("#paymentMethod").value = order.paymentMethod;
  document.querySelector("#noteToOrder").value = order.noteToOrder;
  document.querySelector("#firstName").value = order.customer.firstName;
  document.querySelector("#lastName").value = order.customer.lastName;
  document.querySelector("#phoneNumber").value = order.customer.phoneNumber;
  document.querySelector("#email").value = order.customer.email;
  document.querySelector("#customerNoteToOrder").value =
    order.customer.customerNoteToOrder;
  document.querySelector("#street").value = order.customer.address.street;
  document.querySelector("#houseNumber").value =
    order.customer.address.houseNumber;
  document.querySelector("#zipCode").value = order.customer.address.zipCode;
  document.querySelector("#city").value = order.customer.address.city;
  document.querySelector("#province").value = order.customer.address.province;
  document.querySelector("#country").value = order.customer.address.country;
  document.querySelector("#sum").textContent = order.totalPrice;

  displayProductsInOrder(order.orderItem);
  setTimeout(updateTotalCostDisplay, 0);
});

const displayProductsInOrder = (orderItems) => {
  const productsContainer = document.querySelector(".products-container");
  console.log("displayProductsInOrder called");
  productsContainer.innerHTML = '<h3 class="title-list">Lista produktów</h3>';
  orderItems.forEach((item) => {
    console.log("Loading product with ID: ", item.productId);
    getData(`http://localhost:8080/products/product/${item.productId}`)
      .then((product) => {
        if (product) {
          console.log("Product loaded: ", product);
          addProductToList(
            "selectOrderProducts",
            ++productCount,
            item.unitPrice,
            false,
            { ...product, quantity: item.quantity } // Ensure quantity is included
          );
        } else {
          console.error(`Product with id ${item.productId} not found`);
        }
      })
      .catch((error) => {
        console.error(
          `Error fetching product with id ${item.productId}:`,
          error
        );
      });
  });
};

const submitForm = (form) => {
  const formObject = createFormObject(form);
  console.log("Submitting form with data:", formObject);
  return putData(`http://localhost:8080/orders/update/${id}`, formObject);
};

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const isFormValid = validateForm(formFields);
  console.log(isFormValid);
  if (isFormValid) {
    submitForm(form).then((order) => {
      messageDiv.style.display = "block";
      window.location.href = `order-view.html?id=${order.id}`;
    });
    resetFormFields(formFields);
  }
  window.scrollTo({ top: 0, behavior: "smooth" });
});

document.addEventListener("DOMContentLoaded", () => {
  console.log("DOMContentLoaded event fired");
  loadProducts("selectOrderProducts");
});

document
  .getElementById("add-product-to-order")
  .addEventListener("click", (e) => {
    e.preventDefault();
    addProductToList("selectOrderProducts", ++productCount);
  });
