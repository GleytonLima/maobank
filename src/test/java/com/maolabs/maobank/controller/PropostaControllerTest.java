package com.maolabs.maobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maolabs.maobank.controller.dto.input.AceiteDtoInput;
import com.maolabs.maobank.controller.dto.input.PropostaInformacoesBasicasDtoInput;
import com.maolabs.maobank.controller.dto.input.PropostaInformacoesEnderecoDtoInput;
import com.maolabs.maobank.controller.dto.output.PropostaDtoOutput;
import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import com.maolabs.maobank.service.PropostaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PropostaControllerTest {

    @InjectMocks
    PropostaController propostaController;

    @Mock
    private PropostaService propostaService;

    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    Proposta proposta;
    Endereco endereco;
    Cliente cliente;

    @BeforeEach
    void setUp() throws TransicaoDeStatusNaoSuportadaException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(propostaController).build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setCpf("11111111111");
        proposta = new Proposta();
        proposta.setId(1L);
        proposta.setCliente(cliente);
        proposta.iniciar();
        endereco = new Endereco();
        endereco.setId(1L);
    }

    @Test
    void primeiroPassoEnviaInformacoesBasicas() throws Exception, TransicaoDeStatusNaoSuportadaException {

        Mockito.when(propostaService.criarProposta(ArgumentMatchers.any(Cliente.class))).thenReturn(proposta);

        var input = PropostaInformacoesBasicasDtoInput.builder()
                .cpf("18824227244")
                .dataNascimento(LocalDate.now().minusYears(20))
                .email("papercut@user.com")
                .nome("Gleyton")
                .sobrenome("Lima")
                .build();

        mockMvc.perform(post("/api/v1/proposta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(redirectedUrl("http://localhost:80/api/v1/proposta/1/endereco"));
    }

    @Test
    void segundoPassoEnviaEndereco() throws Exception {

        Mockito.when(propostaService.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(proposta));

        var input = PropostaInformacoesEnderecoDtoInput.builder()
                .bairro("Brooklin")
                .cep("04581-060")
                .cidade("São Paulo")
                .complemento("3º e 4º andar")
                .estado("São Paulo")
                .rua("Rua Pascal Pais, 525")
                .build();

        mockMvc.perform(post("/api/v1/proposta/1/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(redirectedUrl("http://localhost:80/api/v1/proposta/1/documento"));
    }

    @Test
    void terceiroPassoEnviaDocumento() throws Exception {
        Mockito.when(propostaService.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(proposta));

        MockMultipartFile file =
                new MockMultipartFile(
                        "cpf",
                        "cpf.pdf",
                        MediaType.APPLICATION_PDF_VALUE,
                        "<<cpf pdf data>>".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/proposta/1/documento")
                .file(file))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(redirectedUrl("http://localhost:80/api/v1/proposta/1/aceite"));
    }

    @Test
    void quartoPassoExibirDadosParaAceite() throws Exception {
        Mockito.when(propostaService.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(proposta));
        Mockito.when(propostaService.encontrarEnderecoCliente(ArgumentMatchers.any(Proposta.class))).thenReturn(Optional.of(endereco));

        mockMvc.perform(get("/api/v1/proposta/1/aceite")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new PropostaDtoOutput(proposta, endereco))));
    }

    @Test
    void quartoPassoExecutaAceite() throws Exception {
        Mockito.when(propostaService.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(proposta));

        var input = AceiteDtoInput.builder()
                .aceita(true)
                .build();

        mockMvc.perform(post("/api/v1/proposta/1/aceite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}