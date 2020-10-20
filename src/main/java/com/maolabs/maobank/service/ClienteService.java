package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente save(Cliente entity) {
        return clienteRepository.save(entity);
    }

    public void update(Cliente entity) {
        clienteRepository.save(entity);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Page<Cliente> findAll(Specification<Cliente> spec, Pageable pageable) {
        return clienteRepository.findAll(spec, pageable);
    }

}