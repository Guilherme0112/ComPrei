package com.example.loja.controllers.ComprasController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Pagamentos;
import com.example.loja.models.Produto;
import com.example.loja.models.dto.ProdutoQuantidade;
import com.example.loja.repositories.PagamentosRepository;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;
import com.example.loja.service.UsuarioService.AuthService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import jakarta.servlet.http.HttpSession;

@RestController
public class PagamentosController {

        private static final String KEY_MP = "TEST-4070014182739433-032109-cbfd04dc5f8fe1d00a9964af62a247b4-611245292";

        private final ProdutoService produtoService;
        private final ProdutoRepository produtoRepository;
        private final PagamentosRepository pagamentosRepository;
        private final AuthService authService;

        public PagamentosController(ProdutoService produtoService,
                                    ProdutoRepository produtoRepository,
                                    PagamentosRepository pagamentosRepository,
                                    AuthService authService) {
                this.produtoService = produtoService;
                this.produtoRepository = produtoRepository;
                this.pagamentosRepository = pagamentosRepository;
                this.authService = authService;
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
                        PreferenceClient client = new PreferenceClient();
                        BigDecimal total = BigDecimal.ZERO;

                        // Lista com todos os produtos
                        List<PreferenceItemRequest> items = new ArrayList<>();

                        // Verifica se todos os produtos do carrinho de fato existem
                        for (ProdutoQuantidade codigoProduto : produtos) {

                                // Verificação
                                Produto p = produtoService.getProduct(codigoProduto.getCodigo());

                                // Verifica se tem estoque para o produto
                                if(produtoRepository.coutByCodigo(p.getCodigo()) < codigoProduto.getQuantidade()){
                                        throw new ProdutoException("A quantidade é maior que o estoque atual");
                                }

                                // Cria o objeto para constuir a lista
                                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                                        .id(p.getCodigo())
                                        .title(p.getName())
                                        .quantity(codigoProduto.getQuantidade())
                                        .currencyId("BRL")
                                        .unitPrice(p.getPrice())
                                        .build();
                                        
                                        // Adiciona o item a lista
                                        items.add(itemRequest);

                                        // Incrementa com o valor do produto vezes a quantidade
                                        total = total.add(p.getPrice().multiply(BigDecimal.valueOf(codigoProduto.getQuantidade())));
                        }

                        // Links para caso o pagamento seja finalizado, falhou ou pendente
                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                .backUrls(
                                        PreferenceBackUrlsRequest.builder()
                                                .success("https://localhost:8080/payment/success")
                                                .failure("https://localhost:8080/payment/erro")
                                                .pending("https://localhost:8080/payment/erro")
                                                .build())

                                .autoReturn("all")
                                .items(items)
                                .build();

                        Preference preference = client.create(preferenceRequest);

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
        public ModelAndView PaymentSucces(@RequestParam("payment_id") String payment_id) {
                 
                ModelAndView mv = new ModelAndView();

                System.out.println(payment_id);

                mv.setViewName("views/produto/compra/success");
                return mv;
        }

        @GetMapping("/payment/erro")
        public ModelAndView PaymentErro() {
                ModelAndView mv = new ModelAndView();

                mv.setViewName("views/produto/compra/erro");
                return mv;
        }

}
