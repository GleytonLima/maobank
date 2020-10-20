package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.model.PropostaStatusEnum;
import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import com.maolabs.maobank.repository.ClienteRepository;
import com.maolabs.maobank.repository.DocumentoRepository;
import com.maolabs.maobank.repository.EnderecoRepository;
import com.maolabs.maobank.repository.PropostaRepository;
import com.maolabs.maobank.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PropostaServiceTest {

    @InjectMocks
    PropostaService propostaService;

    @Mock
    private PropostaRepository propostaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private DocumentoRepository documentoRepository;
    @Mock
    private StorageService storageService;

    Proposta proposta;
    Cliente cliente;
    Endereco endereco;

    @BeforeEach
    void setUp() {
        proposta = new Proposta();
        proposta.setId(1L);
        cliente = new Cliente();
        proposta.setCliente(cliente);
        endereco = new Endereco();
        endereco.setId(1L);
    }

    @Test
    void findById() {
        Mockito.when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        propostaService.findById(1L);
        verify(propostaRepository, times(1)).findById(1L);
    }

    @Test
    void criarProposta() throws TransicaoDeStatusNaoSuportadaException {
        Mockito.when(propostaRepository.save(ArgumentMatchers.any(Proposta.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(clienteRepository.save(ArgumentMatchers.any(Cliente.class))).then(AdditionalAnswers.returnsFirstArg());
        Proposta novaProposta = propostaService.criarProposta(cliente);
        verify(clienteRepository, times(1)).save(cliente);
        assertThat(novaProposta.getPropostaStatus(), equalTo(PropostaStatusEnum.DADOS_INICIAIS_INFORMADOS));
    }

    @Test
    void cadastrarEndereco() throws TransicaoDeStatusNaoSuportadaException {
        Mockito.when(enderecoRepository.save(ArgumentMatchers.any(Endereco.class))).then(AdditionalAnswers.returnsFirstArg());
        propostaService.cadastrarEndereco(proposta, endereco);
        verify(enderecoRepository, times(1)).save(endereco);
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.ENDERECO_INFORMADO));
    }

    @Test
    void cadastrarDocumento() throws TransicaoDeStatusNaoSuportadaException {
        Mockito.when(propostaRepository.save(ArgumentMatchers.any(Proposta.class))).then(AdditionalAnswers.returnsFirstArg());
        Proposta novaProposta = propostaService.cadastrarDocumento(proposta, new MockMultipartFile(
                "cpf",
                "cpf.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "<<cpf pdf data>>".getBytes(StandardCharsets.UTF_8)));
        verify(propostaRepository, times(1)).save(proposta);
        assertThat(novaProposta.getPropostaStatus(), equalTo(PropostaStatusEnum.AGUARDANDO_ACEITE));
    }

    @Test
    void aceitarProposta() throws TransicaoDeStatusNaoSuportadaException {
        Mockito.when(propostaRepository.save(ArgumentMatchers.any(Proposta.class))).then(AdditionalAnswers.returnsFirstArg());
        Proposta novaProposta = propostaService.aceitarProposta(proposta);
        verify(propostaRepository, times(1)).save(proposta);
        assertThat(novaProposta.getPropostaStatus(), equalTo(PropostaStatusEnum.ACEITE_POSITIVO));
    }

}