package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.repository.EnderecoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {
    private EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco save(Endereco entity) {
        return enderecoRepository.save(entity);
    }

    public void update(Endereco entity) {
        enderecoRepository.save(entity);
    }

    public void delete(Long id) {
        enderecoRepository.deleteById(id);
    }

    public Optional<Endereco> findById(Long id) {
        return enderecoRepository.findById(id);
    }

    public Page<Endereco> findAll(Specification<Endereco> spec, Pageable pageable) {
        return enderecoRepository.findAll(spec, pageable);
    }

}