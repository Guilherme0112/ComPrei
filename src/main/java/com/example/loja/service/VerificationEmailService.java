package com.example.loja.service;

import com.example.loja.exceptions.TokenException;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.repositories.VerificationEmailRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationEmailService {

    private final VerificationEmailRepository verificationEmailRepository;
    private final UsuarioRepository usuarioRepository;

    public VerificationEmailService(VerificationEmailRepository verificationEmailRepository,
                                    UsuarioRepository usuarioRepository) {
        this.verificationEmailRepository = verificationEmailRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /***
     * *
     * Verifica todos os tokens da tabela email_verification no banco de dados e
     * deleta aqueles que já estão expirados
     */
    public void verificationTokens() throws Exception {

        try {

            // Busca todos os tokens
            List<VerificationEmail> allTokens = verificationEmailRepository.findAll();

            for (VerificationEmail token : allTokens) {

                // Verifica se o token está expirado
                if (this.isExpired(token)) {
                    verificationEmailRepository.delete(token);
                }
            }

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    /***
     * Verifica se o token passado pelo parametro está expirado
     *
     * @param token Objeto do Token que será verificado
     * @return Retorna true caso esteja expirado e false caso ainda seja válido
     */
    public boolean isExpired(VerificationEmail token) throws Exception, TokenException {

        try {

            // Busca o token passdo no banco de dados
            VerificationEmail tokenBD = verificationEmailRepository.findByToken(token.getToken()).stream()
                                                                                                 .findFirst()
                                                                                                 .orElseThrow(() -> new TokenException("Token não encontrado"));


            // Verifica se a expiração é antes da data atual
            if (tokenBD.getExpire_in().isBefore(LocalDateTime.now())) {

                verificationEmailRepository.delete(tokenBD);
                return true;
            }

            return false;

        } catch (TokenException e) {
            
            throw new TokenException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

    }

    /**
     * Verifica a autenticidade do token para caso o token seja valido ele ativa a conta
     * do usuário
     * 
     * @param token Token que será verificado
     * @throws Exception
     * @throws TokenException
     */
    public void validationEmailUser(VerificationEmail token) throws Exception, TokenException {

        try {

            // Busca o token passdo no banco de dados
            VerificationEmail tokenBD = verificationEmailRepository.findByToken(token.getToken()).stream()
                                                                                                 .findFirst()
                                                                                                 .orElseThrow(() -> new TokenException("Token não encontrado"));

            // Verifica se o token expirou
            if (this.isExpired(token)) {

                // Deleta o token do banco de dados
                verificationEmailRepository.delete(tokenBD);
                throw new TokenException("O token está expirado");
            }

            // Altera o status da conta para ativo
            usuarioRepository.changeActive(true, tokenBD.getEmail());

            // Deleta o token
            verificationEmailRepository.delete(tokenBD);

        } catch (TokenException e) {

            throw new TokenException(e.getMessage());
            
        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

}
