<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="util.ManchaPT"%>
<%@ page import="java.util.*"%>
<%@ page import="util.Atividade"%>
<%@ page import="util.Cliente"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="java.time.LocalDate"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Calendário Semanal</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body style="height: auto">
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
			<h2 style="text-align: center;">Calendário Semanal</h2>

			<%
			String username = (String) session.getAttribute("username");
			Cliente cliente = GestorSBD.getClienteUsername(username);

			String[] diasSemana = { "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado",
					"Domingo" };

			for (int i = 0; i < diasSemana.length; i++) {
				String diaSemana = diasSemana[i];
				List<ManchaPT> manchas = GestorSBD.getAtividadesSemanais(GestorSBD.getPTIDcliente(cliente.getIDCliente()),
				diaSemana);
			%>
			<h4><%=diaSemana%></h4>
			<table class="mancha-table">
				<thead>
					<tr>
						<th>Atividade</th>
						<th>Hora Inicio</th>
						<th>Hora Fim</th>
						<th>Faixa Etária</th>
						<th>Confirmação PT</th>
						<th>Data</th>
						<th>Participantes</th>
						<th>PT</th>
					</tr>
				</thead>
				<tbody>
					<%
					for (ManchaPT mancha : manchas) {
						Atividade atividade = GestorSBD.getAtividade(mancha.getManchaID());
					%>
					<tr>
						<td><%=GestorSBD.getDesignacaoAtividadeOfMancha(mancha.getManchaID())%>
							<form action="ClienteServlet" method="post">
								<input type="hidden" name="manchaID"
									value="<%=mancha.getManchaID()%>"> <input type="hidden"
									name="flag" value="consultarAtividade">
								<button type="submit"
									class="btn btn-primary create-activity-btn">Consultar</button>
							</form></td>
						<td><%=mancha.getHoraInicio()%></td>
						<td><%=mancha.getHoraFim()%></td>
						<td><%=mancha.getIdadeMin()%> - <%=mancha.getIdadeMax()%></td>
						<td><%=atividade.getConfirmacaoPT()%></td>
						<%
						if (atividade.getTipo().equals("Semanal")) {
						%>
						<td>Semanal</td>
						<%
						} else {
						%>
						<td><%=atividade.getDataReal()%></td>
						<%
						}
						%>
						<td><%=atividade.getTipoParticipantes()%></td>
						<td><%=GestorSBD.getNomePT(mancha.getIDPT())%></td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
			<div class="weekday-divider"></div>
			<%
			}
			%>
		</div>
	</div>
</body>

</html>