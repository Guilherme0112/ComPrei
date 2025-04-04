package com.example.loja.service.UsuarioService;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.EditUserRequest;
import com.example.loja.repositories.UsuarioRepository;

@Service
public class EditUsuarioService {
    
    private final UsuarioRepository usuarioRepository;


    public EditUsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    /** Atualiza as informações do usuário como nome e telefone
     *  
     * @param editUserRequest DTO que trás os dados do usuário
     * @param email email do usuário
     * @throws Exception Erro genérico
     * @throws UsuarioException Erros relacionado ao usuário
     */
    public void save(EditUserRequest editUserRequest, String email) throws Exception, UsuarioException{

        try {
                  
            // Busca os dados do usuário
            String nome = editUserRequest.getNome();
            String telefone = editUserRequest.getTelefone();

            // Verifica se o usuário existe de fato
            Usuario user = usuarioRepository.findByEmail(email, true).stream()
                                                                            .findFirst()
                                                                            .orElseThrow(() -> new UsuarioException("Ocorreu algum erro. Tente novamente mais tarde"));

            // Válida o nome 
            if(nome.length() < 3 || nome.length() > 30){
                throw new UsuarioException("O nome deve conter entre 3 e 30 caracteres");
            }

            // Valida o telefone
            if(telefone.length() != 11 || !telefone.matches("\\d+")){
                throw new UsuarioException("O número de telefone é inválido");
            }

            // Atualiza os dados e os salva
            user.setName(nome);
            user.setTelefone(telefone.startsWith("+55") ? telefone : "+55" + telefone);
            usuarioRepository.save(user);

        } catch(UsuarioException e){

            System.out.println("edit_usuario_service_usuario_exception: " + e.getMessage());
            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            System.out.println("edit_usuario_service_exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
