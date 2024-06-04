import { getData, getOrderById } from "./api.js";

const getOrderIdFromUrl = () => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("id");
};

const id = getOrderIdFromUrl();

const editOrderLink = document.querySelector(".btn.btn-info.text-white");

editOrderLink.href = `/html/edit-order.html?id=${id}`;

getOrderById(`http://localhost:8080/orders/order/${id}`)
  .then((order) => {
    document.querySelector("#dateCreated").value = order.dateCreated;

    document.querySelector("#status").value = order.status;

    document.querySelector("#branch").value = order.branch;

    document.querySelector("#deliveryMethod").value = order.deliveryMethod;
    document.querySelector("#paymentMethod").value = order.paymentMethod;

    document.querySelector("#noteToOrder").value = order.noteToOrder;

    getData("http://localhost:8080/products").then((products) => {
      // Dla każdego elementu orderItem w zamówieniu
      order.orderItem.forEach((orderItem) => {
        // Znajdujemy odpowiadający mu produkt
        const product = products.find(
          (prod) => prod.id === orderItem.productId
        );

        // Tworzymy nowy element div dla każdego produktu
        const productDiv = document.createElement("div");

        productDiv.innerHTML = `
        <div class="display-flex">
          <div class="product-data">
            <div class="add-product">
              <div class="prod-name-wrapper">
                <div class="prod-title">Produkt</div>
                <div class="prod-name">${product.name}</div>
              </div>
              <div class="prod-name-wrapper">
                <div class="prod-title">Ilość</div>
                <div class="prod-name">${orderItem.quantity}</div>
              </div>
              <div class="prod-name-wrapper">
                <div class="prod-title">Cena PLN</div>
                <div class="prod-name">${orderItem.unitPrice}</div>
              </div>
            </div>
            <div class="extra-product-data">
              <div
                id="prodImg"
                class="extra-product-data-prod-img "
                style="background-image: url(${product.imageUrl}"></div>
              <div class="text">
                <span>Id produktu:</span>
                <span>${product.id}</span>
              </div>
              <div class="text">
                <span>Cena</span>
                <span>${product.unitPrice}</span>
                <span>PLN</span>
              </div>
            </div>
          </div>
        </div>`;

        // Znajdź element h3
        const h3Element = document.querySelector(".products-container h3");

        // Dodaj nowy div bezpośrednio po h3
        h3Element.insertAdjacentElement("afterend", productDiv);
      });
    });

    document.querySelector("#customerFirstAndLastName").textContent =
      order.customer.firstName + " " + order.customer.lastName;

    document.querySelector("#customerEmail").textContent = order.customer.email;

    document.querySelector("#customerPhoneNumber").textContent =
      order.customer.phoneNumber;

    document.querySelector("#customerNote").textContent =
      order.customer.customerNoteToOrder;

    document.querySelector("#customerAddress").textContent =
      order.customer.address.street +
      " " +
      order.customer.address.houseNumber +
      ", " +
      order.customer.address.zipCode +
      ", " +
      order.customer.address.city;

    document.querySelector("#customerProvince").textContent =
      order.customer.address.province;

    document.querySelector("#customerCountry").textContent =
      order.customer.address.country;

    document.querySelector("#sum").textContent = order.totalPrice;
  })
  .catch((error) => {
    console.error("Error:", error);
  });
