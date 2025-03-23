package com.example.loja.models.dto;

import java.math.BigDecimal;

import com.mercadopago.resources.preference.Preference;

public class PagamentoInfo {

    private Preference preference;

    private BigDecimal total;

    public PagamentoInfo(){ }

    public PagamentoInfo(Preference preference, BigDecimal total){
        this.preference = preference;
        this.total = total;
     }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    
}
