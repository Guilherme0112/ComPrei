// Inicializa o elemento Header
const header = document.querySelector("header");

// Inicializa a variavel do scroll
let lastScrollTop = 0;

// Adiciona um evento para quando o usuário rolar para cima, ele exibir o Header
// E quando ele rolar para baixo, ocultar o Header
window.addEventListener("scroll", function() {

    // Posição atual do scroll
    let scrollTop = this.window.scrollY || document.documentElement.scrollTop;

    // Se a posição atual do scroll for maior que a última posição do scroll ele esconde o Header
    // Se for menor, ele exibe o header
    scrollTop > lastScrollTop ? header.style.top = "-100px" : header.style.top = "0px";

    // Atualiza a última posição do scroll
    lastScrollTop = scrollTop;
})