<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Equipamentos Menos Usados</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
    <div class="fixed-header">
        <div class="home-link">
            <a href="Gerente.jsp"> <i class="fas fa-home" style="margin-right: 10px"></i> <span
                    class="username"><%=(String) session.getAttribute("username")%></span>
            </a>
        </div>
        <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    </div>
    <div class="wrapper">
        <div class="title" style="margin-top: 100px;">FITNESS TIME</div>
        <div class="center-container">
            <h2>Equipamentos Menos usados</h2>

            <%
                try {
                	LinkedHashMap<Integer, Integer> equipamentos = GestorSBD.equipamentosMenosUsados();

                    if (equipamentos.isEmpty()) {
            %>
            <p>Não há equipamentos disponíveis.</p>
            <%
                    } else {
            %>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Equipamento</th>
                        <th>Número de Reservas</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Map.Entry<Integer, Integer> equipamento : equipamentos.entrySet()) {
                    %>
                    <tr>
                        <td><%= GestorSBD.getDesignacaoEquipamento(equipamento.getKey()) %></td>
                        <td><%= equipamento.getValue() %></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
        </div>
    </div>
</body>

</html>

