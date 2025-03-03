package com.example.loja.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }


    /***
     * Envia o email para o usuário
     * 
     * @param destinatario Quem receberá o email
     * @param assunto Assunto do email
     * @param html String em formato de HTML que o destinatário receberá
     * @throws Exception Erros de execução
     */
    public void sendEmail(String destinatario, String assunto, String html) throws Exception{

        MimeMessage message = mailSender.createMimeMessage();

        try {

            MimeMessageHelper send = new MimeMessageHelper(message, true);
            send.setTo(destinatario);
            send.setSubject(assunto);
            send.setText(html, true);
            send.setFrom("guimendesmen124@gmail.com");

            mailSender.send(message);

            System.out.println("Email para " + destinatario + " enviado com sucesso");


        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
