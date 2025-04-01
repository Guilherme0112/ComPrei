
const statusSelect = document.getElementById("statusSelect");

statusSelect.addEventListener("change", async() =>{

    let statusReembolso = statusSelect.value;

    const res = await fetch(`/admin/reembolso/${statusReembolso}`, {
        method: "GET"
    });

    const dados = await res.json();

    // Deleta os pedidos que estão sendo exibidos
    let reembolsos = document.querySelectorAll("tbody");
    reembolsos.forEach(rembolso => {
        rembolso.remove();
    })

    // Se houver erro
    if(dados[0] == "erro") {

        document.querySelector("#resposta").textContent = dados[1];
        return;
    }

    document.querySelector("#resposta").textContent = "";
    
    // Exibe os pedidos se não houver erros
    dados.forEach(dado => {
        
        let div = createReembolso(dado.id, dado.idPedido, dado.nome, dado.telefone, dado.quando);
        document.querySelector("table").appendChild(div);
    });

    eventButtonsReembolso();
})

