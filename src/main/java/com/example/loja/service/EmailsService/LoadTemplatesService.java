package com.example.loja.service.EmailsService;


import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;


@Service
public class LoadTemplatesService {
    
    private final TemplateEngine templateEngine;

    public LoadTemplatesService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    
    public String resetSenha(String token) throws Exception{

        Context context = new Context();
        context.setVariable("token", token);

        return templateEngine.process("emails/resetSenhaEmail", context);
    }

    public String avisoDeTrocaDeSenha(){
        
        return templateEngine.process("emails/avisoDeTrocaDeSenha", new Context());
    }

    public String welcome(String token){

        Context context = new Context();
        context.setVariable("token", token);

        return templateEngine.process("emails/welcome", context);
    }

    public String confirmacaoDeEmail(String token){

        Context context = new Context();
        context.setVariable("token", token);

        return templateEngine.process("emails/confirmacaoEmail", context);
    }
}
