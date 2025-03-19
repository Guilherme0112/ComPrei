package com.example.loja.controllers.ComprasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.service.ProdutoService;

@Controller
public class ProdutoController {
    
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }


    @GetMapping("/produto/{codigo}")
    public ModelAndView Produto(@PathVariable("codigo") String codigo) throws Exception, ProdutoException{

        ModelAndView mv = new ModelAndView();

        try {
            

            Produto produto = produtoService.getProduct(codigo);

            if(produto == null){
                throw new  ProdutoException("Produto não existe");
            }

            mv.addObject(produto);
            mv.setViewName("views/produto/produto");
            
        } catch (ProdutoException e){

            System.out.println(e.getMessage());
            mv.setViewName("redirect:/");
    
        } catch (Exception e) {

    
            System.out.println(e.getMessage());
            mv.addObject("erro", "Ocorreu um erro. Tente novamente mais tarde");
            mv.setViewName("redirect:/");
        }
        
        return mv;
    }

    @GetMapping("/checkout/{pagamento}")
    public ModelAndView checkout(@PathVariable("pagamento") String pagamento){

        ModelAndView mv = new ModelAndView();

        if(pagamento.equals("pix")){

            mv.setViewName("views/produto/compra/pix");
            
        } else if(pagamento.equals("boleto")){

            mv.setViewName("views/produto/compra/boleto");
            
        } else if(pagamento.equals("cartao")){
            
            mv.setViewName("views/produto/compra/cartao");
        } else {
        
            mv.setViewName("redirect:/carrinho");
        }

        return mv;
    }
}
