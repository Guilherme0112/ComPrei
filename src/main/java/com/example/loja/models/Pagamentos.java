package com.example.loja.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos")
public class Pagamentos {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String email;

  private String pagamento_id;

  private String status = "pending";

  private BigDecimal valor;

  @Column(updatable = false)
  private LocalDateTime criado_em = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

  public Pagamentos(){ }

  public Pagamentos(String email, String pagamento_id, String status, BigDecimal valor){
    this.email = email;
    this.pagamento_id = pagamento_id;
    this.status = status;
    this.valor = valor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPagamento_id() {
    return pagamento_id;
  }

  public void setPagamento_id(String pagamento_id) {
    this.pagamento_id = pagamento_id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public LocalDateTime getCriado_em() {
    return criado_em;
  }

  public void setCriado_em(LocalDateTime criado_em) {
    this.criado_em = criado_em;
  }

  

}
