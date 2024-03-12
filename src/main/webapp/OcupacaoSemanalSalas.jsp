<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="util.SalaReservada"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ocupação Semanal de Salas</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
    <div class="fixed-header">
        <div class="home-link">
            <a href="YourHomeJSPPage.jsp"> <!-- Replace with the link to the home page -->
                <i class="fas fa-home" style="margin-right: 10px"></i>
                <span class="username"><%=(String) session.getAttribute("username")%></span>
            </a>
        </div>
        <a href="LogoutServlet" class="btn btn-secondary logout-btn">Logout</a>
    </div>
    <div class="wrapper">
        <div class="title" style="margin-top: 100px;">FITNESS TIME</div>
        <div class="center-container">
            <h2 style="text-align: center;">Ocupação Semanal de Salas</h2>

            <%
            try {
                List<SalaReservada> salaReservadas = GestorSBD.getSalaOccupation();

                if (salaReservadas.isEmpty()) {
            %>
            <p>No room reservations for the current week.</p>
            <%
                } else {
            %>
            <table class="sala-table">
                <thead>
                    <tr>
                        <th>Sala</th>
                        <th>Atividade</th>
                        <th>Dia de Semana</th>
                        <th>Data</th>
                        <th>Hora de Inicio</th>
                        <th>Hora de Fim</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    for (SalaReservada salaReservada : salaReservadas) {
                    %>
                    <tr>
                        <td><%=GestorSBD.getDesignacaoSala(salaReservada.getIdSala())%></td>
                        <td><%=GestorSBD.getDesignacaoAtividadeOfMancha(salaReservada.getIdAti())%></td>
                        <td><%=salaReservada.getDiaSemana()%></td>
                        <%if(salaReservada.getData() == null){
                        	%>
                        	<td>Semanal</td>
						<%}else{ %>
                        <td><%=salaReservada.getData()%></td>
                        <%} %>
                        <td><%=salaReservada.getHoraInicio()%></td>
                        <td><%=salaReservada.getHoraFim()%></td>
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
