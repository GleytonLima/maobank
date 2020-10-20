package com.maolabs.maobank.service;

import com.maolabs.maobank.config.EmailProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * Created by Olga on 7/15/2016.
 */
@Service("EmailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freeMarkerConfiguration;

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(Mensagem mensagem) {
        try {

            String texto = processarTemplate(mensagem);
            MimeMessage mimeMessage = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getRemetente());
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
            helper.setSubject(mensagem.getAssunto());
            helper.setText(texto, true);

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Não foi possível enviar e-mail", e);
        }
    }

    private String processarTemplate(Mensagem mensagem) {
        try {
            Template template = freeMarkerConfiguration.getTemplate(mensagem.getNomeArquivoTemplate());
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getModelos());
        } catch (IOException | TemplateException e) {
            throw new EmailException("Não foi possível processar template do e-mail", e);
        }
    }

}