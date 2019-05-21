package Enums;

public enum TipoAgendamento {

    EXTERNO("EXTERNO"),
    INTERNO("INTERNO");


    TipoAgendamento(String descricao) {
        this.descricao = descricao;
    }

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }




}
