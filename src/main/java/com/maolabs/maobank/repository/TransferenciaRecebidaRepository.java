package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.TransferenciaRecebida;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransferenciaRecebidaRepository extends PagingAndSortingRepository<TransferenciaRecebida, Long>, JpaSpecificationExecutor<TransferenciaRecebida> {
}