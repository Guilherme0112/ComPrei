document.querySelector("#criar").addEventListener("click", function(){
    window.location.href = "/admin/produtos/criar";
});
document.querySelector("#editar").addEventListener("click", function(){
    var id = document.getElementById("id").value;
    window.location.href = "/admin/produtos/editar/" + id;
});