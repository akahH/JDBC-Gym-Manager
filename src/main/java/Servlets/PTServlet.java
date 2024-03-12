package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import util.Cliente;
import util.Data;
import util.ManchaPT;
import util.Recomendacao;

@WebServlet("/PTServlet")
@MultipartConfig(maxFileSize = 4294967296L)
public class PTServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static GestorSBD gestor = new GestorSBD();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String flag = request.getParameter("flag");
		switch (flag) {
		case "login":
			handleLogin(request, response);
			break;
		case "adicionarManchaPT":
			try {
				adicionarManchaPT(request, response);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "criarAtividade":
			criarAtividade(request, response);
			break;
		case "gerirAtividade":
			gerirAtividade(request, response);
			break;
		case "adicionarClienteAtividade":
			adicionarClienteAtividade(request, response);
			break;
		case "processCriarAtividade":
			
			try {
				processCriarAtividade(request, response);
			} catch (IOException | SQLException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "apagarMancha":
			try {
				apagarMancha(request, response);
			} catch (NumberFormatException | SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "confirmarAtividade":
			confirmarAtividade(request, response);
			break;
		case "cancelarAtividade":
			try {
				cancelarAtividade(request, response);
			} catch (NumberFormatException | IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
		case "criarRecomendacao":
			try {
				criarRecomendacao(request, response);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			PrintWriter out = response.getWriter();
			out.println("<html><body><p>Error: Invalid 'flag' parameter value.</p></body></html>");
			break;
		}
	}



	private void apagarMancha(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, SQLException, IOException {
		String manchaID =  request.getParameter("manchaID");
		GestorSBD.apagarManchaPT(Integer.parseInt(manchaID));
		response.sendRedirect("ManchasPT.jsp");
		
		
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String flag = request.getParameter("flag");

		if ("logout".equals(flag))
			handleLogout(request, response);

	}

	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

		switch (role) {
		case "PT":
			System.out.println("Entra");
			System.out.println(username);
			System.out.println(username);

			if (gestor.loginPT(username, password)) {

				HttpSession session = request.getSession(true);
				session.setAttribute("username", username);
				response.sendRedirect(request.getContextPath() + "/PT.jsp");

			} else
				response.sendRedirect("LogIn.jsp");
			break;
		case "Gerente":
			response.sendRedirect("LogIn.jsp");
			break;
		case "Cliente":
			response.sendRedirect("LogIn.jsp");
		}

	}

	private void adicionarManchaPT(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		int idPT = GestorSBD.getIDPT(username);
		String encodedDiaSemana = request.getParameter("encodedDiaSemana");
		encodedDiaSemana =	java.net.URLDecoder.decode(encodedDiaSemana, "UTF-8");
		System.out.println(encodedDiaSemana);
		String horaInicio = request.getParameter("horaInicio");
		String horaFim = request.getParameter("horaFim");
		int idadeMin = Integer.parseInt(request.getParameter("idadeMin"));
		int idadeMax = Integer.parseInt(request.getParameter("idadeMax"));
		int clubeID = GestorSBD.getPTClubeID(idPT);
		
		ManchaPT mancha = new ManchaPT(idPT, encodedDiaSemana, horaInicio, horaFim,idadeMax , idadeMin);

		if (gestor.adicionarManchaPT(mancha, clubeID))
			response.sendRedirect(request.getContextPath() + "/ManchasPT.jsp");
		else
			response.sendRedirect(request.getContextPath() + "/ManchasPT.jsp?error=true");
	}

	
	private void criarAtividade(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
			String manchaID = request.getParameter("manchaID");
			response.sendRedirect(request.getContextPath() + "/CriarAtividade.jsp?manchaID=" + manchaID);

	}
	
	private void gerirAtividade(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
			String manchaID = request.getParameter("manchaID");
			response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?manchaID=" + manchaID);

	}

	
	
	private void processCriarAtividade(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, ParseException {

		String encodedDesignacao = request.getParameter("encodedDesignacao");
		encodedDesignacao =	java.net.URLDecoder.decode(encodedDesignacao, "UTF-8");
		String tipo = request.getParameter("tipo");
		String tipoParticipantes = request.getParameter("tipoParticipantes");
		String manchaID =  request.getParameter("manchaID");
		String dataAtiv = null;
		String equipamentos = (String) request.getParameter("equipamentosReservadosInput");
		
		List<Integer> equipamentosReservados = new ArrayList<>();
		if(equipamentos != null) {
			String[] equipamenosArray = equipamentos.split(",");
			for(String equipamento : equipamenosArray) {
				System.out.println(Arrays.toString(equipamenosArray));
				System.out.println("Teste" + equipamenosArray[0]);
				System.out.println("Equipamento: " + equipamento);
				equipamentosReservados.add(Integer.parseInt(equipamento));
			}
		}
		int idSala = 0;
		//verifica se o parametro sala foi preenchido
		if(request.getParameter("sala") != null) {
	       idSala = Integer.parseInt(request.getParameter("sala"));
		}
		//verifica o tipo de data da atividade, se é semanal ou numa data definida
		if (tipo.equals("Data")) {
			String dataInput = request.getParameter("dataReal");
			Date dataReal = java.sql.Date.valueOf(dataInput);
			//usa método já criados para determinar se a data é válida e corresponde ao dia de semana para o qual se está a criar a mancha
			if(Data.isDateValid(dataReal) && Data.isDateDayOfWeek(dataReal, GestorSBD.getDayOfWeekMancha(Integer.parseInt(manchaID)))) {
				dataAtiv = dataInput;
				//verificar o tipo de participantes da atividade antes de enviar o pedido para a BD
				if(tipoParticipantes.equals("Individual")) {
					GestorSBD.criarAtividade(Integer.parseInt(manchaID), encodedDesignacao, tipo, tipoParticipantes, 1, 1, dataAtiv, "Pendente");
				}
				if(tipoParticipantes.equals("Grupo")) {
					int partMin = Integer.parseInt(request.getParameter("partMin"));
					int partMax = Integer.parseInt(request.getParameter("partMax"));
					GestorSBD.criarAtividade(Integer.parseInt(manchaID), encodedDesignacao, tipo, tipoParticipantes, partMin, partMax, dataAtiv, "Pendente");
				}
				
				response.sendRedirect(request.getContextPath() + "/ManchasPT.jsp");
				
			}
				
			else
				response.sendRedirect(request.getContextPath() + "/CriarAtividade.jsp?error=true&manchaID=" + manchaID);
		}
		
		
		if(tipo.equals("Semanal")) {

			if(GestorSBD.tipoSemanalValido(Integer.parseInt(manchaID))) {
				if(tipoParticipantes.equals("Individual")) {
					GestorSBD.criarAtividade(Integer.parseInt(manchaID), encodedDesignacao, tipo, tipoParticipantes, 1, 1, dataAtiv, "Pendente");
				}
				if(tipoParticipantes.equals("Grupo")) {
					int partMin = Integer.parseInt(request.getParameter("partMin"));
					int partMax = Integer.parseInt(request.getParameter("partMax"));
					GestorSBD.criarAtividade(Integer.parseInt(manchaID), encodedDesignacao, tipo, tipoParticipantes, partMin, partMax, dataAtiv, "Pendente");
				}
				
				response.sendRedirect(request.getContextPath() + "/ManchasPT.jsp");
			}
			else
				response.sendRedirect(request.getContextPath() + "/CriarAtividade.jsp?error=true&manchaID=" + manchaID);
		}
		if(equipamentosReservados != null || idSala != 0) {
			int idAtividade = GestorSBD.getAtividadeID(Integer.parseInt(manchaID));
			//confirmar novamente que tipo de data estamos a usar para a Reserva neste caso
			if(tipo.equals("Data"))
				GestorSBD.criarReserva(idAtividade, equipamentosReservados, idSala,true);
			else
				GestorSBD.criarReserva(idAtividade, equipamentosReservados, idSala,false);
		}


	}
	
	private void adicionarClienteAtividade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String clienteUsername = request.getParameter("clienteUsername");
		String manchaID =  request.getParameter("manchaID");
		if(GestorSBD.addClienteToAtividade(clienteUsername, Integer.parseInt(manchaID)))
			response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?manchaID=" + manchaID);
		else
			response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?error=true&manchaID=" + manchaID);
				
	}
	
	private void confirmarAtividade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String atividadeID = request.getParameter("atividadeID");
		String manchaID =  request.getParameter("manchaID");
		System.out.println("atividadeID: " + atividadeID);
		if(GestorSBD.confirmarAtividade(Integer.parseInt(atividadeID)))
			response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?manchaID=" + manchaID);
		else
			response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?error=true&manchaID=" + manchaID);
			
		
	}
	
	private void cancelarAtividade(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException, SQLException {
		String atividadeID = request.getParameter("atividadeID");
		String manchaID =  request.getParameter("manchaID");
		System.out.println("atividadeID: " + atividadeID);
		GestorSBD.cancelarAtividade(Integer.parseInt(atividadeID));
		response.sendRedirect(request.getContextPath() + "/AtividadePT.jsp?manchaID=" + manchaID);
	}
	
	private void criarRecomendacao(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		HttpSession session = request.getSession();
		int idPT = GestorSBD.getIDPT((String) session.getAttribute("username"));
		int idCliente = Integer.parseInt(request.getParameter("idCliente")) ;
		int idEquipamento = Integer.parseInt(request.getParameter("idEquipamento"));
		String notas = request.getParameter("notas");
		String encodedNotas= request.getParameter("encodedNotas");
		encodedNotas =	java.net.URLDecoder.decode(encodedNotas, "UTF-8");
		Date dataInicio = Date.valueOf(request.getParameter("dataInicio"));
		Date dataFim = Date.valueOf(request.getParameter("dataFim"));
		Recomendacao recomendacao = new Recomendacao(idPT, idCliente, idEquipamento, notas, dataInicio, dataFim);
		//validar a data da recomendação
		if(!Data.dataValidaRecomendacao(dataInicio.toLocalDate()) && Data.dataValidaRecomendacao(dataFim.toLocalDate(), dataInicio.toLocalDate())) {
			GestorSBD.createRecomendacao(recomendacao);
			response.sendRedirect(request.getContextPath() + "/GerirClientesPT.jsp");
		}
		else
			response.sendRedirect(request.getContextPath() + "/GerirClientesPT.jsp?error=true");

	}

	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("index.html");
	}

}
