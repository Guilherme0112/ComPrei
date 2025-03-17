
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
    
    console.log(data);
})