package com.example.loja.controllers.UsuarioController;

import com.example.loja.enums.Cargo;
import com.example.loja.exceptions.SessionException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.models.UsuarioAddress;
import com.example.loja.repositories.UsuarioAddressRepository;
import com.example.loja.service.UsuarioService.AuthService;
import com.example.loja.service.UsuarioService.ProfileService;
import com.example.loja.util.Util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    private final AuthService authService;
    private final UsuarioAddressRepository usuarioAddressRepository;
    private final ProfileService profileService;

    public ProfileController(AuthService authService,
                             UsuarioAddressRepository usuarioAddressRepository,
                             ProfileService profileService){
        this.authService = authService;
        this.usuarioAddressRepository = usuarioAddressRepository;
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ModelAndView Profile() throws SessionException, Exception {

        ModelAndView mv = new ModelAndView();

        try {

            // Tenta buscar o usuário da sessão
            Usuario user = authService.buscarSessaUsuario();

            if(!usuarioAddressRepository.findByEmail(user.getEmail()).isEmpty()){

                UsuarioAddress verifyUserAddress = usuarioAddressRepository.findByEmail(user.getEmail()).get(0);

                // Verifica se é admin, se sim, adiciona o atributo
                if(user.getRole().equals(Cargo.ROLE_ADMIN)){
                    mv.addObject("admin", true);
                }

                mv.addObject("address", verifyUserAddress);
                mv.setViewName("views/profile/profile");
                return mv;
            }
            
            mv.addObject("address", new UsuarioAddress());
            mv.setViewName("views/profile/profile");

        } catch (SessionException e) {

            mv.setViewName("redirect:/auth/login");
            System.out.println("session_exception: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("exception: " + e.getMessage());
            mv.setViewName("redirect:/auth/login");
        }

        return mv;
    }

    @DeleteMapping("/profile/delete")
    public ResponseEntity<?> ApagarUsuario(@RequestBody Map<String, String> senhaRequest, HttpServletRequest request) throws Exception, UsuarioException{

        try {
            
            // Pega a senha do usuário
            String senha = senhaRequest.get("senha");
            
            // Verifica a autenticação do usuário
            Usuario user = authService.buscarSessaUsuario();
            if(user == null){
                throw new UsuarioException("Ocorreu algum erro. Tente novamente mais tarde");                
            }

            // Verifica se as senhas coencidem
            if(!Util.verifyPass(senha, user.getPassword())){
                throw new UsuarioException("A senha está incorreta"); 
            }

            // Deleta o uusário do banco de dados
            profileService.deleteUser(user);

            // Invalida a sessão do usuário
            request.getSession().invalidate();

            // Remove a autenticação do Security
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok().body(200);

        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Ocorreu algum  erro. Tente novamente mais tarde"));
        }
    }
}
