package com.example.loja.controllers.UsuarioController;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.models.Usuario;
import com.example.loja.models.dto.PasswordRequest;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.ProfileService;
import com.example.loja.util.Util;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EditPasswordController {
    
    private final ProfileService profileService;
    private final AuthService authService;

    public EditPasswordController(ProfileService profileService,
                                  AuthService authService){
        this.profileService = profileService;
        this.authService = authService;
    }


    @GetMapping("/profile/edit/password")
    public ModelAndView TrocarSenha(){

        ModelAndView mv = new ModelAndView();

        mv.addObject("passwordRequest", new PasswordRequest());
        mv.setViewName("views/profile/edit/password");

        return mv;
    }

    @PostMapping("/profile/edit/password")
    public ModelAndView TrocarSenhaPOST(@Valid PasswordRequest passwordRequest, HttpSession http, BindingResult br) throws Exception{
        
        ModelAndView mv = new ModelAndView();

        try {

            // Pegando o objeto do usuário da sessão
            Usuario user = authService.getSession(http);

            // Verifica a sessão do usuário
            if(user == null){
                throw new Exception("Erro ao encontrar a sessão do usuário");
            }
            
            // Verifica se existe erro de validação na senha nova
            if(br.hasErrors()){

                mv.addObject("passwordRequest", passwordRequest);
                mv.setViewName("views/profile/edit/password");
                return mv;
            }
      
            // Verifica se a senha corresponde com a do banco
            if(!Util.verifyPass(passwordRequest.getSenhaAntiga(), user.getPassword())){

                mv.addObject("erro", "A senha está incorreta");
                return mv;
            }

            // Altera a senha
            profileService.alterPassword(passwordRequest, user);

            // Redireciona para /profile
            mv.setViewName("redirect:/profile");

        } catch (Exception e) {

            System.out.println(e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }

        return mv;
    }
}
