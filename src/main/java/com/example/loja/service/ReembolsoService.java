package com.example.loja.service;

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
            Pedidos pedido = pedidosRepository.findById(Long.parseLong(idPedido)).stream()
                                                                                 .findFirst()
                                                                                 .orElseThrow(() -> new PedidosException("Pedido não encontrado"));

            // Verifica se o pedido pertence ao usuário
            if(!pedido.getEmail().equals(email)) throw new PedidosException("Pedido não pertence ao seu email");
            
            // Verifica se o id do pedido já está em um reembolso
            if (!reembolsoRepository.findByIdPedido(Long.parseLong(idPedido)).isEmpty()) throw new PedidosException("O pedido já possui um pedido de reembolso");
            
            // Salva o pedido de reembolso
            reembolsoRepository.save(
                new Reembolsos(
                    email,
                    Long.parseLong(idPedido),
                    Reembolso.PENDENTE
                )
            );

            // Atualiza o pedido para cancelado
            pedido.setStatus(Pedido.CANCELADO);
            pedidosRepository.save(pedido);

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
           pedidosRepository.findById(Long.parseLong(idPedido)).stream()
                                                               .findFirst()
                                                               .orElseThrow(() -> new PedidosException("Pedido não encontrado"));

            // Busca o reembolso e o retorna ou lança uma exceção caso não encontre
            return reembolsoRepository.findByIdPedido(Long.parseLong(idPedido)).stream()
                                                                               .findFirst()
                                                                               .orElseThrow(() -> new PedidosException("Reembolso nao encontrado"));

        } catch (PedidosException e) {

            throw e;

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }
    }

    /** Atualiza o status de um reembolso
     * 
     * @param reembolso Objeto do reembolso
     * @param status Novo status do reembolso
     * @throws Exception Erro genérico
     * @throws PedidosException Erros relacionados ao pedido
     */
    public void updateReembolso(Reembolsos reembolso, String status) throws Exception, PedidosException {

        try {

            // Busca o reembolso            
            reembolsoRepository.findById(reembolso.getId()).stream()
                                                           .findFirst()
                                                           .orElseThrow(() -> new PedidosException("Reembolso não encontrado"));

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
