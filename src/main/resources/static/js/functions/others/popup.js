
/**
 * Abre e fecha a popup com #popup e fecha com #close
 * Para abrir a popup chame a função passando false como paramentro.
 * Para fechar a popup chame a função passando true como paramentro.
 * 
 * @param {*} status Status da popup (true para aberto e false para fechado)
 * @returns //
 */
function Popup(status){

    var popupStats = status;

    if(popupStats == false){
        const popup = document.getElementById("popup");
        popup.style.display = "block";
        return;
    }

    if(popupStats == true){
        document.getElementById("popup").style.display = "none";
        
    }
}