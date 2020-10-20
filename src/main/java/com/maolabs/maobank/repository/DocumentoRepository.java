package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.Documento;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DocumentoRepository extends PagingAndSortingRepository<Documento, Long>, JpaSpecificationExecutor<Documento> {
}