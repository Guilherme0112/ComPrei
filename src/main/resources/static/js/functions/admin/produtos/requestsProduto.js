document.getElementById("btnSubmit").addEventListener("click", async () => {

    let id = document.getElementById("id_produto").value;

    const res = await fetch(`/admin/produtos/${id}`, {
        method: "GET"
    });

    // Deleta a box do produto caso já exista
    document.getElementById("box") ? document.getElementById("box").remove() : document.getElementById("box");

    // Verifica se ocorreu algum erro
    if(!res.ok){
        console.log(res)
        document.getElementById("erro").textContent = "Nenhum produto encontrado com este id: " + id;
        return;
    }
    
    // Se não houver erro, limpa o erro
    document.getElementById("erro").textContent = "";

    const data = await res.json();

    
    document.getElementById("resposta").appendChild(
        createProduct(data[0].id, data[0].photo, data[0].name, data[0].price, data[1])
    );
    // Seleciona os botões de deletar e adiciona os eventos neles
    let allBtnDel = document.querySelectorAll("#btn_del");

    allBtnDel.forEach((btn) => {
        btn.addEventListener("click", async () => {

            let codigo = document.getElementById("id_produto").value;

            const res = await fetch(`/admin/deletar/produtos`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(codigo)
            });

            const data = await res.json();

            if(!res.ok){

            }

            console.log(data);

            box.style.display = "none";
        });
    });
});
