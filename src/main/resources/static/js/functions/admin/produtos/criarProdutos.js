// Evento de redirect quando o usuário apertar em clicar no na tela de 
// buscar produtos
document.querySelector("#criar").addEventListener("click", function(){
    window.location.href = "/admin/produtos/criar";
});

/** Constrói a div do produto
 * 
 * @param {*} id 
 * @param {*} img 
 * @param {*} nome 
 * @param {*} preco 
 * @param {*} quantidade 
 * @returns 
 */
function createProduct(codigo, img, nome, preco, quantidade) {

    const box = document.createElement("div");
    box.className = "box";
    box.dataset.id = codigo;
    box.id = "box";

    const box1 = document.createElement("div");
    box1.style.width = "100%";
    box.appendChild(box1);

    const imgP = document.createElement("img");
    imgP.src = img;
    imgP.className = "box_image";
    imgP.id = "img";
    box1.appendChild(imgP);

    const nomeLabel = document.createElement("p");
    nomeLabel.style.margin = "6px 0";
    nomeLabel.textContent = "Nome:";
    box1.appendChild(nomeLabel);

    const nomeP = document.createElement("p");
    nomeP.id = "nome";
    nomeP.style.fontSize = "13px";
    nomeP.textContent = nome;
    box1.appendChild(nomeP);

    const precoLabel = document.createElement("p");
    precoLabel.style.margin = "6px 0";
    precoLabel.textContent = "Preço:";
    box1.appendChild(precoLabel);

    const precoP = document.createElement("p");
    precoP.id = "preco";
    precoP.style.fontSize = "13px";
    precoP.textContent = preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });;
    box1.appendChild(precoP);

    const quantidadeLabel = document.createElement("p");
    quantidadeLabel.style.margin = "6px 0";
    quantidadeLabel.textContent = "Quantidade:";
    box1.appendChild(quantidadeLabel);

    const quantidadeP = document.createElement("p");
    quantidadeP.id = "quantidade";
    quantidadeP.style.fontSize = "13px";
    quantidadeP.textContent = quantidade;
    box1.appendChild(quantidadeP);

    const box2 = document.createElement("div");
    box2.style.width = "100%";
    box2.style.display = "flex";
    box2.style.justifyContent = "center";
    box2.style.paddingRight = "20px";
    box1.appendChild(box2);

    const btn = document.createElement("button");
    btn.id = "btn_del";
    btn.className = "btn_del";
    btn.style.width = "50px";
    btn.style.paddingTop = "5px";
    btn.style.margin = "10px 0";
    box2.appendChild(btn);

    const imgBtn = document.createElement("img");
    imgBtn.src = "../icons/trash-solid.png";
    imgBtn.width = "20";
    imgBtn.style.filter = "brightness(0) invert(1)";
    btn.appendChild(imgBtn);

    return box;
}
