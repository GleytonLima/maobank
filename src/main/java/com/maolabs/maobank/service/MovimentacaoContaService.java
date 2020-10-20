package com.maolabs.maobank.service;

import com.maolabs.maobank.model.Movimentacao;
import com.maolabs.maobank.repository.MovimentacaoContaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovimentacaoContaService {
    private MovimentacaoContaRepository movimentacaoContaRepository;

    public MovimentacaoContaService(MovimentacaoContaRepository movimentacaoContaRepository) {
        this.movimentacaoContaRepository = movimentacaoContaRepository;
    }

    public Movimentacao save(Movimentacao entity) {
        return movimentacaoContaRepository.save(entity);
    }

    public void update(Movimentacao entity) {
        movimentacaoContaRepository.save(entity);
    }

    public void delete(Long id) {
        movimentacaoContaRepository.deleteById(id);
    }

    public Optional<Movimentacao> findById(Long id) {
        return movimentacaoContaRepository.findById(id);
    }

    public Page<Movimentacao> findAll(Specification<Movimentacao> spec, Pageable pageable) {
        return movimentacaoContaRepository.findAll(spec, pageable);
    }
}