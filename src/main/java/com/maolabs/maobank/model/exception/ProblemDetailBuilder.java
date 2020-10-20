package com.maolabs.maobank.model.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.UUID;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@RequiredArgsConstructor
class ProblemDetailBuilder {
    private final Throwable throwable;

    ProblemDetails build() {
        ProblemDetails detail = new ProblemDetails();
        detail.setType(buildType());
        detail.setTitle(buildTitle());
        detail.setDetail(buildDetailMessage());
        detail.setStatus(buildStatus());
        detail.setInstance(buildInstance());
        return detail;
    }

    private URI buildType() {
        return URI.create(javadocName(throwable.getClass()) + ".html");
    }

    private static String javadocName(Class<?> type) {
        return type.getName()
                .replace('.', '/') // the package names are delimited like a path
                .replace('$', '.'); // nested classes are delimited with a period
    }

    private String buildTitle() {
        return camelToWords(throwable.getClass().getSimpleName());
    }

    private static String camelToWords(String input) {
        return String.join(" ", input.split("(?=\\p{javaUpperCase})"));
    }

    private String buildDetailMessage() {
        String errorDescription = throwable.getLocalizedMessage();
        if (errorDescription == null) errorDescription = throwable.toString();
        return errorDescription;
    }

    private int buildStatus() {
        Status status = throwable.getClass().getAnnotation(Status.class);
        if (status != null) {
            return status.value();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    private URI buildInstance() {
        return URI.create("urn:uuid:" + UUID.randomUUID());
    }

    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface Status {
        int value();
    }
}