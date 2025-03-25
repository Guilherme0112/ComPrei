

/***
 * Funçaõ que faz a requisição para gerar a cobrança
 */
async function  Payment(){

    // Pega os produtos que estão no LocalStorage
    var carrinho = getCarrinho()

    // faz a requisição
    const res = await fetch("/payment", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(
            carrinho
        )
    })

    // Converte a resposta em JSON
    const data = await res.json();

    return data;

}

    

