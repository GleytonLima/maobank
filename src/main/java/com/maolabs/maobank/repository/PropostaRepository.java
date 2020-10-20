package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.Proposta;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PropostaRepository extends PagingAndSortingRepository<Proposta, Long>, JpaSpecificationExecutor<Proposta> {
    Optional<Proposta> findByClienteCpf(String cpf);
}