package com.maolabs.maobank.repository;

import com.maolabs.maobank.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface MovimentacaoContaRepository extends PagingAndSortingRepository<Movimentacao, Long>, JpaSpecificationExecutor<Movimentacao> {
    @Query("SELECT SUM(CASE WHEN (m.tipoValorEnum = com.maolabs.maobank.model.MovimentacaoValorTipoEnum.CREDITO) THEN m.valor WHEN  (m.tipoValorEnum = com.maolabs.maobank.model.MovimentacaoValorTipoEnum.DEBITO)  THEN -m.valor ELSE 0 END) FROM Movimentacao m  WHERE m.contaDestino.id = :contaId\n")
    BigDecimal saldo(Long contaId);
}