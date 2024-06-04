import { getData } from "./api.js";

export const loadProducts = (selectId) => {
  getData("http://localhost:8080/products").then((products) => {
    console.log(products);
    if (products) {
      setProductsOptions(selectId, products);
    }
  });
  handleChangeProductSelect(selectId);
};

const setProductsOptions = (selectId, products) => {
  const select = document.getElementById(selectId);

  if (!select) {
    console.error(`Select element with id ${selectId} not found`);
    return;
  }

  products.forEach((product) => {
    const option = document.createElement("option");
    option.value = product.id;
    option.textContent = product.name;
    option.dataset.price = product.unitPrice;
    option.dataset.imageUrl = product.imageUrl;

    select.appendChild(option);
  });
};

const handleChangeProductSelect = (selectId) => {
  const select = document.getElementById(selectId);

  if (!select) {
    console.error(`Select element with id ${selectId} not found`);
    return;
  }

  select.addEventListener("change", () => {
    const selectedOption = select.options[select.selectedIndex];
    const unitPrice = selectedOption.dataset.price || "0";
    const id = selectedOption.value;
    const imageUrl = selectedOption.dataset.imageUrl;
    setDetailsForSelectedProduct(unitPrice, id, imageUrl);
  });
};

const setDetailsForSelectedProduct = (unitPrice, id, imageUrl) => {
  const unitPriceInput = document.getElementById("ordersProductPrice");
  const unitPriceDisplay = document.getElementById("prodUnitPrice");
  const idDisplay = document.getElementById("prodIdDisplay");
  const productImg = document.getElementById("prodImg");

  unitPriceInput.value = unitPrice;
  unitPriceDisplay.textContent = unitPrice;
  idDisplay.value = id;
  idDisplay.textContent = id;

  if (imageUrl) {
    productImg.style.backgroundImage = `url(${imageUrl})`;
    productImg.textContent = "";
  } else {
    productImg.style.backgroundImage = "none";
    productImg.textContent = "brak zdjęcia";
  }
};

export const addProductToList = (
  selectId,
  productCount,
  unitPrice = 0,
  readOnly = true,
  product = null
) => {
  let productId, productName, productImageUrl, quantity;

  if (product) {
    productId = product.id;
    productName = product.name;
    unitPrice = product.unitPrice;
    productImageUrl = product.imageUrl;
    quantity = product.quantity || 1; // Ensure quantity is used from product
  } else {
    const select = document.getElementById(selectId);

    if (!select) {
      console.error(`Select element with id ${selectId} not found`);
      return;
    }

    const selectedOption = select.options[select.selectedIndex];

    if (
      selectedOption.value === "" ||
      selectedOption.textContent === "Wybierz produkt"
    ) {
      alert("Wybierz poprawny produkt przed dodaniem.");
      return;
    }

    productId = selectedOption.value;
    productName = selectedOption.textContent;
    unitPrice = selectedOption.dataset.price || unitPrice;
    productImageUrl = selectedOption.dataset.imageUrl || "";
    quantity = document.getElementById("quantity").value || 1; // Get the quantity from the input field
  }

  console.log(`addProductToList called with quantity: ${quantity}`);

  const productsContainer = document.querySelector(".products-container");
  const clonedDiv = document.createElement("div");

  configureClonedProductRow(
    clonedDiv,
    productId,
    productName,
    productCount,
    quantity,
    unitPrice,
    productImageUrl,
    readOnly
  );

  productsContainer.appendChild(clonedDiv);
  setTimeout(updateTotalCostDisplay, 0);
};

const configureClonedProductRow = (
  clonedDiv,
  productId,
  name,
  productCount,
  quantity,
  unitPrice,
  imageUrl,
  readOnly
) => {
  console.log(`configureClonedProductRow called with quantity: ${quantity}`);

  clonedDiv.innerHTML = `
    <div class="display-flex">
      <div class="product-data">
        <div class="add-product">
          <div>
            <label for="">Produkt</label>
            <select class="form-control prod-name" readonly>
              <option value="${productId}" selected>${name}</option>
            </select>
          </div>
          <div class="form-group field-quantity">
            <label for="">Ilość</label>
            <input type="number" class="form-control" value="${quantity}" min="1" step="1" ${
    readOnly ? "readonly" : ""
  }>
          </div>
          <div class="form-group field-product-price">
            <label for="">Cena PLN</label>
            <input type="number" class="form-control" value="${unitPrice}" step="0.01" ${
    readOnly ? "readonly" : ""
  }>
          </div>
        </div>
        <div class="extra-product-data">
          <div id="prodImg" class="extra-product-data-prod-img" style="background-image: url('${imageUrl}')">
            ${imageUrl ? "" : "brak zdjęcia"}
          </div>
          <div class="text">
            <span>Id produktu: <span id="prodIdDisplay">${productId}</span></span>
          </div>
          <div class="text">
            <span>Cena szt: <span id="prodUnitPrice">${unitPrice}</span> PLN</span>
          </div>
        </div>
      </div>
      <button type="button" class="btn btn-info small-button">-</button>
    </div>`;

  const button = clonedDiv.querySelector("button");

  if (button) {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      const productContainer = e.target.closest(".display-flex");

      if (productContainer) {
        productContainer.parentNode.removeChild(productContainer);
        setTimeout(updateTotalCostDisplay, 0);
      }
    });
  }
};

export const updateTotalCostDisplay = () => {
  const sum = document.querySelector("#sum");
  const sumInput = document.querySelector("#sum-input");

  const totalPrice = calculateTotalCost();
  sum.textContent = totalPrice;
  sumInput.value = totalPrice;
};

const calculateTotalCost = () => {
  let totalCost = 0;

  const quantityInputs = document.querySelectorAll(
    ".products-container .field-quantity input"
  );
  const unitPriceInputs = document.querySelectorAll(
    ".products-container .field-product-price input"
  );

  for (let i = 0; i < quantityInputs.length; i++) {
    const quantity = Number(quantityInputs[i].value);
    const unitPrice = Number(unitPriceInputs[i].value);
    totalCost += quantity * unitPrice;
  }

  return totalCost;
};
