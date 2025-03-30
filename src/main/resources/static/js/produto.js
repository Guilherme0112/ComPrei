// Inicializa as varaiveis
const share = document.getElementById("compartilhar");
const carrinho = document.getElementById("carrinho");
const comprar = document.getElementById("comprar");
const msg_link = document.getElementById("msg");
const codigo = document.getElementById("codigo").textContent;

// Atualiza a quantidade desse produto na lixeira
document.getElementById("amountCar").textContent = getItemCarrinho(codigo)?.quantidade || 0;

// Adiciona o produto no carrinho
carrinho.addEventListener("click", function(){

    // Seta o produto no carrinho (LocalStorage)
    setItemCarrinho(codigo);

    // Pega o valor atual que está sendo exibido e converte para integer
    let amountCar = document.getElementById("amountCar");
    let amountCarInteger = parseInt(amountCar.textContent);

    // Incrementa quando o usuário adiciona o produto no carrinho
    amountCarInteger++;

    // Atualiza a quantidade para o usuário
    amountCar.textContent = amountCarInteger;

    // Adiciona a mensagem e exibe a popup com a mensagem para o usuário
    msg_link.textContent = "Produto adicionado no carrinho com sucesso!";
    msg_link.style.display = "block";

    // Depois de 2 segundos ele oculta a popup
    setTimeout(() => {
        msg_link.style.display = "none";
    }, 2000);
})


// Evento para compartilhar link
share.addEventListener("click", function(){

    // Pega o link da página
    const link = window.location.href;

    // Copia o link para o usuário
    navigator.clipboard.writeText(link).then(() => {

        // Exibe uma popup avisando que o link foi copiado
        msg_link.textContent = "Link copiado com sucesso!";
        msg_link.style.display = "block";

        // Após 2 segundos fecha a popup
        setTimeout(() => {
            msg_link.style.display = "none";
        }, 2000);

    }).catch(err => {

        console.log(err);
    })
})

// Se o usuário clicar em comprar, ele é redirecionado para o carrinho para 
// finalizar a compra 
comprar.addEventListener("click", function(){

    setItemCarrinho(codigo);
    window.location.href = "/carrinho";
})