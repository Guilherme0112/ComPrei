document.querySelector("#criar").addEventListener("click", function(){
    window.location.href = "/admin/produtos/criar";
});
document.querySelector("#editar").addEventListener("click", function(){
    var codigo = document.getElementById("codigo").value;
    window.location.href = "/admin/produtos/editar/" + codigo;
});