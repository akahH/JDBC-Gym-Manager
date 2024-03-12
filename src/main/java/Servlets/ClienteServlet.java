package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import util.Cliente;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static GestorSBD gestor = new GestorSBD();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		// Flag que gere as operações do Servlet, recebida em forms
		String flag = request.getParameter("flag");
		System.out.println("Flag: " + flag);
		switch (flag) {
		case "login":
			handleLogin(request, response);
			break;
		case "criarConta":
			criarCliente(request, response);
			break;
		case "criarPatologia":
			try {
				criarPatologia(request, response);
			} catch (ServletException | IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "criarObjetivo":
			try {
				criarObjetivo(request, response);
			} catch (ServletException | IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "apagarPatologia":
			try {
				apagarPatologia(request, response);
			} catch (ServletException | IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "apagarObjetivo":
			try {
				apagarObjetivo(request, response);
			} catch (ServletException | IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "atualizarPerfil":
			try {
				atualizarPerfilCliente(request, response);
			} catch (ServletException | IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
		case "aderirAtividade":
			try {
				aderirAtividade(request, response);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "cancelarInscricao":
			try {
				cancelarInscricaoAtividade(request, response);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "consultarAtividade":
			consultarAtividade(request, response);
			break;
		default:
			PrintWriter out = response.getWriter();
			out.println("<html><body><p>Error: Invalid 'flag' parameter value.</p></body></html>");
			break;
		}
	}

	/**
	 * Método para o cliente realizar o login
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

		switch (role) {
		case "Cliente":
			System.out.println("Entra");
			System.out.println(username);
			System.out.println(username);

			if (gestor.loginCliente(username, password)) {

				HttpSession session = request.getSession(true);
				session.setAttribute("username", username);
				response.sendRedirect(request.getContextPath() + "/Cliente.jsp");

			} else
				response.sendRedirect("LogIn.jsp");
			break;
		case "Gerente":
			response.sendRedirect("LogIn.jsp");
			break;
		case "PT":
			response.sendRedirect("LogIn.jsp");
		}

	}

	/**
	 * Método para procesar o pedido de criar um cliente usando os parametros
	 * recebidos da pagina JSP e métodos de GestorSBD
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void criarCliente(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nif = request.getParameter("nif");
		String nome = request.getParameter("nome");
		String apelido = request.getParameter("apelido");
		String email = request.getParameter("email");
		String telemovel = request.getParameter("telemovel");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String dataInput = request.getParameter("dataNasc");
		int idPT = Integer.parseInt(request.getParameter("idPT"));

		try {
			if (GestorSBD.criarCliente(nif, nome, apelido, dataInput, email, telemovel, username, password, idPT))
				response.sendRedirect("LogIn.jsp");
			else
				response.sendRedirect("CriarCliente.jsp?error=true");
		} catch (NumberFormatException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método para processar o pedido de criar uma patologia
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void criarPatologia(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String descricao = request.getParameter("patologiaDescricao");
		int clienteID = Integer.parseInt(request.getParameter("clienteID"));
		descricao = java.net.URLDecoder.decode(descricao, "UTF-8");
		GestorSBD.criarPatologia(clienteID, descricao);
		response.sendRedirect("AtualizarPerfilCliente.jsp");
	}

	/**
	 * Método para processar o pedido de criar um objetivo
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void criarObjetivo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String descricao = request.getParameter("objetivoDescricao");
		descricao = java.net.URLDecoder.decode(descricao, "UTF-8");
		int clienteID = Integer.parseInt(request.getParameter("clienteID"));
		GestorSBD.criarObjetivo(clienteID, descricao);
		response.sendRedirect("AtualizarPerfilCliente.jsp");
	}
	
	private void apagarPatologia(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String descricao = request.getParameter("descricaoPatologiaTabela");
		int clienteID = Integer.parseInt(request.getParameter("clienteID"));
		descricao = java.net.URLDecoder.decode(descricao, "UTF-8");
		//System.out.println(descricao);
		GestorSBD.apagarPatologia(clienteID, descricao);
		response.sendRedirect("AtualizarPerfilCliente.jsp");
	}

	private void apagarObjetivo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String descricao = request.getParameter("descricaoObjetivoTabela");
		descricao = java.net.URLDecoder.decode(descricao, "UTF-8");
		int clienteID = Integer.parseInt(request.getParameter("clienteID"));
		GestorSBD.apagarObjetivo(clienteID, descricao);
		response.sendRedirect("AtualizarPerfilCliente.jsp");
	}

	private void atualizarPerfilCliente(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String nome = request.getParameter("nome");
		String apelido = request.getParameter("apelido");
		nome = java.net.URLDecoder.decode(nome, "UTF-8");
		apelido = java.net.URLDecoder.decode(apelido, "UTF-8");
		String email = request.getParameter("email");
		String telemovel = request.getParameter("telemovel");
		String username = (String) request.getSession().getAttribute("username");

		GestorSBD.atualizarPerfilCliente(username, nome, apelido, email, telemovel);

		response.sendRedirect("AtualizarPerfilCliente.jsp");
	}

	private void aderirAtividade(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		String clienteUsername = request.getParameter("clienteUsername");
		String manchaID = request.getParameter("manchaID");
		Cliente cliente = GestorSBD.getClienteUsername(clienteUsername);
		String tipo = request.getParameter("tipo");
		System.out.println(tipo);
		if (GestorSBD.addClienteToAtividade(cliente.getNome() + " " + cliente.getApelido(),
				Integer.parseInt(manchaID))) {
			if (tipo.equals("individual"))
				response.sendRedirect(request.getContextPath() + "/AtividadesIndividuais.jsp");
			else if (tipo.equals("grupo"))
				response.sendRedirect(request.getContextPath() + "/AtividadesGrupo.jsp");
			else if (tipo.equals("semanal"))
				response.sendRedirect(request.getContextPath() + "/CalendarioSemanal.jsp");
		}

		else {
			if (tipo.equals("individual"))
				response.sendRedirect(request.getContextPath() + "/AtividadesIndividuais.jsp?error=true");
			if (tipo.equals("grupo"))
				response.sendRedirect(request.getContextPath() + "/AtividadesGrupo.jsp?error=true");
			if (tipo.equals("semanal"))
				response.sendRedirect(request.getContextPath() + "/CalendarioSemanal.jsp?error=true");
		}

	}

	private void cancelarInscricaoAtividade(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		int clienteID = Integer.parseInt(request.getParameter("clienteID"));
		int idAti = Integer.parseInt(request.getParameter("idAti"));
		String tipo = request.getParameter("tipo");
		if (GestorSBD.cancelarInscricaoAtividade(idAti, clienteID)) {
			if (tipo.equals("individual"))
				response.sendRedirect(request.getContextPath() + "/AtividadesIndividuais.jsp");
			if (tipo.equals("grupo"))
				response.sendRedirect(request.getContextPath() + "/AtividadesGrupo.jsp");
			if (tipo.equals("semanal"))
				response.sendRedirect(request.getContextPath() + "/CalendarioSemanal.jsp");
		}

		else {
			if (tipo.equals("individual"))
				response.sendRedirect(request.getContextPath() + "/AtividadesIndividuais.jsp?error=true");
			if (tipo.equals("grupo"))
				response.sendRedirect(request.getContextPath() + "/AtividadesGrupo.jsp?error=true");
			if (tipo.equals("semanal"))
				response.sendRedirect(request.getContextPath() + "/CalendarioSemanal.jsp?error=true");
		}

	}

	private void consultarAtividade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String manchaID = request.getParameter("manchaID");
		response.sendRedirect(request.getContextPath() + "/ConsultaAtividadeCliente.jsp?manchaID=" + manchaID);

	}

}