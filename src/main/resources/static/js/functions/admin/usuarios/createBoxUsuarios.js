
function createUsuarios(id, name, email, telefone, role) {
    // Cria e adiciona a box 1 ao container
    const box = document.createElement("div");
    box.className = "box";
    box.dataset.id = id;
    box.style.width = "450px";

    // Cria e adiciona o nome do usuário ao container
    const p1 = document.createElement("p");
    p1.textContent = name;
    box.appendChild(p1);

    // Cria e adiciona o email do usuário ao container
    const p2 = document.createElement("p");
    p2.textContent = email;
    box.appendChild(p2);

    // Cria e adiciona o telefone do usuário ao container
    const p3 = document.createElement("p");
    p3.textContent = telefone;
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
    if (role == "ROLE_CLIENTE") {

        // Dá a opção de banir e dar admin
        btnDel.textContent = "Banir";
        btnAdmin.textContent = "Dar Admin";
        box1.appendChild(btnDel);
        box1.appendChild(btnAdmin);

        // Se já estiver banido
    } else if (role == "ROLE_BANIDO") {

        // Dá apenas a opção de desbanir
        btnDel.textContent = "Desbanir"
        box1.appendChild(btnDel);

        // Se já for admin
    } else if (role == "ROLE_ADMIN") {

        // Dá apenas a opção de remover o admin
        btnAdmin.textContent = "Remover Admin";
        box1.appendChild(btnAdmin);
    }

    // Depois de contruída, adiciona ao container 
    box.appendChild(box1)

    return box;
}