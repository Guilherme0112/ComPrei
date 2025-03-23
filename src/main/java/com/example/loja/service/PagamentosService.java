package com.example.loja.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.models.dto.PagamentoInfo;
import com.example.loja.models.dto.ProdutoQuantidade;
import com.example.loja.repositories.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

@Service
public class PagamentosService {

    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

    public PagamentosService(ProdutoService produtoService,
            ProdutoRepository produtoRepository) {

        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    /***
     * Faz uma requisição para a API do mercado pago para verificar se o pagamento
     * foi aprovado
     * 
     * @param payment_id Id do pagamento
     * @param KEY_MP Chave de acesso para a requisição
     * @return Resposta da API
     * @throws Exception Erro genérico
     */
    public JsonNode verifyPayment(String payment_id, String KEY_MP) throws Exception {

        try {

            // URL da requisição
            String url = "https://api.mercadopago.com/v1/payments/" + payment_id;

            // Configurações da requisição
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Adicionando método e headers
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + KEY_MP);

            // Status da requisição
            int responseCode = con.getResponseCode();

            // Lê a resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Mapeia o objeto e converte de string para JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());

            return jsonNode;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

   /***
    * Cria a preferência de pagamento para o cliente 
    *
    * @param produtos Lista de produtos que serão cobrados
    * @param KEY_MP Token de acesso para a API
    * @return Retorna o DTO que tem o total a ser cobrado e o objeto da preferencia
    * @throws Exception Erro genérico
    */
    public PagamentoInfo createPayment(List<ProdutoQuantidade> produtos, String KEY_MP) throws Exception, MPApiException, MPException, ProdutoException {

        try {

            PreferenceClient client = new PreferenceClient();
            BigDecimal total = BigDecimal.ZERO;

            // Lista com todos os produtos
            List<PreferenceItemRequest> items = new ArrayList<>();

            // Verifica se todos os produtos do carrinho de fato existem
            for (ProdutoQuantidade codigoProduto : produtos) {

                // Verificação
                Produto p = produtoService.getProduct(codigoProduto.getCodigo());

                // Verifica se tem estoque para o produto
                if (produtoRepository.coutByCodigo(p.getCodigo()) < codigoProduto.getQuantidade()) {
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
                                    .success("http://localhost:8080/payment/success")
                                    .failure("http://localhost:8080/payment/erro")
                                    .pending("http://localhost:8080/payment/erro")
                                    .build())

                    .autoReturn("all")
                    .items(items)
                    .build();

            return new PagamentoInfo(client.create(preferenceRequest), total);

        } catch (ProdutoException e){

            throw new ProdutoException(e.getMessage());

        } catch (MPApiException e) {

            System.out.println("mpapiexception: " + e.getApiResponse().getContent());
            throw new MPApiException(e.getMessage(), null);

        } catch (MPException e) {

            System.out.println("mpexception: " + e.getMessage());
            throw new MPException(e.getMessage());

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}
