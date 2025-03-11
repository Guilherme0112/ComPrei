const btn = document.getElementById("btnSubmit");
var allBtnDel = null;
var allBtnAdmin = null;

const err = document.createElement("p");
err.style.width = "auto";
err.style.textAlign = "center";
err.className = "erro_msg";

btn.addEventListener("click", async () => {
    var id = document.getElementById("id_produto").value;

    const res = await fetch(`/admin/usuarios/${id}`, {
        method: "GET"
    });

    const data = await res.json();

    if (document.querySelector(".erro_msg")) {
        document.querySelector(".erro_msg").remove();
    }

    const divPai = document.getElementById("resposta");

    if (data.length == 0) {
        
        err.textContent = "Nenhum usuario encontrado com este id: " + id;
        document.querySelector(".box_search").appendChild(err);

        document.querySelector(".box") ? document.querySelector(".box").remove() : document.querySelector(".box");

        return;
    }

    if (document.querySelector(".box")) {
        document.querySelector(".box").remove();
    }

    const box = document.createElement("div");
    box.className = "box";
    box.dataset.id = data[0].id;

    const p1 = document.createElement("p");
    p1.textContent = data[0].name;

    const p2 = document.createElement("p");
    p2.textContent = data[0].email;

    const p3 = document.createElement("p");
    p3.textContent = data[0].telefone;
    
    const box1 = document.createElement("div");
    box1.style.width = "100%";
    box1.style.display = "flex";
    box1.style.justifyContent = "center";
    box1.style.flexWrap = "wrap";

    const btnAdmin = document.createElement("button");
    btnAdmin.id = "btn_admin";
    btnAdmin.style.margin = "10px 0";

    const btnDel = document.createElement("button");
    btnDel.className = "btn_del";
    btnDel.id = "btn_del";
    btnDel.style.margin = "10px";
    
    if(data[0].role == "CLIENTE") {
        btnDel.textContent = "Banir";
        btnAdmin.textContent = "Dar Admin";
        box1.appendChild(btnDel);
        box1.appendChild(btnAdmin);
        
    } else if (data[0].role == "BANIDO"){
        
        btnDel.textContent = "Desbanir"
        box1.appendChild(btnDel);
        
    } else if (data[0].role == "ADMIN"){

        btnAdmin.textContent = "Remover Admin";
        box1.appendChild(btnAdmin);
    }

    
    divPai.appendChild(box);
    box.appendChild(p1);
    box.appendChild(p2);
    box.appendChild(p3);
    box.appendChild(box1)



    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnDel = document.querySelectorAll("#btn_del");

    allBtnDel.forEach((btn) => {
        btn.addEventListener("click", async () => {

            var id = document.getElementById("id_produto").value;

            const res = await fetch(`/admin/usuarios/banir/${id}`, {
                method: "GET"
            });

            const data = await res.json();

            console.log(data);

            if (!data) {
                return;
            }

            document.querySelector(".box").remove();
        });
    });

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnAdmin = document.querySelectorAll("#btn_admin");

    allBtnAdmin.forEach((btn) => {
        btn.addEventListener("click", async () => {

            var id = document.getElementById("id_produto").value;

            const res = await fetch(`/admin/usuarios/admin/${id}`, {
                method: "GET"
            });

            const data = await res.json();

            console.log(data);

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
