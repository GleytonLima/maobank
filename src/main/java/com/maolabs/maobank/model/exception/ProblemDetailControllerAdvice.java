package com.maolabs.maobank.model.exception;

import com.maolabs.maobank.controller.dto.validacao.ErroFormularioDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ProblemDetailControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity toProblemDetail(Throwable throwable) {

        ProblemDetails detail = new ProblemDetailBuilder(throwable).build();

        log.debug(detail.toString(), throwable);

        return ResponseEntity.status(detail.getStatus())
                .contentType(ProblemDetails.JSON_MEDIA_TYPE)
                .body(detail);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handle(MethodArgumentNotValidException expection) {

        ProblemDetails detail = new ProblemDetailBuilder(expection).build();

        List<ErroFormularioDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = expection.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            String mensagem = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            ErroFormularioDto erro = new ErroFormularioDto(fieldError.getField(), mensagem);
            dto.add(erro);
        }

        if (!dto.isEmpty()) {
            detail.setAditionalData(Map.of("camposComErro", dto));
            detail.setTitle("Erro ao validar campos");
        }

        return ResponseEntity.status(detail.getStatus())
                .contentType(ProblemDetails.JSON_MEDIA_TYPE)
                .body(detail);

    }

}