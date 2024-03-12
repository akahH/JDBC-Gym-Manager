package util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public class Data {

	
	/**
	 * M�todo que converte a string da designacao de um dia de semana para o enum DayOfWeek de java.time
	 * @param diaSemana
	 * @return
	 */
	public static DayOfWeek covertDiaSemanaToDayOfWeek(String diaSemana) {
	    switch (diaSemana.toLowerCase()) {
	        case "segunda-feira":
	            return DayOfWeek.MONDAY;
	        case "ter�a-feira":
	            return DayOfWeek.TUESDAY;
	        case "quarta-feira":
	            return DayOfWeek.WEDNESDAY;
	        case "quinta-feira":
	            return DayOfWeek.THURSDAY;
	        case "sexta-feira":
	            return DayOfWeek.FRIDAY;
	        case "s�bado":
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
			return "S�bado";
		case SUNDAY:
			return "Domingo";
		case THURSDAY:
			return "Quinta-Feira";
		case TUESDAY:
			return "Ter�a-Feira";
		case WEDNESDAY:
			return "Quarta-Feira";
		default:
			throw new IllegalArgumentException("Invalid day name: " + diaSemana);
		
		}
	}
	
	
	/**
	 * M�todo para verificar se a data de cria��o de uma mancha tem pelo menos 72h antes da sua realiza��o
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
     * M�todo para verificar se a data corresponde ao dia de semana pretendido
     * @param date
     * @param diaSemana
     * @return
     */
    public static boolean isDateDayOfWeek(Date date, DayOfWeek diaSemana) {

        LocalDate localDate = date.toLocalDate();
        return localDate.getDayOfWeek() == diaSemana;
    }
    
    /**
     * M�todo que retorna a idade atrav�s da data de nascimento
     * @param dataNasc
     * @return
     */
    public static int idade(LocalDate dataNasc) {
        return LocalDate.now().getYear() - dataNasc.getYear();
    }
    
    /**
     * M�todo que confirma se a idade � valida, usada para confirma��o de faixa et�ria
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
     * M�todo que confirma se existem pelo menos 48h quando o PT realiza a confirmacao dada uma data
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
     * M�todo que confirma se existem pelo menos 48h quando o PT realiza a confirmacao dado um dia de semana
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
     * M�todo que confirma se a data da recomendacao n�o � anterior � data atual
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
     * M�todo que confirma se a data de fim da recomenda��o � depois da data de inicio
     * @param date
     * @param date2
     * @return
     */
    public static boolean dataValidaRecomendacao(LocalDate date, LocalDate date2) {
        return date2.isBefore(date);
    }
    
    
	
}
