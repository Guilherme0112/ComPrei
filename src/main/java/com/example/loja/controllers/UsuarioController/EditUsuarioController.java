package com.example.loja.controllers.UsuarioController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.SessionException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.EditUserRequest;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.EditUsuarioService;
import com.example.loja.util.Util;

import jakarta.servlet.http.HttpSession;

@Controller
public class EditUsuarioController {

    private final EditUsuarioService editUsuarioService;
    private final AuthService authService;

    public EditUsuarioController(EditUsuarioService editUsuarioService,
            AuthService authService) {
        this.editUsuarioService = editUsuarioService;
        this.authService = authService;
    }

    @GetMapping("/profile/edit")
    public ModelAndView EditUserGET(HttpSession http) throws SessionException {

        ModelAndView mv = new ModelAndView();

        try {

            Usuario user = authService.getSession(http);

            // Envia os dados para as inputs caso já houver os dados
            EditUserRequest editUserRequest = new EditUserRequest();
            editUserRequest.setNome(user.getName());
            editUserRequest.setTelefone(user.getTelefone().replaceFirst("\\+55", ""));

            mv.addObject("editUserRequest", editUserRequest);
            mv.setViewName("views/profile/edit/usuario");

        } catch (Exception e) {
            // TODO: handle exception
        }
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView EditUserPOST(EditUserRequest editUserRequest, HttpSession http)
            throws Exception, UsuarioException {

        ModelAndView mv = new ModelAndView();

        try {

            // Busca a sessão do usuário
            Usuario user = authService.getSession(http);

            if (!Util.verifyPass(editUserRequest.getSenha(), user.getPassword())) {
                throw new UsuarioException("A senha está incorreta");
            }

            // Faz as alterações
            editUsuarioService.save(editUserRequest, user.getEmail());

            mv.setViewName("redirect:/profile");

        } catch (UsuarioException e) {

            System.out.println("usuario_exception_controller: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/profile/edit/usuario");

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro interno. Tente novamente mais tarde");
            mv.setViewName("views/profile/edit/usuario");
        }

        return mv;
    }
}
