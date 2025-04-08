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

    // Pega a credencial do usuário que está na div pai
    let usuario = document.getElementById("usuario").value;

    // Busca os dados do usuário
    const res = await fetch(`/admin/usuarios/${usuario}`, {
        method: "GET"
    });
    
    // Verifica se ocorreu algum erro
    if(!res.ok){
        
        err.textContent = "Nenhum usuario encontrado com este email ou id: " + usuario;
        document.querySelector(".box_search").appendChild(err);
        
        // Se estiver algum usuário sendo exibido, é deletado a exibição
        document.querySelector(".box") ? document.querySelector(".box").remove() : document.querySelector(".box");
        return;
    }
    

    // Retorna a resposta em JSON
    const data = await res.json();

    // Se existir alguma mensagem erro, o remove
    if (document.querySelector(".erro_msg")) {
        document.querySelector(".erro_msg").remove();
    }

    // Busca o div pai
    const divPai = document.getElementById("resposta");

    // Remove se houver algum usuário que estava sendo exibido anteriormente
    if (document.querySelector(".box")) {
        document.querySelector(".box").remove();
    }

    // Chama a função que cria o container
    let box = createUsuarios(data[0].id, data[0].name, data[0].email, data[0].telefone, data[0].role);
    divPai.appendChild(box)
    box.style.display = "block";

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnDel = document.querySelectorAll("#btn_del");

    // Adiciona um evento para cada um
    allBtnDel.forEach((btn) => {

        btn.addEventListener("click", async () => {
 
            // Pega a credencial do usuário que está na div pai
            let usuario = btn.parentElement.parentElement.dataset.id;

            // Faz a requisição
            const res = await fetch(`/admin/usuarios/banir`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(usuario)
            });

            // Retorna a resposta
            const data = await res.json();

            console.log(data);

            // Se der erro, para o fluxo
            if (!res.ok) {
                return;
            }

            // Se der certo, remove o container onde o usuário estava sendo exibido
            document.querySelector(".box").remove();
        });
    });

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnAdmin = document.querySelectorAll("#btn_admin");

    // Adiciona um evento para cada um
    // Evento para setar admin para algum usuário
    allBtnAdmin.forEach((btn) => {

        btn.addEventListener("click", async () => {

            // Pega a credencial do usuário que está na div pai
            let usuario = btn.parentElement.parentElement.dataset.id;

            // Faz a requisição
            const res = await fetch(`/admin/usuarios/setar`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(usuario)
            });

            // Busca a resposta
            const data = await res.json();

            console.log(data);

            // Se houber erro, exibe-o
            if (!res.ok) {
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
