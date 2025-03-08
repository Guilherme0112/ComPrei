package com.example.loja.service.UsuarioService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.TokenException;
import com.example.loja.models.ResetPassword;
import com.example.loja.repositories.ResetPasswordRepository;

@Service
public class ResetPasswordService {
    
    private final ResetPasswordRepository resetPasswordRepository;

    public ResetPasswordService(ResetPasswordRepository resetPasswordRepository){
        this.resetPasswordRepository = resetPasswordRepository;
    }

    public void verifyAllTokens() throws Exception, TokenException{


        try {
            
            // Busca todos os tokens
            List<ResetPassword> allTokens = resetPasswordRepository.findAll();

            for (ResetPassword token : allTokens){

                if(this.isExpired(token.getToken())){
                    resetPasswordRepository.delete(token);
                }
            }

        } catch (Exception e) {
            
            System.out.println("exception: " + e.getMessage());
            throw new Exception(e.getMessage());

        }
    }


    public boolean isExpired(String token) throws Exception{

       try {

           // Busca o token passdo no banco de dados
           List<ResetPassword> tokenList = resetPasswordRepository.findByToken(token);

           // Verifica se o token existe
           if (tokenList.isEmpty()) {
                return true;
           }

           // Transforma o token que era em formato List para Object
           // Busca o valor de expire_in (LocalDateTime que informa quando o token expira)
           ResetPassword tokenObj = tokenList.get(0);
           LocalDateTime expire_in = tokenObj.getExpire_in();

           // Verifica se a expiração é antes da data atual
            if(expire_in.isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))){

                resetPasswordRepository.delete(tokenObj);
                return  true;
            }

           return false;

       } catch (Exception e) {

           throw new Exception(e.getMessage());
       }

    }
}
