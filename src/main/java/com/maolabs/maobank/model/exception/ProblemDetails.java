package com.maolabs.maobank.model.exception;

import lombok.Data;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem Details for HTTP APIs
 * https://tools.ietf.org/html/rfc7807
 */
@Data
public class ProblemDetails {
    public static final MediaType JSON_MEDIA_TYPE = MediaType.valueOf("application/problem+json");
    private URI type;
    private String title;
    private String detail;
    private Integer status;
    private URI instance;
    private Map<String, Object> aditionalData = new HashMap<>();
}