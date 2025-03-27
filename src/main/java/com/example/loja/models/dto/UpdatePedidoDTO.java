package com.example.loja.models.dto;

import com.example.loja.enums.Pedido;

public class UpdatePedidoDTO {
    
    private Long id;

    private Pedido pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    
}
