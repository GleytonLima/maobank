package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.Conta;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContaRepository extends PagingAndSortingRepository<Conta, Long>, JpaSpecificationExecutor<Conta> {
    Optional<Conta> findByNumeroAgenciaAndNumeroConta(String agenciaDestino, String contaDestino);
}