<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="util.Recomendacao" %>
<%@ page import="classes.GestorSBD" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Recomendações de PT</title>
    <link rel="stylesheet"
        href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
<div class="fixed-header">
		<div class="home-link">
			<a href="Cliente.jsp"> <i class="fas fa-home"
				style="margin-right: 10px"></i> <span class="username"><%=(String) session.getAttribute("username")%></span>
			</a>
		</div>
		<a href="ClienteServlet?flag=logout"
			class="btn btn-secondary logout-btn">Logout</a>
	</div>
    <div class="wrapper">
        <div class="title" style="margin-top: 100px;">FITNESS TIME</div>
        <div class="center-container">
            <h2>Recomendações</h2>

            <%
                String username = (String) session.getAttribute("username");
                int idCliente = GestorSBD.getClienteUsername(username).getIDCliente();
                List<Recomendacao> recomendacoes = GestorSBD.getRecomendacoesByCliente(idCliente);

                if (recomendacoes.isEmpty()) {
            %>
            <p>Não há recomendações disponíveis.</p>
            <%
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = sdf.format(new Date());
            %>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Equipamento</th>
                        <th>Notas</th>
                        <th>Data Início</th>
                        <th>Data Fim</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Recomendacao recomendacao : recomendacoes) {
                            String dataFim = sdf.format(recomendacao.getDataFim());
                            if (currentDate.compareTo(dataFim) <= 0) {
                    %>
                    <tr>
                        <td><%=  GestorSBD.getDesignacaoEquipamento(recomendacao.getIdEquipamento())  %></td>
                        <td><%= recomendacao.getNotas() %></td>
                        <td><%= sdf.format(recomendacao.getDataInicio()) %></td>
                        <td><%= dataFim %></td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
            <%
                }
            %>
        </div>
    </div>
</body>

</html>
