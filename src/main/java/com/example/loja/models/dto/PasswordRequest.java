package com.example.loja.models.dto;

import jakarta.validation.constraints.Size;

public class PasswordRequest {
    
    private String senhaAntiga;

    @Size(min = 6, max= 10, message = "A senha deve ter entre 6 e 10")
    private String senhaNova;

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    
}
