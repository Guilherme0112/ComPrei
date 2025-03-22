package com.example.loja.util;

import java.util.Random;
import java.util.UUID;

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

    /***
     * Gera um token aleatório
     * 
     * @return Retorna o token aleatório em formato de String
     */
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }

    /**
     * Método que gera senhas aleatórias
     * 
     * @return Retorna a senha
     */
    public static String generateSenha(){

        StringBuilder senha = new StringBuilder();
        Random random = new Random();

        String[] alfa = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                         "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", 
                         "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        int largura = random.nextInt(5) + 6;

        for(int i = 0; i < largura; i++){

            int indice = random.nextInt(alfa.length);
            senha.append(alfa[indice]);
        }
        
        return senha.toString();

    }
}
