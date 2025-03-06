
var r_password = document.getElementById("r_password");
var password = document.getElementById("password");


document.getElementById("form").addEventListener("submit", function(event){

    if(r_password.value != password.value){

        event.preventDefault();
        document.getElementById("erro_password").textContent = "As senhas não coencidem";
        
    } else {
        
        document.getElementById("erro_password").textContent = "";
    }
})

