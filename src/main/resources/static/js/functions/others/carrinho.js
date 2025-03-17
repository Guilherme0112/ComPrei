function setItemCarrinho(codigo){

    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

    const produtoExiste = carrinho.find(item => item.codigo === codigo);

    if(produtoExiste){
        produtoExiste.quantidade++;
    } else {

        carrinho.push({codigo: codigo, quantidade: 1});
    }

    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}

function getItemCarrinho(codigo){

    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

    const produto = carrinho.find(item => item.codigo === codigo);

    if(produto){
        return { codigo: produto.codigo, quantidade: produto.quantidade };
    } else {
        return null;
    }
}

function getCarrinho(){
    return JSON.parse(localStorage.getItem("carrinho")) || [];
}

function dropItemCarrinho(codigo){

    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

    carrinho = carrinho.filter(item => item.codigo !== codigo);

    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}