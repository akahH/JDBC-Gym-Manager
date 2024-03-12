<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="util.Atividade"%>
<%@ page import="util.Cliente"%>
<%@ page import="util.RecursosReservados"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Gerir Atividade</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<link rel="stylesheet" type="text/css" href="mainCSS.css">

</head>

<body>
	<%
	String manchaID = request.getParameter("manchaID");
	%>
	<div class="fixed-header">
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
			<h2 style="text-align: center;">Gestão da Atividade</h2>


			<%
			Atividade atividade = GestorSBD.getAtividade(Integer.parseInt(manchaID));
			List<Cliente> clientesInscritos = GestorSBD.getClientesAtividade(atividade.getIdAti());
			RecursosReservados recursosReservados = GestorSBD.getRecursosReservadosForAtividade(atividade.getIdAti());
			%>

			<div class="form-group">
				<label for="designacao">Designação:</label> <input type="text"
					class="form-control" id="designacao"
					value="<%=atividade.getDesignacao()%>" readonly>
			</div>
			<div class="form-group">
				<label for="tipo">Tipo:</label> <input type="text"
					class="form-control" id="tipo" value="<%=atividade.getTipo()%>"
					readonly>
			</div>
			<div class="form-group">
				<label for="tipoParticipantes">Tipo de Participantes:</label> <input
					type="text" class="form-control" id="tipoParticipantes"
					value="<%=atividade.getTipoParticipantes()%>" readonly>
			</div>
			<div class="form-group">
				<label for="partMin">Participantes Mínimos:</label> <input
					type="text" class="form-control" id="partMin"
					value="<%=atividade.getPartMin()%>" readonly>
			</div>
			<div class="form-group">
				<label for="partMax">Participantes Máximos:</label> <input
					type="text" class="form-control" id="partMax"
					value="<%=atividade.getPartMax()%>" readonly>
			</div>
			<%
			if (atividade.getTipo().equals("Data")) {
			%>
			<div class="form-group">
				<label for="dataReal">Data:</label> <input type="text"
					class="form-control" id="dataReal"
					value="<%=atividade.getDataReal()%>" readonly>
			</div>
			<%
			}
			%>
			<div class="form-group">
				<label for="confirmacaoPT">Confirmação PT:</label> <input
					type="text" class="form-control" id="confirmacaoPT"
					value="<%=atividade.getConfirmacaoPT()%>" readonly>
			</div>
			<%
			if (recursosReservados.getIdSalas().size() > 0) {
			%>
			<h3 style="text-align: center;">Reserva</h3>
			<table class="equipamento-table">
				<thead>
					<tr>
						<th>Salas Reservadas</th>
						<th>Equipamentos Reservados</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<%
							List<Integer> idSalas = recursosReservados.getIdSalas();
							for (Integer salaID : idSalas) {
								String salaDesignacao = GestorSBD.getDesignacaoSala(salaID);
							%> <%=salaDesignacao%><br> <%
 }
 %>
						</td>
						<td>
							<%
							List<Integer> idEquipamentos = recursosReservados.getIdEquipamentos();
							for (Integer equipamentoID : idEquipamentos) {
								String equipamentoDesignacao = GestorSBD.getDesignacaoEquipamento(equipamentoID);
							%> <%=equipamentoDesignacao%><br> <%
 }
 %>
						</td>
					</tr>
				</tbody>
			</table>
			<%
			}
			%>
			<div class="form-group">
				<h3>Clientes Inscritos:</h3>
				<ul>
					<%
					for (Cliente cliente : clientesInscritos) {
					%>
					<li><%=cliente.getNome()%> <%=cliente.getApelido()%></li>
					<%
					}
					%>
				</ul>
			</div>
			<div id="searchClienteDiv" style="display: block;">
				<label style="text-align: center">Pesquisar Cliente: <br>
					<input autocomplete="off" list="clientes" id="cliente"
					type="search" name="clienteInput"
					oninput="showClientes(this.value);" placeholder="Pesquisar Cliente"
					style="width: 100%;">
				</label> <br>
				<div id="clientes"></div>
			</div>
			<form action="PTServlet" method="post" style="text-align: center;">
				<input type="hidden" name="flag" value="adicionarClienteAtividade">
				<input type="hidden" id="atividadeID" name="atividadeID"
					value="<%=atividade.getIdAti()%>"> <input type="hidden"
					name="manchaID" value="<%=manchaID%>"> <input type="hidden"
					id="clienteUsername" name="clienteUsername" value="">
				<button type="submit" id="adicionarClienteButton" disabled
					class="btn btn-primary" style="margin: 10px; width: 100%">Adicionar
					Cliente</button>
			</form>

			<form action="PTServlet" method="post" style="text-align: center;">
				<input type="hidden" name="flag" value="confirmarAtividade">
				<input type="hidden" id="atividadeID" name="atividadeID"
					value="<%=atividade.getIdAti()%>"> <input type="hidden"
					name="manchaID" value="<%=manchaID%>">
				<button type="submit" class="btn btn-success"
					style="margin: 10px; width: 80%">Confirmar Atividade</button>
			</form>

			<form action="PTServlet" method="post" style="text-align: center;">
				<input type="hidden" name="flag" value="cancelarAtividade">
				<input type="hidden" id="atividadeID" name="atividadeID"
					value="<%=atividade.getIdAti()%>"> <input type="hidden"
					name="manchaID" value="<%=manchaID%>">
				<button type="submit" class="btn btn-danger"
					style="margin: 10px; width: 80%">Cancelar Atividade</button>
			</form>

		</div>
	</div>

	<script>

function getXHR() {
    var invocation = null;
    try {
        invocation = new XMLHttpRequest();
    } catch (e) {
        try {
            invocation = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                invocation = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                alert("Your browser broke!");
                return null;
            }
        }
    }
    return invocation;
}

var xmlHttp = getXHR();

function showClientes(str) {
    if (event.key === 'Enter') {
        return;
    }
    var url = "ClientesAtividade"; 
    url += "?idAti=<%=atividade.getIdAti()%>&query=" + encodeURIComponent(str);
			url = encodeURI(url);
			xmlHttp.onreadystatechange = function() {
				if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
					console.log("enter");
					console.log(xmlHttp.responseText);
					document.getElementById("clientes").innerHTML = xmlHttp.responseText;
				}
			}
			xmlHttp.open("GET", url, true);
			xmlHttp.send();
		}

		function setClienteUsername(username) {
			var adicionarClienteButton = document
					.getElementById("adicionarClienteButton");
			adicionarClienteButton.disabled = false;
			adicionarClienteButton.innerText = "Adicionar " + username;
			document.getElementById("clienteUsername").value = encodeURIComponent(username);
		}
	</script>
</body>

</html>
