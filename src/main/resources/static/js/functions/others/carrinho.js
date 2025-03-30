/** Adiciona um código de barras ao carrinhp
 * 
 * @param {*} codigo Código de barras do produto
 */
function setItemCarrinho(codigo){

    // Busca o carrinho
    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

    // Busca o produto no carrinho
    const produtoExiste = carrinho.find(item => item.codigo === codigo);

    // Se o produto existir ele incrementa a quantidade, se não, adiciona no localStorage
    produtoExiste ? produtoExiste.quantidade++ : carrinho.push({codigo: codigo, quantidade: 1});
    
    // Atualiza o carrinho no localStorage
    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}

/** Busca algum produto com algum determinado código no localStorage
 * 
 * @param {*} codigo Código de barras do produto
 * @returns Retorna NULL caso não exista produto e retorna o o código e 
 * a quantidade (De produtos que o usuário colocou no carrinho) do produto caso exista
 */
function getItemCarrinho(codigo){

    // Busca os dados do carrinho
    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

    // Busca o produto que tem o codigo de barras do parametro
    const produto = carrinho.find(item => item.codigo === codigo);

    // Se o produto existir, retorna uma array com o codigo e quantidade do produto se não, retorna null
    return produto ?  { codigo: produto.codigo, quantidade: produto.quantidade } : null;

}

/** Busca o carrinho completo
 * 
 * @returns Retorna em JSON o carrinho com todos os dados ou uma array vazia para caso não tenha dados
 */
function getCarrinho(){
    return JSON.parse(localStorage.getItem("carrinho")) || [];
}

/** Reduz a quantidade de um produto no LocalStorage
 * 
 * @param {*} codigo Código de barras do produto
 */
function dropItemCarrinho(codigo){

    // Trás o carrinho com os dados e busca o produto no carrinho
    let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];
    const produto = carrinho.find(item => item.codigo === codigo);

    // Se o produto existir e a quantidade for maior que 1, faz o decremento
    if(produto){

        // Se a quantidade de produtos for maior que 1, ele decrementa do localStorage
        // Se for menor que 1, ele deleta o registro do localStorage
        produto.quantidade > 1 ? produto.quantidade-- : carrinho = carrinho.filter(item => item.codigo !== codigo);
    }

    // Atualiza o carrinho com o valor decrementado ou totalmente removido
    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}