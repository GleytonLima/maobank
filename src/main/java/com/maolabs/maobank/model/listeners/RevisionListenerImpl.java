package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.CustomRevisionEntity;
import com.maolabs.maobank.util.customvalidators.WebUtils;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

@Component
public class RevisionListenerImpl implements RevisionListener {

    private final WebUtils webUtils;

    public RevisionListenerImpl(WebUtils webUtils) {
        this.webUtils = webUtils;
    }

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
        customRevisionEntity.setIp(webUtils.getClientIp());
    }
}