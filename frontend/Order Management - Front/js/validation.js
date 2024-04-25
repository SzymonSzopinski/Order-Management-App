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
    field.classList.remove("success"); // Zastąp 'success' nazwą klasy reprezentującej stan sukcesu
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
    emptyMessage: "Imię nie może pozostać bez wartości",
  },
  lastName: {
    emptyMessage: "Nazwisko nie może pozostać bez wartości",
  },
  email: {
    emptyMessage: "Email nie może pozostać bez wartości",
    regexMessage: "Nieprawidłowy format adresu email",
    regex: /\S+@\S+\.\S+/,
  },
  phoneNumber: {
    emptyMessage: "Numer telefonu nie może pozostać bez wartości",
    regexMessage: "Wprowadź prawidłowy numer telefonu",
    regex: /^\d{9}$/,
  },
  street: {
    emptyMessage: "Ulica nie może pozostać bez wartości",
  },
  houseNumber: {
    emptyMessage: "Numer domu/mieszkania nie może pozostać bez wartości",
  },
  zipCode: {
    emptyMessage: "Kod pocztowy nie może pozostać bez wartości",
    regexMessage: "Wprowadź prawidłowy kod pocztowy, np. 00-000",
    regex: /^\d{2}-\d{3}$/,
  },
  city: {
    emptyMessage: "Miasto nie może pozostać bez wartości",
  },
  province: {
    emptyMessage: "Województwo nie może pozostać bez wartości",
  },
  country: {
    emptyMessage: "Kraj nie może pozostać bez wartości",
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
    field.id === "paymentMethod"
  ) {
    setValidationState(field, true, "");
    isValid = true;
  }

  return isValid;
};

export const validateForm = (formFields) => {
  let isFormValid = true;

  formFields.forEach((field) => {
    const isFieldValid = validateInputs(field);
    if (!isFieldValid) {
      isFormValid = false;
    }
  });

  return isFormValid;
};
