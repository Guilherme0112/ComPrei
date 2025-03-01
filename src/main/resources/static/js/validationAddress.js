// Input onde o usuário coloca o CEP
var inputCep = document.querySelector("#cep");

inputCep.addEventListener("input", async() =>{

    if(inputCep.value.length == 8){

        // Se houver algum hífen, o remove
        var cep = inputCep.value;
        cep = cep.replace(/-/g, "");

        // Requisição
        const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`, {
            method: "GET"
        })
        
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

