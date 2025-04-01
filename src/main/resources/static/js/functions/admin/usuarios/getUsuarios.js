// Inicializa as varaiveis
const btn = document.getElementById("btnSubmit");
var allBtnDel = null;
var allBtnAdmin = null;

// Cria o elemento odne os erros serão exibidos
const err = document.createElement("p");
err.style.width = "auto";
err.style.textAlign = "center";
err.className = "erro_msg";

// Evento ao botão que vai buscar o usuário
btn.addEventListener("click", async () => {

    // Id do usuário que está na input
    var id = document.getElementById("id_produto").value;

    // Busca os dados do usuário
    const res = await fetch(`/admin/usuarios/${id}`, {
        method: "GET"
    });

    // Retorna a resposta em JSON
    const data = await res.json();

    // Se existir algum erro, o remove
    if (document.querySelector(".erro_msg")) {
        document.querySelector(".erro_msg").remove();
    }

    // Busca o div pai
    const divPai = document.getElementById("resposta");

    // Se não houver dados, exibe a mensagem de erro
    if (data.length == 0) {
        
        err.textContent = "Nenhum usuario encontrado com este id: " + id;
        document.querySelector(".box_search").appendChild(err);
        
        // Se estiver algum usuário sendo exibido, é deletado a exibição
        document.querySelector(".box") ? document.querySelector(".box").remove() : document.querySelector(".box");

        return;
    }

    // Remove se houver algum usuário que estava sendo exibido anteriormente
    if (document.querySelector(".box")) {
        document.querySelector(".box").remove();
    }

    // Chama a função que cria o container
    let box = createUsuarios(data[0].id, data[0].name, data[0].email, data[0].telefone, data[0].role);
    divPai.appendChild(box)
    divPai.style.display = "block";

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnDel = document.querySelectorAll("#btn_del");

    // Adiciona um evento para cada um
    allBtnDel.forEach((btn) => {

        btn.addEventListener("click", async () => {

            // Busca o id que está na input
            var id = document.getElementById("id_produto").value;

            // Faz a requisição
            const res = await fetch(`/admin/usuarios/banir/${id}`, {
                method: "GET"
            });

            // Retorna a resposta
            const data = await res.json();

            // Se der erro, para o fluxo
            if (!data) {
                return;
            }

            // Se der certo, remove o container onde o usuário estava sendo exibido
            document.querySelector(".box").remove();
        });
    });

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnAdmin = document.querySelectorAll("#btn_admin");

    // Adiciona um evento para cada um
    allBtnAdmin.forEach((btn) => {

        btn.addEventListener("click", async () => {

            // Id que está na input
            var id = document.getElementById("id_produto").value;

            // Faz a requisição
            const res = await fetch(`/admin/usuarios/admin/${id}`, {
                method: "GET"
            });

            // Busca a resposta
            const data = await res.json();

            // Se houber erro, exibe-o
            if (data["erro"]) {
                err.textContent = data["erro"];
                document.querySelector(".box_search").appendChild(err);
                return;
            }

            err.textContent = data[1];
            document.querySelector(".box_search").appendChild(err);
            document.querySelector(".box").remove();
        });
    });
});
