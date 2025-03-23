package com.example.loja.controllers.ComprasController;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.enums.Pedido;
import com.example.loja.exceptions.PagamentoException;
import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Pagamentos;
import com.example.loja.models.Pedidos;
import com.example.loja.models.dto.PagamentoInfo;
import com.example.loja.models.dto.ProdutoQuantidade;
import com.example.loja.repositories.PagamentosRepository;
import com.example.loja.repositories.PedidosRepository;
import com.example.loja.service.PagamentosService;
import com.example.loja.service.UsuarioService.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import jakarta.servlet.http.HttpSession;

@RestController
public class PagamentosController {

        private static final String KEY_MP = "TEST-4070014182739433-032109-cbfd04dc5f8fe1d00a9964af62a247b4-611245292";

       
        private final PagamentosRepository pagamentosRepository;
        private final AuthService authService;
        private final PedidosRepository pedidosRepository;
        private final PagamentosService pagamentosService;

        public PagamentosController(PagamentosRepository pagamentosRepository,
                                    AuthService authService,
                                    PedidosRepository pedidosRepository,
                                    PagamentosService pagamentosService) {
                
                this.pagamentosRepository = pagamentosRepository;
                this.authService = authService;
                this.pedidosRepository = pedidosRepository;
                this.pagamentosService = pagamentosService;
        }

        @GetMapping("/payment")
        public ModelAndView Payment() {

                ModelAndView mv = new ModelAndView();

                mv.setViewName("views/produto/compra/payment");
                return mv;
        }

        @PostMapping("/payment")
        public String PaymentPOST(@RequestBody List<ProdutoQuantidade> produtos, HttpSession http) throws Exception, ProdutoException, MPApiException, MPException{

                try {
                        // Token de acesso para as requisições
                        MercadoPagoConfig.setAccessToken(KEY_MP);
                        
                        // Cria o pagamento para o cliente
                        PagamentoInfo pagamentoInfo = pagamentosService.createPayment(produtos, KEY_MP);

                        // Resposta da camada service
                        Preference preference = pagamentoInfo.getPreference();
                        BigDecimal total = pagamentoInfo.getTotal();
                        
                        // Salva os dados do pagamento
                        pagamentosRepository.save(new Pagamentos(
                                authService.getSession(http).getEmail(),
                                preference.getId(),
                                "pending",
                                total
                        ));

                        // Retorna o link para efetuar o apagamento
                        return preference.getInitPoint().toString();

                } catch (ProdutoException e) {

                        System.out.println("produtoexception: " + e.getMessage());
                        return e.getMessage();
                        
                } catch (MPApiException e) {

                        System.out.println("mpapiexception: " + e.getApiResponse().getContent());
                        return "Ocorreu algum erro ao gerar a forma de pagamento. Tente novamente mais tarde";

                } catch (MPException e) {

                        System.out.println("mpexception: " + e.getMessage());
                        return "Ocorreu algum erro na hora de criar a forma de pagamento. Tente novamente mais tarde";

                } catch (Exception e) {

                        System.out.println("exception: " + e.getMessage());
                        return "Ocorreu algum erro. Tente novamente mais tarde";
                }
        }

        @GetMapping("/payment/success")
        public ModelAndView PaymentSucces(@RequestParam("payment_id") String payment_id, @RequestParam("preference_id") String preference_id, HttpSession http) throws Exception, PagamentoException {
                 
                ModelAndView mv = new ModelAndView();

                try {

                        // Verifica se existe o id na url
                        if(payment_id.isEmpty() || preference_id.isEmpty()){
                                mv.setViewName("redirect:/profile");
                                return mv;
                        }

                        // Verifica se a transação foi aprovada
                        JsonNode jsonNode = pagamentosService.verifyPayment(payment_id, KEY_MP);

                        // Verifica o status de pagamento para atualizar no banco de dados
                        if(!jsonNode.get("status").asText().equals("approved")){
                                throw new PagamentoException("Para fazer o pedido você deve concluir a compra");
                        }
                        
                        // Pega o registro do pagamento no banco de dados
                        Pagamentos objPreference = pagamentosRepository.findByPreferenceId(preference_id).get(0);
                        if(objPreference == null){
                                throw new PagamentoException("O id da preferência não existe");
                         }

                        // Como ele só vai passar se o status do pagamento for aprovado, ele atualiza no banco o status do pagamento para aprovado
                        // Também adiciona o id do pagamento (só tinha o id da preferencia)
                        objPreference.setStatus("approved");
                        objPreference.setPagamento_id(payment_id);
                        pagamentosRepository.save(objPreference);

                        // Cria o pedido para os admins e o salva no banco de dados
                        pedidosRepository.save(
                                new Pedidos(payment_id, 
                                            new BigDecimal(jsonNode.get("transaction_amount").asText()),
                                            jsonNode.get("additional_info").get("items").asText(),
                                            authService.getSession(http).getEmail(),
                                            Pedido.ENVIADO
                                )
                        );

                        mv.setViewName("views/produto/compra/success");
                        
                } catch(PagamentoException e){

                        System.out.println(e.getMessage());
                        mv.setViewName("redirect:/payment/erro");
                } catch (Exception e) {

                        System.out.println(e.getMessage());
                        mv.setViewName("redirect:/payment/erro");
                }

                return mv;
        }

        @GetMapping("/payment/erro")
        public ModelAndView PaymentErro() {

                ModelAndView mv = new ModelAndView();

                mv.setViewName("views/produto/compra/erro");
                return mv;
        }

}
