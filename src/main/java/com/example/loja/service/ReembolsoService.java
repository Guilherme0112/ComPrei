package com.example.loja.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Pedido;
import com.example.loja.enums.Reembolso;
import com.example.loja.exceptions.PedidosException;
import com.example.loja.models.Pedidos;
import com.example.loja.models.Reembolsos;
import com.example.loja.repositories.PedidosRepository;
import com.example.loja.repositories.ReembolsosRepository;

@Service
public class ReembolsoService {

    private final ReembolsosRepository reembolsoRepository;
    private final PedidosRepository pedidosRepository;

    public ReembolsoService(ReembolsosRepository reembolsoRepository,
            PedidosRepository pedidosRepository) {
        this.reembolsoRepository = reembolsoRepository;
        this.pedidosRepository = pedidosRepository;
    }

    /** Cria um pedido de reembolso
     * 
     * 
     * @param idPedido Id do pedido que será reembolsado
     * @throws Exception Erros genéricos
     * @throws PedidosException Erros relacionados ao pedido
     */
    public void createReembolso(String idPedido, String email) throws Exception, PedidosException {

        try {

            // Verifica se o pedido existe
            Optional<Pedidos> pedido = pedidosRepository.findById(Long.parseLong(idPedido));

            // Verifica se o pedido existe
            pedido.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Pedido nao encontrado"));

            // Verifica se o pedido pertence ao usuário
            if(!pedido.get().getEmail().equals(email)){
                throw new PedidosException("Pedido nao pertence ao seu email");
            }
            
            // Verifica se o id do pedido já está em um reembolso
            if (!reembolsoRepository.findByIdPedido(Long.parseLong(idPedido)).isEmpty()) {
                throw new PedidosException("Pedido ja possui um reembolso");
            }

            // Salva o pedido de reembolso
            reembolsoRepository.save(
                new Reembolsos(email,
                               Long.parseLong(idPedido),
                               Reembolso.PENDENTE
                )
            );


            // Atualiza o pedido para cancelado
            pedido.get().setStatus(Pedido.CANCELADO);
            pedidosRepository.save(pedido.get());

        } catch (PedidosException e) {

            throw e;

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }

    /** Busca os dados de um reembolso
     * 
     * @param idPedido Id do pedido que é reembolsado
     * @return Reembolso
     * @throws Exception Erro genérico
     * @throws PedidosException Erros relacionados ao pedido
     */
    public Reembolsos getReembolso(String idPedido) throws Exception, PedidosException {

        try {

            // Verifica se o pedido existe
            Optional<Pedidos> pedido = pedidosRepository.findById(Long.parseLong(idPedido));

            pedido.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Pedido nao encontrado"));

            // Busca o reembolso
            List<Reembolsos> reembolso = reembolsoRepository.findByIdPedido(Long.parseLong(idPedido));

            // Verifica se o reembolso foi encontrado
            reembolso.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

            return reembolso.get(0);

        } catch (PedidosException e) {

            throw e;

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }

    /** Atualiza o status de em reembolso
     * 
     * @param reembolso Objeto do reembolso
     * @param status Novo status do reembolso
     * @throws Exception Erro genérico
     * @throws PedidosException Erros relacionados ao pedido
     */
    public void updateReembolso(Reembolsos reembolso, String status) throws Exception, PedidosException {

        try {

            // Busca o reembolso            
            Optional<Reembolsos> reembolsoBusca = reembolsoRepository.findById(reembolso.getId());
            reembolsoBusca.stream()
            .findFirst()
            .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

            // Atualiza o reembolso
            reembolso.setStatus(Reembolso.valueOf(status.toUpperCase()));
            reembolsoRepository.save(reembolso);

        } catch (PedidosException e) {

            throw e;

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }
}
