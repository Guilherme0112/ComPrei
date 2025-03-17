const share = document.getElementById("compartilhar");
const carrinho = document.getElementById("carrinho");
const comprar = document.getElementById("comprar");
const msg_link = document.getElementById("msg");
const codigo = document.getElementById("codigo").textContent;

// Atualiza a quantidade desse produto na lixeira
document.getElementById("amountCar").textContent = getItemCarrinho(codigo)?.quantidade || 0;

// Adiciona o produto no carrinho
carrinho.addEventListener("click", function(){

    setItemCarrinho(codigo);

    let amountCar = document.getElementById("amountCar");
    let amountCarInteger = parseInt(amountCar.textContent);
    amountCarInteger++;
    amountCar.textContent = amountCarInteger;

    msg_link.textContent = "Produto adicionado no carrinho com sucesso!";
    msg_link.style.display = "block";

    setTimeout(() => {
        msg_link.style.display = "none";
    }, 2000);
})


// Evento para compartilhar link
share.addEventListener("click", function(){

    const link = window.location.href;

    navigator.clipboard.writeText(link).then(() => {


        msg_link.textContent = "Link copiado com sucesso!";
        msg_link.style.display = "block";

        setTimeout(() => {
            msg_link.style.display = "none";
        }, 2000);

    }).catch(err => {

        console.log(err);
    })
})

comprar.addEventListener("click", function(){

    setItemCarrinho(codigo);

    window.location.href = "/carrinho";
})