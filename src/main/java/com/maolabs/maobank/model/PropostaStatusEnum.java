package com.maolabs.maobank.model;

import java.util.Arrays;
import java.util.List;

public enum PropostaStatusEnum {
    NOVA("Nova Proposta"),
    DADOS_INICIAIS_INFORMADOS("Dados Iniciais Informados", NOVA),
    ENDERECO_INFORMADO("Endereço Informado", DADOS_INICIAIS_INFORMADOS),
    AGUARDANDO_ACEITE("Aguardando aceite do cliente", ENDERECO_INFORMADO),
    ACEITE_POSITIVO("Aceite Posititvo", AGUARDANDO_ACEITE),
    ACEITE_NEGATIVO("Aceite Negativo", AGUARDANDO_ACEITE),
    LIBERADA("Proposta Liberada", ACEITE_POSITIVO),
    NAO_LIBERADA("Proposta não liberada", ACEITE_POSITIVO);

    private String descricao;
    private List<PropostaStatusEnum> statusAnteriores;

    PropostaStatusEnum(String descricao, PropostaStatusEnum... statusAnteriores) {
        this.descricao = descricao;
        this.statusAnteriores = Arrays.asList(statusAnteriores);
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean naoPodeAlterarPara(PropostaStatusEnum novoStatus) {
        if (novoStatus == this) {
            return false;
        }
        return !novoStatus.statusAnteriores.contains(this);
    }

    public boolean finalizada() {
        return (this.equals(ACEITE_NEGATIVO) ||
                this.equals(ACEITE_POSITIVO) ||
                this.equals(LIBERADA) ||
                this.equals(NAO_LIBERADA));
    }
}
