package com.example.loja.service.UsuarioService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.UsuarioException;
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
            }

            // Salva no banco de dados com o email do usuario
            usuarioAddress.setUser_email(email);
            usuarioAddressRepository.save(usuarioAddress);

        } catch (Exception e) {
            
            System.out.println("exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Verifica se o usuário tem um endereço cadastrado
     * 
     * @param email Email do usuário    
     * @return Objeto que contém as informações do endereço do usuário
     * @throws Exception Erro genérico
     * @throws UsuarioException Se o usuário não tiver endereço cadastrado
     */
    public UsuarioAddress verifyAddress(String email) throws Exception, UsuarioException{

        try {
            
            return usuarioAddressRepository.findByEmail(email).stream()
                                                              .findFirst()
                                                              .orElseThrow(() -> new UsuarioException("É obrigatório ter um endereço cadastrado"));

        } catch(UsuarioException e){

            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
    
}
