package com.example.loja.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.loja.models.Usuario;
import com.example.loja.models.dto.LoginRequest;
import com.example.loja.repositories.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
    
    public ResponseEntity<?> createSession(LoginRequest loginRequest) throws Exception{

        try {
            
            String email = loginRequest.getEmail();
            String senha = loginRequest.getSenha();

            if(email.isEmpty() || senha.isEmpty()){
                return ResponseEntity.badRequest().body(Map.of("erro", "Você deve preencher todos os campos"));
            }

            List<Usuario> existEmail = usuarioRepository.findByEmail(email, true);

            if(existEmail.isEmpty()){
                return ResponseEntity.badRequest().body(Map.of("erro", "As credenciais estão incorretas"));
            }

            Usuario user = existEmail.get(0);
            String passwordBD = user.getPassword();

            return ResponseEntity.ok().body(Map.of("sucess", "Conta registrada com sucesso"));

        } catch (Exception e) {
            
            System.out.println("erro_exception: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
       

    
    }
}
