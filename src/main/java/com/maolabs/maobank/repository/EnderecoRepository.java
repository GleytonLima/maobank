package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.Endereco;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnderecoRepository extends PagingAndSortingRepository<Endereco, Long>, JpaSpecificationExecutor<Endereco> {
}