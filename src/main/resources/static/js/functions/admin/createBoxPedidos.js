/** Cria o elemento pai que constroi o container com
 * os dados dao pedido
 *  
 * @returns retorna a div pai com os dados
 */
function createPedido(idProduto, nome, telefone, valor, quando){

    // Pega todos os staus possíveis
    const status = ["PEDIDO", "TRANSITO", "ENTREGUE", "CANCELADO"];

    // Cria o container
    const divpai = document.createElement("tbody");

    // Cria e adiciona o nome ao container
    const nomeCampo = document.createElement("th");
    nomeCampo.textContent = nome;
    divpai.appendChild(nomeCampo);

    // Cria e adiciona o telefone ao container
    const telefoneCampo = document.createElement("th");
    telefoneCampo.textContent = telefone.replace(/(\d{3})(\d{2})(\d{5})(\d{4})/, "$1 ($2) $3-$4");
    divpai.appendChild(telefoneCampo);

    // Cria e adiciona o valor ao container
    const valorCampo = document.createElement("th");
    valorCampo.textContent = valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    divpai.appendChild(valorCampo);

    // Cria o select
    const thSelect = document.createElement("th")
    const select = document.createElement("select");
    select.id = "select";

    // Adiciona todos os status para o select
    status.forEach(stats => {

        const option = document.createElement("option");
        option.textContent = stats;
        option.value = stats;
        select.appendChild(option);
    });

    // Adiciona o select no campo th
    thSelect.appendChild(select);
    divpai.appendChild(thSelect);

    let data = new Date(quando);
    let format = new Intl.DateTimeFormat('pt-BR', { 
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false    
     });

    // Cria e adiciona quando foi feito a compra ao container
    const quandoCampo = document.createElement("th");
    quandoCampo.textContent = format.format(data);
    divpai.appendChild(quandoCampo);

    // Cria o Th
    const buttonTh = document.createElement("th");
    buttonTh.id = "column_save";
    buttonTh.style.opacity = "0";
    buttonTh.style.pointerEvents = "none";
    buttonTh.style.transition = "opacity .3s ease-in-out";

    // Cria o botão dentro do TH
    const buttonSave = document.createElement("button");
    buttonSave.id = "save";
    buttonSave.dataset.id = idProduto;
    buttonSave.style.backgroundColor = "#5bf15b";
    buttonSave.style.width = "50px";
    buttonSave.style.paddingTop = "8px";
    buttonTh.appendChild(buttonSave);

    // Cria o icone dentro do botão
    const btnIcon = document.createElement("img");
    btnIcon.src = "../icons/check-solid.png";
    btnIcon.width = "20";
    btnIcon.style.filter = "brightness(0) invert(1)";
    buttonSave.appendChild(btnIcon);

    divpai.appendChild(buttonTh);

    return divpai;
}