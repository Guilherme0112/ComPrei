
// Quando clicar no botão ele chama a função que abre e fecha uma popup
document.querySelector("#del").addEventListener("click", function(){
    Popup(false);
})

// Evento de fechar a popup quando o usuário clicar no 'x'
document.querySelector("#close").addEventListener("click", function(){
    Popup(true);
})

// Evento que dispara e requisição de pedido de deleção de conta do usuário
document.getElementById("submit_del").addEventListener("click", async() =>{

    // Pega a senha que o usuário colocou na input
    var password = document.getElementById("password").value;

    // Faz a requisição
    const res = await fetch("/profile/delete", {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json" // Define o tipo do conteúdo como JSON
        },
        body: JSON.stringify({
            senha: password
        })
    });

    // Pega a resposta
    const data = await res.json();

    // Se retornar erro o exibe e finaliza o fluxo
    if(data.erro){
        document.getElementById("erro_password").textContent = data.erro;
        return;
    }

    // Redireciona o usuário para a página de login se o usuário deletou a conta
    window.location.href = "/auth/login";
})


