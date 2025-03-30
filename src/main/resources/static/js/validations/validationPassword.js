// Busca os elementos das inputs da senha
var r_password = document.getElementById("r_password");
var password = document.getElementById("password");

// Adiciona um evento que é disparado quando o usuário faz o submit do formulário
document.getElementById("form").addEventListener("submit", function(event){

    // Verifica se as senhas coencidem
    if(r_password.value == password.value){
        document.getElementById("erro_password").textContent = "";
        return;
    }
    
    // Se não coencidir, não submita o formulário e exibe a mensagem de erro
    event.preventDefault();
    document.getElementById("erro_password").textContent = "As senhas não coencidem";
})

