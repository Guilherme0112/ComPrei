package com.example.loja.controllers.AdminController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.loja.models.Produto;
import com.example.loja.models.Usuario;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.service.AdminService.APIAdminProdutosService;

@RestController
public class APIAdminController {

    private final APIAdminProdutosService apiAdminProdutosService;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public APIAdminController(APIAdminProdutosService apiAdminProdutosService,
                              ProdutoRepository produtoRepository,
                              UsuarioRepository usuarioRepository){
        this.apiAdminProdutosService = apiAdminProdutosService;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }
 
    @GetMapping("/admin/produtos/{codigo}")
    public List<?> AdminProdutosGET(@PathVariable("codigo") String codigo){
        
        try {
        
            if(!codigo.matches("\\d+")){
                return List.of("erro", "O código  é inválido");
            }

            return produtoRepository.findByCodigoDeBarras(codigo);

        } catch (Exception e) {
            
            return List.of("erro", e.getMessage());
        }

    }


    @GetMapping("/admin/pedidos/{id}")
    public List<?> AdminPedidosGET(@PathVariable("id") String id){
        
        try {
        
            if(!id.matches("\\d+")){
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            Optional<Produto> produto = produtoRepository.findById(idLong);

            return produto.map(Collections::singletonList)
                                         .orElseGet(Collections::emptyList);

        } catch (Exception e) {
            
            return List.of("erro", e.getMessage());
        }
    }


    @GetMapping("/admin/usuarios/{id}")
    public List<?> AdminUsuariosGET(@PathVariable("id") String id){
        try {
        
            if(!id.matches("\\d+")){
                return List.of("erro", "O código  é inválido");
            }

            Long idLong = Long.parseLong(id);

            Optional<Usuario> usuario = usuarioRepository.findById(idLong);

            return usuario.map(Collections::singletonList)
                                         .orElseGet(Collections::emptyList);

        } catch (Exception e) {
            
            return List.of("erro", e.getMessage());
        }
    }
}

