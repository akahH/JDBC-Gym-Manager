package util;

import java.sql.Date;

public class Recomendacao {

    private int idRecomendacao;
    private int idPT;
    private int idCliente;
    private int idEquipamento;
    private String notas;
    private Date dataInicio;
    private Date dataFim;

    public Recomendacao(int idPT, int idCliente, int idEquipamento, String notas, Date dataInicio, Date dataFim) {
        this.idPT = idPT;
        this.idCliente = idCliente;
        this.idEquipamento = idEquipamento;
        this.notas = notas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getIdRecomendacao() {
        return idRecomendacao;
    }

    public void setIdRecomendacao(int idRecomendacao) {
        this.idRecomendacao = idRecomendacao;
    }

    public int getIdPT() {
        return idPT;
    }

    public void setIdPT(int idPT) {
        this.idPT = idPT;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(int idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}

