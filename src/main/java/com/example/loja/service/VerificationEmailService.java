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

                // Verifica se o token é válido
                if(!this.isValidToken(token.getToken())){
                    verificationEmailRepository.delete(token);
                    break;
                }

                // Verifica se o token está expirado
                if(this.isExpired(token.getToken())) {
                    verificationEmailRepository.delete(token);
                    break;
                }
            }

        } catch (Exception e){

            throw new Exception(e.getMessage());
        }
    }


    /***
     * Método que verifica o token que é passado como parametro, ele deleta caso o token esteja expirado ou inválido
     *
     * @param token Token que será verificado
     * @return Retorna TRUE caso o token ainda seja válido, e false caso ele seja inválido ou expirado
     * */
    public boolean isValidToken(String token) throws Exception{

        try {

            List<VerificationEmail> tokenList = verificationEmailRepository.findByToken(token);

            if(tokenList.isEmpty()){
                return false;
            }

            return true;

        } catch (Exception e) {

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

               return  true;
           }

           return false;

       } catch (Exception e) {

           throw new Exception(e.getMessage());
       }

    }
}
