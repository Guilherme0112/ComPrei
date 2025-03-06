package com.example.loja.controllers.UsuarioController;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.models.Usuario;
import com.example.loja.models.UsuarioAddress;
import com.example.loja.repositories.UsuarioAddressRepository;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.UsuarioAddressService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UsuarioAddressController {
    
    private final UsuarioAddressRepository usuarioAddressRepository;
    private final AuthService authService;
    private final UsuarioAddressService usuarioAddressService;

    public UsuarioAddressController(UsuarioAddressRepository usuarioAddressRepository,
                                    AuthService authService,
                                    UsuarioAddressService usuarioAddressService){
        this.usuarioAddressRepository = usuarioAddressRepository;
        this.authService = authService;
        this.usuarioAddressService = usuarioAddressService;
    }

    @GetMapping("/profile/edit/address")
    public ModelAndView Config(HttpSession http){

        ModelAndView mv = new ModelAndView();

        try {

            Usuario user = authService.getSession(http);

            if(usuarioAddressRepository.findByEmail(user.getEmail()).isEmpty()){

                mv.addObject(new UsuarioAddress());
                mv.setViewName("views/profile/edit/address");
                return mv;
            }
            


            mv.addObject("usuarioAddress", usuarioAddressRepository.findByEmail(user.getEmail()).get(0));
            mv.setViewName("views/profile/edit/address");
            
        } catch (Exception e) {

            mv.setViewName("views/profile/address");
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
        }

        return mv;
    }

    @PostMapping("/profile/edit/address")
    public ModelAndView ConfigPost(@Valid UsuarioAddress usuarioAddress, BindingResult br, HttpSession http) throws Exception{

        ModelAndView mv = new ModelAndView();

        try {

            
            if(br.hasErrors()){

                mv.setViewName("/views/profile/edit/address");
                mv.addObject(usuarioAddress);
                return mv;
            }

            // Chama o service pegando o email da sessão
            usuarioAddressService.saveAddress(usuarioAddress, authService.getSession(http).getEmail());

            mv.setViewName("redirect:/profile");

        } catch (Exception e) {
            
            System.out.println("exception: " + e.getMessage());
            mv.setViewName("views/profile/edit/address");
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
        }

        return mv;
    }

}
