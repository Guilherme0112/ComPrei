
// Verifica se as senhas coencidem
document.getElementById("form").addEventListener("submit", function(event){
    var pass = document.getElementById("password").value;
    var r_pass = document.getElementById("r_password").value;
    
    if(pass != r_pass){
        event.preventDefault();
        document.getElementById("erro_password").textContent = "As senhas não coendicem";
        return false;
    }
});