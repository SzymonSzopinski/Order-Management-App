import { validateForm, formFields, resetFormFields } from "./validation.js";
import { createFormObject } from "./form.js";
import { postOrder, getData } from "./api.js";

const form = document.querySelector("#form");
const messageDiv = document.querySelector("#order-added-success");
const closeAlertButton = document.querySelector("#order-added-success .close");

// set date input as actual day
const today = new Date();
const day = String(today.getDate()).padStart(2, "0");
const month = String(today.getMonth() + 1).padStart(2, "0"); // Miesiące są indeksowane od 0 w JavaScript
const year = today.getFullYear();
const currentDate = `${year}-${month}-${day}`;
document.querySelector('input[type="date"]').value = currentDate;

//alert succes div close
closeAlertButton.addEventListener("click", () => {
  messageDiv.style.display = "none";
});

const submitForm = (form) => {
  const formObject = createFormObject(form);
  // clearProductRows();
  console.log(formObject);
  return postOrder("http://localhost:8080/orders", formObject);
};

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const isFormValid = validateForm(formFields);
  console.log(isFormValid); //log if isFormValid is true
  if (isFormValid) {
    submitForm(form).then(() => {
      form.reset();
      messageDiv.style.display = "block";
    });
    resetFormFields(formFields);
  }
  window.scrollTo({ top: 0, behavior: "smooth" });
});

const setProductsOptions = (products) => {
  const select = document.getElementById("selectOrderProducts");

  products.forEach((product) => {
    const option = document.createElement("option");
    option.value = product.id;
    option.textContent = product.name;
    option.dataset.price = product.unitPrice;

    select.appendChild(option);
  });
};

const setDetailsForSelectedProduct = (unitPrice, id) => {
  const unitPriceInput = document.getElementById("ordersProductPrice");
  const unitPriceDisplay = document.getElementById("prodUnitPrice");
  const idDisplay = document.getElementById("prodIdDisplay");

  unitPriceInput.value = unitPrice;
  unitPriceDisplay.textContent = unitPrice;
  idDisplay.value = id;
  idDisplay.textContent = id;
};

const handleChangeProductSelect = () => {
  const select = document.getElementById("selectOrderProducts");

  select.addEventListener("change", () => {
    const selectedOption = select.options[select.selectedIndex];
    const unitPrice = selectedOption.dataset.price || "0";
    const id = selectedOption.value;
    setDetailsForSelectedProduct(unitPrice, id);
  });
};

const loadProducts = () => {
  getData("http://localhost:8080/products").then((products) => {
    if (products) {
      setProductsOptions(products);
    }
  });
  handleChangeProductSelect();
};

// Module for managing order products
const orderProductsManager = (() => {
  let productCount = document.querySelectorAll(".product-container").length - 1;

  // Get the selected product ID and name from dropdown
  const getSelectedProductId = () => {
    const select = document.getElementById("selectOrderProducts");
    const selectedOption = select.options[select.selectedIndex];

    return {
      id: selectedOption.value,
      name: selectedOption.textContent,
    };
  };

  // Clone and configure product row
  const cloneProductRow = () => {
    const displayFlexDiv = document.querySelector(".display-flex");
    const selectedProduct = getSelectedProductId();
    const clonedDiv = displayFlexDiv.cloneNode(true);
    configureClonedProductRow(
      clonedDiv,
      selectedProduct.id,
      selectedProduct.name
    );

    productCount++;

    return clonedDiv;
  };

  const configureClonedProductRow = (clonedDiv, productId, productName) => {
    const select = clonedDiv.querySelector(".add-product select");
    const label = clonedDiv.querySelector(".add-product label");
    const inputQuantity = clonedDiv.querySelector(".field-quantity input");
    const inputPrice = clonedDiv.querySelector(".field-product-price input");
    const button = clonedDiv.querySelector("#add-product-to-order");

    console.log(button);

    if (select) {
      // Remove all options
      while (select.firstChild) {
        select.removeChild(select.firstChild);
      }

      // Add the selected option
      const option = document.createElement("option");
      option.value = productId;
      option.text = productName;
      select.appendChild(option);

      select.name = `orderItem[${productCount}][productId]`;
      select.setAttribute("readonly", "true");
    }
    if (label) {
      label.textContent = "Produkt";
    }

    if (inputQuantity) {
      inputQuantity.setAttribute("readonly", true);
      inputQuantity.name = `orderItem[${productCount}][quantity]`;
    }

    if (inputPrice) {
      inputPrice.setAttribute("readonly", true);
      inputPrice.name = `orderItem[${productCount}][unitPrice]`;
    }

    console.log(button);

    button.textContent = "-";

    if (button) {
      button.addEventListener("click", (e) => {
        e.preventDefault();

        const productContainer = e.target.closest(".display-flex");

        if (productContainer) {
          productContainer.parentNode.removeChild(productContainer);

          updateTotalCostDisplay();
        }
      });
    }

    clonedDiv.classList.remove("product-container");
  };

  // Remove empty list message if present
  const updateEmptyListMessage = () => {
    const productsListContainer = document.querySelector(".products-container");
    const emptyListMessage = productsListContainer.querySelector(".empty-list");
    if (emptyListMessage) {
      productsListContainer.removeChild(emptyListMessage);
    }
  };

  // Public method to add product to list
  const addProductToList = () => {
    const productId = getSelectedProductId();
    const clonedProductRow = cloneProductRow(productId);

    updateEmptyListMessage();
    document.querySelector(".products-container").appendChild(clonedProductRow);
    updateTotalCostDisplay();
  };

  return {
    addProduct: addProductToList,
  };
})();

const updateTotalCostDisplay = () => {
  const sum = document.querySelector("#sum");
  const sumInput = document.querySelector("#sum-input");

  const totalPrice = calculateTotalCost();
  sum.textContent = totalPrice;
  sumInput.value = totalPrice;
};

//calculate the total cost of all products in the list
const calculateTotalCost = () => {
  let totalCost = 0;

  const quantityInputs = document.querySelectorAll(
    'input[name^="orderItem"][name$="[quantity]"]'
  );
  const unitPriceInputs = document.querySelectorAll(
    'input[name^="orderItem"][name$="[unitPrice]"]'
  );

  for (let i = 0; i < quantityInputs.length; i++) {
    const quantity = Number(quantityInputs[i].value);
    const unitPrice = Number(unitPriceInputs[i].value);
    totalCost += quantity * unitPrice;
  }

  return totalCost;
};

// Event listener setup
document
  .getElementById("add-product-to-order")
  .addEventListener("click", (e) => {
    e.preventDefault();
    orderProductsManager.addProduct();
  });

document.addEventListener("DOMContentLoaded", loadProducts);
