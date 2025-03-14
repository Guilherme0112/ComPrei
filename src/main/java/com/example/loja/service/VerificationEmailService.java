package com.example.loja.service;

import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.VerificationEmailRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationEmailService {

    private final VerificationEmailRepository verificationEmailRepository;

    public VerificationEmailService(VerificationEmailRepository verificationEmailRepository){
        this.verificationEmailRepository = verificationEmailRepository;
    }

    /*** *
     * Verifica todos os tokens da tabela email_verification no banco de dados e deleta aqueles que já estão expirados
     */
    public void verificationTokens() throws Exception{

        try {

            // Busca todos os tokens
            List<VerificationEmail> allTokens = verificationEmailRepository.findAll();

            for (VerificationEmail token : allTokens){

                // Verifica se o token está expirado
                if(this.isExpired(token.getToken())) {
                    verificationEmailRepository.delete(token);
                }
            }

        } catch (Exception e){

            throw new Exception(e.getMessage());
        }
    }


    /***
     * Verifica se o token passado pelo parametro está expirado
     *
     * @param token Token que será verificado
     * @return Retorna true caso esteja expirado e false caso ainda seja válido
     */
    public boolean isExpired(String token) throws Exception{

       try {

           // Busca o token passdo no banco de dados
           List<VerificationEmail> tokenList = verificationEmailRepository.findByToken(token);

           // Verifica se o token existe
           if (tokenList.isEmpty()) {
                return true;
           }

           // Transforma o token que era em formato List para Object
           // Busca o valor de expire_in (LocalDateTime que informa quando o token expira)
           VerificationEmail tokenObj = tokenList.get(0);
           LocalDateTime expire_in = tokenObj.getExpire_in();

           // Verifica se a expiração é antes da data atual
            if(expire_in.isBefore(LocalDateTime.now())){

                verificationEmailRepository.delete(tokenObj);
                return  true;
            }

           return false;

       } catch (Exception e) {

           throw new Exception(e.getMessage());
       }

    }
}
