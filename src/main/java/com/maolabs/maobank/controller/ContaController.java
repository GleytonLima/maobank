package com.maolabs.maobank.controller;

import com.maolabs.maobank.controller.dto.input.ContaRedefinirSenhaDtoInput;
import com.maolabs.maobank.model.Conta;
import com.maolabs.maobank.model.ContaRedefinirSenhaToken;
import com.maolabs.maobank.service.ContaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/conta", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/api/v1/conta", tags = {"Conta - ContaController"})
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("{contaId}/definir-senha/{token}")
    @ApiOperation(value = "Conta - Checar Status Token", notes = "Verifica status do token")
    public ResponseEntity verificaStatusProposta(@PathVariable(value = "contaId") Long contaId, @PathVariable(value = "token") String token) {

        ResponseEntity responseErro = getResponseEntity(contaId, token);
        if (responseErro != null) return responseErro;

        return ResponseEntity.ok().build();

    }

    @PostMapping("{contaId}/definir-senha/{token}")
    @ApiOperation(value = "Conta - Redefinir Senha", notes = "Conta - Redefinir Senha")
    public ResponseEntity redefinirSenha(@PathVariable(value = "contaId") Long contaId, @PathVariable(value = "token") String token, @RequestBody ContaRedefinirSenhaDtoInput input) {

        ResponseEntity reponseErro = getResponseEntity(contaId, token);
        if (reponseErro != null) return reponseErro;


        Optional<Conta> contaOptional = contaService.findById(contaId);

        contaService.redefinirSenha(contaOptional.get(), input.getSenha());

        return ResponseEntity.ok().build();
    }

    @PostMapping("{contaId}/checar-senha")
    @ApiOperation(value = "Conta - Checar Senha", notes = "Conta - Checar Senha - Somente para demonstração")
    public ResponseEntity checarSenha(@PathVariable(value = "contaId") Long contaId, @RequestBody ContaRedefinirSenhaDtoInput input) {

        Optional<Conta> contaOptional = contaService.findById(contaId);

        Conta conta = contaOptional.get();

        boolean matches = passwordEncoder.matches(input.getSenha(), conta.getSenha());

        return ResponseEntity.ok().body(matches);
    }

    @PostMapping("{contaId}/saldo")
    @ApiOperation(value = "Conta - Checar Saldo", notes = "Conta - Checar Saldo")
    public ResponseEntity consultaSaldo(@PathVariable(value = "contaId") Long contaId, @RequestBody ContaRedefinirSenhaDtoInput input) {

        Optional<Conta> contaOptional = contaService.findById(contaId);

        Conta conta = contaOptional.get();

        boolean matches = passwordEncoder.matches(input.getSenha(), conta.getSenha());

        if(matches) {
            BigDecimal saldo = contaService.calcularSaldo(conta);

            return ResponseEntity.ok().body(saldo);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private ResponseEntity getResponseEntity(Long contaId, String token) {
        Optional<Conta> contaOptional = contaService.findById(contaId);
        Optional<ContaRedefinirSenhaToken> contaToken = contaService.findContaRedefinirSenhaTokenByToken(token);

        if (!contaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!contaToken.isPresent() || contaToken.get().isTokenExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return null;
    }


}
