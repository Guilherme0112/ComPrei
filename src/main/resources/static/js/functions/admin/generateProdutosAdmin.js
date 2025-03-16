const btn = document.getElementById("btnSubmit");
var allBtnDel = null;
var box = document.getElementById("box");

btn.addEventListener("click", async () => {
    var id = document.getElementById("id_produto").value;

    const res = await fetch(`/admin/produtos/${id}`, {
        method: "GET"
    });

    var data = await res.json();

    if (document.querySelector(".erro_msg")) {
        document.querySelector(".erro_msg").remove();
    }

    // Verifica se há dados para exibir a mensagem de erro caso não tenha
    if (!data[0]) {
        const p1 = document.createElement("p");
        p1.className = "erro_msg";
        p1.textContent = "Nenhum produto encontrado com este código: " + id;
        p1.style.width = "auto";
        p1.style.textAlign = "center";
        document.querySelector(".box_search").appendChild(p1);

        box.style.display = "none";

    }  else {

        // Se tiver, exibe os dados
        box.style.display = "flex";
        document.getElementById("codigo").value = data[0].codigo;
        document.getElementById("image").src = data[0].photo;
        document.getElementById("nome").textContent = data[0].name;
        document.getElementById("preco").textContent = data[0].price.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
        document.getElementById("quantidade").textContent = 12;
    }

    // Define data como null para evitar de ter dados (quando se tem dados ele não oculta a box)
    data = null;

    // Seleciona os botões de deletar e adiciona os eventos neles
    allBtnDel = document.querySelectorAll("#btn_del");

    allBtnDel.forEach((btn) => {
        btn.addEventListener("click", async () => {

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

            box.style.display = "none";
        });
    });
});
