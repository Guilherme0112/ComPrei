
// Função que retorna a div dos produtos do carrinho
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
    price.id = "preco";
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


document.addEventListener("DOMContentLoaded", async() => {

    // Redireciona o usuário para o login
    if(document.getElementById("login")){
        document.getElementById("login").addEventListener("click", function(){
            window.location.href = "/auth/login";
        });
    }

    var produtos = [];

    const produtosCarrinho = getCarrinho();
    
    produtosCarrinho.forEach(produto => {
        produtos.push(produto.codigo);
    });
    
    // Faz a requisição para buscar os produtos do carrinho
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
    let total = 0;

    // Cria os produtos do carrinho 
    data.forEach(dado => {
        total = total + (dado.price * getItemCarrinho(dado.codigo).quantidade);
        box = createBox(dado.photo, dado.name, getItemCarrinho(dado.codigo).quantidade, dado.price, dado.codigo);
        carrinho.appendChild(box);
    });

    // Coloca o valor total do carrinho
    document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

    // Pega todos os botões de aumentar e diminuir
    let menos = document.querySelectorAll("#menos");
    let mais = document.querySelectorAll("#mais");

    // Evento que adiciona produto do carrinho
    mais.forEach(button => {
        button.addEventListener("click", function(){

            setItemCarrinho(button.parentElement.id);   

            let amount = button.parentElement.querySelector("#quantidade");
            let precoProduto = parseFloat(button.parentElement.querySelector("#preco").textContent.replace(/[^\d,]/g, '').replace(',', '.'));
            let total = document.getElementById("total").textContent.replace(/[^\d,]/g, '').replace(',', '.');
            total = parseFloat(total) + precoProduto;
            document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            amount.textContent = parseInt(amount.textContent) + 1;
        })
    });

    // Evento que tira produto do carrinho
    menos.forEach(button => {
        button.addEventListener("click", function(){

            dropItemCarrinho(button.parentElement.id);
            
            let amount = button.parentElement.querySelector("#quantidade");

            if(!getItemCarrinho(button.parentElement.id)){
                button.parentElement.remove();
            }

            let precoProduto = parseFloat(button.parentElement.querySelector("#preco").textContent.replace(/[^\d,]/g, '').replace(',', '.'));
            let total = document.getElementById("total").textContent.replace(/[^\d,]/g, '').replace(',', '.');
            total = total - precoProduto;
            document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            amount.textContent = parseInt(amount.textContent) - 1;

        })
    });

    // Redireciona o usuário para a forma de pagamento
    document.querySelector("#buy").addEventListener("click", async(event) =>{

        event.preventDefault();

        // window.location.href = "/payment";

        const res = await Payment();

        console.log(res);


    });
})