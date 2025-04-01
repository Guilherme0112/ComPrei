
const statusSelect = document.getElementById("statusSelect");

statusSelect.addEventListener("change", async() =>{

    let statusPedido = statusSelect.value;

    const res = await fetch(`/admin/pedidos/${statusPedido}`, {
        method: "GET"
    });

    const dados = await res.json();

    // Deleta os pedidos que estão sendo exibidos
    let pedidos = document.querySelectorAll("tbody");
    pedidos.forEach(pedido => {
        pedido.remove();
    })

    // Se houver rro
    if(dados[0] == "erro") {

        document.querySelector("#resposta").textContent = dados[1];
        return;
    }

    // Exibe os pedidos se não houver erros
    dados.forEach(dado => {
        
        let div = createPedido(dado.id, dado.nome, dado.telefone, dado.valor, dado.quando);
        document.querySelector("table").appendChild(div);
    });

    eventButtonsPedidos();
})

