package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Conta;
import com.maolabs.maobank.model.ContaRedefinirSenhaToken;
import com.maolabs.maobank.model.Movimentacao;
import com.maolabs.maobank.model.TransferenciaRecebida;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.repository.ContaRedefinirSenhaTokenRepository;
import com.maolabs.maobank.repository.ContaRepository;
import com.maolabs.maobank.repository.MovimentacaoContaRepository;
import com.maolabs.maobank.repository.TransferenciaRecebidaRepository;
import com.maolabs.maobank.transferencia.TransferenciaRecebidaRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ContaService {
    private final ContaRedefinirSenhaTokenRepository contaRedefinirSenhaTokenRepository;
    private final ContaRepository contaRepository;

    private final PasswordEncoder passwordEncoder;

    private final MovimentacaoContaRepository movimentacaoContaRepository;

    private final TransferenciaRecebidaRepository transferenciaRecebidaRepository;

    public ContaService(ContaRepository contaRepository, ContaRedefinirSenhaTokenRepository contaRedefinirSenhaTokenRepository, PasswordEncoder passwordEncoder, MovimentacaoContaRepository movimentacaoContaRepository, TransferenciaRecebidaRepository transferenciaRecebidaRepository) {
        this.contaRepository = contaRepository;
        this.contaRedefinirSenhaTokenRepository = contaRedefinirSenhaTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.movimentacaoContaRepository = movimentacaoContaRepository;
        this.transferenciaRecebidaRepository = transferenciaRecebidaRepository;
    }

    public Optional<Conta> findById(Long id) {
        return contaRepository.findById(id);
    }

    @Transactional
    public ContaRedefinirSenhaToken criarTokenRedefinirSenhaParaConta(Conta conta) {
        ContaRedefinirSenhaToken token = new ContaRedefinirSenhaToken(conta);
        return contaRedefinirSenhaTokenRepository.save(token);
    }

    public Optional<ContaRedefinirSenhaToken> findContaRedefinirSenhaTokenByToken(String token) {
        return contaRedefinirSenhaTokenRepository.findByToken(token);
    }

    public void redefinirSenha(Conta conta, String senha) {
        conta.setSenha(senha, passwordEncoder);
        contaRepository.save(conta);
    }

    public Optional<Conta> findByNumeroContaAgencia(String agenciaDestino, String contaDestino) {
        return contaRepository.findByNumeroAgenciaAndNumeroConta(agenciaDestino, contaDestino);
    }

    public BigDecimal calcularSaldo(Conta conta) {
        return movimentacaoContaRepository.saldo(conta.getId());
    }

    @Transactional
    public Conta criarConta(Proposta proposta) {
        Conta conta = Conta.gerarNovaConta(proposta, passwordEncoder);
        return contaRepository.save(conta);
    }

    @Transactional
    public Optional<Movimentacao> criarMovimentacaoContaTransferenciaRecebida(TransferenciaRecebidaRecord transferencia) {
        Optional<Conta> contaOptional = findByNumeroContaAgencia(transferencia.getAgenciaDestino(), transferencia.getContaDestino());
        if (contaOptional.isPresent()) {
            Movimentacao movimentacao = transferencia.buildMovimentacaoConta(contaOptional.get());
            movimentacaoContaRepository.save(movimentacao);
            TransferenciaRecebida transferenciaRecebida = transferencia.buildTransferenciaRecebida(movimentacao);
            transferenciaRecebidaRepository.save(transferenciaRecebida);
            return Optional.of(movimentacao);
        }
        return Optional.empty();
    }
}