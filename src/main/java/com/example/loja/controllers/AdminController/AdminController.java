package com.example.loja.controllers.AdminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.models.Produto;

@Controller
public class AdminController {
    
    @GetMapping("/admin")
    public ModelAndView Admin(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("views/admin/admin");
        return mv;
    }

    @GetMapping("/admin/produtos")
    public ModelAndView AdminProdutosGET(){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/admin/produtos");
        return mv;
    }
    
    @GetMapping("/admin/produtos/criar")
    public ModelAndView AdminProdutosCriarGET(){

        ModelAndView mv = new ModelAndView();
        mv.addObject("produto", new Produto());
        mv.setViewName("views/admin/criarProdutos");
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
