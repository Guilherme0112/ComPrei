package com.example.loja.controllers.AdminController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.loja.exceptions.ProdutoException;
import com.example.loja.models.Produto;
import com.example.loja.models.dto.AmountProducts;
import com.example.loja.repositories.ProdutoRepository;
import com.example.loja.service.ProdutoService;
import com.example.loja.util.Util;

import jakarta.validation.Valid;

@RestController
public class APIAdminProdutosController {

    private static final String UPLOAD_DIR = "/uploads/";
    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public APIAdminProdutosController(ProdutoRepository produtoRepository,
                                      ProdutoService produtoService) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;

    }

    @GetMapping("/admin/produtos/{codigo}")
    public List<?> AdminProdutosGET(@PathVariable("codigo") String codigo) {

        try {

            if (!codigo.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            // Busca a quantidade de registros que tem no banco de dados
            Long amountP = produtoRepository.coutByCodigo(codigo);
            AmountProducts amount =  new AmountProducts(amountP.toString());
            
            // Cria o LIMIT e OFFSET para buscar somente a primeira resposta
            Pageable pageable = PageRequest.of(0, 1);
            Produto produtoObj = produtoRepository.findByCodigoDeBarras(codigo, pageable).get(0);

            // Cria o array list dos objetos que serão retornados
            List<Object> response = new ArrayList<>();

            // Retorna uma array com 2 arrays dentro [0: produto, 1: quantidade]
            response.add(0, produtoObj);
            response.add(1, amount);
        
            return response; 

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }

    }

    @PostMapping("/admin/produtos/criar")
    public ModelAndView CriarProduto(@Valid Produto produto,    
                                     BindingResult br,
                                     @RequestParam String amount, 
                                     @RequestParam MultipartFile file) throws Exception, ProdutoException{

        ModelAndView mv = new ModelAndView();

        try {

            if(br.hasErrors()){
                mv.addObject(produto);
                mv.setViewName("views/admin/produtos/criarProdutos");
                return mv;
            }
            
            
            if(file.isEmpty()){
                throw new ProdutoException("A foto é obrigatório");
            }

            File dir = new File(UPLOAD_DIR);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName = file.getOriginalFilename().replaceAll(" ", "");
            File serverFile = new File("/app/" + dir.getAbsolutePath() + File.separator + Util.generateToken() + "_" + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

            stream.write(file.getBytes());
            stream.close();

            produto.setPhoto(serverFile.toString().replaceFirst("/app", ""));


            if (!amount.matches("\\d+")) {
                throw new ProdutoException("A quantidade deve ser um valor válido");
            }

            int amountInt = Integer.parseInt(amount);

            if (amountInt > 10000 || amountInt < 1){
                throw new ProdutoException("A quantidade não pode ser maior que 10000 ou menor que 1");
            }

            for(int i = 0; i < amountInt; i++){

                Produto produto2 = new Produto();
                produto2.setCodigo(produto.getCodigo());                
                produto2.setDescription(produto.getDescription());             
                produto2.setName(produto.getName());             
                produto2.setPhoto(produto.getPhoto());            
                produto2 .setPrice(produto.getPrice());          

                produtoService.createProduct(produto2);
            }

            mv.setViewName("redirect:/admin/produtos");

        } catch(ProdutoException e){

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", e.getMessage());
            mv.setViewName("views/admin/produtos/criarProdutos");

        } catch (Exception e) {

            System.out.println("api_controller: " + e.getMessage());
            mv.addObject("erro", "Ocorreu algum erro. Tente novamente mais tarde");
            mv.setViewName("views/admin/produtos/criarProdutos");
        }

        return mv;
    }


    @DeleteMapping("/admin/deletar/produtos")
    public List<?> DeletarProduto(@RequestBody Map<String, String> codigoMap) throws Exception, ProdutoException {
        try {

            // Válida o codigo para garantir que somente seja composto apenas por números números
            String codigo = codigoMap.get("codigo");
            if (!codigo.matches("\\d+")) {
                return List.of("erro", "O código  é inválido");
            }

            // Pega somente o primeiro resultado da query
            Pageable pageable = PageRequest.of(0, 1);

            // Verifica se existe o produto
            if (produtoRepository.findByCodigoDeBarras(codigo, pageable).isEmpty()) {
                throw new ProdutoException("Produto não existe");
            }

            // Busca a imagem e a deleta caso exista
            Produto produto = produtoRepository.findByCodigoDeBarras(codigo, pageable).get(0);
            Path path = Paths.get("/app" + produto.getPhoto());
            Files.deleteIfExists(path);

            // Deleta do banco de dados
            produtoRepository.deleteByCodigo(produto.getCodigo());

            return List.of(200, "Produto deletado com sucesso");

        } catch (ProdutoException e) {

            return List.of("erro", e.getMessage());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return List.of("erro", "Ocorreu algum erro. Tente novamente mais tarde");
        }
    }

}
