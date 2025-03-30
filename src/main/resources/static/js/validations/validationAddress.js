// Input onde o usuário coloca o CEP
var inputCep = document.querySelector("#cep");

// Evento que atualiza os campos automáticamente quando o usuário
// preenche o CEP
inputCep.addEventListener("input", async() =>{

    // Caso o CEP esteja com a largura certa
    if(inputCep.value.length == 8){

        // Se houver algum hífen, o remove
        var cep = inputCep.value;
        cep = cep.replace(/-/g, "");

        // Busca os dados referente ao CEP
        const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`, {
            method: "GET"
        })
        
        // Retorna os dados em JSON
        var data = await res.json();

        // Atualiza os valores das inputs conforme o CEP informado
        // Coloca o valor que a API informou sobre o CEP
        if(data.cep != null){
            document.querySelector("#rua").value = data.logradouro;
            document.querySelector("#bairro").value = data.bairro;
            document.querySelector("#cidade").value = data.localidade;
        }

    }
})

