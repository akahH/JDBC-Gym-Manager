<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="util.ManchaPT"%>
<%@ page import="java.util.*"%>
<%@ page import="util.Atividade"%>
<%@ page import="classes.GestorSBD"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manchas de Disponibilidade</title>
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
<script>
	function showErrorPopUp() {

		const urlParams = new URLSearchParams(window.location.search);
		const errorParam = urlParams.get('error');

		if (errorParam === 'true') {

			alert("Horas de nova mancha inválidas.");
		}
	}
	window.onload = showErrorPopUp;
</script>

</head>

<body style="height:auto">
	<div class="fixed-header" >
		<div class="home-link">
			<a href="PT.jsp"> <i class="fas fa-home"
				style="margin-right: 10px"></i> <span class="username"><%=(String) session.getAttribute("username")%></span>
			</a>
		</div>
		<a href="GerenteServlet?flag=logout"
			class="btn btn-secondary logout-btn">Logout</a>
	</div>
	<div class="wrapper">
		<div class="title" style="margin-top: 100px;">FITNESS TIME</div>
		<div class="center-container">
			<h2 style="text-align: center;">Manchas de Disponibilidade</h2>

			<%
			HttpSession sessao = request.getSession();
			String username = (String) sessao.getAttribute("username");
			int idPT = GestorSBD.getIDPT(username);

			String[] diasSemana = { "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado",
					"Domingo" };

			for (int i = 0; i < diasSemana.length; i++) {
				String diaSemana = diasSemana[i];
				List<ManchaPT> manchas = GestorSBD.getManchaPT(diaSemana, idPT);
			%>
			<h4><%=diaSemana%></h4>
			<table class="mancha-table">
				<thead>
					<tr>
						<th>Atividade</th>
						<th>Hora Inicio</th>
						<th>Hora Fim</th>
						<th>Faixa Etária</th>
						<th>Estado</th>
						<th>Gestão</th>
					</tr>
				</thead>
				<tbody>
					<%
					for (ManchaPT mancha : manchas) {
					%>
					<tr>
						<td>
							<%
							if (GestorSBD.checkIfManchaHasAtividade(mancha.getManchaID())) {
							%> <%=GestorSBD.getDesignacaoAtividadeOfMancha(mancha.getManchaID())%>

							<%
							} else {
							%>
							<form action="PTServlet" method="post">
								<input type="hidden" name="manchaID"
									value="<%=mancha.getManchaID()%>"> <input type="hidden"
									name="flag" value="criarAtividade">
								<button type="submit"
									class="btn btn-primary create-activity-btn">Adicionar</button>
							</form> <%
 }
 %>
						</td>
						<td><%=mancha.getHoraInicio()%></td>
						<td><%=mancha.getHoraFim()%></td>
						<td><%=mancha.getIdadeMin()%> - <%=mancha.getIdadeMax()%></td>
						<td>
							<%
							if (GestorSBD.checkIfManchaHasAtividade(mancha.getManchaID())) {
								Atividade atividade = GestorSBD.getAtividade(mancha.getManchaID());
							%> <%=atividade.getConfirmacaoPT()%> <%
 } else {
 %> N/A <%
 }
 %>
						</td>
						<td>
							<form action="PTServlet" method="post">
								<input type="hidden" name="manchaID"
									value="<%=mancha.getManchaID()%>">
								<input type="hidden" name="flag" value="apagarMancha">
								<button type="submit"
									class="btn btn-primary create-activity-btn">Apagar
									Mancha</button>
							</form> <%
 if (GestorSBD.checkIfManchaHasAtividade(mancha.getManchaID())) {
 %>
							<form action="PTServlet" method="post">
								<input type="hidden" name="flag" value="gerirAtividade">
								<input type="hidden" name="manchaID"
									value="<%=mancha.getManchaID()%>">
								<button type="submit"
									class="btn btn-secondary create-activity-btn"
									style="background-color: orange;">Gerir Atividade</button>
							</form> <%
 }
 %>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
			<a href="AdicionarManchaPT.jsp"
				class="btn btn-primary create-activity-btn">+</a>
			<div class="weekday-divider"></div>
			<%
			}
			%>
		</div>
	</div>
</body>

</html>

