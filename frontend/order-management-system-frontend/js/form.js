export function createFormObject(form) {
  let formObject = {};

  new FormData(form).forEach((value, key) => {
    const keys = key.split("[").map((k) => k.replace("]", ""));
    keys.reduce((acc, k, i) => {
      if (i === keys.length - 1) {
        if (Number.isInteger(parseInt(k))) {
          acc.push(value);
        } else {
          acc[k] = value;
        }
      } else {
        if (Number.isInteger(parseInt(keys[i + 1]))) {
          acc[k] = acc[k] || [];
        } else {
          acc[k] = acc[k] || {};
        }
      }
      return acc[k];
    }, formObject);
  });

  formObject.orderItem = [];

  const productContainers = document.querySelectorAll(
    ".products-container .display-flex"
  );
  productContainers.forEach((container) => {
    const productId = container.querySelector(".prod-name option").value;
    const quantity = container.querySelector(".field-quantity input").value;
    const unitPrice = container.querySelector(
      ".field-product-price input"
    ).value;

    formObject.orderItem.push({
      productId: productId,
      quantity: parseInt(quantity, 10),
      unitPrice: parseFloat(unitPrice),
    });
  });

  return formObject;
}
