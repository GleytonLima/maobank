package com.maolabs.maobank.model.events;

import com.maolabs.maobank.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEvent {

    private EmailService.Mensagem mensagem;

}