package util;

import java.sql.Date;

public class Atividade {
    private int idAti;
    private int idManchaPT;
    private String designacao;
    private String tipo;
    private String tipoParticipantes;
    private int partMin;
    private int partMax;
    private Date dataReal;
    private String confirmacaoPT;


    public Atividade(int idAti, int idManchaPT, String designacao, String tipo, String tipoParticipantes,
                     int partMin, int partMax, Date dataReal, String confirmacaoPT) {
        this.idAti = idAti;
        this.idManchaPT = idManchaPT;
        this.designacao = designacao;
        this.tipo = tipo;
        this.tipoParticipantes = tipoParticipantes;
        this.partMin = partMin;
        this.partMax = partMax;
        this.dataReal = dataReal;
        this.confirmacaoPT = confirmacaoPT;
    }

    public int getIdAti() {
        return idAti;
    }

    public int getIdManchaPT() {
        return idManchaPT;
    }

    public String getDesignacao() {
        return designacao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTipoParticipantes() {
        return tipoParticipantes;
    }

    public int getPartMin() {
        return partMin;
    }

    public int getPartMax() {
        return partMax;
    }

    public Date getDataReal() {
        return dataReal;
    }

    public String getConfirmacaoPT() {
        return confirmacaoPT;
    }

    public void setIdAti(int idAti) {
        this.idAti = idAti;
    }

    public void setIdManchaPT(int idManchaPT) {
        this.idManchaPT = idManchaPT;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTipoParticipantes(String tipoParticipantes) {
        this.tipoParticipantes = tipoParticipantes;
    }

    public void setPartMin(int partMin) {
        this.partMin = partMin;
    }

    public void setPartMax(int partMax) {
        this.partMax = partMax;
    }

    public void setDataReal(Date dataReal) {
        this.dataReal = dataReal;
    }

    public void setConfirmacaoPT(String confirmacaoPT) {
        this.confirmacaoPT = confirmacaoPT;
    }

}
