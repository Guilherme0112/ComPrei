
// Evento inicia após o carregamento total da página
async function eventButtons() {

    // Seleciona todos os selects da página
    let selects = document.querySelectorAll("#select");

    // Inicializa a variavel onde armazenará o id e o status do pedido
    let updatePedidos = {};

    // Adiciona o evento para exibir o botão de salvar 
    selects.forEach(select => {

        // Função para todos os selects
        select.addEventListener("change", function () {

            // Deleta os dados da array (se tiver)
            updatePedidos = {};

            // Pega o id do pedido
            var id = select.parentElement.parentElement.querySelector("#column_save").querySelector("#save").getAttribute("data-id");

            // Adiciona os dados na array
            updatePedidos.id = id;
            updatePedidos.status = select.value;

            // Exibe o botão de atualizar
            select.parentElement.parentElement.querySelector("#column_save").style.pointerEvents = "auto";
            select.parentElement.parentElement.querySelector("#column_save").style.opacity = "1";
        })
    });

    // Adiciona um evento para todos os botões de salvar da página
    var allSave = document.querySelectorAll("#save");
    allSave.forEach(save => {

        // Envia os dados para o back-end quando clicar em salvar
        save.addEventListener("click", async () => {

            // Faz a requisição para atualizar o status do pedido
            const res = await fetch("/admin/pedidos/edit/status", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatePedidos)
            })

            // Pega a resposta
            const data = await res.json();

            // Verifica se retornou algum erro
            if (!data == 200) {

                return;
            }

            // Oculta o botão de salvar após salvar
            save.parentElement.parentElement.querySelector("#column_save").style.pointerEvents = "none";
            save.parentElement.parentElement.querySelector("#column_save").style.opacity = "0";

        })
    });
}


