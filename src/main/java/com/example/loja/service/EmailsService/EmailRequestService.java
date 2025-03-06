package com.example.loja.service.EmailsService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.models.EmailRequests;
import com.example.loja.models.Usuario;
import com.example.loja.repositories.EmailRequestRepository;

@Service
public class EmailRequestService {

    private final EmailRequestRepository emailRequestRepository;

    public EmailRequestService(EmailRequestRepository emailRequestRepository) {
        this.emailRequestRepository = emailRequestRepository;
    }

    /***
     * Verifica todos os requests feito para garantir que não sobre carrege o
     * sistema
     */
    public void verifyAllRequests() throws Exception {

        try {

            List<EmailRequests> email_requests = emailRequestRepository.findAll();

            for (EmailRequests request : email_requests) {

                // Pega a diferença entre as duas datas (em minutos)
                Long diferenca = Duration.between(request.getWhen(), LocalDateTime.now()).toMinutes();

                // Se tiver mais de 2 minutos de diferença, ele apaga o registro
                if (diferenca > 0 && diferenca <= 2) {
                    emailRequestRepository.delete(request);
                }
            }

        } catch (Exception e) {

            System.out.println("email_request_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Verifica a autenticidade da requisição
     * 
     * @param emailRequests Objeto que tem os dados da requisição
     * @param email Email do usuário que fez a requisição
     * @return Retorna TRUE caso o usuário possa prosseguir com a requisição (tiver
     *         mais de 2 minutos a requisição) e
     *         FALSE caso contrário
     */
    public boolean verifyUserRequest(Usuario usuario) throws Exception, EmailRequestException{

        try {

            // Busca as requisições de email do usuário
            EmailRequests emailRequests = emailRequestRepository.findByEmail(usuario.getEmail()).get(0);

            // Diferença (minutos) entre o registro da requisição e a hora atual
            Long diferenca = Duration.between(emailRequests.getWhen(), LocalDateTime.now()).toMinutes();

            // Verifica se foi de fato o usuário que fez a requisição
            if(emailRequests.getEmail() != usuario.getEmail()){
                return false;
            }

            // Se tiver mais de 2 minutos de diferença, ele apaga o registro
            if (diferenca > 0 && diferenca <= 2) {
                emailRequestRepository.delete(emailRequests);
                throw new EmailRequestException("Você já pediu um email, espere um pouco");
            }

            return true;

        } catch (EmailRequestException e) {

            System.out.println("email_request_service: " + e.getMessage());
            throw new Exception(e.getMessage());

        } catch (Exception e) {

            System.out.println("email_request_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
