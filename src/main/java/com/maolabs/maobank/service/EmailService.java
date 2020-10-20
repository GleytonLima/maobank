package com.maolabs.maobank.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Map;
import java.util.Set;

/**
 * Fonte: https://github.com/eugenp/tutorials/blob/master/spring-mvc-basics-2/src/main/java/com/baeldung/spring/mail/EmailService.java
 * Created by Olga on 8/22/2016.
 */
public interface EmailService {
    void sendSimpleMessage(Mensagem mensagem);

    @Getter
    @Builder
    class Mensagem {

        @Singular
        private Set<String> destinatarios;
        @NonNull
        private String assunto;
        @NonNull
        private String nomeArquivoTemplate;
        @Singular
        private Map<String, Object> modelos;
    }
}