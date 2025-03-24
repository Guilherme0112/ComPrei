package com.example.loja.service;

import org.springframework.stereotype.Service;

import com.example.loja.exceptions.PagamentoException;
import com.example.loja.models.Pagamentos;
import com.example.loja.repositories.PagamentosRepository;

@Service
public class MPApiPreferencesService {

    private final PagamentosRepository pagamentosRepository;

    public MPApiPreferencesService(PagamentosRepository pagamentosRepository){
        this.pagamentosRepository = pagamentosRepository;
    }

    public Pagamentos verifyPreferencePayment(String preference_id) throws Exception, PagamentoException{

        try {

            // Verifica se existe a preferencia no banco de dados
            if (pagamentosRepository.findByPreferenceId(preference_id).isEmpty()) {
                throw new PagamentoException("O id da preferência não existe");
            }

            Pagamentos objPreference = pagamentosRepository.findByPreferenceId(preference_id).get(0);

            // Verifica se é um id que já está pago
            if(objPreference.getStatus().equals("approved")){
                new PagamentoException("Este pagamento já foi aprovado");
            }

            return objPreference;

        } catch (PagamentoException e){

            throw new PagamentoException(e.getMessage());

        } catch (Exception e) {
            
            throw new Exception(e.getMessage());
        }

    }
}
