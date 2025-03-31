
/**
 * Abre e fecha a popup com #popup e fecha com #close
 * Para abrir a popup chame a função passando false como paramentro.
 * Para fechar a popup chame a função passando true como paramentro.
 * 
 * @param {*} status Status da popup (true para aberto e false para fechado)
 * @returns //
 */
function Popup(status){

    // Se o status for FALSE, abre a popup
    if(status == false){
        const popup = document.getElementById("popup");
        popup.style.display = "block";

        document.getElementById("overlay").style.display = "block";

        return;
    }

    // Se o status for TRUE, fecha a popup
    if(status == true){

        document.getElementById("overlay").style.display = "none";
        document.getElementById("popup").style.display = "none";
        
    }
}