package com.maolabs.maobank.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ClienteTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Cliente target = new Cliente();

    @Test
    public void clienteMenorDeDezoitoAnosDeveGerarRestrição() {

        LocalDate dataNascimento = LocalDate.now().minusYears(17);
        target.setDataNascimento(dataNascimento);

        Set<ConstraintViolation<Cliente>> constraintViolations = validator
                .validate(target);

        Optional<ConstraintViolation<Cliente>> violationOptional = constraintViolations.stream()
                .filter(constraintViolation ->
                        constraintViolation.getPropertyPath().toString().equals("dataNascimento")
                )
                .findFirst();

        assertThat("Menor de 18 anos deve gerar restrição",
                violationOptional.isPresent(), equalTo(true));
    }

    @Test
    public void clienteMaiorOuIgualDezoitoAnosNaoDeveGerarRestrição() {

        LocalDate dataNascimento = LocalDate.now().minusYears(18);
        target.setDataNascimento(dataNascimento);

        Set<ConstraintViolation<Cliente>> constraintViolations = validator
                .validate(target);

        Optional<ConstraintViolation<Cliente>> violationOptional = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getPropertyPath().toString().equals("dataNascimento"))
                .findFirst();

        assertThat("Cliente com idade maior ou igual a 18 anos não deve gerar restrição",
                violationOptional.isPresent(), equalTo(false));
    }

    @Test
    public void clienteComCPFInvalidoGeraRestricao() {

        target.setCpf("11111111111");

        Set<ConstraintViolation<Cliente>> constraintViolations = validator
                .validate(target);

        Optional<ConstraintViolation<Cliente>> violationOptional = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getPropertyPath().toString().equals("cpf"))
                .findFirst();

        assertThat("Cliente com CPF invalido deve gerar restrição",
                violationOptional.isPresent(), equalTo(true));

        target.setCpf("299.215.100-96");

        constraintViolations = validator
                .validate(target);

        violationOptional = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getPropertyPath().toString().equals("cpf"))
                .findFirst();

        assertThat("Cliente com CPF invalido deve gerar restrição",
                violationOptional.isPresent(), equalTo(true));

        target.setCpf("559473010");

        constraintViolations = validator
                .validate(target);

        violationOptional = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getPropertyPath().toString().equals("cpf"))
                .findFirst();

        assertThat("Cliente com CPF invalido deve gerar restrição",
                violationOptional.isPresent(), equalTo(true));
    }

    @Test
    public void clienteComCPFValidoNaoDeveGerarRestricao() {

        target.setCpf("00559473010");

        Set<ConstraintViolation<Cliente>> constraintViolations = validator
                .validate(target);

        Optional<ConstraintViolation<Cliente>> violationOptional = constraintViolations.stream()
                .filter(constraintViolation -> constraintViolation.getPropertyPath().toString().equals("cpf"))
                .findFirst();

        assertThat("Cliente com CPF valido nao deve gerar restrição",
                violationOptional.isPresent(), equalTo(false));
    }
}