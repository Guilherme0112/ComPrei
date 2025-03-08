// Status da popup (False para fechada e TRUE para aberta)
var popupStats = false;

document.getElementById("del").addEventListener("click", async () => {

    if(popupStats == false){

        const popup = document.getElementById("popup");
        popup.style.display = "block";
        popupStats = true;
        return;
    } 

});

document.getElementById("submit_del").addEventListener("click", async() =>{

    if(popupStats != true){
        return;
    }

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

    if(popupStats == true){

        document.getElementById("popup").style.display = "none";
        popupStats = false;
    }
})

