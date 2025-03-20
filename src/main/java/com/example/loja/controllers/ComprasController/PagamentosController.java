package com.example.loja.controllers.ComprasController;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;

@RestController
public class PagamentosController {

    @PostMapping("/payment/pix")
    public ModelAndView PIX(){

        ModelAndView mv = new ModelAndView();
  
        MercadoPagoConfig.setAccessToken("ENV_ACCESS_TOKEN");

        Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("x-idempotency-key", <SOME_UNIQUE_VALUE>);
        
        MPRequestOptions requestOptions = MPRequestOptions.builder()
            .customHeaders(customHeaders)
            .build();

        PaymentClient client = new PaymentClient();

        PaymentCreateRequest paymentCreateRequest =
        PaymentCreateRequest.builder()
            .transactionAmount(new BigDecimal("100"))
            .description("Título do produto")
            .paymentMethodId("pix")
            .dateOfExpiration(OffsetDateTime.of(2023, 1, 10, 10, 10, 10, 0, ZoneOffset.UTC))
            .payer(
                PaymentPayerRequest.builder()
                    .email("PAYER_EMAIL")
                    .firstName("Test")
                    .identification(
                        IdentificationRequest.builder().type("CPF").number("19119119100").build())
                    .build())
            .build();

        client.create(paymentCreateRequest, requestOptions);


        return mv;

    }

}
