package com.example.loja.service.UsuarioService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loja.enums.Cargo;
import com.example.loja.exceptions.EmailRequestException;
import com.example.loja.exceptions.UsuarioException;
import com.example.loja.models.Pagamentos;
import com.example.loja.models.Pedidos;
import com.example.loja.models.Usuario;
import com.example.loja.models.UsuarioAddress;
import com.example.loja.models.VerificationEmail;
import com.example.loja.repositories.PagamentosRepository;
import com.example.loja.repositories.PedidosRepository;
import com.example.loja.repositories.UsuarioAddressRepository;
import com.example.loja.repositories.UsuarioRepository;
import com.example.loja.repositories.VerificationEmailRepository;
import com.example.loja.service.EmailsService.EmailRequestService;
import com.example.loja.service.EmailsService.EmailService;
import com.example.loja.service.EmailsService.LoadTemplatesService;
import com.example.loja.util.Util;

@Service
public class ProfileService {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final EmailRequestService emailRequestService;
    private final VerificationEmailRepository verificationEmailRepository;
    private final PedidosRepository pedidosRepository;
    private final UsuarioAddressRepository usuarioAddressRepository;
    private final PagamentosRepository pagamentosRepository;
    private final LoadTemplatesService loadTemplatesService;

    public ProfileService(UsuarioRepository usuarioRepository,
            EmailService emailService,
            EmailRequestService emailRequestService,
            VerificationEmailRepository verificationEmailRepository,
            PedidosRepository pedidosRepository,
            UsuarioAddressRepository usuarioAddressRepository,
            PagamentosRepository pagamentosRepository,
            LoadTemplatesService loadTemplatesService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.emailRequestService = emailRequestService;
        this.verificationEmailRepository = verificationEmailRepository;
        this.pedidosRepository = pedidosRepository;
        this.usuarioAddressRepository = usuarioAddressRepository;
        this.pagamentosRepository = pagamentosRepository;
        this.loadTemplatesService = loadTemplatesService;

    }

    /***
     * Deleta o registro do usuário do banco de dados
     * 
     * @param user Objeto do usuário que será deletado
     * @throws UsuarioException Erros referentes ao usuário
     * @throws Exception        Erro genérico
     */
    public void deleteUser(Usuario user) throws Exception, UsuarioException {
        try {

            List<Usuario> userVerify = usuarioRepository.findByEmail(user.getEmail(), true);

            // Verifica se a conta existe
            if (userVerify.isEmpty()) throw new UsuarioException("Erro ao apagar conta. Tente novamente mais tarde");
            

            String email = userVerify.get(0).getEmail();

            // Deleta todos os pedidos do usuário
            List<Pedidos> pedidos = pedidosRepository.findByEmail(email);
            pedidosRepository.deleteAll(pedidos);

            // Deleta os dados de endereço do usuário
            List<UsuarioAddress> address = usuarioAddressRepository.findByEmail(email);
            usuarioAddressRepository.deleteAll(address);

            List<Pagamentos> pagamentos = pagamentosRepository.findByEmail(email);
            pagamentosRepository.deleteAll(pagamentos);

            // Deleta a conta
            usuarioRepository.delete(userVerify.get(0));

        } catch (UsuarioException e) {

            System.out.println("profile_service: " + e.getMessage());
            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            System.out.println("profile_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Ativa a conta do usuário que a tem inativa
     * 
     * @param user Objeto do usuário que tem a conta inativa
     * @throws Exception        Erro genérico
     * @throws UsuarioException Erros relacionados ao usuário
     */
    public synchronized void activeAccount(Usuario user) throws Exception, UsuarioException, EmailRequestException {

        try {

            if (user.getActive() == true) throw new UsuarioException("Esta conta já está ativa");

            // Verifica as requisições do usuário
            emailRequestService.verifyUserRequest(user.getEmail());

            // Cria o token
            String token = Util.generateToken();

            // Envia o e-mail de verificação
            emailService.sendEmail(
                user.getEmail(),
                "Confirmação de e-mail",
                loadTemplatesService.confirmacaoDeEmail(token)
            );

            // Salva a requisição no banco de dados
            emailRequestService.saveRequestEmail(user.getEmail());

            // Salva o token no banco de dados para a verificação
            verificationEmailRepository.save(
                new VerificationEmail(user.getEmail(), token)
            );

        } catch (EmailRequestException | UsuarioException e) {

            throw e;

        } catch (Exception e) {
            System.out.println("profile_service: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /***
     * Cria o registro do usuário no banco de dados
     * 
     * @param usuario Objeto do usuário
     * @throws Exception Erros de execução e etc...
     */
    public void createUser(Usuario usuario) throws Exception, UsuarioException {

        try {

            // V alida a senha do usuário
            if (usuario.getPassword().length() < 6 || usuario.getPassword().length() > 10) throw new UsuarioException("A senha deve ter entre 6 e 10 caracteres"); 

            // Verifica se o e-mail está já está sendo usado
            if (!usuarioRepository.findByEmail(usuario.getEmail(), true).isEmpty()) {

                if (usuarioRepository.findByEmail(usuario.getEmail(), true).get(0).getRole().equals(Cargo.ROLE_BANIDO)) {
                    throw new UsuarioException("Está conta já existe e está banida");
                }

                throw new UsuarioException("Este e-mail já está sendo usado");
            }

            // Caso o usuário já tenha o e-mail registrado, mas a conta está inativa
            if (!usuarioRepository.findByEmail(usuario.getEmail(), false).isEmpty()) {

                // Método para ativar a conta
                this.activeAccount(usuario);
                return;
            }

            // Criptografa a senha do usuário, seta o cargo e coloca o +55 no teledone
            usuario.setPassword(Util.Bcrypt(usuario.getPassword()));
            usuario.setTelefone("+55" + usuario.getTelefone());
            usuario.setRole(Cargo.ROLE_CLIENTE);

            usuarioRepository.save(usuario);

        } catch (UsuarioException e) {

            throw new UsuarioException(e.getMessage());

        } catch (Exception e) {

            System.out.println("auth_service: " + e.getMessage());
            throw new Exception("Ocorreu algum erro. Tente novamente mais tarde");
        }
    }
}
