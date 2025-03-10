const btn = document.getElementById("btnSubmit");
var allBtnDel = null;

btn.addEventListener("click", async () => {
    var id = document.getElementById("id_produto").value;

    const res = await fetch(`/admin/produtos/${id}`, {
        method: "GET"
    });

    const data = await res.json();

    if (document.querySelector(".erro_msg")) {
        document.querySelector(".erro_msg").remove();
    }

    const divPai = document.getElementById("resposta");

    if (!data[0]) {
        const p1 = document.createElement("p");
        p1.className = "erro_msg";
        p1.textContent = "Nenhum produto encontrado com este código: " + id;
        p1.style.width = "auto";
        p1.style.textAlign = "center";
        document.querySelector(".box_search").appendChild(p1);

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
    p2.textContent = data[0].description;

    const p3 = document.createElement("p");
    p3.textContent = data[0].price;

    const btnDel = document.createElement("button");
    btnDel.className = "btn_del";
    btnDel.id = "btn_del";
    btnDel.textContent = "x";

    divPai.appendChild(box);

    box.appendChild(p1);
    box.appendChild(p2);
    box.appendChild(p3);
    box.appendChild(btnDel);

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnDel = document.querySelectorAll("#btn_del");

    allBtnDel.forEach((btn) => {
        btn.addEventListener("click", async () => {
            console.log("Clicou no botão de deletar!");

            var id = document.getElementById("id_produto").value;

            const res = await fetch(`/admin/deletar/produtos`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    codigo: id
                })
            });

            const data = await res.json();

            console.log(data);

            if (!data[0] && !data) {
                return;
            }

            document.querySelector(".box").remove();
        });
    });
});
