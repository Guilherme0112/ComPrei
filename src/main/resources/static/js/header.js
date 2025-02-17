const header = document.querySelector("header");

let lastScrollTop = 0;

window.addEventListener("scroll", function() {
    let scrollTop = this.window.scrollY || document.documentElement.scrollTop;

    scrollTop > lastScrollTop ? header.style.top = "-100px" : header.style.top = "0px";

    lastScrollTop = scrollTop;
})