package com.maolabs.maobank.controller;

import com.maolabs.maobank.controller.dto.input.AceiteDtoInput;
import com.maolabs.maobank.controller.dto.input.PropostaInformacoesBasicasDtoInput;
import com.maolabs.maobank.controller.dto.input.PropostaInformacoesEnderecoDtoInput;
import com.maolabs.maobank.controller.dto.output.PropostaDtoOutput;
import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.model.exception.PropostaNaoLiberadaException;
import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import com.maolabs.maobank.service.PropostaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static com.maolabs.maobank.util.customvalidators.Utils.getURLWithContextPath;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/proposta", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/api/v1/proposta", tags = {"Proposta - PropostaController"})
public class PropostaController {

    private final PropostaService propostaService;

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @PostMapping
    @ApiOperation(value = "Proposta - Passo 01 - Informações básicas", notes = "Passo 01 - Cria uma proposta dadas informações básicas")
    public ResponseEntity primeiroPassoEnviaInformacoesBasicas(@Valid @RequestBody PropostaInformacoesBasicasDtoInput input, HttpServletRequest request) {

        Cliente cliente = input.build();

        try {
            Proposta propostaNova = propostaService.criarProposta(cliente);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION,
                    getURLWithContextPath(request)
                            .concat("/proposta/")
                            .concat(propostaNova.getId().toString())
                            .concat("/endereco"));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (TransicaoDeStatusNaoSuportadaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }

    }

    @PostMapping("/{propostaId}/endereco")
    @ApiOperation(value = "Proposta - Passo 02 - Dados de endereço", notes = "Passo 02 - Dá continuidade ao processo de proposta incluindo os dados de endereço")
    public ResponseEntity segundoPassoEnviaEndereco(@PathVariable("propostaId") Long propostaId, @RequestBody PropostaInformacoesEnderecoDtoInput input, HttpServletRequest request) {

        Optional<Proposta> propostaOptional = propostaService.findById(propostaId);
        if (!propostaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Proposta proposta = propostaOptional.get();
        try {
            propostaService.cadastrarEndereco(proposta, input.build());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION,
                    getURLWithContextPath(request)
                            .concat("/proposta/")
                            .concat(proposta.getId().toString())
                            .concat("/documento"));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (TransicaoDeStatusNaoSuportadaException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

    }

    @PostMapping("/{propostaId}/documento")
    @ApiOperation(value = "Proposta - Passo 03 - Upload de documento", notes = "Passo 03 - Dá continuidade ao processo de proposta incluindo a foto do CPF")
    public ResponseEntity terceiroPassoEnviaDocumento(@PathVariable("propostaId") Long propostaId, @RequestPart(value = "cpf", required = false) MultipartFile cpf, HttpServletRequest request) {

        Optional<Proposta> propostaOptional = propostaService.findById(propostaId);

        if (!propostaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Proposta proposta = propostaOptional.get();

        try {
            propostaService.cadastrarDocumento(proposta, cpf);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION,
                    getURLWithContextPath(request)
                            .concat("/proposta/")
                            .concat(proposta.getId().toString())
                            .concat("/aceite"));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (TransicaoDeStatusNaoSuportadaException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{propostaId}/aceite")
    @ApiOperation(value = "Proposta - Passo 04.01 - Resumo da Proposta para aceite", notes = "Passo 04.01 - Dá continuidade ao processo de proposta exibindo resumo da proposta")
    public ResponseEntity quartoPassoExibirDadosParaAceite(@PathVariable("propostaId") Long propostaId) {

        Optional<Proposta> propostaOptional = propostaService.findById(propostaId);

        if (propostaOptional.isPresent()) {
            Optional<Endereco> endereco = propostaService.encontrarEnderecoCliente(propostaOptional.get());
            return ResponseEntity.ok(new PropostaDtoOutput(propostaOptional.get(), endereco.get()));
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/{propostaId}/aceite")
    @ApiOperation(value = "Proposta - Passo 04.02 - Aceite do Cliente", notes = "Passo 04.02 - Dá continuidade ao processo de proposta por meio do aceite do cliente")
    public ResponseEntity quartoPassoExecutaAceite(@PathVariable("propostaId") Long propostaId, @RequestBody AceiteDtoInput aceiteDtoInput) throws TransicaoDeStatusNaoSuportadaException {

        Optional<Proposta> propostaOptional = propostaService.findById(propostaId);

        if (!propostaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Proposta proposta = propostaOptional.get();

        try {
            if (aceiteDtoInput.isAceita()) {
                proposta = propostaService.aceitarProposta(proposta);
                propostaService.enviarParaAnaliseLiberacao(proposta);
                return ResponseEntity.ok().build();
            }
            propostaService.rejeitar(proposta);
            return ResponseEntity.ok().build();

        } catch (TransicaoDeStatusNaoSuportadaException | Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        } catch (PropostaNaoLiberadaException e) {
            propostaService.naoLiberar(proposta);
            return ResponseEntity.ok().build();
        }
    }
}
