<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="classes.GestorSBD"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Exportar Perfis XML</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
	<div class="wrapper">
		<div class="title" style="">FITNESS TIME</div>
		<a href="GerenteServlet?flag=logout"
			class="btn btn-secondary logout-btn">Logout</a>
		<div class="center-container">
			<div class="rooms-container">
				<h2 style="text-align: center;">Perfis de Clientes</h2>
				<table class="rooms-table">
					<thead>
						<tr>
							<th>ID Cliente</th>
							<th>Nome Cliente</th>
							<th>Exportar</th>
						</tr>
					</thead>
					<tbody>
						<%
						Map<Integer, String> clientes = GestorSBD.getClientes();
						List<Integer> idClientes = new ArrayList<>(clientes.keySet());

						for (int cliente : idClientes) {
						%>
						<tr>
							<td><%=cliente%></td>
							<td><%=clientes.get(cliente)%></td>
							<td class="edit-delete-col">
								<form action="GerenteServlet" method="post">
									<input type="hidden" name="clienteId" value="<%=cliente%>">
									<input type="hidden" name="flag" value="exportarXML">
									<button type="submit"
										class="btn btn-primary create-resource-btn">Exportar</button>
								</form>
							</td>
						</tr>
						<%
						}
						%>
					</tbody>
				</table>
			</div>
			<div class="rooms-container">
				<h2 style="text-align: center;">Perfis de PT</h2>
				<table class="rooms-table">
					<thead>
						<tr>
							<th>ID PT</th>
							<th>Nome PT</th>
							<th>Exportar</th>
						</tr>
					</thead>
					<tbody>
						<%
						Map<Integer, String> pts = GestorSBD.getPTs();
						List<Integer> idPTs = new ArrayList<>(pts.keySet());

						for (int pt : idPTs) {
						%>
						<tr>
							<td><%=pt%></td>
							<td><%=pts.get(pt)%></td>
							<td class="edit-delete-col">
								<form action="GerenteServlet" method="post">
									<input type="hidden" name="clienteId" value="<%=pt%>">
									<input type="hidden" name="flag" value="exportarXMLPT">
									<button type="submit"
										class="btn btn-primary create-resource-btn">Exportar</button>
								</form>
							</td>
						</tr>
						<%
						}
						%>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>

</html>