package util;

import java.util.List;

public class RecursosReservados {
	 private List<Integer> idSalas;
	    private List<Integer> idEquipamentos;

	    public RecursosReservados(List<Integer> idSalas, List<Integer> idEquipamentos) {
	        this.idSalas = idSalas;
	        this.idEquipamentos = idEquipamentos;
	    }

	    public List<Integer> getIdSalas() {
	        return idSalas;
	    }

	    public List<Integer> getIdEquipamentos() {
	        return idEquipamentos;
	    }
}
