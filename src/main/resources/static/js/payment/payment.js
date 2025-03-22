
document.addEventListener("DOMContentLoaded", async () => {

    var carrinho = getCarrinho()

    const res = await fetch("/payment", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(
            carrinho
        )
    })

    console.log(JSON.stringify(carrinho));

    const data = await res.text();

    if(!data){
        document.querySelector("#text").textContent = "Ocorreu algum erro. Tente novamente mais tarde";
        return;
    }

    document.querySelector("#link").href = data;
    window.location.href = data;
})

