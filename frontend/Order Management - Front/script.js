const sidebar = document.querySelector(".sidebar");
const toggleBtn = document.querySelector(".toggle-btn");
const form = document.querySelector("#form");
const fieldsToValidate = [
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
  "#orderStatus",
  "#branch",
  "#shippingMethod",
  "#paymentMethod",
];
const formFields = fieldsToValidate.map((id) => document.querySelector(id));

formFields.forEach((field) => {
  field.addEventListener("input", () => {
    validateInputs(field);
  });

  field.addEventListener("focusout", () => {
    validateInputs(field);
  });
});

form.addEventListener("submit", (e) => {
  e.preventDefault();

  formFields.forEach((field) => {
    validateInputs(field);
  });
});

toggleBtn.addEventListener("click", () => {
  sidebar.classList.toggle("active");
});

const setError = (element, message) => {
  const formControl = element.parentElement;
  console.log(formControl);
  const errorDisplay = formControl.querySelector(".error");

  errorDisplay.innerText = message;
  element.classList.add("is-invalid"); // Dodajemy klasę 'is-invalid' bezpośrednio do elementu input
  element.classList.remove("success"); // Jeśli masz taką klasę przypisaną do inputów, tutaj zostanie usunięta
};

const setSuccess = (element) => {
  const formControl = element.parentElement;
  const errorDisplay = formControl.querySelector(".error");

  if (element.tagName.toLowerCase() === "select") {
    element.classList.add("success");
    console.log(element);
  } else {
    errorDisplay.innerText = "";
    element.classList.add("success");
    element.classList.remove("is-invalid");
  }
};

const setSuccessForSelect = (element) => {
  element.classList.add("success");
};

const validateInputs = (field) => {
  const fieldValue = field.value.trim();

  if (field.id === "firstName") {
    if (fieldValue === "") {
      setError(field, "Imię nie może pozostać bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "lastName") {
    if (fieldValue === "") {
      setError(field, "Nazwisko nie może pozostać bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "email") {
    if (fieldValue === "") {
      setError(field, "Email nie może pozostać bez wartości");
    } else if (!/\S+@\S+\.\S+/.test(fieldValue)) {
      setError(field, "Wprowadź prawidłowy adres e-mail");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "phoneNumber") {
    if (fieldValue === "") {
      setError(field, "Numer telefonu nie może pozostać bez wartości");
    } else if (!/^\d{9}$/.test(fieldValue)) {
      setError(field, "Wprowadź prawidłowy numer telefonu");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "street") {
    if (fieldValue === "") {
      setError(field, "Ulica nie może pozostać bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "houseNumber") {
    if (fieldValue === "") {
      setError(field, "Numer domu nie może pozostać bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "zipCode") {
    if (fieldValue === "") {
      setError(field, "Kod pocztowy nie może pozostać bez wartości");
    } else if (!/^\d{2}-\d{3}$/.test(fieldValue)) {
      setError(field, "Wprowadź prawidłowy kod pocztowy, np. 00-000");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "city") {
    if (fieldValue === "") {
      setError(field, "Miasto nie może pozostać bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "province") {
    if (fieldValue === "") {
      setError(field, "Województwo nie moze pozostac bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (field.id === "country") {
    if (fieldValue === "") {
      setError(field, "Kraj nie moze pozostac bez wartości");
    } else {
      setSuccess(field);
    }
  } else if (
    field.id === "orderStatus" ||
    field.id === "branch" ||
    field.id === "shippingMethod" ||
    field.id === "paymentMethod"
  ) {
    setSuccess(field);
  }
};
