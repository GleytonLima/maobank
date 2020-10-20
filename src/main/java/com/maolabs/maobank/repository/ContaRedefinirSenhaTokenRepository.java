package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.ContaRedefinirSenhaToken;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContaRedefinirSenhaTokenRepository extends PagingAndSortingRepository<ContaRedefinirSenhaToken, Long>, JpaSpecificationExecutor<ContaRedefinirSenhaToken> {
    Optional<ContaRedefinirSenhaToken> findByToken(String token);
}