package com.example.loja.service.AdminService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Reembolso;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.exceptions.ReembolsoException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Reembolsos;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.UpdateReembolsoDTO;
import com.example.loja.repositories.ReembolsosRepository;
import com.example.loja.repositories.UsuarioRepository;

@Service
public class AdminReembolsoService {

    private final ReembolsosRepository reembolsoRepository;
    private final UsuarioRepository usuarioRepository;

    public AdminReembolsoService(ReembolsosRepository reembolsoRepository,
            UsuarioRepository usuarioRepository) {
        this.reembolsoRepository = reembolsoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /** Método que busca todos os reembolsos com um determinado status
     * 
     * @param status Status que será procurado
     * @return Lista de objetos com os reembolsos
     * @throws Exception Erro genérico
     * @throws PedidosException Erros relacionados aos pedidos
     * @throws UsuarioException
     */
    public List<UpdateReembolsoDTO> getReembolsoByStatus(Reembolso status)  throws Exception, ReembolsoException, UsuarioException {

        try {
            
            // Incializa a varaivel de resposta
            List<UpdateReembolsoDTO> response = new ArrayList<>();

            // Busca o reembolso no banco de dados ou lança uma exceção
            List<Reembolsos> reembolsoObj = reembolsoRepository.findByStatus(status);

            // Verifica se tem dados
            if(reembolsoObj.isEmpty()) throw new ReembolsoException("Reembolso nao encontrado");
                                                                             
            // Percorre a lista para criar o DTO
            for(Reembolsos reembolso : reembolsoObj) {

                // Busca o usuario no banco de dados
                Usuario user = usuarioRepository.findByEmail(reembolso.getEmail(), true).stream()
                                                                                               .findFirst()   
                                                                                               .orElseThrow(() -> new UsuarioException("Usuario nao encontrado"));                                     
                // Adiociona o DTO na lista de resposta
                response.add(
                    new UpdateReembolsoDTO(
                        reembolso.getId(),
                        reembolso.getIdPedido(),
                        user.getName(),
                        user.getTelefone(),
                        reembolso.getQuando()
                    )
                );
            }

            return response;

        } catch (ReembolsoException | UsuarioException e) {
            
            throw e;

        } catch (Exception e){

            throw new Exception(e.getMessage());
        }
    }

}
