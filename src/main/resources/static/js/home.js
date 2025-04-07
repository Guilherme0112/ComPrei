/** Função que cria os produtos para o usuário na tela
 * 
 * @param {*} codigo Código de barras do produto
 * @param {*} foto  Foto do produto
 * @param {*} nome Nome do produto
 * @param {*} preco Preço do produto
 * @returns A div com os atributos preenchidos
 */
function createBox(codigo, foto, nome, preco){

    // Cria o elemento pai
    const divPai = document.createElement("a");
    divPai.href = "/produto/" + codigo;
    divPai.className = "box_product";

    // Adiciona a imagem do produto a div
    const img = document.createElement("img")
    img.src = foto;
    img.className = "box_product_img";
    divPai.appendChild(img);

    // Adiciona o nome do produto
    const name = document.createElement("p");
    name.className = "box_product_name";
    name.textContent = nome;
    divPai.appendChild(name);

    // Adiciona o preço do produto formatado
    const price = document.createElement("p");
    price.className = "box_product_buy";
    price.textContent = preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });;
    divPai.appendChild(price);

    const msgParcelamento = document.createElement("p");
    msgParcelamento.className = "box_product_parcelamento";
    msgParcelamento.textContent = "em até 12x no cartão";
    divPai.appendChild(msgParcelamento);

    // Retorna a div construída
    return divPai;
}

// Incializa as variaveis que serão utilizadas na função
// getData()
let load = false; 
let indice = 0;

/**
 * Obtém os dados dos produtos para a página por indice para retornar os dados conforme o usuário
 * for rolando a página (Retorna 15 registros)
 * 
 * @returns Os dados dos produtos
 */
async function getData() {
    
    // Verifica se está carregando os registros, se sim, finaliza o fluxo
    if(load) {
        return;
    }
    
    // Se não tiver carregando, atualiza o status para buscar os registros
    load = true;
    
    // Faz a requisição que busca os produtos
    const res = await fetch(`/produto/get/${indice}`, {
        method: "GET"
    });
    
    // Pega a resposta em JSON
    const data = await res.json();
    
    // Atualiza o status que finalizou  a requisição
    load = false; 

    // Incrementa o índice para não trazer dados repetidos
    indice++;

    // Retorna os produtos
    return data.produtos;

}

/**
 * Função que pega os dados e os adiciona na página
 * 
 * @returns Null caso não haja mais dados
 */
async function loadProducts() {
   
    // Busca os dados
    const produtos = await getData(indice);

    // Verifica se não existe dados
    if (Array.isArray(produtos) && produtos.length === 0) {
        return;
    }

    // Busca o container onde os produtos serão inseridos
    const container = document.getElementById("produtos");

    // Para cada produto ele cria a div pai e o insere no container
    produtos.forEach(produto => {
        var box = createBox(produto.codigo, produto.photo, produto.name, produto.price);
        container.appendChild(box);
     });
}

// Chama o evento de inserir produtos na página assim que a página é carregada
document.addEventListener("DOMContentLoaded", async() =>{
    await loadProducts();
})

// Adiciona o evento de trazer mais produtos assim que o usuário rolar a página
window.addEventListener("scroll", async() =>{
    
    // Retorna a altura visível da janela do navegador
    const alturaVP = window.innerHeight;
    // Retorna a altura total da página
    const alturaP = document.documentElement.scrollHeight;
    // Retorna a posição do scroll
    const scroll = window.scrollY;
    
    // A soma entre scroll e a alturaVP representa o ponto mais baixo 
    // onde o usuário pode chegar, logo se ela for igual a o tamanho total
    // da tela, significa que o usuário chegou ao final da página
    if(alturaVP + scroll >= alturaP){
  
        // Carrega mais produtos
        await loadProducts();
    }
})

/**
 * Função que remove todos os produtos da página
 */
function removeProduct(){
    let produtos = document.querySelectorAll(".box_product");

    produtos.forEach(produto => {
        produto.remove();
    });
}

document.querySelector("#btnBuscar").addEventListener("click", async() => {

    // Armazena o valor do campo de busca
    let search = document.querySelector("#busca").value;

    // Verifica se o campo de busca está vazio
    if(search == "") {
        return;
    }

    // Remove todos os produtos da tela
    removeProduct();

    // Faz a requisição
    const res = await fetch(`/produto/get?query=${search}`, {
        method: "GET"
    });

    // Converte a resposta em JSON
    let produtos = await res.json();

    produtos.forEach(produto => {
        let box = createBox(produto.codigo, produto.photo, produto.name, produto.price);
        document.getElementById("produtos").appendChild(box);
     });
})