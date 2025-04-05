package com.example.loja.service.EmailsService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.models.EmailRequests;
import com.example.loja.repositories.EmailRequestRepository;

@Service
public class EmailRequestService {

    private final EmailRequestRepository emailRequestRepository;

    public EmailRequestService(EmailRequestRepository emailRequestRepository) {
        this.emailRequestRepository = emailRequestRepository;
    }

    public void saveRequestEmail(String email) throws Exception, EmailRequestException {

        try {

            // Verifica se já existe alguma requisição feita pelo usuário
            this.verifyUserRequest(email);

            // Busca alguma requisição feita pelo o usuário
            List<EmailRequests> findEmail = emailRequestRepository.findByEmail(email);

            // Se tiver vazia ele cria um novo registro
            if (findEmail.isEmpty()) {

                emailRequestRepository.save(
                    new EmailRequests(
                        email
                    )
                );

                return;
            }

            // Se já existir, ele apaga as requisições anteriores
            for (EmailRequests i : findEmail) {
                emailRequestRepository.delete(i);
            }

        } catch (EmailRequestException e) {
            
            throw e;

        } catch (Exception e) {

            System.out.println("Exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Verifica todos os requests feito para garantir que não sobre carrege o
     * sistema
     */
    public void verifyAllRequests() throws Exception {

        try {

            List<EmailRequests> email_requests = emailRequestRepository.findAll();

            for (EmailRequests request : email_requests) {

                // Se tiver mais de 1 minuto de diferença, ele apaga o registro
                if (request.getQuando().until(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                        ChronoUnit.MINUTES) > 1) {
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
     * @param email         Email do usuário que fez a requisição
     * @return Retorna TRUE caso o usuário possa prosseguir com a requisição (tiver
     *         mais de 2 minutos a requisição) e
     *         FALSE caso contrário
     */
    public void verifyUserRequest(String email) throws Exception, EmailRequestException {

        try {

            // Verifica todos as requisições
            verifyAllRequests();

            // Busca as requisições de email do usuário e verifica se está vazia
            EmailRequests emailRequests = emailRequestRepository.findByEmail(email).stream()
                                                                                   .findFirst()
                                                                                   .orElse(null);

            // Se tiver mais de 1 minutos de diferença, ele apaga o registro
            if (emailRequests.getQuando().until(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                    ChronoUnit.MINUTES) < 1) {

                throw new EmailRequestException("Já enviamos seu e-mail, espere um pouco para pedir outro");
            }

        } catch (EmailRequestException e) {

            System.out.println("email_request_service: " + e.getMessage());
            throw new EmailRequestException(e.getMessage());

        } catch (Exception e) {

            System.out.println("email_request_service_exception: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
