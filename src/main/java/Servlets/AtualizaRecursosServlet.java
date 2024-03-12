package Servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.RecursosReservados;

@WebServlet("/AtualizaRecursosServlet")
public class AtualizaRecursosServlet extends HttpServlet {

	/**
	 * Classe que após pedido GET retorna a lista de recursos disponiveis para a realização de uma reserva
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String manchaID = request.getParameter("manchaID");
		String dateReal = request.getParameter("dateReal");
		String tipo = request.getParameter("tipo");
		System.out.println("Tipo: " + tipo);
		System.out.println("Data: " + dateReal);
		int ptID = GestorSBD.getIDPT(username);
		int clubeID = 0;
		try {
			clubeID = GestorSBD.getPTClubeID(ptID);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Obter Listas com todas as salas e equipamentos do clube
		Map<String, HashMap<String, String>> salas = GestorSBD.getSala(clubeID);
		Map<String, HashMap<String, String>> equipamentos = GestorSBD.getEquipamentos(clubeID);

		// Conforme a opção usada, Semanal ou Data, vamos recolher a informação
		// associada a este tipo

		// No caso de semanal, precisamos usar o ID da mancha para obter a String com o
		// dia da semana
		String diaSemana = null;
		try {
			diaSemana = GestorSBD.diaSemanalMancha(Integer.parseInt(manchaID));
		} catch (NumberFormatException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Usando o objeto RecursosReservados conseguimos obter os recursos que se
		// encontram reservados,
		// ou no dia da semana caso seja semanal, ou na data caso seja uma única vez,
		// tal como ainda se as horas da mancha se sobrepõem às horas das reservas
		// efetuadas
		RecursosReservados recursosReservados = null;
		try {
			System.out.println(!dateReal.equals(""));
			if (tipo.equals("Data"))
				recursosReservados = GestorSBD.getRecursosReservados(Integer.parseInt(manchaID), dateReal, null);
			else
				recursosReservados = GestorSBD.getRecursosReservados(Integer.parseInt(manchaID), null, diaSemana);
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Obter a lista das salas e equipamentos que cumprem os critérios necessários
		List<Integer> salasReservadas = recursosReservados.getIdSalas();
		List<Integer> equipamentosReservados = recursosReservados.getIdEquipamentos();
		StringBuilder htmlContent = new StringBuilder();

		// Equipamento Div
		htmlContent.append("<div class=\"form-group\" id=\"equipamentoDiv\" style=\"display: block;\">");
		htmlContent.append("<label for=\"equipamento\">Equipamento:</label>");
		htmlContent.append("<table class=\"equipamento-table\">");
		htmlContent.append("<thead><tr><th>Equipamento</th><th>Adicionar/Remover</th></tr></thead>");
		htmlContent.append("<tbody>");

		Set<String> equipamentoKeys = equipamentos.keySet();
		Iterator<String> equipamentoIterator = equipamentoKeys.iterator();

		while (equipamentoIterator.hasNext()) {
			String equipamentoID = equipamentoIterator.next();
			Map<String, String> equipamentoInfo = equipamentos.get(equipamentoID);
			String equipamentoDesignacao = equipamentoInfo.get("designacao");
			boolean isReserved = equipamentosReservados.contains(Integer.parseInt(equipamentoID));

			if (!isReserved) {
				htmlContent.append("<tr id=\"").append(equipamentoID).append("\">");
				htmlContent.append("<td>").append(equipamentoDesignacao).append("</td>");
				htmlContent.append("<td>");
				htmlContent.append("<button type=\"button\" class=\"btn btn-secondary add-btn\"");
				htmlContent.append("onclick=\"toggleEquipamento('").append(equipamentoID)
						.append("')\">Adicionar</button>");
				htmlContent.append("</td>");
				htmlContent.append("</tr>");
			}
		}

		htmlContent.append("</tbody></table>");
		htmlContent.append(
				"<input type=\"hidden\" id=\"equipamentosReservadosInput\" name=\"equipamentosReservadosInput\" value=\"0\">");
		htmlContent.append("</div>");

		// Sala Div
		htmlContent.append("<div class=\"form-group\" id=\"salaDiv\" style=\"display: block;\">");
		htmlContent.append("<label for=\"sala\">Sala:</label>");
		htmlContent.append("<select class=\"form-control\" id=\"sala\" name=\"sala\">");

		Set<String> salaKeys = salas.keySet();
		Iterator<String> salaIterator = salaKeys.iterator();
		
		//Criação da lista de opções com as salas disponiveis
		while (salaIterator.hasNext()) {
			String salaID = salaIterator.next();
			Map<String, String> salaInfo = salas.get(salaID);
			String salaDesignacao = salaInfo.get("designacao");
			boolean isReserved = salasReservadas.contains(Integer.parseInt(salaID));

			if (!isReserved) {
				htmlContent.append("<option value=\"").append(salaID).append("\">").append(salaDesignacao)
						.append("</option>");
			}
		}

		htmlContent.append("</select>");
		htmlContent.append("</div>");
		String generatedHTML = htmlContent.toString();
		response.setContentType("text/html");
		response.getWriter().write(generatedHTML);

	}

}
