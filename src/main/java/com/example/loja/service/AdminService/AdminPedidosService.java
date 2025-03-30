package com.example.loja.service.AdminService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Pedido;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Pedidos;
import com.example.loja.models.Usuario;
import com.example.loja.models.dto.PedidoDTO;
import com.example.loja.repositories.PedidosRepository;
import com.example.loja.repositories.UsuarioRepository;

@Service
public class AdminPedidosService {

    private final PedidosRepository pedidosRepository;
    private final UsuarioRepository usuarioRepository;

    public AdminPedidosService(PedidosRepository pedidosRepository, 
                               UsuarioRepository usuarioRepository){
        this.pedidosRepository = pedidosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PedidoDTO> getPedidos(String status) throws Exception, PedidosException, UsuarioException {


        try {
           
            List<PedidoDTO> response = new ArrayList<>();

            Pedido statusObj = Pedido.valueOf(status.toUpperCase());

            // Busca os status com um determinado status
            List<Pedidos> pedidos = pedidosRepository.findByStatus(statusObj);

            // Verifica se existe algum dado e se não existir ele lança uma exceção
            pedidos.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Não existem produtos com este status"));

            for (Pedidos pedido : pedidos) {

                // Busca o usuário que tem o email do pedido
                List<Usuario> user = usuarioRepository.findByEmail(pedido.getEmail(), true);
                user.stream()
                    .findFirst()
                    .orElseThrow(() -> new UsuarioException("O dono do pedido não existe"));

                // Cria o DTO com os dados
                PedidoDTO pedidoDto = new PedidoDTO(
                    pedido.getId(),
                    user.get(0).getName(),
                    user.get(0).getTelefone(),
                    pedido.getValor(),
                    pedido.getStatus().toString(),
                    pedido.getQuando()
                );

                // Adiciona o DTO na lista de resposta
                response.add(pedidoDto);

            }

            return response;

        } catch(PedidosException | UsuarioException e){ 

            throw e;
          
        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }

        
    }

    public void updatePedido(Long id, String status, String email) throws Exception, PedidosException, UsuarioException {

        try {

            // Verifica se existe no banco de dados
            Pedidos pedido = pedidosRepository.findById(id).stream()
                                                          .findFirst()
                                                          .orElseThrow(() -> new PedidosException("Usuário não existe"));

            // Verifica se existe algum usuário e se não existir lança uma exception
            Usuario user = usuarioRepository.findByEmail(email, true).stream()
                                                                            .findFirst()
                                                                            .orElseThrow(() -> new UsuarioException("Usuário não existe"));

           Pedido novoStatus = Pedido.valueOf(status.toUpperCase());
           pedido.setStatus(novoStatus);
           pedidosRepository.save(pedido);

        } catch(PedidosException | UsuarioException e){

            throw e;

        } catch (IllegalArgumentException e){

            throw new PedidosException("O formato do status é inválido");

        }catch (Exception e) {

            throw new Exception(e.getMessage());
        }

    }
}
