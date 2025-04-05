package com.example.loja.controllers.ComprasController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.repositories.PedidosRepository;
import com.example.loja.service.UsuarioService.AuthService;

@Controller
public class PedidosController {

  private final AuthService authService;
  private final PedidosRepository pedidosRepository;

  public PedidosController(AuthService authService,
      PedidosRepository pedidosRepository) {
    this.authService = authService;
    this.pedidosRepository = pedidosRepository;
  }

  @GetMapping("/profile/pedidos")
  public ModelAndView Pedidos() throws Exception {

    ModelAndView mv = new ModelAndView();

    try {

      // Busca o email do usuário 
      String emailUser = authService.buscarSessaoUsuario().getEmail();

      // Retorna os pedidos referente ao email do usuário (Caso não haja email, ele retorna nada)
      mv.addObject("pedidos", pedidosRepository.findByEmail(emailUser));

    } catch (Exception e) {

      System.out.println(e.getMessage());
      mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
    }

    mv.setViewName("views/produto/pedido/pedidos");
    return mv;

  }
}
