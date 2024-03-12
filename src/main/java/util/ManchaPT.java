package util;


public class ManchaPT {

    private int IDPT;
    private int manchaID;
    private String diaSemana;
    private String horaInicio;
    private String horaFim;
    private int idadeMax;
    private int idadeMin;


    public ManchaPT() {
       
    }

    public ManchaPT(int manchaID, int IDPT, String diaSemana, String horaInicio, String horaFim, int idadeMax, int idadeMin) {
    	this.manchaID = manchaID;
        this.IDPT = IDPT;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.idadeMax = idadeMax;
        this.idadeMin = idadeMin;
    }
    
    public ManchaPT(int IDPT, String diaSemana, String horaInicio, String horaFim, int idadeMax, int idadeMin) {
        this.IDPT = IDPT;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.idadeMax = idadeMax;
        this.idadeMin = idadeMin;
    }
    
    public int getManchaID() {
        return manchaID;
    }

    public void setManchaID(int manchaID) {
        this.manchaID = manchaID;
    }

    public int getIDPT() {
        return IDPT;
    }

    public void setIDPT(int IDPT) {
        this.IDPT = IDPT;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public int getIdadeMax() {
        return idadeMax;
    }

    public void setIdadeMax(int idadeMax) {
        this.idadeMax = idadeMax;
    }

    public int getIdadeMin() {
        return idadeMin;
    }

    public void setIdadeMin(int idadeMin) {
        this.idadeMin = idadeMin;
    }
}