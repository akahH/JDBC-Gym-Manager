package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.Cliente;

@WebServlet("/ClientesAtividade")
public class ClientesAtividade extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//System.out.println("Enter ClientesAtividade");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
    		String username = (String) session.getAttribute("username");
    		int idPT = GestorSBD.getIDPT(username);
            String query = request.getParameter("query");
            List<Cliente> clientesPT = null;
			try {
				clientesPT = GestorSBD.getClientesPT(idPT);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            List<String> searchResults = Cliente.autocompleteClientes(query, 1, 10, clientesPT);
    		Iterator<String> iterator = searchResults.iterator();
    		while (iterator.hasNext())
    			if("option".compareToIgnoreCase("br")==0)
    				out.write((String) iterator.next()+ "<br>");
    			else {
    				String nome = (String)iterator.next();
    				System.out.println(nome);
    				out.write("<div onclick=\"setClienteUsername(" +"'"  +nome  +"'" + ")\">" + nome + "</div>");
    			}
    				
    	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    }
