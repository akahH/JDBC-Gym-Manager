package util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public class Data {

	
	/**
	 * Método que converte a string da designacao de um dia de semana para o enum DayOfWeek de java.time
	 * @param diaSemana
	 * @return
	 */
	public static DayOfWeek covertDiaSemanaToDayOfWeek(String diaSemana) {
	    switch (diaSemana.toLowerCase()) {
	        case "segunda-feira":
	            return DayOfWeek.MONDAY;
	        case "terça-feira":
	            return DayOfWeek.TUESDAY;
	        case "quarta-feira":
	            return DayOfWeek.WEDNESDAY;
	        case "quinta-feira":
	            return DayOfWeek.THURSDAY;
	        case "sexta-feira":
	            return DayOfWeek.FRIDAY;
	        case "sábado":
	            return DayOfWeek.SATURDAY;
	        case "domingo":
	            return DayOfWeek.SUNDAY;
	        default:
	            throw new IllegalArgumentException("Invalid day name: " + diaSemana);
	    }
	}
	
	public static String covertDayOfWeekToDiaSemana(DayOfWeek diaSemana) {
		switch(diaSemana) {
		case FRIDAY:
			return "Sexta-Feira";
		case MONDAY:
			return "Segunda-Feira";
		case SATURDAY:
			return "Sábado";
		case SUNDAY:
			return "Domingo";
		case THURSDAY:
			return "Quinta-Feira";
		case TUESDAY:
			return "Terça-Feira";
		case WEDNESDAY:
			return "Quarta-Feira";
		default:
			throw new IllegalArgumentException("Invalid day name: " + diaSemana);
		
		}
	}
	
	
	/**
	 * Método para verificar se a data de criação de uma mancha tem pelo menos 72h antes da sua realização
	 * @param dataReal
	 * @return
	 */
    public static boolean isDateValid(Date dataReal) {
    	
    	System.out.println("dataReal: " + dataReal);
        LocalDate dataCurr = LocalDate.now();
        LocalDate dataAtiv = dataReal.toLocalDate();

        return (dataAtiv.compareTo(dataCurr.plusDays(3)) > 0);
    }
    
    /**
     * Método para verificar se a data corresponde ao dia de semana pretendido
     * @param date
     * @param diaSemana
     * @return
     */
    public static boolean isDateDayOfWeek(Date date, DayOfWeek diaSemana) {

        LocalDate localDate = date.toLocalDate();
        return localDate.getDayOfWeek() == diaSemana;
    }
    
    /**
     * Método que retorna a idade através da data de nascimento
     * @param dataNasc
     * @return
     */
    public static int idade(LocalDate dataNasc) {
        return LocalDate.now().getYear() - dataNasc.getYear();
    }
    
    /**
     * Método que confirma se a idade é valida, usada para confirmação de faixa etária
     * @param dataNasc
     * @param idadeMin
     * @param idadeMax
     * @return
     */
    public static boolean idadeValida(LocalDate dataNasc, int idadeMin, int idadeMax) {
        int idade = idade(dataNasc);
        return idade >= idadeMin && idade <= idadeMax;
    }
    
    /**
     * Método que confirma se existem pelo menos 48h quando o PT realiza a confirmacao dada uma data
     * @param dataAtiv
     * @return
     */
    public static boolean confirmacaoPTData(LocalDate dataAtiv) {
    	 LocalDateTime dataTimeAtiv = LocalDateTime.of(dataAtiv, LocalDateTime.now().toLocalTime());
    	 LocalDateTime currentDateTime = LocalDateTime.now();
    	 Duration duration = Duration.between(currentDateTime, dataTimeAtiv);
         long hoursDifference = Math.abs(duration.toHours());
         //System.out.println(hoursDifference);
    	 return hoursDifference >= 48;
    }
    
    /**
     * Método que confirma se existem pelo menos 48h quando o PT realiza a confirmacao dado um dia de semana
     * @param dataAtiv
     * @return
     */
    public static boolean confirmacaoPTDiaSemana(DayOfWeek dataAtiv) {
   	 
   	LocalDateTime currentDateTime = LocalDateTime.now();
   	//System.out.println(currentDateTime);
  
   	LocalDateTime proximaSemAtiv = currentDateTime.with(dataAtiv);
 	//System.out.println(proximaSemAtiv);
   	 if (currentDateTime.getDayOfWeek().getValue() > dataAtiv.getValue()) {
   		proximaSemAtiv = proximaSemAtiv.plusWeeks(1);
     }
   	 Duration duration = Duration.between(currentDateTime, proximaSemAtiv);
     long hoursDifference = Math.abs(duration.toHours());
     //System.out.println(hoursDifference);
   	 return hoursDifference >= 48;
   }
    
    /**
     * Método que confirma se a data da recomendacao não é anterior à data atual
     * @param date
     * @return
     */
    public static boolean dataValidaRecomendacao(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate.toString());
        System.out.println(date.toString());
        return date.isBefore(currentDate);
    }
    
    /**
     * Método que confirma se a data de fim da recomendação é depois da data de inicio
     * @param date
     * @param date2
     * @return
     */
    public static boolean dataValidaRecomendacao(LocalDate date, LocalDate date2) {
        return date2.isBefore(date);
    }
    
    
	
}
