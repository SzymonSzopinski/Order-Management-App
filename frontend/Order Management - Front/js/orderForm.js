import { validateForm, formFields, resetFormFields } from "./validation.js";
import { createFormObject } from "./form.js";
import { postOrder } from "./api.js";
import {
  loadProducts,
  addProductToList,
  updateTotalCostDisplay,
} from "./productOrderManager.js";

let productCount = 0; // Define productCount globally

const form = document.querySelector("#form");
const messageDiv = document.querySelector("#order-added-success");
const closeAlertButton = document.querySelector("#order-added-success .close");
const emptyList = document.querySelector(".empty-list");

// Set date input as actual day
const today = new Date();
const day = String(today.getDate()).padStart(2, "0");
const month = String(today.getMonth() + 1).padStart(2, "0");
const year = today.getFullYear();
const currentDate = `${year}-${month}-${day}`;
document.querySelector('input[type="date"]').value = currentDate;

// Alert success div close
closeAlertButton.addEventListener("click", () => {
  messageDiv.style.display = "none";
});

const submitForm = (form) => {
  const formObject = createFormObject(form);
  return postOrder("http://localhost:8080/orders", formObject);
};

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const isFormValid = validateForm(formFields);
  console.log(isFormValid); // Log if isFormValid is true
  if (isFormValid) {
    submitForm(form).then((order) => {
      form.reset();
      messageDiv.style.display = "block";
      window.location.href = `order-view.html?id=${order.id}`;
    });
    resetFormFields(formFields);
  }
  window.scrollTo({ top: 0, behavior: "smooth" });
});

// Event listener setup
document.addEventListener("DOMContentLoaded", () => {
  loadProducts("selectOrderProducts");
});

document
  .getElementById("add-product-to-order")
  .addEventListener("click", (e) => {
    e.preventDefault();
    addProductToList("selectOrderProducts", ++productCount);

    if (emptyList) {
      emptyList.remove();
    }
  });
