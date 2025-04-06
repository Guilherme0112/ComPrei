/** Cria o elemento que é exibido para o usuário na tela de carrinho
 * 
 * @param {*} img Imagem do produto
 * @param {*} nome  Nome do produto
 * @param {*} quantidadeP Quantidade do produtos que o usuário colocou no carrinho
 * @param {*} preco Preço do produto
 * @param {*} codigo Código de barras do produto
 * @returns Retorna o elemento pai com a div construída
 */
function createBox(img, nome, quantidadeP, preco, codigo){

    // Cria o container onde terá as informações
    const divPai = document.createElement("div");
    divPai.id = codigo;
    divPai.className = "box_carrinho";

    // Cria e adiciona a imagem ao container
    const imgP = document.createElement("img");
    imgP.className = "box_img_carrinho";
    imgP.src = img;
    divPai.appendChild(imgP);

    // Cria e adiciona o nome do produto ao container
    const nomeProduto = document.createElement("p");
    nomeProduto.className = "box_nome_produto";
    nomeProduto.textContent = nome;
    divPai.appendChild(nomeProduto);
    
    // Cria e adiciona o preço do produto ao container
    const price = document.createElement("p");
    price.className = "box_preco_produto";
    price.id = "preco";
    price.textContent = preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    divPai.appendChild(price);

    // Cria e adiciona a quantidade ao container
    const quantidade = document.createElement("p");
    quantidade.className = "box_quantidade_produto";
    quantidade.id = "quantidade";
    quantidade.textContent = quantidadeP;
    divPai.appendChild(quantidade);

    const divBtn = document.createElement("div");
    divBtn.className = "box_btn_carrinho";
    divPai.appendChild(divBtn);

    // Cria e adiciona o botão de adicionar mais produtos ao carrinho ao container
    const buttonMais = document.createElement("button")
    buttonMais.id = "mais";
    buttonMais.style.width = "40px";
    buttonMais.textContent = "+"
    divBtn.appendChild(buttonMais);

    // Cria e adiciona o botão de remover produtos do carrinho ao container
    const buttonMenos = document.createElement("button")
    buttonMenos.id = "menos"
    buttonMenos.style.width = "40px";
    buttonMenos.textContent = "-"
    divBtn.appendChild(buttonMenos);

    // Retorna a div pai construída
    return divPai;
}

//  Assim que a página é carregada ele verifica se existe um botão 
//  com o #login, se existir significa que o usuário não está logado, então é 
//  adicionado um evento que ao clicar nele o usuário é redirecionado a tela de login
document.addEventListener("DOMContentLoaded", async() => {

    // Verifica se o botão existe
    if(document.getElementById("login")){

        // Adiciona o evento de redirecionamento quando o botão é clicado
        document.getElementById("login").addEventListener("click", function(){
            window.location.href = "/auth/login";
        });
    }

    // Inicializa a variavel de produtos que serão enviados para o backend
    var produtos = [];

    // Pega os produtos que estão no carrinho
    const produtosCarrinho = getCarrinho();
    
    // Adiciona todos os códigos de barras para a array
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
    
    // Pega a resposta em JSON
    const data = await res.json();

    // Busca o elemento onde serão exibidos os produtos
    const carrinho = document.getElementById("carrinho");

    // Inicializa a var box (Ela armazenará o elemento pai que é o produto do carrinho)
    let box = null;
    // Vai somando a quantidade os produtos para ter o valor total da venda
    let total = 0;

    // Cria os produtos do carrinho 
    data.forEach(dado => {

        // Soma o preço do produto e multiplica pela quantidade
        total = total + (dado.price * getItemCarrinho(dado.codigo).quantidade);

        // Cria o elemento para o carrinho
        box = createBox(dado.photo, dado.name, getItemCarrinho(dado.codigo).quantidade, dado.price, dado.codigo);

        // Adiciona ao container do carrinho
        carrinho.appendChild(box);
    });

    // Coloca o valor total do carrinho
    document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

    // Pega todos os botões de aumentar e diminuir
    let menos = document.querySelectorAll("#menos");
    let mais = document.querySelectorAll("#mais");

    // Evento que adiciona produto do carrinho
    // Ele sempre pega o codigo dos produtos pegando o parente do elemento
    mais.forEach(button => {

        button.addEventListener("click", function(){

            // Adiciona mais um produto ao carrinho (LocalStorage)
            setItemCarrinho(button.parentElement.parentElement.id);   

            // Pega a quantidade que está no front
            let amount = button.parentElement.parentElement.querySelector("#quantidade");
            // Busca o valor do produto formata retirando tudo o que não for número
            let precoProduto = parseFloat(button.parentElement.parentElement.querySelector("#preco").textContent.replace(/[^\d,]/g, '').replace(',', '.'));
            // Busca o valor total formata retirando tudo o que não for número
            let total = document.getElementById("total").textContent.replace(/[^\d,]/g, '').replace(',', '.');
            // Converte o valor para Float e soma com o valor do produto
            total = parseFloat(total) + precoProduto;
            // Atualiza o valor para o usuário
            document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            // Atualiza a quantidade de produtos para o usuário
            amount.textContent = parseInt(amount.textContent) + 1;
        })
    });

    // Evento que tira produto do carrinho
    // Ele sempre pega o codigo dos produtos pegando o parente do elemento
    menos.forEach(button => {

        button.addEventListener("click", function(){

            // Remove o produto do localStorage
            dropItemCarrinho(button.parentElement.parentElement.id);
            
            // Pega a div que tem a quantidade de produtos que o usuário colocou no carrinho
            let amount = button.parentElement.parentElement.querySelector("#quantidade");

            // Se no localsotage não existir algum produto com um determinado código, o deleta
            if(!getItemCarrinho(button.parentElement.parentElement.id)){
                button.parentElement.parentElement.remove();
            }

            // Pega o preço do produto que está sendo exibida para o usuário
            let precoProduto = parseFloat(button.parentElement.parentElement.querySelector("#preco").textContent.replace(/[^\d,]/g, '').replace(',', '.'));
            // Busca o valor total que está sendo exibido para o usuário
            let total = document.getElementById("total").textContent.replace(/[^\d,]/g, '').replace(',', '.');
            // Subtraí pelo valor do produto
            total = total - precoProduto;
            // Atualiza o valor total para o usuário
            document.getElementById("total").textContent = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            // Atualiza a quantidade de produtos para o usuário
            amount.textContent = parseInt(amount.textContent) - 1;

        })
    });

    // Redireciona o usuário para a forma de pagamento
    document.querySelector("#buy").addEventListener("click", async(event) =>{

        event.preventDefault();

        const res = await Payment();

        console.log(res);

        if(res["0"] != "OK"){
            document.getElementById("address_msg").style.display = "block";
            return;
        }

        document.getElementById("address_msg").style.display = "none";

        window.location.href = res["1"];

    });
})