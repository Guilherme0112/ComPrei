document.querySelector("#del").addEventListener("click", function(){
    Popup(false);
})

document.getElementById("submit_del").addEventListener("click", async() =>{

    var password = document.getElementById("password").value;

    const res = await fetch("/profile/delete", {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json" // Define o tipo do conteúdo como JSON
        },
        body: JSON.stringify({
            senha: password
        })
    });

    const data = await res.json();
 
    console.log(data)

    if(data.erro){
        document.getElementById("erro_password").textContent = data.erro;
        return;
    }

    console.log(res);
    window.location.href = "/auth/login";
})

document.querySelector("#close").addEventListener("click", function(){

    Popup(true);
})

