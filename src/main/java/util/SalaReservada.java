package util;

public class SalaReservada {

    private final int idSala;
    private String horaInicio;
    private String horaFim;
    private String data;
    private String diaSemana;
    private int idAti;

    public SalaReservada( int idSala, int idAti, String diaSemana, String data, String horaInicio, String horaFim ) {
        this.idSala = idSala;
        this.idAti = idAti;
        this.data = data;
        this.horaInicio =  horaInicio;
        this.horaFim =  horaFim;
        this.diaSemana =  diaSemana;

    }

    public int getIdSala() {
        return idSala;
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

    public int getIdAti() {
        return idAti;
    }

    public void setIdAti(int idAti) {
        this.idAti = idAti;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }
    
    public void setDiaSemana(String diaSemana) {
    	this.diaSemana = diaSemana;
    }
    
    public String getDiaSemana() {
    	return diaSemana;
    }
}
