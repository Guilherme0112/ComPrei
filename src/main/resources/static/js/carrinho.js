function createBox(img, nome, quantidadeP, preco){

    const divPai = document.createElement("div");
    divPai.className = "box_carrinho";

    const imgP = document.createElement("img");
    imgP.className = "box_img_carrinho";
    imgP.src = img;

    const nomeProduto = document.createElement("p");
    nomeProduto.className = "box_nome_produto";
    nomeProduto.textContent = nome;

    const price = document.createElement("p");
    price.className = "box_preco_produto";
    price.textContent = preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

    const quantidade = document.createElement("p");
    quantidade.className = "box_quantidade_produto";
    quantidade.textContent = quantidadeP;

    divPai.appendChild(imgP);
    divPai.appendChild(nomeProduto);
    divPai.appendChild(price);
    divPai.appendChild(quantidade);

    return divPai;
}


document.addEventListener("DOMContentLoaded", async() =>{
    var produtos = [];

    const produtosCarrinho = getCarrinho();
    
    produtosCarrinho.forEach(produto => {
        produtos.push(produto.codigo);
    });
    
    const res = await fetch("/carrinho/produtos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            produtos
        })
    });
    
    const data = await res.json();
    
    const carrinho = document.getElementById("carrinho");
    let box = null;
    data.forEach(dado => {
        box = createBox(dado.photo, dado.name, getItemCarrinho(dado.codigo).quantidade, dado.price);
        carrinho.appendChild(box);
    });
})