package Servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/GerenteServlet")
@MultipartConfig(maxFileSize = 4294967296L)
public class GerenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static GestorSBD gestor = new GestorSBD();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String flag = request.getParameter("flag");

		switch (flag) {
		case "login":
			handleLogin(request, response);
			break;
		case "alterarHorario":
			alteraHorario(request, response);
			break;
		case "alterarContactos":
			alterarContactos(request, response);
			break;
		case "editRecurso":
			String tipoRecurso = request.getParameter("tipoRecurso");
			String recursoID = request.getParameter("recursoID");
			if ("sala".equals(tipoRecurso) || "equipamento".equals(tipoRecurso)) {
				response.sendRedirect(request.getContextPath() + "/EditRecurso.jsp?tipoRecurso=" + tipoRecurso
						+ "&recursoID=" + recursoID);
			} else {
				response.sendRedirect(request.getContextPath() + "/error.jsp");
			}
			break;
		case "consultarRecurso":
			tipoRecurso = request.getParameter("tipoRecurso");
			recursoID = request.getParameter("recursoID");
			if ("sala".equals(tipoRecurso) || "equipamento".equals(tipoRecurso)) {
				response.sendRedirect(request.getContextPath() + "/ConsultarRecurso.jsp?tipoRecurso=" + tipoRecurso
						+ "&recursoID=" + recursoID);
			} else {
				response.sendRedirect(request.getContextPath() + "/error.jsp");
			}
			break;
		case "processEdit":
			editarRecurso(request, response);
			break;
		case "criarRecurso":
			criarRecurso(request, response);
			break;
		case "apagarMultimedia":
			String multimediaID = request.getParameter("multimediaID");
			 tipoRecurso = request.getParameter("tipoRecurso");
			 recursoID = request.getParameter("recursoID");
			gestor.apagarMultimedia(Integer.parseInt(multimediaID));
			response.sendRedirect(request.getContextPath() + "/EditRecurso.jsp?tipoRecurso=" + tipoRecurso
					+ "&recursoID=" + recursoID);
			break;
		case "criarPT":
            criarPT(request, response);
            break;
		case "importXML":
            importXML(request, response);
            break;
		case "exportarXML":
            exportXML(request, response);
            break;
		case "exportarXMLPT":
            exportXMLPT(request, response);
            break;
		default:
			PrintWriter out = response.getWriter();
			out.println("<html><body><p>Erro: A directoria não existe</p></body></html>");
			break;
		}
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
		case "Gerente":
			System.out.println("Entra");
			System.out.println(username);
			System.out.println(username);

			if (gestor.loginGerente(username, password)) {

				HttpSession session = request.getSession(true);
				session.setAttribute("username", username);
				response.sendRedirect(request.getContextPath() + "/Gerente.jsp");

			} else
				response.sendRedirect("LogIn.jsp");
			break;
		case "PT":
			response.sendRedirect("LogIn.jsp");
			break;
		case "Cliente":
			response.sendRedirect("LogIn.jsp");
		}

	}

	private void alteraHorario(HttpServletRequest request, HttpServletResponse response) throws IOException {

		HttpSession session = request.getSession();
		String usernameGerente = (String) session.getAttribute("username");

		String dia = request.getParameter("dia");
		String horaAbertura = request.getParameter("horaAbertura");
		String horaFecho = request.getParameter("horaFecho");

		int clubeID = GestorSBD.getClubeID(usernameGerente);

		gestor.alteraHorarioClube(dia, horaAbertura, horaFecho, clubeID);
		response.sendRedirect(request.getContextPath() + "/AtualizarClube.jsp");

	}

	private void alterarContactos(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String tipoContacto = request.getParameter("tipoContacto");
		String novoContacto = request.getParameter("novoContacto");

		HttpSession session = request.getSession();
		String usernameGerente = (String) session.getAttribute("username");
		int clubeID = GestorSBD.getClubeID(usernameGerente);

		if ("telefone".equals(tipoContacto) && novoContacto.matches("\\d{9}")) {
			gestor.alterarTelefoneClube(clubeID, novoContacto);
		} else if ("email".equals(tipoContacto)) {
			gestor.alterarEmailClube(clubeID, novoContacto);
		}
		response.sendRedirect(request.getContextPath() + "/AtualizarClube.jsp");
	}

	private void editarRecurso(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tipoRecurso = request.getParameter("tipoRecurso");
		String recursoID = request.getParameter("recursoID");
		String novaDesignacao = request.getParameter("designacao");
		String novoEstado = request.getParameter("estado");
		String tipoMultimedia = request.getParameter("tipoMultimedia");
		Part multimediaPart = request.getPart("multimedia");
		
		if(tipoRecurso.equals("sala")) {
			if(novaDesignacao.length() > 1)
				gestor.alterarSalaDesignacao(novaDesignacao, Integer.parseInt(recursoID));
			gestor.alterarSalaEstado(novoEstado, Integer.parseInt(recursoID));
		}
		else if(tipoRecurso.equals("equipamento")) {
			if(novaDesignacao.length() > 1)
				gestor.alterarEquipamentoDesignacao(novaDesignacao, Integer.parseInt(recursoID));
			gestor.alterarEquipamentoEstado(novoEstado, Integer.parseInt(recursoID));
		}

		if(multimediaPart != null && multimediaPart.getSize() > 0)
			gestor.setMultimedia(tipoMultimedia, tipoRecurso, Integer.parseInt(recursoID), multimediaPart);
		response.sendRedirect("RecursosClube.jsp");
	}
	
	
	private void criarRecurso(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        HttpSession sessao = request.getSession();
        String usernameGerente = (String) sessao.getAttribute("username");
        int clubeID = GestorSBD.getClubeID(usernameGerente);
		String tipoRecurso = request.getParameter("tipoRecurso");
		String novaDesignacao = request.getParameter("designacao");
		String novoEstado = request.getParameter("estado");
		String tipoMultimedia = request.getParameter("tipoMultimedia");
		Part multimediaPart = request.getPart("multimedia");
		
		//Confirmar o tipo de recurso a ser criado
		if(tipoRecurso.equals("Sala")) {
			gestor.criarSala(clubeID, novaDesignacao, novoEstado, multimediaPart, tipoMultimedia);
		}
		else if(tipoRecurso.equals("Equipamento")) {
			gestor.criarEquipamento(clubeID, novaDesignacao, novoEstado, multimediaPart, tipoMultimedia);
		}
		response.sendRedirect("RecursosClube.jsp");
	}
	
    private void criarPT(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
        HttpSession sessao = request.getSession();
        String usernameGerente = (String) sessao.getAttribute("username");
        int clubeID = GestorSBD.getClubeID(usernameGerente);
        String nome = request.getParameter("nome");
        String apelido = request.getParameter("apelido");
        String email = request.getParameter("email");
        int telemovel = Integer.parseInt(request.getParameter("telemovel"));
        Part fotografia = request.getPart("fotografia");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        gestor.criarPT(nome, apelido, email, telemovel, fotografia, username, password,clubeID);
        response.sendRedirect("CriarPerfis.jsp");
    }
    
    private void importXML(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession sessao = request.getSession();
        String usernameGerente = (String) sessao.getAttribute("username");
        String tipoPerfil = request.getParameter("tipoPerfil");
        System.out.println(tipoPerfil);
        int clubeID = GestorSBD.getClubeID(usernameGerente);
    	Part part = request.getPart("xmlFile");
        InputStream xmlFile = part.getInputStream();
        if (gestor.processXMLStream(xmlFile,tipoPerfil,clubeID)) {
            response.sendRedirect("CriarPerfis.jsp");
        } else {
            response.sendRedirect("ImportProfileXML.jsp");
        }
    }
    
    private void exportXML(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    	int clienteId = Integer.parseInt(request.getParameter("clienteId"));
        String xmlContent = GestorSBD.exportClienteToXML(clienteId);
        //define o tipo do item a ser exportado
        response.setContentType("application/xml");
        //define que o ficheiro deve ser descarregado para a pasta de downloads pré-definida
        response.setHeader("Content-Disposition", "attachment; filename=cliente.xml");
        PrintWriter out = response.getWriter();
        out.write(xmlContent);
        
    }
    
    private void exportXMLPT(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    	int clienteId = Integer.parseInt(request.getParameter("clienteId"));
        String xmlContent = GestorSBD.exportPTToXML(clienteId);
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=pt.xml");
        PrintWriter out = response.getWriter();
        out.write(xmlContent);
        
    }

	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("index.html");
	}


}