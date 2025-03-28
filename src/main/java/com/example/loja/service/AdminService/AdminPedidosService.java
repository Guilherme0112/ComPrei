package com.example.loja.service.AdminService;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Pedido;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Pedidos;
import com.example.loja.models.Usuario;
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
