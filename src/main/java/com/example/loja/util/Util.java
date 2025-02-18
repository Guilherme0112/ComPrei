package com.example.loja.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Util {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /***
     * Criptografa a senha 
     * 
     * @param senha Senha que será criptografada
     * @return Hash gerado a partir da senha criptografada
     */
    public static String Bcrypt(String senha){
        return encoder.encode(senha);
    }

    /***
     * Verifica se o hash corresponde ao hash da senha 
     * 
     * @param senha Senha que será verificada
     * @param hash Hash que geralmente fica no banco de dados
     * @return Retorna TRUE caso a senha corresponda ao hash e FALSE caso não corresponda
     */
    public static boolean verifyPass(String senha, String hash){
        return encoder.matches(senha, hash);
    }
}
