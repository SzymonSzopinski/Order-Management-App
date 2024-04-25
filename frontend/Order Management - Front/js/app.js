const sidebar = document.querySelector(".sidebar");
const toggleBtn = document.querySelector(".toggle-btn");

//sidebar close
toggleBtn.addEventListener("click", () => {
  sidebar.classList.toggle("active");
});
