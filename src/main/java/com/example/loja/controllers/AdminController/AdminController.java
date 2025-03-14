package com.example.loja.controllers.AdminController;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.models.Produto;
import com.example.loja.repositories.ProdutoRepository;

@Controller
public class AdminController {
    
    private final ProdutoRepository produtoRepository;

    public AdminController(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/admin")
    public ModelAndView Admin(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/admin/admin");
        return mv;
    }

    @GetMapping("/admin/produtos")
    public ModelAndView AdminProdutosGET(){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/admin/produtos/produtos");
        return mv;
    }
    
    @GetMapping("/admin/produtos/criar")
    public ModelAndView AdminProdutosCriarGET(){

        ModelAndView mv = new ModelAndView();
        mv.addObject("produto", new Produto());
        mv.setViewName("views/admin/produtos/criarProdutos");
        return mv;
    }

    @GetMapping("/admin/produtos/editar/{codigo}")
    public ModelAndView AdminProdutosEditarGET(@PathVariable("codigo") String codigo) throws Exception{

        ModelAndView mv = new ModelAndView();

        try {
            
            List<Produto> produto = produtoRepository.findByCodigoDeBarras(codigo);

            if(produto.isEmpty()){
                mv.setViewName("redirect:/admin/produtos");
                return mv;
            }

            mv.addObject("produto", produto.get(0));
            mv.setViewName("views/admin/produtos/editarProdutos");

        } catch (Exception e) {

            System.out.println(e.getMessage());
            mv.setViewName("redirect:/admin/produtos/editarProdutos");
        }

        return mv;

    }

    @GetMapping("/admin/pedidos")
    public ModelAndView AdminPedidosGET(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/admin/pedidos");
        return mv;
    }
    @GetMapping("/admin/usuarios")
    public ModelAndView AdminUsuariosGET(){

        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/admin/usuarios");
        return mv;
    }

}
