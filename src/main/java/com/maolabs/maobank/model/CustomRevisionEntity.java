package com.maolabs.maobank.model;

import com.maolabs.maobank.model.listeners.RevisionListenerImpl;
import lombok.Data;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

@Entity(name = "AUDITORIA_dados_adicionais")
@Data
@RevisionEntity(RevisionListenerImpl.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {
    public String ip;
}