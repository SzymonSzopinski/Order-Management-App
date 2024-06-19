const fields = [
  "#firstName",
  "#lastName",
  "#email",
  "#phoneNumber",
  "#street",
  "#houseNumber",
  "#zipCode",
  "#city",
  "#province",
  "#country",
  "#status",
  "#branch",
  "#deliveryMethod",
  "#paymentMethod",
  "#customerNoteToOrder",
];

export const formFields = fields.map((id) => document.querySelector(id));

formFields.forEach((field) => {
  field.addEventListener("input", () => {
    validateInputs(field);
  });
  field.addEventListener("focusout", () => validateInputs(field));
});

export const resetFormFields = (formFields) => {
  formFields.forEach((field) => {
    field.classList.remove("success");
  });
};

export const setValidationState = (element, isSuccess, errorMessage) => {
  const formControl = element.parentElement;
  const errorDisplay = formControl.querySelector(".error");

  if (isSuccess) {
    if (errorDisplay) {
      errorDisplay.innerText = "";
    }
    element.classList.add("success");
    element.classList.remove("is-invalid");
  } else {
    errorDisplay.innerText = errorMessage;
    element.classList.add("is-invalid");
    element.classList.remove("success");
  }

  if (element.tagName.toLowerCase() === "select" && isSuccess) {
    element.classList.add("success");
  }
};

const fieldsToValidate = {
  firstName: {
    emptyMessage: "First name cannot be empty",
  },
  lastName: {
    emptyMessage: "Last name cannot be empty",
  },
  email: {
    emptyMessage: "Email cannot be empty",
    regexMessage: "Invalid email format",
    regex: /\S+@\S+\.\S+/,
  },
  phoneNumber: {
    emptyMessage: "Phone number cannot be empty",
    regexMessage:
      "Please enter a valid phone number. For US: e.g., (123) 456-7890, for others: e.g., 123456789",
    regex: /^(\(\d{3}\) \d{3}-\d{4}|\d{9})$/,
  },
  street: {
    emptyMessage: "Street cannot be empty",
  },
  houseNumber: {
    emptyMessage: "House number cannot be empty",
  },
  zipCode: {
    emptyMessage: "Zip code cannot be empty",
    regexMessage:
      "Please enter a valid zip code. For US: e.g., 12345 or 12345-6789, for Poland: e.g., 00-000",
    regex: /^(\d{5}(-\d{4})?|\d{2}-\d{3})$/,
  },
  city: {
    emptyMessage: "City cannot be empty",
  },
  province: {
    emptyMessage: "Province cannot be empty",
  },
  country: {
    emptyMessage: "Country cannot be empty",
  },
};

export const validateInputs = (field) => {
  const fieldValue = field.value.trim();
  let isValid = false;

  if (fieldsToValidate.hasOwnProperty(field.id)) {
    const { emptyMessage, regexMessage, regex } = fieldsToValidate[field.id];

    if (fieldValue === "") {
      setValidationState(field, false, emptyMessage);
    } else if (regex && !regex.test(fieldValue)) {
      setValidationState(field, false, regexMessage);
    } else {
      setValidationState(field, true, "");
      isValid = true;
    }
  } else if (
    field.id === "status" ||
    field.id === "branch" ||
    field.id === "deliveryMethod" ||
    field.id === "paymentMethod" ||
    field.id === "customerNoteToOrder"
  ) {
    setValidationState(field, true, "");
    isValid = true;
  }

  return isValid;
};

const validateProductList = () => {
  const productsContainer = document.querySelector(".products-container");
  const productItems = productsContainer.querySelectorAll(".display-flex");

  if (productItems.length === 0) {
    alert("Order must contain at least one product.");
    return false;
  }

  return true;
};

export const validateForm = (formFields) => {
  let isFormValid = true;

  formFields.forEach((field) => {
    const isFieldValid = validateInputs(field);
    if (!isFieldValid) {
      isFormValid = false;
    }
  });

  const isProductListValid = validateProductList();
  if (!isProductListValid) {
    isFormValid = false;
  }

  return isFormValid;
};
