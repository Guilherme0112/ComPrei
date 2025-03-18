function createBox(img, nome, quantidadeP, preco, codigo){

    const divPai = document.createElement("div");
    divPai.id = codigo;
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
    quantidade.id = "quantidade";
    quantidade.textContent = quantidadeP;

    const buttonMais = document.createElement("button")
    buttonMais.id = "mais";
    buttonMais.style.width = "40px";
    buttonMais.textContent = "+"

    const buttonMenos = document.createElement("button")
    buttonMenos.id = "menos"
    buttonMenos.style.width = "40px";
    buttonMenos.textContent = "-"

    divPai.appendChild(imgP);
    divPai.appendChild(nomeProduto);
    divPai.appendChild(price);
    divPai.appendChild(quantidade);
    divPai.appendChild(buttonMais);
    divPai.appendChild(buttonMenos);

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
        box = createBox(dado.photo, dado.name, getItemCarrinho(dado.codigo).quantidade, dado.price, dado.codigo);
        carrinho.appendChild(box);
    });

    let menos = document.querySelectorAll("#menos");
    let mais = document.querySelectorAll("#mais");

    mais.forEach(button => {
        button.addEventListener("click", function(){
            dropItemCarrinho(button.parentElement.id);   
            let amount = button.parentElement.querySelector("#quantidade");
            amount.textContent = parseInt(amount.textContent) + 1;
        })
    });

    menos.forEach(button => {
        button.addEventListener("click", function(){
            dropItemCarrinho(button.parentElement.id);
            
            let amount = button.parentElement.querySelector("#quantidade");

            if(!getItemCarrinho(button.parentElement.id)){
                button.parentElement.remove();
            }
            amount.textContent = parseInt(amount.textContent) - 1;

        })
    });
})