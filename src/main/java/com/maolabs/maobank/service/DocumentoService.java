package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Documento;
import com.maolabs.maobank.repository.DocumentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentoService {
    private DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    public Documento save(Documento entity) {
        return documentoRepository.save(entity);
    }

    public void update(Documento entity) {
        documentoRepository.save(entity);
    }

    public void delete(Long id) {
        documentoRepository.deleteById(id);
    }

    public Optional<Documento> findById(Long id) {
        return documentoRepository.findById(id);
    }

    public Page<Documento> findAll(Specification<Documento> spec, Pageable pageable) {
        return documentoRepository.findAll(spec, pageable);
    }
}