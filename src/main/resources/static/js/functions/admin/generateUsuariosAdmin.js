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

    // Cria e adiciona a box 1 ao container
    const box = document.createElement("div");
    box.className = "box";
    box.dataset.id = data[0].id;
    divPai.appendChild(box);

    // Cria e adiciona o nome do usuário ao container
    const p1 = document.createElement("p");
    p1.textContent = data[0].name;
    box.appendChild(p1);

    // Cria e adiciona o email do usuário ao container
    const p2 = document.createElement("p");
    p2.textContent = data[0].email;
    box.appendChild(p2);

    // Cria e adiciona o telefone do usuário ao container
    const p3 = document.createElement("p");
    p3.textContent = data[0].telefone;
    box.appendChild(p3);
    
    // Cria uma div dentro do container
    const box1 = document.createElement("div");
    box1.style.width = "100%";
    box1.style.display = "flex";
    box1.style.justifyContent = "center";
    box1.style.flexWrap = "wrap";

    // Botão para setar admin para o usuário
    const btnAdmin = document.createElement("button");
    btnAdmin.id = "btn_admin";
    btnAdmin.style.margin = "10px 0";

    // Botão para banir o usuário
    const btnDel = document.createElement("button");
    btnDel.className = "btn_del";
    btnDel.id = "btn_del";
    btnDel.style.margin = "10px";
    
    // Se o usuário for um cliente comum
    if(data[0].role == "CLIENTE") {

        // Dá a opção de banir e dar admin
        btnDel.textContent = "Banir";
        btnAdmin.textContent = "Dar Admin";
        box1.appendChild(btnDel);
        box1.appendChild(btnAdmin);
        
    // Se já estiver banido
    } else if (data[0].role == "BANIDO"){
        
        // Dá apenas a opção de desbanir
        btnDel.textContent = "Desbanir"
        box1.appendChild(btnDel);
        
    // Se já for admin
    } else if (data[0].role == "ADMIN"){

        // Dá apenas a opção de remover o admin
        btnAdmin.textContent = "Remover Admin";
        box1.appendChild(btnAdmin);
    }

    // Depois de contruída, adiciona ao container 
    box.appendChild(box1)



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
