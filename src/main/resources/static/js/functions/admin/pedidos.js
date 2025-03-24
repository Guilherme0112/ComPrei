document.addEventListener("DOMContentLoaded", async() => {

    const res = fetch("/admin/get/pedidos", {
        method: "GET"
    });

    const data = res.json();

    console.log(data);

})