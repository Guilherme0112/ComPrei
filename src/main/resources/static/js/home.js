
function createBox(codigo, foto, nome, preco){

    const divPai = document.createElement("a");
    divPai.href = "/produto/" + codigo;
    divPai.className = "box_product";

    const img = document.createElement("img")
    img.src = foto;
    img.className = "box_product_img";

    const name = document.createElement("p");
    name.className = "box_product_name";
    name.textContent = nome;

    const price = document.createElement("p");
    price.className = "box_product_buy";
    price.textContent = preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });;

    divPai.appendChild(img);
    divPai.appendChild(name);
    divPai.appendChild(price);

    return divPai;
}


let indice = 0;
let load = false; 

async function getData(indice) {
    
    if(load) {
        return;
    }
    
    load = true;
    
    const res = await fetch(`/produto/get/${indice}`, {
        method: "GET"
    });
    
    const data = await res.json();
    
    load = false; 
    indice++;
    return data.produtos;

}

async function loadProducts() {
   
    const res = await getData(indice);

    console.log(res)

    const container = document.createElement("div");
    container.className = "container_destaques";
    container.id = "destaques";
    document.getElementById("main").appendChild(container)

    res.forEach(re => {
        var box = createBox(re.codigo, re.photo, re.name, re.price);
        container.appendChild(box)
        console.log(re)
     });
}

document.addEventListener("DOMContentLoaded", async() =>{
    await loadProducts();
})


window.addEventListener("scroll", async() =>{
    
    const alturaVP = window.innerHeight;
    const alturaP = document.documentElement.scrollHeight;
    const scroll = window.scrollY;
    
    if(alturaVP + scroll >= alturaP){
  
        await loadProducts();

        console.log("chegou ao final da página")
    }
})