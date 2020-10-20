package com.maolabs.maobank.controller;

import com.maolabs.maobank.transferencia.TransferenciaProducer;
import com.maolabs.maobank.transferencia.TransferenciaRecebidaRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/transferencia", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/api/v1/transferencia", tags = {"Transferência - TransferenciaRecebidaController"})
public class TransferenciaRecebidaController {

    @Autowired
    private TransferenciaProducer transferenciaProducer;

    @PostMapping
    @ApiOperation(value = "Transferência - Gerar Transferência", notes = "Somente para demonstrar transferencia")
    public ResponseEntity transferir(@Valid @RequestBody TransferenciaRecebidaRecord transferenciaRecebidaRecord) {
        transferenciaProducer.criarTransferencia(transferenciaRecebidaRecord);
        return ResponseEntity.ok().build();
    }

}
