package com.example.loja.service.UsuarioService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.models.UsuarioAddress;
import com.example.loja.repositories.UsuarioAddressRepository;


@Service
public class UsuarioAddressService {
    
    private final UsuarioAddressRepository usuarioAddressRepository;

    public UsuarioAddressService(UsuarioAddressRepository usuarioAddressRepository){
        this.usuarioAddressRepository = usuarioAddressRepository;
    }

    /***
     * Salvo o registro no banco de dados
     * 
     * @param usuarioAddress Objeto do endereço do usuario
     * @param email Email do usuário que preencheu
     * @throws Exception Caso houver algum erro do servidor
     */
    public void saveAddress(UsuarioAddress usuarioAddress, String email) throws Exception{

        try {     

            // Busca algum registro desse usuário
            List<UsuarioAddress> verifyUserAddress = usuarioAddressRepository.findByEmail(email);

            // Verifica se já existe um registro
            if(!verifyUserAddress.isEmpty()){

                // Quando já existe um registro, basta ter o mesmo id que o spring atualiza os valores
                usuarioAddress.setId(verifyUserAddress.get(0).getId());
                usuarioAddressRepository.save(usuarioAddress);                
            }

            // Salva no banco de dados com o email do usuario
            usuarioAddress.setUser_email(email);
            usuarioAddressRepository.save(usuarioAddress);

        } catch (Exception e) {
            
            System.out.println("exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    
}
