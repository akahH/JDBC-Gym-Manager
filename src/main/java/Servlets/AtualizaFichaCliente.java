package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.GestorSBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.Cliente;
import util.Data;
import util.Recomendacao;

@WebServlet("/AtualizaFichaCliente")
public class AtualizaFichaCliente extends HttpServlet {
    /**
	 * Classe que após pedido GET retorna o conteúdo hmtl/jsp com o perfil do cliente seleccionado
	 */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nome = request.getParameter("nome");

        try {
        	System.out.println("Nome: " + nome);
            Cliente cliente = GestorSBD.getCliente(nome);
            
            //listas de patologias, objetivos e recomendacoes do cliente
            List<String> patologias = GestorSBD.getPatologias(cliente.getIDCliente());
            List<String> objetivos = GestorSBD.getObjetivos(cliente.getIDCliente());
            List<Recomendacao> recomendacoes = GestorSBD.getRecomendacoesByCliente(cliente.getIDCliente());
            
    		HttpSession session = request.getSession();
    		String username = (String) session.getAttribute("username");
    		int idPT = GestorSBD.getIDPT(username);
    		int clubeID = GestorSBD.getPTClubeID(idPT);

   
            PrintWriter out = response.getWriter();
            out.println("<html><body>");

   
            out.println("<h2 style=\"margin: 10px;\">Perfil de Cliente Resumido</h2>");
            out.println("<table class=\"mancha-table\" >");
            out.println("<thead>");
            out.println("<tr><th>Nome</th><th>Idade</th><th>Email</th></tr>");
            out.println("</thead>");
            out.println("<tbody>");
            out.println("<tr><td>" + cliente.getNome() + " " + cliente.getApelido() + "</td><td>"
                    + Data.idade(cliente.getDataNasc().toLocalDate()) + "</td><td>" + cliente.getEmail() + "</td></tr>");
            out.println("</tbody>");
            out.println("</table>");

     
            out.println("<h3 style=\"margin: 10px;\">Patologias</h3>");
            out.println("<table class=\"mancha-table\">");
            out.println("<tr><th>Descrição</th></tr>");
            for (String patologia : patologias) {
                out.println("<tr><td>" + patologia + "</td></tr>");
            }
            out.println("</table>");


            out.println("<h3 style=\"margin: 10px;\">Objetivos</h3>");
            out.println("<table class=\"mancha-table\">");
            out.println("<tr><th>Descrição</th></tr>");
            for (String objetivo : objetivos) {
                out.println("<tr><td>" + objetivo + "</td></tr>");
            }
            out.println("</table>");


            out.println("<h3 style=\"margin: 10px;\">Recomendações</h3>");
            out.println("<table class=\"mancha-table\">");
            out.println("<tr><th>Equipamento</th><th>Notas</th><th>Data Inicio</th><th>Data Fim</th></tr>");
            for (Recomendacao recomendacao : recomendacoes) {
                out.println("<tr><td>" + GestorSBD.getDesignacaoEquipamento(recomendacao.getIdEquipamento()) 
                        + "</td><td>" + recomendacao.getNotas() + "</td><td>" + recomendacao.getDataInicio()
                        + "</td><td>" + recomendacao.getDataFim() + "</td></tr>");
            }
            out.println("</table>");
            out.println("<h3 style=\"margin: 10px;\">Adicionar Nova Recomendação</h3>");
            out.println("<form action=\"PTServlet\" method=\"post\" id=\"form-recomendacao\">");
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"idEquipamento\">Selecionar Equipamento:</label>");
            out.println("<select class=\"form-control\" name=\"idEquipamento\">");
            //Lista dos equipamentos associados às recomendações
            HashMap<String, HashMap<String, String>> equipamentos = GestorSBD.getEquipamentos(clubeID);
            for (Map.Entry<String, HashMap<String, String>> entry : equipamentos.entrySet()) {
                String idequipamento = entry.getKey();
                String designacao = entry.getValue().get("designacao");
                out.println("<option value=\"" + idequipamento + "\">" + designacao + "</option>");
            }
            out.println("</select>");
            out.println("</div>");
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"notas\">Notas:</label>");
            out.println("<textarea class=\"form-control\" name=\"notas\" id=\"notas\" rows=\"4\"></textarea>");
            out.println("</div>");
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"dataInicio\">Data Inicio:</label>");
            out.println("<input type=\"date\" class=\"form-control\" name=\"dataInicio\">");
            out.println("</div>");
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"dataFim\">Data Fim:</label>");
            out.println("<input type=\"date\" class=\"form-control\" name=\"dataFim\">");
            out.println("</div>");
            out.println("<input type=\"hidden\" name=\"flag\" value=\"criarRecomendacao\">");
            out.println("<input type=\"hidden\" name=\"encodedNotas\" id=\"encodedNotas\">");
            out.println("<input type=\"hidden\" name=\"idCliente\" value=\"" + cliente.getIDCliente() + "\">");
            out.println("<button type=\"submit\" class=\"btn btn-secondary\" style=\"background-color: orange;\">Adicionar Recomendacao</button>");

            out.println("</form>");
            out.println("	<script>\r\n"
            		+ "        document.getElementById('form-recomendacao').addEventListener('submit', function() {\r\n"
            		+ "            var notasInput = document.getElementById('notas');\r\n"
            		+ "            var encodedNotasInput = document.getElementById('encodedNotas');\r\n"
            		+ "            encodedNotasInput.value = encodeURIComponent(notasInput.value);\r\n"
            		+ "            console.log(encodedNotasInput.value);\r\n"
            		+ "        });\r\n"
            		+ "	</script>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

