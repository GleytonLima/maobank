package com.maolabs.maobank.util.customvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MaiorDezoitoAnosValidator implements
        ConstraintValidator<MaiorDezoitoAnosContraint, LocalDate> {

    @Override
    public void initialize(MaiorDezoitoAnosContraint cpf) {
    }

    @Override
    public boolean isValid(LocalDate dataNascimento,
                           ConstraintValidatorContext cxt) {
        if ((dataNascimento != null)) {
            return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
        } else {
            return false;
        }
    }

}