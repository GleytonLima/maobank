package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Documento;
import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.model.exception.PropostaNaoLiberadaException;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import com.maolabs.maobank.repository.ClienteRepository;
import com.maolabs.maobank.repository.DocumentoRepository;
import com.maolabs.maobank.repository.EnderecoRepository;
import com.maolabs.maobank.repository.PropostaRepository;
import com.maolabs.maobank.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Random;

@Service
public class PropostaService {
    private PropostaRepository propostaRepository;
    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;
    private DocumentoRepository documentoRepository;
    private StorageService storageService;

    private final String subFolder = "documentos/";

    public PropostaService(PropostaRepository propostaRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, DocumentoRepository documentoRepository, StorageService storageService) {
        this.propostaRepository = propostaRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.documentoRepository = documentoRepository;
        this.storageService = storageService;
    }

    public Optional<Proposta> findById(Long id) {
        return propostaRepository.findById(id);
    }

    @Transactional
    public Proposta aceitarProposta(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException {
        proposta.aceitar();
        return propostaRepository.save(proposta);
    }

    @Transactional
    public void enviarParaAnaliseLiberacao(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException, PropostaNaoLiberadaException {
        int contadorTentativas = 0;
        int numeroMaximoTentativasDeValidacao = 2;

        while (contadorTentativas < numeroMaximoTentativasDeValidacao) {
            try {
                int random = new Random().nextInt(100);
                if (random <= 90) {
                    proposta.liberar();
                    break;
                }
                throw new Exception("Falha na validacao");
            } catch (Exception e) {
                ++contadorTentativas;
            }

            if (contadorTentativas == 2) {
                throw new PropostaNaoLiberadaException("Erro ao liberar conta");
            }
        }

        propostaRepository.save(proposta);
    }

    @Transactional
    public void rejeitar(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException {
        proposta.rejeitar();
        propostaRepository.save(proposta);
    }

    public void naoLiberar(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException {
        proposta.naoLiberar();
        propostaRepository.save(proposta);
    }

    @Transactional
    public Proposta criarProposta(Cliente cliente) throws TransicaoDeStatusNaoSuportadaException {
        Optional<Proposta> propostaPreExistente = propostaRepository.findByClienteCpf(cliente.getCpf());

        if (propostaPreExistente.isPresent() && propostaPreExistente.get().propostaFinalizada()) {
            return propostaPreExistente.get();
        }

        Proposta propostaNova = new Proposta();
        propostaNova.iniciar();

        if (propostaPreExistente.isPresent()) {
            cliente.setId(propostaPreExistente.get().getCliente().getId());
            propostaNova = propostaPreExistente.get();
        }

        propostaNova.concluirDadosBasicos();
        Cliente clienteCriado = clienteRepository.save(cliente);
        propostaNova.setCliente(clienteCriado);

        return propostaRepository.save(propostaNova);
    }

    @Transactional
    public Endereco cadastrarEndereco(Proposta proposta, Endereco endereco) throws TransicaoDeStatusNaoSuportadaException {
        proposta.concluirDadosEndereco();
        endereco.setCliente(proposta.getCliente());

        Optional<Endereco> enderecoOptional = enderecoRepository.findById(proposta.getCliente().getId());
        if (enderecoOptional.isPresent()) {
            endereco.setId(enderecoOptional.get().getId());
        }

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public Proposta cadastrarDocumento(Proposta proposta, MultipartFile cpf) throws TransicaoDeStatusNaoSuportadaException {
        proposta.enviarParaAceite();
        String url = this.subFolder.concat(proposta.getId().toString());
        storageService.store(cpf, url, false);
        Documento documento = new Documento(url, proposta.getCliente());
        documentoRepository.save(documento);
        return propostaRepository.save(proposta);
    }

    public Optional<Endereco> encontrarEnderecoCliente(Proposta proposta) {
        return enderecoRepository.findById(proposta.getCliente().getId());
    }
}